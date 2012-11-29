/*
 *    Copyright 2012 Henning Rogge
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.hrogge.CompactPDFExport;

import java.awt.Color;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

import jaxbGenerated.datenxml.*;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;

import de.hrogge.CompactPDFExport.PDFSonderfertigkeiten.Kategorie;

public class FrontSeite extends PDFSeite {
	public FrontSeite(PDDocument d, float marginX, float marginY,
			float textMargin) throws IOException {
		super(d, marginX, marginY, textMargin);
	}

	public void erzeugeSeite(Daten daten, PDJpeg bild,
			String[] guteEigenschaften, SortedMap<PDFSonderfertigkeiten, Boolean> alleSF,
			boolean tzm) throws IOException {
		int patzerHoehe, patzerBreite, festerHeaderHoehe;
		int notizen, kampfBreite, blockBreite, vorNachTeileLaenge;
		int leer, y, bloecke, hoehe;
		List<Kampfset> kampfsets;
		List<Nahkampfwaffe> nahkampf;
		List<Fernkampfwaffe> fernkampf;
		List<Vorteil> vorteile, nachteile;
		List<Kampfset> ruestung;
		List<Schild> schilde;
		List<PDFSonderfertigkeiten> sfListe;

		boolean zeigeSchilde;

		kampfsets = new ArrayList<Kampfset>();

		for (Kampfset set : daten.getKampfsets().getKampfset()) {
			if (set.isTzm() != tzm || !set.isInbenutzung()) {
				continue;
			}

			kampfsets.add(set);
		}

		if (kampfsets.size() == 0) {
			/*
			 * KEINE aktiven Kampfsets... zumindest das erste nehmen und
			 * aktivieren!
			 */
			for (Kampfset set : daten.getKampfsets().getKampfset()) {
				if (set.isTzm() == tzm) {
					kampfsets.add(set);

					set.setInbenutzung(true);
					break;
				}
			}
		}

		/* Vor und Nachteile sind statisch */
		vorteile = new ArrayList<Vorteil>();
		nachteile = new ArrayList<Vorteil>();

		extrahiereVorNachteile(daten, vorteile, nachteile);

		vorNachTeileLaenge = (Math.max(vorteile.size(), nachteile.size()) + 1) / 2;

		/* Sonderfertigkeiten extrahieren */
		PDFSonderfertigkeiten.Kategorie kat[] = { Kategorie.KAMPF,
				Kategorie.GEWEIHT, Kategorie.LITURGIE };
		sfListe = PDFSonderfertigkeiten.extrahiereKategorien(alleSF.keySet(), kat);
		if (sfListe.size() == 0) {
			sfListe.addAll(alleSF.keySet());
		}
		
		for (PDFSonderfertigkeiten sf : sfListe) {
			alleSF.put(sf, true);
		}

		/* Layout für den Rest errechnen */
		hoehe = 72;
		patzerHoehe = 13;
		blockBreite = (cellCountX - 3) / 4;
		kampfBreite = blockBreite * 3 + 2;

		do {
			/* Variable Daten für Frontseite erfassen */
			nahkampf = new ArrayList<Nahkampfwaffe>();
			fernkampf = new ArrayList<Fernkampfwaffe>();
			ruestung = new ArrayList<Kampfset>();
			schilde = new ArrayList<Schild>();

			zeigeSchilde = false;

			for (Kampfset set : kampfsets) {
				for (Nahkampfwaffe w : set.getNahkampfwaffen()
						.getNahkampfwaffe()) {
					w.setNummer(set.getNr());
					nahkampf.add(w);
				}

				for (Fernkampfwaffe w : set.getFernkampfwaffen()
						.getFernkampfwaffe()) {
					w.setNummer(set.getNr());
					fernkampf.add(w);
				}

				for (Schild s : set.getSchilder().getSchild()) {
					s.setNummer(set.getNr());
					schilde.add(s);
					zeigeSchilde = true;
				}
				if (set.getRuestungen().getRuestung().size() > 0) {
					ruestung.add(set);
				}
			}

			/* Layout Parameter */
			if (kampfsets.size() == 1) {
				patzerBreite = (kampfBreite - 1) / 2;
			} else {
				patzerBreite = (kampfBreite - 1) * 2 / 5;
			}
			/* variabler Kampfteil */
			bloecke = 3;
			if (zeigeSchilde) {
				/* 4 variabel große Blöcke zusammen mit Schilden/Parierwaffen */
				bloecke++;
			}

			leer = hoehe - (5 + 1 + 9 + 1 + vorNachTeileLaenge + 1) - patzerHoehe;
			leer -= 2 + nahkampf.size();
			leer -= 2 + fernkampf.size();
			leer -= 2 + ruestung.size();
			if (zeigeSchilde) {
				leer -= 2 + schilde.size();
			}

			if (leer >= 3 + bloecke) {
				notizen = leer - bloecke;
				leer -= notizen;
			} else {
				notizen = 0;
			}

			if (leer < 0) {
				/*
				 * letztes Kampfset entfernen um genug Platz für den Rest zu
				 * bekommen
				 */
				kampfsets.remove(kampfsets.size() - 1);
			}
		} while (leer < 0);

		/* Kann Seite gekürzt werden ? */
		if (notizen > 8) {
			int alteHoehe = hoehe;
			
			hoehe = Math.max(60, hoehe - (notizen-8));
			notizen -= (alteHoehe - hoehe);
		}
		
		/* Fixen Teil der PDF Seite erzeugen */
		initPDFStream(hoehe);

		stream.setStrokingColor(Color.BLACK);
		stream.setLineWidth(1f);

		y = charakterDaten(daten);
		if (y == 4) {
			/* korrigieren für kurzen Header */
			leer++;
		}
		festerHeaderHoehe = basisWerte(y, daten, bild, guteEigenschaften);

		/* Padding für Kampfblöcke und Vor/Nachteile */
		while (leer > 0) {
			if (leer > 0) {
				nahkampf.add(null);
				leer--;
			}

			if (leer > 0) {
				fernkampf.add(null);
				leer--;
			}

			if (leer > 0) {
				ruestung.add(null);
				leer--;
			}
			if (zeigeSchilde && leer > 0) {
				schilde.add(null);
				leer--;
			}
		}

		/* Flexiblen Teil der PDF Seite erzeugen */
		y = vorteileNachteile(festerHeaderHoehe, vorteile, nachteile,
				vorNachTeileLaenge);

		PDFSonderfertigkeiten.zeichneTabelle(this, kampfBreite + 1, y,
				cellCountX, hoehe, "Sonderfertigkeiten", sfListe);

		if (notizen > 0) {
			drawLabeledBox(0, y, kampfBreite, y + notizen - 1, "Notizen");

			if (!daten.getAngaben().getNotizen().getText().equals("Notizen")) {
				Notizen n = daten.getAngaben().getNotizen();

				String[] t = { n.getN0(), n.getN1(), n.getN2(), n.getN3(),
						n.getN4(), n.getN5(), n.getN6(), n.getN7(), n.getN8(),
						n.getN9(), n.getN10(), n.getN11() };

				for (int i = 0; i < notizen - 2 && i < t.length && t[i] != null; i++) {
					drawText(PDType1Font.HELVETICA, 0, kampfBreite, y + i + 1,
							t[i], false);
				}
			}
			y += notizen;
		}

		y = drawTabelle(0, kampfBreite, y,
				nahkampf.toArray(new Nahkampfwaffe[nahkampf.size()]),
				new NahkampfTable(kampfBreite));
		y = drawTabelle(0, kampfBreite, y,
				fernkampf.toArray(new Fernkampfwaffe[fernkampf.size()]),
				new FernkampfTabelle(kampfBreite));
		if (tzm) {
			y = drawTabelle(0, kampfBreite, y, ruestung.toArray(),
					new RuestungsTabelle(kampfBreite));
		} else {
			y = drawTabelle(0, kampfBreite, y, ruestung.toArray(),
					new RuestungsTabelleOhneTZM(kampfBreite));
		}
		if (zeigeSchilde) {
			y = drawTabelle(0, kampfBreite, y, schilde.toArray(),
					new ParierwaffenTabelle(kampfBreite));
		}

		/* 13 Zeilen fuer den Rest */
		y = cellCountY - patzerHoehe;
		patzerBlock(0, patzerBreite, y);

		y = initiativeAusweichen(kampfsets, patzerBreite + 1, kampfBreite, y,
				tzm);

		y = waffenlos(kampfsets, patzerBreite + 1, kampfBreite, y, tzm);

		y = basisKampfBlock(daten.getEigenschaften(), patzerBreite + 1,
				kampfBreite, y);

		stream.close();
	}

	private int basisKampfBlock(Eigenschaften eigen, int x1, int x2, int y)
			throws IOException {
		String[][] werte;

		werte = new String[4][3];
		werte[0] = new String[] { "Attacke",
				eigen.getAttacke().getAkt().toString(), "(MU+GE+KK)/5" };
		werte[1] = new String[] { "Parade",
				eigen.getParade().getAkt().toString(), "(IN+GE+KK)/5" };
		werte[2] = new String[] { "Fernkampf",
				eigen.getFernkampfBasis().getAkt().toString(), "(IN+FF+KK)/5" };
		werte[3] = new String[] { "Initiative",
				eigen.getInitiative().getAkt().toString(), "(2xMU+IN+GE)/5" };

		drawTabelle(x1, x2, y, werte, new BasisKampfTabelle(x2 - x1));
		return y + werte.length + 2;
	}

	private int basisWerte(int offsetY, Daten daten, PDJpeg bild,
			String[] guteEigW) throws IOException {
		int boxW;
		int offset2, offset3, offset4;
		int height;

		String[] guteEig = { "Mut", "Klugheit", "Intuition", "Charisma",
				"Fingerfertigkeit", "Gewandheit", "Konstitution", "Körperkraft" };

		String[] basis = { "Lebensenergie", "Ausdauer", "Astralenergie",
				"Karmaenergie", "Magieresistenz", "Abenteuerpunkte",
				"verbraucht", "übrig" };
		String[] basisW;

		String[] sonstige = { "Geschwindigkeit", "Sozialstatus", "Stufe",
				"Wundschwelle", "Regeneration LE", "Regeneration AE" };
		String[] sonstigeW;

		Angaben angaben = daten.getAngaben();
		Eigenschaften eigenschaften = daten.getEigenschaften();

		basisW = new String[basis.length];
		basisW[0] = eigenschaften.getLebensenergie().getAkt().toString();
		basisW[1] = eigenschaften.getAusdauer().getAkt().toString();
		if (angaben.isMagisch()) {
			basisW[2] = eigenschaften.getAstralenergie().getAkt().toString();
		} else {
			basisW[2] = "";
		}
		if (angaben.isGeweiht()) {
			basisW[3] = eigenschaften.getKarmaenergie().getAkt().toString();
		} else {
			basisW[3] = "";
		}
		basisW[4] = eigenschaften.getMagieresistenz().getAkt().toString();
		basisW[5] = angaben.getAp().getGesamt().toString();
		basisW[6] = angaben.getAp().getGenutzt().toString();
		basisW[7] = angaben.getAp().getFrei().toString();

		sonstigeW = new String[sonstige.length];
		sonstigeW[0] = eigenschaften.getGeschwindigkeit().getAkt().toString();
		sonstigeW[1] = eigenschaften.getSozialstatus().getAkt().toString();
		sonstigeW[2] = angaben.getStufe41().getAkt().toString();
		sonstigeW[3] = angaben.getWundschwelle().toString();
		sonstigeW[4] = angaben.getLeregeneration();
		if (angaben.isMagisch()) {
			sonstigeW[5] = angaben.getAspregeneration();
		} else {
			sonstigeW[5] = "";
		}

		/* formatierung */
		height = 8;
		boxW = 3;

		offset2 = viertelBreite + 1;
		offset3 = offset2 + viertelBreite + 1;
		offset4 = offset3 + viertelBreite + 1;

		if (bild != null) {
			drawImage(offset4, offsetY + 1, cellCountX, offsetY + height + 1,
					bild);
		}

		drawLabeledBox(0, offsetY, viertelBreite, offsetY + height + 1,
				"Gute Eigenschaften");
		drawLabeledBox(offset2, offsetY, offset2 + viertelBreite, offsetY
				+ height + 1, "Grundwerte");
		drawLabeledBox(offset3, offsetY, offset3 + viertelBreite, offsetY
				+ height + 1, "Sonstige Werte");
		drawLabeledBox(offset4, offsetY, cellCountX, offsetY + height + 1,
				"Bild");

		stream.setLineWidth(0.1f);

		for (int y = offsetY + 1; y < offsetY + 10; y++) {
			addLine(viertelBreite - 2 * boxW, y, viertelBreite, y);
			addLine(offset2 + viertelBreite - 2 * boxW, y, offset2
					+ viertelBreite, y);
			addLine(offset3 + viertelBreite - 2 * boxW, y, offset3
					+ viertelBreite, y);
		}
		addLine(viertelBreite - 2 * boxW, offsetY + 1,
				viertelBreite - 2 * boxW, offsetY + 1 + height);
		addLine(viertelBreite - boxW, offsetY + 1, viertelBreite - boxW,
				offsetY + 1 + height);
		addLine(offset2 + viertelBreite - 2 * boxW, offsetY + 1, offset2
				+ viertelBreite - 2 * boxW, offsetY + 1 + height);
		addLine(offset2 + viertelBreite - boxW, offsetY + 1, offset2
				+ viertelBreite - boxW, offsetY + 1 + height);
		addLine(offset3 + viertelBreite - 2 * boxW, offsetY + 1, offset3
				+ viertelBreite - 2 * boxW, offsetY + 1 + height);
		addLine(offset3 + viertelBreite - boxW, offsetY + 1, offset3
				+ viertelBreite - boxW, offsetY + 1 + height);
		stream.closeAndStroke();

		for (int y = 0; y < guteEig.length; y++) {
			drawText(PDType1Font.HELVETICA_BOLD, 0, viertelBreite - 2 * boxW, y
					+ offsetY + 1, guteEig[y], false);

			drawText(PDType1Font.HELVETICA, viertelBreite - 2 * boxW,
					viertelBreite - boxW, y + offsetY + 1, guteEigW[y], true);
		}
		for (int y = 0; y < basis.length; y++) {
			drawText(PDType1Font.HELVETICA_BOLD, offset2
					+ (y == 6 || y == 7 ? 2 : 0), offset2 + viertelBreite - 2
					* boxW, y + offsetY + 1, basis[y], false);

			drawText(PDType1Font.HELVETICA, offset2 + viertelBreite - 2 * boxW,
					offset2 + viertelBreite - boxW, y + offsetY + 1, basisW[y],
					true);
		}
		int len = sonstige.length;
		if (!angaben.isMagisch()) {
			len--;
		}
		for (int y = 0; y < len; y++) {
			drawText(PDType1Font.HELVETICA_BOLD, offset3, offset3
					+ viertelBreite - 2 * boxW, y + offsetY + 1, sonstige[y],
					false);

			drawText(PDType1Font.HELVETICA, offset3 + viertelBreite - 2 * boxW,
					offset3 + viertelBreite - boxW, y + offsetY + 1,
					sonstigeW[y], true);
		}

		return offsetY + 2 + height;
	}

	private int charakterDaten(Daten daten) throws IOException {
		Angaben angaben = daten.getAngaben();
		int zeile = 0;

		/* erste Zeile */
		drawText(PDType1Font.HELVETICA_BOLD, 0, 4, zeile, "Name:", false);
		drawText(PDType1Font.HELVETICA, 4, 31, zeile, angaben.getName(), true);

		drawText(PDType1Font.HELVETICA_BOLD, 30, 34, zeile, "Stand:", false);
		drawText(PDType1Font.HELVETICA, 34, 40, zeile, angaben.getStand(),
				false);

		drawText(PDType1Font.HELVETICA_BOLD, 40, 43, zeile, "Alter:", false);
		drawText(PDType1Font.HELVETICA, 43, 46, zeile, angaben.getAlter()
				.toString(), true);

		drawText(PDType1Font.HELVETICA_BOLD, 46, 52, zeile, "Geburtstag:",
				false);
		drawText(PDType1Font.HELVETICA, 52, cellCountX, zeile,
				angaben.getGeburtstag(), true);
		zeile++;

		/* zweite Zeile (evt. doppelt) */
		if (angaben.getRasse().length() > 20
				|| angaben.getKultur().length() > 40
				|| angaben.getProfession().getText().length() > 60) {
			drawText(PDType1Font.HELVETICA_BOLD, 0, 4, zeile, "Rasse:", false);
			drawText(PDType1Font.HELVETICA, 4, 30, zeile, angaben.getRasse(),
					true);

			drawText(PDType1Font.HELVETICA_BOLD, 30, 35, zeile, "Kultur:",
					false);
			drawText(PDType1Font.HELVETICA, 35, cellCountX, zeile,
					angaben.getKultur(), true);
			zeile++;

			drawText(PDType1Font.HELVETICA_BOLD, 0, 6, zeile, "Profession:",
					false);
			drawText(PDType1Font.HELVETICA, 6, cellCountX, zeile, angaben
					.getProfession().getText(), true);
			zeile++;
		} else {
			drawText(PDType1Font.HELVETICA_BOLD, 0, 4, zeile, "Rasse:", false);
			drawText(PDType1Font.HELVETICA, 4, 11, zeile, angaben.getRasse(),
					true);

			drawText(PDType1Font.HELVETICA_BOLD, 11, 15, zeile, "Kultur:",
					false);
			drawText(PDType1Font.HELVETICA, 15, 30, zeile, angaben.getKultur(),
					true);

			drawText(PDType1Font.HELVETICA_BOLD, 30, 36, zeile, "Profession:",
					false);
			drawText(PDType1Font.HELVETICA, 36, cellCountX, zeile, angaben
					.getProfession().getText(), true);
			zeile++;
		}

		/* dritte Zeile */
		drawText(PDType1Font.HELVETICA_BOLD, 0, 7, zeile, "Geschlecht:", false);
		drawText(PDType1Font.HELVETICA, 7, 9, zeile, angaben.getGeschlecht()
				.substring(0, 1), false);

		drawText(PDType1Font.HELVETICA_BOLD, 11, 15, zeile, "Größe:", false);
		drawText(PDType1Font.HELVETICA, 15, 20, zeile, angaben.getGroesse()
				.toString() + " cm", false);

		drawText(PDType1Font.HELVETICA_BOLD, 20, 25, zeile, "Gewicht:", false);
		drawText(PDType1Font.HELVETICA, 25, 30, zeile, angaben.getGewicht()
				.toString() + " kg", false);

		drawText(PDType1Font.HELVETICA_BOLD, 30, 36, zeile, "Haarfarbe:", false);
		drawText(PDType1Font.HELVETICA, 36, 44, zeile, angaben.getHaarfarbe(),
				false);

		drawText(PDType1Font.HELVETICA_BOLD, 44, 52, zeile, "Augenfarbe:",
				false);
		drawText(PDType1Font.HELVETICA, 52, cellCountX, zeile,
				angaben.getAugenfarbe(), true);
		zeile++;

		/* Linien für Charakter-Daten */
		for (int y = 1; y <= zeile; y++) {
			stream.drawLine(leftEdge, getY(y), rightEdge, getY(y));
		}

		return zeile + 1;
	}

	private void extrahiereVorNachteile(Daten daten, List<Vorteil> vorteile,
			List<Vorteil> nachteile) {
		for (Vorteil v : daten.getVorteile().getVorteil()) {
			List<Vorteil> gruppe = null;

			if (v.isIstvorteil()) {
				gruppe = vorteile;
			} else if (v.isIstnachteil()) {
				gruppe = nachteile;
			}

			if (v.getAuswahlen() != null
					&& v.getAuswahlen().getAuswahl().size() > 0) {
				for (String auswahl : v.getAuswahlen().getAuswahl()) {
					gruppe.add(new VorteilAuswahl(v, auswahl));
				}
			} else {
				gruppe.add(v);
			}
		}
	}

	private int initiativeAusweichen(List<Kampfset> sets, int x1, int x2,
			int y, boolean tzm) throws IOException {
		String[][] data;
		List<String> data0, data1;

		data0 = new ArrayList<String>();
		data1 = new ArrayList<String>();
		data0.add("Initiative");
		data1.add("Ausweichen");
		for (Kampfset set : sets) {
			long ini;

			ini = set.getIni().longValue();
			if (tzm) {
				ini -= Long.parseLong(set.getRuestungzonen().getBehinderung());
			} else {
				ini -= Long
						.parseLong(set.getRuestungeinfach().getBehinderung());
			}
			data0.add(Long.toString(ini));
			data1.add(set.getAusweichen().toString());
		}

		data = new String[2][4];
		data[0] = data0.toArray(new String[data0.size()]);
		data[1] = data1.toArray(new String[data0.size()]);

		drawTabelle(x1, x2, y, data, new IniAusweichenTabelle(sets.size() + 1,
				x2 - x1));
		return y + 4;
	}

	private void patzerBlock(int x1, int x2, int y) throws IOException {
		String nahkampfPatzer[][];
		String fernkampfPatzer[][];

		nahkampfPatzer = new String[6][3];
		nahkampfPatzer[0] = new String[] { "2", "-4", "Waffe zerstört" };
		nahkampfPatzer[1] = new String[] { "3-5", "-2", "Sturz" };
		nahkampfPatzer[2] = new String[] { "6-8", "-2", "Stolpern" };
		nahkampfPatzer[3] = new String[] { "9-10", "-2", "Waffe verloren" };
		nahkampfPatzer[4] = new String[] { "11", "-3", "Eigentreffer" };
		nahkampfPatzer[5] = new String[] { "12", "-4", "Schwerer Eigentreffer" };

		fernkampfPatzer = new String[4][3];
		fernkampfPatzer[0] = new String[] { "2", "-4", "Waffe zerstört" };
		fernkampfPatzer[1] = new String[] { "3", "-3", "Waffe beschädigt " };
		fernkampfPatzer[2] = new String[] { "4-10", "-2", "Fehlschuss" };
		fernkampfPatzer[3] = new String[] { "11-12", "-3",
				"Gefährten getroffen" };

		y = drawTabelle(x1, x2, y, nahkampfPatzer, new PatzerTabelle(
				"Nahkampfpatzer (WdS Seite 85)", x2 - x1));
		y = drawTabelle(x1, x2, y, fernkampfPatzer, new PatzerTabelle(
				"Fernkampfpatzer (WdS Seite 99)", x2 - x1));
	}

	private int vorteileNachteile(int offset, List<Vorteil> vorteile,
			List<Vorteil> nachteile, int count) throws IOException {
		int breite, y;
		List<Vorteil> vorteile1, vorteile2, nachteile1, nachteile2;
		breite = 15;

		/* aufspalten */
		vorteile1 = new ArrayList<Vorteil>();
		vorteile2 = new ArrayList<Vorteil>();
		nachteile1 = new ArrayList<Vorteil>();
		nachteile2 = new ArrayList<Vorteil>();

		y = (vorteile.size() + 1) / 2;
		vorteile1.addAll(vorteile.subList(0, y));
		vorteile2.addAll(vorteile.subList(y, vorteile.size()));

		y = (nachteile.size() + 1) / 2;
		nachteile1.addAll(nachteile.subList(0, y));
		nachteile2.addAll(nachteile.subList(y, nachteile.size()));

		/* add padding */
		for (y = vorteile1.size(); y < count; y++) {
			vorteile1.add(null);
		}
		for (y = vorteile2.size(); y < count; y++) {
			vorteile2.add(null);
		}
		for (y = nachteile1.size(); y < count; y++) {
			nachteile1.add(null);
		}
		for (y = nachteile2.size(); y < count; y++) {
			nachteile2.add(null);
		}

		drawTabelle(0, breite, offset, vorteile1.toArray(), new VorteilTabelle(
				breite));
		drawTabelle(breite + 1, 2 * breite + 1, offset, vorteile2.toArray(),
				new VorteilTabelle(breite));
		drawTabelle(2 * breite + 2, 3 * breite + 2, offset,
				nachteile1.toArray(), new VorteilTabelle("Nachteile", breite));
		drawTabelle(3 * breite + 3, cellCountX, offset, nachteile2.toArray(),
				new VorteilTabelle("Nachteile", breite));

		return offset + count + 2;
	}

	private int waffenlos(List<Kampfset> sets, int x1, int x2, int y,
			boolean tzm) throws IOException {
		String[][] daten;
		int anzahl;
		String be;

		if (sets.size() == 0) {
			drawTabelle(x1, x2, y, new String[] { null, null },
					new WaffenlosTabelle(x2 - x1, 1));
		}
		daten = new String[2][8];
		daten[0][0] = "Raufen";
		daten[0][1] = sets.get(0).getRaufen().getTp();
		daten[1][0] = "Ringen";
		daten[1][1] = sets.get(0).getRingen().getTp();

		if (tzm) {
			be = sets.get(0).getRuestungzonen().getBehinderung();
		} else {
			be = sets.get(0).getRuestungeinfach().getBehinderung();
		}
		for (int i = 0; i < sets.size(); i++) {
			daten[0][2 + i * 2] = sets.get(i).getRaufen().getAt();
			daten[0][3 + i * 2] = sets.get(i).getRaufen().getPa();
			daten[1][2 + i * 2] = sets.get(i).getRingen().getAt();
			daten[1][3 + i * 2] = sets.get(i).getRingen().getPa();

			if (tzm) {
				if (!sets.get(i).getRuestungzonen().getBehinderung().equals(be)) {
					be = null;
				}
			} else {
				if (!sets.get(i).getRuestungeinfach().getBehinderung()
						.equals(be)) {
					be = null;
				}
			}
		}

		anzahl = be != null ? 4 : (2 + sets.size() * 2);
		drawTabelle(x1, x2, y, daten, new WaffenlosTabelle(x2 - x1, anzahl));
		return y + 4;
	}

	private class BasisKampfTabelle extends AbstractTabellenZugriff {
		public BasisKampfTabelle(int breite) {
			super(new String[] { null, "Wert", "Formel" },
					new int[] { 0, 4, 12 }, 0, "Basiswerte", breite);
		}

		@Override
		public String get(Object obj, int x) {
			String[] daten = (String[]) obj;
			return daten[x];
		}
	}

	private class FernkampfTabelle extends AbstractTabellenZugriff {
		public FernkampfTabelle(int breite) {
			super(new String[] { "#", null, "AT", "TP", "Entfernung",
					"TP/Entfernung" }, new int[] { 2, 0, 3, 5, 10, 8 }, 0,
					"Fernkampfwaffe", breite);
		}

		@Override
		public String get(Object obj, int x) {
			Fernkampfwaffe waffe = (Fernkampfwaffe) obj;

			switch (x) {
			case 0:
				return waffe.getNummer().toString();
			case 1:
				return waffe.getName();
			case 2:
				return waffe.getAt();
			case 3:
				return waffe.getTp();
			case 4:
				return waffe.getReichweite();
			case 5:
				return waffe.getTpmod();
			}
			return "";
		}

		@Override
		public Color getBackgroundColor(Object o, int x) {
			Fernkampfwaffe waffe = (Fernkampfwaffe) o;

			return waffe.getNummer().longValue() == 2 ? Color.LIGHT_GRAY : null;
		}
	}

	private class IniAusweichenTabelle extends AbstractTabellenZugriff {
		public IniAusweichenTabelle(int colcount, int breite) {
			super(new String[] { null, "#1", "#2", "#3" }, new int[] { 0, 3, 3,
					3 }, colcount, "BE-abhängige Werte", breite);
		}

		@Override
		public String get(Object obj, int x) {
			String[] data = (String[]) obj;

			return data[x];
		}

		@Override
		public Color getBackgroundColor(Object o, int x) {
			return x == 2 ? Color.LIGHT_GRAY : null;
		}
	}

	private class NahkampfTable extends AbstractTabellenZugriff {
		public NahkampfTable(int width) {
			super(new String[] { "#", null, "AT", "PA", "TP", "TP/KK", "DK",
					"INI", "Bruchfaktor", "", "", "", "" }, new int[] { 2, 0,
					3, 3, 4, 4, 3, 3, 2, 2, 2, 2, 2 }, 0, "Nahkampfwaffe",
					width);
		}

		@Override
		public String get(Object obj, int x) {
			Nahkampfwaffe waffe = (Nahkampfwaffe) obj;

			switch (x) {
			case 0:
				return waffe.getNummer().toString();
			case 1:
				return waffe.getName();
			case 2:
				return waffe.getAt();
			case 3:
				return waffe.getPa();
			case 4:
				return waffe.getTpinkl();
			case 5:
				return waffe.getTpkk().getSchwelle().toString() + "/"
						+ waffe.getTpkk().getSchrittweite().toString();
			case 6:
				return waffe.getDk().replace(" ", "");
			case 7:
				return waffe.getIni().toString();
			case 8:
				if (waffe.getBfakt().intValue() == -9) {
					return "-";
				}
				return waffe.getBfakt().toString();
			}
			return "";
		}

		@Override
		public Color getBackgroundColor(Object o, int x) {
			Nahkampfwaffe waffe = (Nahkampfwaffe) o;

			return waffe.getNummer().longValue() == 2 ? Color.LIGHT_GRAY : null;
		}

		@Override
		public int getColumnSpan(int x) {
			return x == 8 ? 5 : 1;
		}
	}

	private class ParierwaffenTabelle extends AbstractTabellenZugriff {
		public ParierwaffenTabelle(int breite) {
			super(new String[] { "#", null, "PA", "WM", "INI", "Bruchfaktor",
					"", "", "", "" },
					new int[] { 2, 0, 3, 4, 3, 2, 2, 2, 2, 2 }, 0,
					"Parierwaffe/Schild", breite);
		}

		@Override
		public String get(Object obj, int x) {
			Schild s = (Schild) obj;

			switch (x) {
			case 0:
				return s.getNummer().toString();
			case 1:
				return s.getName();
			case 2:
				return s.getPa();
			case 3:
				return s.getMod();
			case 4:
				return s.getIni().toString();
			case 5:
				return s.getBfakt().toString();
			}
			return "";
		}

		@Override
		public Color getBackgroundColor(Object o, int x) {
			Schild waffe = (Schild) o;

			return waffe.getNummer().longValue() == 2 ? Color.LIGHT_GRAY : null;
		}

		@Override
		public int getColumnSpan(int x) {
			return x == 5 ? 5 : 1;
		}
	}

	private class PatzerTabelle extends AbstractTabellenZugriff {
		public PatzerTabelle(String titel, int breite) {
			super(new String[] { "Wurf", "INI", null }, new int[] { 3, 3, 0 },
					0, titel, breite);
		}

		@Override
		public String get(Object obj, int x) {
			String[] daten = (String[]) obj;
			return daten[x];
		}
	}

	private class RuestungsTabelle extends AbstractTabellenZugriff {
		public RuestungsTabelle(int breite) {
			super(new String[] { "#", null, "RS", "BE", "Ko", "Br", "Rü", "Ba",
					"LA", "RA", "LB", "RB" }, new int[] { 2, 0, 3, 3, 2, 2, 2,
					2, 2, 2, 2, 2 }, 0, "Rüstung", breite);
		}

		public RuestungsTabelle(String[] col, int[] colwidth, int colcount,
				String titel, int width) {
			super(col, colwidth, colcount, titel, width);
		}

		@Override
		public String get(Object obj, int x) {
			Kampfset set = (Kampfset) obj;
			String name, name2;

			switch (x) {
			case 0:
				return set.getNr().toString();
			case 1:
				name = "";

				for (Ruestung r : set.getRuestungen().getRuestung()) {
					if (name.length() > 0) {
						name2 = "/";
					} else {
						name2 = "";
					}
					name2 = name2 + r.getName();

					if (name.length() + name2.length() > 45) {
						return name.length() + "/...";
					}
					name = name + name2;
				}
				return name;
			case 2:
				return filter(set.getRuestungzonen().getGesamtzonenschutz());
			case 3:
				return set.getRuestungzonen().getBehinderung();
			case 4:
				return filter(set.getRuestungzonen().getKopf());
			case 5:
				return filter(set.getRuestungzonen().getBrust());
			case 6:
				return filter(set.getRuestungzonen().getRuecken());
			case 7:
				return filter(set.getRuestungzonen().getBauch());
			case 8:
				return filter(set.getRuestungzonen().getLinkerarm());
			case 9:
				return filter(set.getRuestungzonen().getRechterarm());
			case 10:
				return filter(set.getRuestungzonen().getLinkesbein());
			case 11:
				return filter(set.getRuestungzonen().getRechtesbein());
			}
			return "";
		}

		@Override
		public Color getBackgroundColor(Object o, int x) {
			Kampfset waffe = (Kampfset) o;

			return waffe.getNr().longValue() == 2 ? Color.LIGHT_GRAY : null;
		}

		protected String filter(BigInteger input) {
			if (input.intValue() == 0) {
				return "";
			}
			return input.toString();
		}
	}

	private class RuestungsTabelleOhneTZM extends RuestungsTabelle {
		public RuestungsTabelleOhneTZM(int breite) {
			super(new String[] { "#", null, "RS", "BE" }, new int[] { 2, 0, 3,
					3 }, 0, "Rüstung", breite);
		}

		@Override
		public String get(Object obj, int x) {
			Kampfset set = (Kampfset) obj;

			switch (x) {
			case 2:
				return filter(set.getRuestungeinfach().getGesamt());
			case 3:
				return set.getRuestungeinfach().getBehinderung().trim();
			}
			return super.get(obj, x);
		}
	}

	private class VorteilAuswahl extends Vorteil {
		private Vorteil referenz;
		private String auswahl;

		public VorteilAuswahl(Vorteil referenz, String auswahl) {
			super();
			this.referenz = referenz;
			this.auswahl = auswahl;
		}

		public Vorteil getVAReferenz() {
			return referenz;
		}

		public String getVAText() {
			return auswahl;
		}
	}

	private class VorteilTabelle extends AbstractTabellenZugriff {
		boolean vorteil;

		public VorteilTabelle(int breite) {
			super(new String[] { null }, new int[] { 0 }, 0, "Vorteile", breite);
			vorteil = true;
		}

		public VorteilTabelle(String titel, int breite) {
			super(new String[] { null, "Wert" }, new int[] { 0, 2 }, 0, titel,
					breite);
			vorteil = false;
		}

		@Override
		public String get(Object obj, int x) {
			if (obj instanceof VorteilAuswahl) {
				VorteilAuswahl va = (VorteilAuswahl) obj;

				if (x == 0) {
					return va.getVAReferenz().getBezeichner() + ": "
							+ va.getVAText();
				} else {
					return "";
				}
			}

			Vorteil v = (Vorteil) obj;
			String out;

			switch (x) {
			case 0:
				out = v.getBezeichner();
				if (vorteil && v.getWert() != null) {
					out = out + " " + v.getWert().toString();
				}
				return out;
			case 1:
				if (v.getWert() != null) {
					return v.getWert().toString();
				}
				return "";
			}
			return "";
		}
	}

	private class WaffenlosTabelle extends AbstractTabellenZugriff {
		public WaffenlosTabelle(int breite, int anzahl) {
			super(
					new String[] { null, "TP(A)", anzahl == 1 ? "AT/PA" : "AT/PA #1",
							"", "AT/PA #2", "", "AT/PA #3", "" },
							new int[] { 0, 4, 3, 3, 3, 3, 3, 3 },
							anzahl, "Waffenlos", breite);
		}

		@Override
		public String get(Object obj, int x) {
			String daten[] = (String[]) obj;
			return daten[x];
		}

		@Override
		public Color getBackgroundColor(Object o, int x) {
			return x == 4 || x == 5 ? Color.LIGHT_GRAY : null;
		}
		
		@Override
		public int getColumnSpan(int x) {
			return (x == 2 || x == 4 || x == 6) ? 2 : 1;
		}
	}
}

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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.Collator;
import java.util.*;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.hrogge.CompactPDFExport.PDFSonderfertigkeiten.Kategorie;
import de.hrogge.CompactPDFExport.gui.Konfiguration;
import org.w3c.dom.Text;

public class FrontSeite extends PDFSeite {
	public FrontSeite(PDDocument d) throws IOException {
		super(d);
	}

	public void erzeugeSeite(ExtXPath xpath, PDJpeg bild, PDJpeg hintergrund,
			String[] guteEigenschaften, List<PDFSonderfertigkeiten> alleSF,
			Hausregeln h, List<String> commands, boolean tzm, Konfiguration k) throws IOException, XPathExpressionException {
		int patzerHoehe, patzerBreite, festerHeaderHoehe, professionsZeilen;
		int notizen, kampfBreite, blockBreite, vorNachTeileLaenge;
		int leer, y, bloecke, hoehe, charakterDatenHoehe, sfY;
		List<PDFVorteil> vorteile, nachteile;
		List<PDFSonderfertigkeiten> sfListe;

		boolean zeigeFernkampf, zeigeRuestung, zeigeSchilde;

		tzm = true;
		NodeList kampfsets = xpath.evaluateList(
				"kampfsets/kampfset[@inbenutzung='true' and @tzm=$0]", Boolean.toString(tzm));
		if (kampfsets.getLength() == 0) {
			kampfsets = xpath.evaluateList("kampfsets/kampfset[@tzm=$0]", Boolean.toString(tzm));
		}

		/* Vor und Nachteile sind statisch */
		vorteile = new ArrayList<PDFVorteil>();
		nachteile = new ArrayList<PDFVorteil>();

		extrahiereVorNachteile(xpath, vorteile, nachteile);

		for (String cmd : commands) {
			String[] split = cmd.split(":");
			String wert = "";
			
			if (split.length == 2) {
				wert = split[1];
			}
			else if (split.length > 2) {
				continue;
			}
//			PDFVorteil v = h.getEigenenVorteil(split[0], wert);
//			if (v != null) {
//				vorteile.add(v);
//			}
//
//			v = h.getEigenenNachteil(split[0], wert);
//			if (v != null) {
//				nachteile.add(v);
//			}
		}

		if (!k.getOptionsDaten(Konfiguration.FRONT_MEHRSF)) {
			vorNachTeileLaenge = (Math.max(vorteile.size(), nachteile.size()) + 1) / 2;
		} else if (vorteile.size() > nachteile.size()) {
			vorNachTeileLaenge = Math.max((vorteile.size() + 1) / 2,
					nachteile.size());
		} else {
			vorNachTeileLaenge = Math.max(vorteile.size(),
					(nachteile.size() + 1) / 2);
		}

		/* Sonderfertigkeiten extrahieren */
		PDFSonderfertigkeiten.Kategorie kat[] = { Kategorie.KAMPF,
				Kategorie.GEWEIHT, Kategorie.LITURGIE };
		sfListe = PDFSonderfertigkeiten.extrahiereKategorien(alleSF, kat);

		/* Layout für den Rest errechnen */
		hoehe = 72;
		patzerHoehe = 13;
		blockBreite = (cellCountX - 3) / 4;
		kampfBreite = blockBreite * 3 + 2;

		/* Wieviele Zeilen werden fuer die Profession gebraucht ? */
		professionsZeilen = berechneProfessionsZeilen(xpath);

		Map<Integer, Node> nahkampf = new TreeMap<Integer, Node>();
		Map<Integer, Node> fernkampf = new TreeMap<Integer, Node>();
		Map<Integer, Node> ruestung = new TreeMap<Integer, Node>();
		Map<Integer, Node> schilde = new TreeMap<Integer, Node>();

		leer = -1;
		notizen = 0;
		patzerBreite = 0;

		zeigeFernkampf = false;
		zeigeRuestung = false;
		zeigeSchilde = false;

		for (int visibleSets = kampfsets.getLength(); visibleSets > 0 && leer < 0; visibleSets--) {
			NodeList list;
			int number;
			
			/* Variable Daten für Frontseite erfassen */
			nahkampf.clear();
			fernkampf.clear();
			ruestung.clear();
			schilde.clear();

			zeigeFernkampf = k
					.getOptionsDaten(Konfiguration.FRONT_IMMER_FERNKAMPF);
			zeigeRuestung = k
					.getOptionsDaten(Konfiguration.FRONT_IMMER_RUESTUNGEN);
			zeigeSchilde = k.getOptionsDaten(Konfiguration.FRONT_IMMER_SCHILDE);

			for (int setidx = 0; setidx < visibleSets; setidx++) {
				number = xpath.evaluateInt("@nr", kampfsets.item(setidx));
				list = xpath.evaluateList("nahkampfwaffen/nahkampfwaffe", kampfsets.item(setidx));
				for (int idx = 0; idx < list.getLength(); idx++) {
					nahkampf.put(number, list.item(idx));
				}

				list = xpath.evaluateList("fernkampfwaffen/fernkampfwaffe", kampfsets.item(setidx));
				for (int idx = 0; idx < list.getLength(); idx++) {
					fernkampf.put(number, list.item(idx));
					zeigeFernkampf = true;
				}

				list = xpath.evaluateList("schilder/schild", kampfsets.item(setidx));
				for (int idx = 0; idx < list.getLength(); idx++) {
					schilde.put(number, list.item(idx));
					zeigeSchilde = true;
				}

				list = xpath.evaluateList("ruestungen/ruestung", kampfsets.item(setidx));
				if (list.getLength() > 0) {
					ruestung.put(number, kampfsets.item(setidx));
					zeigeRuestung = true;
				}
			}

			/* Layout Parameter */
			if (kampfsets.getLength() == 1) {
				patzerBreite = (kampfBreite - 1) / 2;
			} else {
				patzerBreite = (kampfBreite - 1) * 2 / 5;
			}
			/* variabler Kampfteil */
			bloecke = 1;
			if (zeigeFernkampf) {
				bloecke++;
			}
			if (zeigeRuestung) {
				bloecke++;
			}
			if (zeigeSchilde) {
				bloecke++;
			}

			leer = hoehe
					- (4 + professionsZeilen + 1 + 9 + 1 + vorNachTeileLaenge + 1)
					- patzerHoehe;
			leer -= 2 + nahkampf.size();
			if (zeigeFernkampf) {
				leer -= 2 + fernkampf.size();
			}
			if (zeigeRuestung) {
				leer -= 2 + ruestung.size();
			}
			if (zeigeSchilde) {
				leer -= 2 + schilde.size();
			}

			if (leer >= 3 + bloecke) {
				notizen = leer - bloecke;
				leer -= notizen;
			} else {
				notizen = 0;
			}

			if (leer >= 0) {
				break;
			}
		}

		/* Kann Seite gekürzt werden ? */
		if (notizen > 8) {
			int alteHoehe = hoehe;

			hoehe = Math.max(60, hoehe - (notizen - 8));
			notizen -= (alteHoehe - hoehe);
		}

		/* Fixen Teil der PDF Seite erzeugen */
		initPDFStream(hoehe);

		stream.setStrokingColor(Color.BLACK);
		stream.setLineWidth(1f);

		y = charakterDatenHoehe = charakterDaten(xpath, professionsZeilen);

		festerHeaderHoehe = basisWerte(y, xpath, bild, guteEigenschaften, k);

		/* Padding für Kampfblöcke und Vor/Nachteile */
		for (int i=65535; leer > 0; ) {
			if (leer > 0) {
				nahkampf.put(i++, null);
				leer--;
			}

			if (zeigeFernkampf && leer > 0) {
				fernkampf.put(i++, null);
				leer--;
			}

			if (zeigeRuestung && leer > 0) {
				ruestung.put(i++, null);
				leer--;
			}
			if (zeigeSchilde && leer > 0) {
				schilde.put(i++, null);
				leer--;
			}
		}

		/* Flexiblen Teil der PDF Seite erzeugen */
		y = vorteileNachteile(festerHeaderHoehe, vorteile, nachteile,
				vorNachTeileLaenge, k);

		if (!k.getOptionsDaten(Konfiguration.FRONT_MEHRSF)) {
			sfY = y;
		} else {
			sfY = charakterDatenHoehe;
		}

		if (sfListe.size() < (hoehe - sfY) / 2) {
			sfListe.clear();
			sfListe.addAll(alleSF);
		}
		Collections.sort(sfListe);

		PDFSonderfertigkeiten.zeichneTabelle(this, kampfBreite + 1, sfY,
				cellCountX, hoehe, "Sonderfertigkeiten", sfListe);
		if (notizen > 0) {
			drawLabeledBox(0, y, kampfBreite, y + notizen - 1, "Notizen");

			String[] t = xpath.evaluate("angaben/notizen/text").split("\n");
			for (int i = 0, j = 0; j < notizen - 2 && i < t.length
					&& t[i] != null; i++) {
				if (t[i].length() > 0 &&!t[i].startsWith("@")) {
					drawText(PDType1Font.HELVETICA, 0, kampfBreite, y + j
							+ 1, t[i], false);
					j++;
				}
			}
			y += notizen;
		}

		y = drawTabelle(0, kampfBreite, y,
				nahkampf.values().toArray(),
				new NahkampfTable(xpath, kampfBreite));
		if (zeigeFernkampf) {
			y = drawTabelle(0, kampfBreite, y,
					fernkampf.values().toArray(),
					new FernkampfTabelle(xpath, kampfBreite));
		}
		if (zeigeRuestung) {
			y = drawTabelle(0, kampfBreite, y, ruestung.values().toArray(),
					new RuestungsTabelle(xpath, kampfBreite, tzm));
		}
		if (zeigeSchilde) {
			y = drawTabelle(0, kampfBreite, y, schilde.values().toArray(),
					new ParierwaffenTabelle(xpath, kampfBreite));
		}

		/* 13 Zeilen fuer den Rest */
		y = cellCountY - patzerHoehe;
		patzerBlock(0, patzerBreite, y);

		y = initiativeAusweichen(xpath, kampfsets, patzerBreite + 1, kampfBreite, y,
				tzm);

		y = waffenlos(xpath, kampfsets, patzerBreite + 1, kampfBreite, y, tzm);

		y = basisKampfBlock(xpath, patzerBreite + 1,
				kampfBreite, y);

		stream.close();
	}

	private int basisKampfBlock(ExtXPath xpath, int x1, int x2, int y)
			throws IOException, XPathExpressionException {
		String[][] werte;

		werte = new String[4][3];
		werte[0] = new String[] { "Attacke",
				xpath.evaluate("eigenschaften/attacke/akt"), "(MU+GE+KK)/5" };
		werte[1] = new String[] { "Parade",
				xpath.evaluate("eigenschaften/parade/akt"), "(IN+GE+KK)/5" };
		werte[2] = new String[] { "Fernkampf",
				xpath.evaluate("eigenschaften/fernkampf-basis/akt"), "(IN+FF+KK)/5" };
		werte[3] = new String[] { "Initiative",
				xpath.evaluate("eigenschaften/initiative/akt"), "(2xMU+IN+GE)/5" };

		drawTabelle(x1, x2, y, werte, new BasisKampfTabelle(x2 - x1));
		return y + werte.length + 2;
	}

	private int basisWerte(int offsetY, ExtXPath xpath, PDJpeg bild,
			String[] guteEigW, Konfiguration k) throws IOException, XPathExpressionException {
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

		if (k.getOptionsDaten(Konfiguration.FRONT_KAUFBAREEIGENSCHAFTEN)) {
			guteEig[0] += " (" + kaufbar(xpath, "mut") + ")";
			guteEig[1] += " (" + kaufbar(xpath, "klugheit") + ")";
			guteEig[2] += " (" + kaufbar(xpath, "intuition") + ")";
			guteEig[3] += " (" + kaufbar(xpath, "charisma") + ")";
			guteEig[4] += " (" + kaufbar(xpath, "fingerfertigkeit")
					+ ")";
			guteEig[5] += " (" + kaufbar(xpath, "gewandheit") + ")";
			guteEig[6] += " (" + kaufbar(xpath, "konstitution") + ")";
			guteEig[7] += " (" + kaufbar(xpath, "koerperkraft") + ")";
		}

		basisW = new String[basis.length];
		basisW[0] = xpath.evaluate("eigenschaften/lebensenergie/akt");
		basisW[1] = xpath.evaluate("eigenschaften/ausdauer/akt");
		if (xpath.evaluateBool("angaben/magisch")) {
			basisW[2] = xpath.evaluate("eigenschaften/astralenergie/akt");
		} else {
			basisW[2] = "";
		}
		if (xpath.evaluateBool("angaben/geweiht/text()")) {
			basisW[3] = xpath.evaluate("eigenschaften/karmaenergie/akt");
		} else {
			basisW[3] = "";
		}
		basisW[4] = xpath.evaluate("eigenschaften/magieresistenz/akt");;
		basisW[5] = xpath.evaluate("angaben/ap/gesamt");
		basisW[6] = xpath.evaluate("angaben/ap/genutzt");
		basisW[7] = xpath.evaluate("angaben/ap/frei");

		sonstigeW = new String[sonstige.length];
		sonstigeW[0] = xpath.evaluate("eigenschaften/geschwindigkeit/akt");
		sonstigeW[1] = xpath.evaluate("eigenschaften/sozialstatus/akt");
		sonstigeW[2] = xpath.evaluate("angaben/stufe41/akt");
		sonstigeW[3] = xpath.evaluate("angaben/wundschwelle");
		sonstigeW[4] = xpath.evaluate("angaben/leregeneration");
		if (xpath.evaluateBool("angaben/magisch")) {
			sonstigeW[5] = xpath.evaluate("angaben/aspregeneration");
		} else {
			sonstigeW[5] = "";
		}

		/* formatierung */
		height = 8;
		boxW = 3;

		offset2 = viertelBreite + 1;
		offset3 = offset2 + viertelBreite + 1;
		offset4 = offset3 + viertelBreite + 1;

		drawLabeledBox(0, offsetY, viertelBreite, offsetY + height + 1,
				"Gute Eigenschaften");
		drawLabeledBox(offset2, offsetY, offset2 + viertelBreite, offsetY
				+ height + 1, "Grundwerte");
		drawLabeledBox(offset3, offsetY, offset3 + viertelBreite, offsetY
				+ height + 1, "Sonstige Werte");

		if (!k.getOptionsDaten(Konfiguration.FRONT_MEHRSF)) {
			if (bild != null) {
				drawImage(offset4, offsetY + 1, cellCountX, offsetY + height
						+ 1, bild);
			}

			drawLabeledBox(offset4, offsetY, cellCountX, offsetY + height + 1,
					"Bild");
		}

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
		if (!xpath.evaluateBool("angaben/magisch")) {
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

	private int berechneProfessionsZeilen(ExtXPath xpath) throws IOException, XPathExpressionException {
		float breite;
		String profession = xpath.evaluate("angaben/profession/text");
		if (xpath.evaluate("angaben/rasse").length() < 20
				&& xpath.evaluate("angaben/kultur").length() < 40
				&& profession.length() < 60) {
			return 0;
		}

		/* Maximaler Stauchungsfaktor 1.5 */
		breite = berechneTextUeberlauf(PDType1Font.HELVETICA, 6, cellCountX, 1, profession);
		return (int) Math.ceil(breite / 1.5f);
	}

	private int charakterDaten(ExtXPath xpath, int professionsZeilen)
			throws IOException, XPathExpressionException {
		int zeile = 0;
		String profession;

		/* erste Zeile */
		drawText(PDType1Font.HELVETICA_BOLD, 0, 4, zeile, "Name:", false);
		drawText(PDType1Font.HELVETICA, 4, 31, zeile, xpath.evaluate("angaben/name"), true);

		drawText(PDType1Font.HELVETICA_BOLD, 30, 34, zeile, "Stand:", false);
		drawText(PDType1Font.HELVETICA, 34, 40, zeile, xpath.evaluate("angaben/stand"),
				false);

		drawText(PDType1Font.HELVETICA_BOLD, 40, 43, zeile, "Alter:", false);
		drawText(PDType1Font.HELVETICA, 43, 46, zeile, xpath.evaluate("angaben/alter")
				.toString(), true);

		drawText(PDType1Font.HELVETICA_BOLD, 46, 52, zeile, "Geburtstag:",
				false);
		drawText(PDType1Font.HELVETICA, 52, cellCountX, zeile,
				xpath.evaluate("angaben/geburtstag"), true);
		zeile++;

		/* zweite Zeile (evt. mehrfach) */
		profession = xpath.evaluate("angaben/profession/text");
		if (professionsZeilen == 0) {
			drawText(PDType1Font.HELVETICA_BOLD, 0, 4, zeile, "Rasse:", false);
			drawText(PDType1Font.HELVETICA, 4, 11, zeile, xpath.evaluate("angaben/rasse"),
					true);

			drawText(PDType1Font.HELVETICA_BOLD, 11, 15, zeile, "Kultur:",
					false);
			drawText(PDType1Font.HELVETICA, 15, 30, zeile, xpath.evaluate("angaben/kultur"),
					true);

			drawText(PDType1Font.HELVETICA_BOLD, 30, 36, zeile, "Profession:",
					false);
			drawText(PDType1Font.HELVETICA, 36, cellCountX, zeile, profession,
					true);

			zeile++;
		} else {
			drawText(PDType1Font.HELVETICA_BOLD, 0, 4, zeile, "Rasse:", false);
			drawText(PDType1Font.HELVETICA, 4, 30, zeile, xpath.evaluate("angaben/rasse"),
					true);

			drawText(PDType1Font.HELVETICA_BOLD, 30, 35, zeile, "Kultur:",
					false);
			drawText(PDType1Font.HELVETICA, 35, cellCountX, zeile,
					xpath.evaluate("angaben/kultur"), true);
			zeile++;

			drawText(PDType1Font.HELVETICA_BOLD, 0, 6, zeile, "Profession:",
					false);
			for (int i = 0; i < professionsZeilen; i++) {
				int pos, l, r;
				String teil;

				pos = profession.length() / (professionsZeilen - i);
				if (professionsZeilen - i == 1) {
					teil = profession;
				} else {
					l = profession.lastIndexOf(' ', pos);
					r = profession.indexOf(' ', pos);
					if ((pos - l) < (r - pos)) {
						pos = l;
					} else {
						pos = r;
					}

					teil = profession.substring(0, pos);
					profession = profession.substring(pos + 1);
				}

				drawText(PDType1Font.HELVETICA, 6, cellCountX, zeile, teil,
						true);
				zeile++;
			}
		}

		/* dritte Zeile */
		drawText(PDType1Font.HELVETICA_BOLD, 0, 7, zeile, "Geschlecht:", false);
		drawText(PDType1Font.HELVETICA, 7, 9, zeile, xpath.evaluate("angaben/geschlecht")
				.substring(0, 1), false);

		drawText(PDType1Font.HELVETICA_BOLD, 11, 15, zeile, "Größe:", false);
		drawText(PDType1Font.HELVETICA, 15, 20, zeile, xpath.evaluate("angaben/groesse")
				.toString() + " cm", false);

		drawText(PDType1Font.HELVETICA_BOLD, 20, 25, zeile, "Gewicht:", false);
		drawText(PDType1Font.HELVETICA, 25, 30, zeile, xpath.evaluate("angaben/gewicht")
				.toString() + " kg", false);

		drawText(PDType1Font.HELVETICA_BOLD, 30, 36, zeile, "Haarfarbe:", false);
		drawText(PDType1Font.HELVETICA, 36, 44, zeile, xpath.evaluate("angaben/haarfarbe"),
				false);

		drawText(PDType1Font.HELVETICA_BOLD, 44, 52, zeile, "Augenfarbe:",
				false);
		drawText(PDType1Font.HELVETICA, 52, cellCountX, zeile,
				xpath.evaluate("angaben/augenfarbe"), true);
		zeile++;

		/* Linien für Charakter-Daten */
		for (int y = 1; y <= zeile; y++) {
			addLine(0, y, cellCountX, y);
		}
		stream.closeAndStroke();

		return zeile + 1;
	}

	private void extrahiereGruppe(ExtXPath xpath, Node node, List<PDFVorteil> target) throws XPathExpressionException {
		NodeList nl = xpath.evaluateList("auswahlen/auswahl", node);
		String bezeichner = xpath.evaluate("bezeichner", node);
		for (int i=0; i<nl.getLength(); i++) {
			String name = xpath.evaluate("name", nl.item(i));
			String wert = xpath.evaluate("wert", nl.item(i));

			target.add(new PDFVorteil(bezeichner + ": " + name, wert));
		}
		if (nl.getLength() == 0){
			target.add(new PDFVorteil(bezeichner, xpath.evaluate("wert", node)));
		}
	}

	private void extrahiereVorNachteile(ExtXPath xpath, List<PDFVorteil> vorteile,
			List<PDFVorteil> nachteile) throws XPathExpressionException {
		NodeList nl = xpath.evaluateList("vorteile/vorteil[istvorteil[contains(text(), 'true')]]");
		for (int i=0; i<nl.getLength(); i++) {
			extrahiereGruppe(xpath, nl.item(i), vorteile);
		}
		nl = xpath.evaluateList("vorteile/vorteil[istvorteil[contains(text(), 'false')]]");
		for (int i=0; i<nl.getLength(); i++) {
			extrahiereGruppe(xpath, nl.item(i), nachteile);
		}
	}

	private int initiativeAusweichen(ExtXPath xpath, NodeList sets, int x1, int x2,
			int y, boolean tzm) throws IOException, XPathExpressionException {
		String[][] data;
		List<String> data0, data1;

		data0 = new ArrayList<String>();
		data1 = new ArrayList<String>();
		data0.add("Initiative");
		data1.add("Ausweichen");
		for (int idx=0; idx < sets.getLength(); idx++) {
			long ini;

			ini = xpath.evaluateInt("ini", sets.item(idx));
			if (tzm) {
				ini -= xpath.evaluateInt("ruestungzonen/behinderung", sets.item(idx));
			} else {
				ini -= xpath.evaluateInt("ruestungeinfach/behinderung", sets.item(idx));
			}
			data0.add(Long.toString(ini));
			data1.add(xpath.evaluate("ausweichen", sets.item(idx)));
		}

		data = new String[2][4];
		data[0] = data0.toArray(new String[data0.size()]);
		data[1] = data1.toArray(new String[data0.size()]);

		drawTabelle(x1, x2, y, data, new IniAusweichenTabelle(sets.getLength() + 1,
				x2 - x1));
		return y + 4;
	}

	private String kaufbar(ExtXPath xpath, String name) throws XPathExpressionException {
		int akt, mod, start;

		akt = xpath.evaluateInt("eigenschaft/$0/akt", name);
		mod = xpath.evaluateInt("eigenschaft/$0/modi", name);
		start = xpath.evaluateInt("eigenschaft/$0/start", name);

		return Integer.toString(((start - mod) * 3 + 1) / 2 - (akt - mod));
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

	private int vorteileNachteile(int offset, List<PDFVorteil> vorteile,
			List<PDFVorteil> nachteile, int count, Konfiguration k)
			throws IOException {
		List<PDFVorteil> box1, box2, box3, box4;
		boolean vierBoxen;
		int breite, x, y;

		breite = 15;

		/* aufspalten */
		box1 = new ArrayList<PDFVorteil>();
		box2 = new ArrayList<PDFVorteil>();
		box3 = new ArrayList<PDFVorteil>();
		box4 = new ArrayList<PDFVorteil>();

		Collections.sort(vorteile);
		Collections.sort(nachteile);

		vierBoxen = !k.getOptionsDaten(Konfiguration.FRONT_MEHRSF);
		if (vierBoxen) {
			y = (vorteile.size() + 1) / 2;
			box1.addAll(vorteile.subList(0, y));
			box2.addAll(vorteile.subList(y, vorteile.size()));

			y = (nachteile.size() + 1) / 2;
			box3.addAll(nachteile.subList(0, y));
			box4.addAll(nachteile.subList(y, nachteile.size()));
		} else {
			if (vorteile.size() > nachteile.size()) {
				y = (vorteile.size() + 1) / 2;
				box1.addAll(vorteile.subList(0, y));
				box2.addAll(vorteile.subList(y, vorteile.size()));

				box3.addAll(nachteile);
				y = Math.max(y, nachteile.size());
			} else {
				y = (nachteile.size() + 1) / 2;
				box3.addAll(nachteile.subList(0, y));
				box4.addAll(nachteile.subList(y, nachteile.size()));

				box1.addAll(vorteile);
				y = Math.max(y, vorteile.size());
			}
		}

		/* add padding */
		for (y = box1.size(); y < count; y++) {
			box1.add(null);
		}
		if (vierBoxen || vorteile.size() > nachteile.size()) {
			for (y = box2.size(); y < count; y++) {
				box2.add(null);
			}
		}
		for (y = box3.size(); y < count; y++) {
			box3.add(null);
		}
		if (vierBoxen || nachteile.size() >= vorteile.size()) {
			for (y = box4.size(); y < count; y++) {
				box4.add(null);
			}
		}

		x = 0;

		drawTabelle(x, x + viertelBreite, offset, box1.toArray(),
				new VorteilTabelle("Vorteile", breite));
		x += viertelBreite + 1;

		if (box2.size() > 0) {
			drawTabelle(x, x + viertelBreite, offset, box2.toArray(),
					new VorteilTabelle("Vorteile", breite));
			x += viertelBreite + 1;
		}

		drawTabelle(x, x + viertelBreite, offset, box3.toArray(),
				new VorteilTabelle("Nachteile", breite));
		x += viertelBreite + 1;

		if (box4.size() > 0) {
			drawTabelle(x, x + viertelBreite, offset, box4.toArray(),
					new VorteilTabelle("Nachteile", breite));
		}

		return offset + count + 2;
	}

	private int waffenlos(ExtXPath xpath, NodeList sets, int x1, int x2, int y,
			boolean tzm) throws IOException, XPathExpressionException {
		String[][] daten;
		int anzahl;
		String be;

		if (sets.getLength() == 0) {
			drawTabelle(x1, x2, y, new String[] { null, null },
					new WaffenlosTabelle(x2 - x1, 1));
		}
		daten = new String[2][8];
		daten[0][0] = "Raufen";
		daten[0][1] = xpath.evaluate("raufen/tp", sets.item(0));
		daten[1][0] = "Ringen";
		daten[1][1] = xpath.evaluate("ringen/tp", sets.item(0));

		if (tzm) {
			be = xpath.evaluate("ruestungszonen/behinderung", sets.item(0));
		} else {
			be = xpath.evaluate("ruestungeinfach/behinderung", sets.item(0));
		}
		for (int i = 0; i < sets.getLength(); i++) {
			daten[0][2 + i * 2] = xpath.evaluate("raufen/at", sets.item(i));
			daten[0][3 + i * 2] = xpath.evaluate("raufen/pa", sets.item(i));
			daten[1][2 + i * 2] = xpath.evaluate("ringen/at", sets.item(i));
			daten[1][3 + i * 2] = xpath.evaluate("ringen/pa", sets.item(i));

			if (tzm) {
				if (!xpath.evaluate("ruestungszonen/behinderung", sets.item(i)).equals(be)) {
					be = null;
				}
			} else {
				if (!xpath.evaluate("ruestungeinfach/behinderung", sets.item(i)).equals(be)) {
					be = null;
				}
			}
		}

		anzahl = be != null ? 4 : (2 + sets.getLength() * 2);
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
		private ExtXPath xpath;
		public FernkampfTabelle(ExtXPath xpath, int breite) {
			super(new String[] { "#", null, "AT", "TP", "Entfernung",
					"TP/Entfernung" }, new int[] { 2, 0, 3, 5, 10, 8 }, 0,
					"Fernkampfwaffe", breite);
			this.xpath = xpath;
		}

		@Override
		public String get(Object obj, int x) {
			Node n = (Node) obj;

			try {
				switch (x) {
					case 0:
						return xpath.evaluate("../../@nr", n);
					case 1:
						return xpath.evaluate("name", n);
					case 2:
						return xpath.evaluate("at", n);
					case 3:
						return xpath.evaluate("tp", n);
					case 4:
						return xpath.evaluate("reichweite", n);
					case 5:
						return xpath.evaluate("tpmod", n);
				}
			} catch (XPathExpressionException e) {
			}
			return "";
		}

		@Override
		public Color getBackgroundColor(Object o, int x) {
			Node n = (Node) o;

			try {
				return (xpath.evaluateInt("../../@nr", n) & 1) == 0 ? Color.LIGHT_GRAY : null;
			} catch (XPathExpressionException e) {
				return null;
			}
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
		private ExtXPath xpath;
		public NahkampfTable(ExtXPath xpath, int width) {
			super(new String[] { "#", null, "AT", "PA", "TP", "TP/KK", "DK",
					"INI", "Bruchfaktor", "", "", "", "" }, new int[] { 2, 0,
					3, 3, 4, 4, 3, 3, 2, 2, 2, 2, 2 }, 0, "Nahkampfwaffe",
					width);
			this.xpath = xpath;
		}

		@Override
		public String get(Object obj, int x) {
			Node n = (Node)obj;

			try {
				switch (x) {
				case 0:
					return xpath.evaluate("../../@nr", n);
				case 1:
					return xpath.evaluate("name", n);
				case 2:
					return xpath.evaluate("at", n);
				case 3:
					return xpath.evaluate("pa", n);
				case 4:
					return xpath.evaluate("tpinkl", n);
				case 5:
					return xpath.evaluate("tpkk/schwelle", n) + "/"
							+ xpath.evaluate("tpkk/schrittweite", n);
				case 6:
					return xpath.evaluate("dk", n).replace(" ", "");
				case 7:
					return xpath.evaluate("ini", n);
				case 8:
					int bf = xpath.evaluateInt("bfakt", n);
					if (bf < -7) {
						return "-";
					}
					return Integer.toString(bf);
				}
			} catch (XPathExpressionException e) {
			}
			return "";
		}

		@Override
		public Color getBackgroundColor(Object o, int x) {
			Node n = (Node)o;

			try {
				return (xpath.evaluateInt("../../@nr", n) & 1) == 0 ? Color.LIGHT_GRAY : null;
			} catch (XPathExpressionException e) {
				return null;
			}
		}

		@Override
		public int getColumnSpan(int x) {
			return x == 8 ? 5 : 1;
		}
	}

	private class ParierwaffenTabelle extends AbstractTabellenZugriff {
		private ExtXPath xpath;
		public ParierwaffenTabelle(ExtXPath xpath, int breite) {
			super(new String[] { "#", null, "AT", "PA", "WM", "INI", "Bruchfaktor",
					"", "", "", "" },
					new int[] { 2, 0, 3, 3, 4, 3, 2, 2, 2, 2, 2 }, 0,
					"Parierwaffe/Schild", breite);
			this.xpath = xpath;
		}

		@Override
		public String get(Object obj, int x) {
			Node n = (Node) obj;

			try {
				switch (x) {
					case 0:
						return xpath.evaluate("../../@nr", n);
					case 1:
						return xpath.evaluate("name", n);
			case 2:
				return xpath.evaluate("at", n);
			case 3:
				return xpath.evaluate("pa", n);
			case 4:
				return xpath.evaluate("mod", n);
			case 5:
				return xpath.evaluate("ini", n);
			case 6:
				return xpath.evaluate("bfakt", n);
			}
			} catch (XPathExpressionException e) {
			}
			return "";
		}

		@Override
		public Color getBackgroundColor(Object o, int x) {
			Node n = (Node)o;

			try {
				return (xpath.evaluateInt("../../@nr", n) & 1) == 0 ? Color.LIGHT_GRAY : null;
			} catch (XPathExpressionException e) {
				return null;
			}
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
		private boolean tzm;
		private ExtXPath xpath;

		public RuestungsTabelle(ExtXPath xpath, int breite, boolean tzm) {
			super(new String[] { "#", null, "RS", "BE", "Ko", "Br", "Rü", "Ba",
					"LA", "RA", "LB", "RB" }, new int[] { 2, 0, 3, 3, 2, 2, 2,
					2, 2, 2, 2, 2 }, tzm ? 0 : 4, "Rüstung", breite);
			this.tzm = tzm;
			this.xpath = xpath;
		}

		public RuestungsTabelle(String[] col, int[] colwidth, int colcount,
				String titel, int width) {
			super(col, colwidth, colcount, titel, width);
		}

		@Override
		public String get(Object obj, int x) {
			Node n = (Node) obj;

			try {
			switch (x) {
			case 0:
					return xpath.evaluate("../../@nr", n);
				case 1:
					return erzeugeName(n);
				case 2:
					if (!tzm) {
						return filter(xpath.evaluateInt("ruestungeinfach/gesammt", n));
					}
					return filter(xpath.evaluateInt("ruestungzonen/gesamtzonenschutz", n));
			case 3:
				if (!tzm) {
					return xpath.evaluate("ruestungeinfach/behinderung", n);
				}
				return xpath.evaluate("ruestungzonen/behinderung", n);
			case 4:
				return filter(xpath.evaluateInt("ruestungzonen/kopf", n));
			case 5:
				return filter(xpath.evaluateInt("ruestungzonen/brust", n));
			case 6:
				return filter(xpath.evaluateInt("ruestungzonen/ruecken", n));
			case 7:
				return filter(xpath.evaluateInt("ruestungzonen/bauch", n));
			case 8:
				return filter(xpath.evaluateInt("ruestungzonen/linkerarm", n));
			case 9:
				return filter(xpath.evaluateInt("ruestungzonen/rechterarm", n));
			case 10:
				return filter(xpath.evaluateInt("ruestungzonen/linkesbein", n));
			case 11:
				return filter(xpath.evaluateInt("ruestungzonen/rechtesbein", n));
			}
			} catch (XPathExpressionException e) {
			}
			return "";
		}

		@Override
		public Color getBackgroundColor(Object o, int x) {
			Node n = (Node)o;

			try {
				return (xpath.evaluateInt("../../@nr", n) & 1) == 0 ? Color.LIGHT_GRAY : null;
			} catch (XPathExpressionException e) {
				return null;
			}
		}

		protected String filter(int input) {
			if (input == 0) {
				return "";
			}
			return Integer.toString(input);
		}

		private String erzeugeName(Node n) throws XPathExpressionException {
			List<String> l = new ArrayList<String>();
			String text;

			NodeList nl = xpath.evaluateList("ruestungen/ruestung/name", n);
			for (int idx = 0; idx < nl.getLength(); idx++) {
				l.add(nl.item(idx).getTextContent());
			}

			Collections.sort(l, Collator.getInstance());

			for (int i = 1; i < l.size(); i++) {
				String mod = l.get(i - 1).replace("links", "rechts");

				if (mod.equals(l.get(i))) {
					l.set(i - 1, mod.replace("rechts", "L/R"));
					l.remove(i);
				}
			}
			text = "";
			for (String t : l) {
				text = text + ";" + t;
			}
			return text.substring(1);
		}
	}

	private class VorteilTabelle extends AbstractTabellenZugriff {
		public VorteilTabelle(String titel, int breite) {
			super(new String[] { null, "Wert" }, new int[] { 0, 2 }, 0, titel,
					breite);
		}

		@Override
		public String get(Object obj, int x) {
			PDFVorteil v = (PDFVorteil) obj;

			switch (x) {
			case 0:
				return v.getName();
			case 1:
				return v.getWert();
			}
			return "";
		}
	}

	private class WaffenlosTabelle extends AbstractTabellenZugriff {
		public WaffenlosTabelle(int breite, int anzahl) {
			super(new String[] { null, "TP(A)",
					anzahl == 1 ? "AT/PA" : "AT/PA #1", "", "AT/PA #2", "",
					"AT/PA #3", "" }, new int[] { 0, 4, 3, 3, 3, 3, 3, 3 },
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

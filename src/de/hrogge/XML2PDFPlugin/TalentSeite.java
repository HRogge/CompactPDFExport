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

package de.hrogge.XML2PDFPlugin;

import java.awt.Color;
import java.io.IOException;
import java.util.*;

import jaxbGenerated.datenxml.Daten;
import jaxbGenerated.datenxml.Sonderfertigkeit;
import jaxbGenerated.datenxml.Talent;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class TalentSeite extends PDFSeite {
	PDPageContentStream stream;

	public TalentSeite(PDDocument d, float marginX, float marginY,
			float textMargin) throws IOException {
		super(d, marginX, marginY, textMargin, 63, 72);
	}

	public void erzeugeSeite(Daten daten, String[] guteEigenschaften)
			throws IOException {
		List<Talent> talente;
		List<Sonderfertigkeit> sonderfertigkeiten;

		String kategorien1[] = { "Kampftalente", "Körperliche Talente",
				"Gesellschaftliche Talente", "Naturtalente" };
		String kategorien2[] = { "Wissenstalente", "Sprachen/Schriften",
				"Handwerkstalente", "Sondertalente/Gaben" };

		TalentListe gruppe1[] = new TalentListe[4];
		TalentListe gruppe2[] = new TalentListe[4];
		TalentListe metatalente = new TalentListe();
		sonderfertigkeiten = new ArrayList<Sonderfertigkeit>();
		int sf_offset;

		for (int i = 0; i < gruppe1.length; i++) {
			gruppe1[i] = new TalentListe();
		}
		for (int i = 0; i < gruppe2.length; i++) {
			gruppe2[i] = new TalentListe();
		}

		talente = daten.getTalentliste().getTalent();

		for (Talent t : talente) {
			TalentListe liste;

			if (t.getBereich().equals("Kampf")) {
				liste = gruppe1[0];
			} else if (t.getBereich().equals("Körperlich")) {
				liste = gruppe1[1];
			} else if (t.getBereich().equals("Gesellschaft")) {
				liste = gruppe1[2];
			} else if (t.getBereich().equals("Natur")) {
				if (t.isMetatalent()) {
					liste = metatalente;
				} else {
					liste = gruppe1[3];
				}
			} else if (t.getBereich().equals("Wissen")) {
				liste = gruppe2[0];
			} else if (t.getBereich().equals("Sprachen")
					|| t.getBereich().equals("Schriften")) {
				liste = gruppe2[1];
			} else if (t.getBereich().equals("Handwerk")) {
				liste = gruppe2[2];
			} else {
				liste = gruppe2[3];
			}

			liste.add(t);

			if (t.getSpezialisierungen() != null
					&& t.getSpezialisierungen().length() > 0) {
				for (String s : t.getSpezialisierungen().split(",")) {
					s = s.trim();

					liste.add(new TalentSpezialisierung(t, s));
				}
			}
		}

		for (Sonderfertigkeit s : daten.getSonderfertigkeiten()
				.getSonderfertigkeit()) {
			if (!s.getBereich().contains("Magisch") && (
					s.getBereich().contains("Sonst")
					)) {
				sonderfertigkeiten.add(s);
			}
		}

		if (sonderfertigkeiten.size() < 10) {
			sf_offset = 5;
		} else {
			sf_offset = (sonderfertigkeiten.size() + 3) / 2 + 1;
		}

		stream = new PDPageContentStream(doc, page);

		/* Titelzeile */
		titelzeile(guteEigenschaften);

		/* linke spalte */
		talentSpalte(gruppe1, kategorien1, 0, (cellCountX - 1) / 2, 6, true);

		/* Metatalente */
		zeichneTalentKategorie(metatalente, cellCountY - 6, 0,
				(cellCountX - 1) / 2, "Metatalente", 5);

		/* rechte spalte */
		talentSpalte(gruppe2, kategorien2, (cellCountX - 1) / 2 + 1,
				cellCountX, sf_offset, false);

		/* Sonderfertigkeiten */
		sonderfertigkeiten(sonderfertigkeiten, (cellCountX - 1) / 2 + 1,
				sf_offset);
		stream.close();
	}

	private void zeichneKampfTalentKategorie(TalentListe tl, int offset, int x1,
			int x2, int anzahl) throws IOException {
		Collections.sort(tl, new TalentComparator());

		/* leerzeilen hinzufügen */
		while (tl.size() < anzahl) {
			tl.add(null);
		}
		drawTabelle(stream, x1, x2, offset, tl.toArray(), new KampfTalentTabelle(x2 - x1));
	}

	private void zeichneTalentKategorie(TalentListe tl, int offset, int x1,
			int x2, String titel, int anzahl) throws IOException {
		Collections.sort(tl, new TalentComparator());

		/* leerzeilen hinzufügen */
		while (tl.size() < anzahl) {
			tl.add(null);
		}
		drawTabelle(stream, x1, x2, offset, tl.toArray(), new TalentTabelle(titel, x2 - x1));
	}

	private void sonderfertigkeiten(List<Sonderfertigkeit> sf, int offsetX,
			int hoehe) throws IOException {
		Sonderfertigkeit s;
		String name;
		int breite, y1, y2;
		breite = cellCountX - offsetX;

		drawLabeledBox(stream, offsetX, cellCountY - hoehe, cellCountX,
				cellCountY, "Talent-Sonderfertigkeiten");

		stream.setStrokingColor(Color.BLACK);
		stream.setLineWidth(0.1f);
		addLine(stream, offsetX + breite / 2, cellCountY - (hoehe - 1),
				offsetX + breite / 2, cellCountY);
		for (int y = cellCountY - (hoehe - 2); y < cellCountY; y++) {
			addLine(stream, offsetX, y, cellCountX, y);
		}
		stream.closeAndStroke();

		y1 = cellCountY - hoehe + 1;
		y2 = cellCountY;
		for (int y = y1; !sf.isEmpty() && y < y2; y++) {
			s = sf.remove(0);
			name = s.getName();
			if (s.getBereich().contains("Geländekunde")) {
				name = "Geländekunde (" + name + ")";
			}
			drawText(stream, PDType1Font.HELVETICA, offsetX, offsetX + breite
					/ 2, y, name, false);
			if (sf.isEmpty()) {
				break;
			}
			
			s = sf.remove(0);
			name = s.getName();
			if (s.getBereich().contains("Geländekunde")) {
				name = "Geländekunde (" + name + ")";
			}
			drawText(stream, PDType1Font.HELVETICA, offsetX + breite / 2,
					cellCountX, y, name, false);
		}

	}

	private void talentSpalte(TalentListe[] talentListen, String[] kategorien,
			int x1, int x2, int footer, boolean links) throws IOException {
		int maximaleTalente, uebrig;
		int offset[], leer[];

		maximaleTalente = cellCountY - 2 * kategorien.length - 2 - footer;

		uebrig = maximaleTalente;
		for (TalentListe tl : talentListen) {
			uebrig -= tl.size();
		}

		offset = new int[talentListen.length];
		leer = new int[talentListen.length];

		/* Offsets errechnen */
		offset[0] = 2;
		for (int k = 1; k < talentListen.length; k++) {
			leer[k - 1] = uebrig / (talentListen.length - k + 1);
			uebrig -= leer[k - 1];
			offset[k] = offset[k - 1] + talentListen[k - 1].size()
					+ leer[k - 1] + 2;
		}
		leer[leer.length - 1] = uebrig;

		/* talentbloecke zeichnen */
		for (int k = 0; k < talentListen.length; k++) {
			if (links && k == 0) {
				zeichneKampfTalentKategorie(talentListen[k], offset[k], x1, x2,
						talentListen[k].size() + leer[k]);
			} else {
				zeichneTalentKategorie(talentListen[k], offset[k], x1, x2,
						kategorien[k], talentListen[k].size() + leer[k]);
			}
		}
	}

	private void titelzeile(String[] guteEigenschaften) throws IOException {
		String[] titel = { "MU:", "KL:", "IN:", "CH:", "FF:", "GE:", "KK:",
				"KO:" };

		for (int i = 0; i < titel.length; i++) {
			int x = i * 8 + 1;

			drawText(stream, PDType1Font.HELVETICA_BOLD, x + 0, x + 3, 0, 2,
					titel[i], true);
			drawText(stream, PDType1Font.HELVETICA_BOLD, x + 3, x + 6, 0, 2,
					guteEigenschaften[i], true);
		}
	}

	private class KampfTalentTabelle extends TalentTabelle {
		public KampfTalentTabelle(int width) {
			super(new String[] { null, "AT", "", "PA", "", "TaW", "", "BE",
					"*", "SKT" }, new int[] { 0, 2, 2, 2, 2, 2, 2, 3, 3, 2 },
					0, "Kampftalente", width);
		}

		@Override
		public String get(Object obj, int x) {
			Talent t = (Talent) obj;

			if (t instanceof TalentSpezialisierung) {
				TalentSpezialisierung ts = (TalentSpezialisierung) t;
				switch (x) {
				case 0:
					return ts.getSpezName();
				case 1:
					return Integer.toString(Integer.parseInt(ts.getSpezReferenz().getAt())+1);
				case 3:
					return Integer.toString(Integer.parseInt(ts.getSpezReferenz().getPa())+1);
				case 5:
					return Integer.toString(ts.getSpezValue());
				} 
				return "";
			}

			if (x == 0) {
				return super.get(obj, x);
			} else if (x >= 5) {
				return super.get(obj, x - 3);
			}

			switch (x) {
			case 1:
				return t.getAt();
			case 3:
				return t.getPa();
			}
			return "";
		}
		
		@Override
		public int getIndent(Object o, int x) {
			return (o instanceof TalentSpezialisierung) && x == 0 ? 2:0;
		}
		
		@Override
		public int getColumnSpan(int x) {
			if (x == 1 || x == 3 || x == 5) {
				return 2;
			}
			return 1;
		}
	}
	
	private class TalentComparator implements Comparator<Talent> {
		@Override
		public int compare(Talent o1, Talent o2) {
			TalentSpezialisierung s1 = null, s2 = null;
			int result;

			if (o1 instanceof TalentSpezialisierung) {
				s1 = (TalentSpezialisierung) o1;
				o1 = s1.getSpezReferenz();
			}
			if (o2 instanceof TalentSpezialisierung) {
				s2 = (TalentSpezialisierung) o2;
				o2 = s2.getSpezReferenz();
			}
			if (o1.getName().startsWith("L/S")
					&& !o2.getName().startsWith("L/S"))
				return 1;
			if (!o1.getName().startsWith("L/S")
					&& o2.getName().startsWith("L/S"))
				return -1;
			result = o1.getName().compareTo(o2.getName());
			if (result != 0) {
				return result;
			}

			if (s1 == null && s2 != null) {
				return -1;
			}
			if (s1 != null && s2 == null) {
				return 1;
			}
			if (s1 != null && s2 != null) {
				return s1.getSpezName().compareTo(s2.getSpezName());
			}
			return 0;
		}
	}

	private class TalentListe extends ArrayList<Talent> {
		private static final long serialVersionUID = 1L;
	}

	private class TalentSpezialisierung extends Talent {
		private String spezName;
		private int spezValue;
		private Talent referenz;

		public TalentSpezialisierung(Talent t, String spezialisierung) {
			super();
			referenz = t;
			spezName = spezialisierung;
			spezValue = t.getWert().intValue() + 2;
		}

		public String getSpezName() {
			return spezName;
		}

		public Talent getSpezReferenz() {
			return referenz;
		}

		public int getSpezValue() {
			return spezValue;
		}
	}

	private class TalentTabelle extends AbstractTabellenZugriff {
		public TalentTabelle(String titel, int width) {
			super(new String[] { null, "Probe", "TaW", "", "BE", "*", "SKT" },
					new int[] { 0, 6, 2, 2, 3, 3, 2 }, 0, titel, width);
		}

		public TalentTabelle(String[] col, int[] colwidth, int colcount,
				String titel, int width) {
			super(col, colwidth, colcount, titel, width);
		}

		@Override
		public String get(Object obj, int x) {
			Talent t = (Talent) obj;
			String name, stern;

			if (t instanceof TalentSpezialisierung) {
				TalentSpezialisierung ts = (TalentSpezialisierung) t;
				if (x == 0) {
					return "      " + ts.getSpezName();
				} else if (x == 2) {
					return Integer.toString(ts.getSpezValue());
				} else {
					return "";
				}
			}
			switch (x) {
			case 0:
				name = t.getName();
				if (t.getSprachkomplexität() != null) {
					name += " (" + t.getSprachkomplexität();
					if (Boolean.TRUE.equals(t.isMuttersprache())) {
						name += ",M";
					} else if (Boolean.TRUE.equals(t.isZweitlehrsprache())) {
						name += ",Z";
					} else if (Boolean.TRUE.equals(t.isSchriftmuttersprache())) {
						name += ",M";
					}
					name += ")";
				}
				return name;
			case 1:
				return t.getProbe();
			case 2:
				if (t.getWert() != null) {
					return t.getWert().toString();
				}
				return "";
			case 4:
				if (t.getBehinderung() != null) {
					return t.getBehinderung();
				}
				return "";
			case 5:
				stern = "";
				if (t.isLeittalent()) {
					stern += "L";
				}
				if (t.isMeisterhandwerk()) {
					stern += "M";
				}
				if (t.isMirakelminus()) {
					stern += "+";
				}
				if (t.isMirakelminus()) {
					stern += "-";
				}
				return stern;
			case 6:
				if (t.getLernkomplexität() != null && !t.isMetatalent()) {
					return t.getLernkomplexität();
				}
				return "";
			}
			return "";
		}
		
		@Override
		public int getColumnSpan(int x) {
			return x == 2 ? 2 : 1;
		}
	}
}

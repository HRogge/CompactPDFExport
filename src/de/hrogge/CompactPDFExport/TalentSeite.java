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

import java.io.IOException;
import java.util.*;

import jaxbGenerated.datenxml.Daten;
import jaxbGenerated.datenxml.Talent;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

import de.hrogge.CompactPDFExport.PDFSonderfertigkeiten.Kategorie;

public class TalentSeite extends PDFSeite {
	public TalentSeite(PDDocument d, float marginX, float marginY,
			float textMargin) throws IOException {
		super(d, marginX, marginY, textMargin, 72);
	}

	public boolean erzeugeSeite(Daten daten, String[] guteEigenschaften,
			List<PDFSonderfertigkeiten> alleSF) throws IOException {
		List<Talent> talente;
		List<PDFSonderfertigkeiten> sfList;

		String kategorien1[] = { "Kampftalente", "Körperliche Talente",
				"Gesellschaftliche Talente", "Naturtalente" };
		String kategorien2[] = { "Wissenstalente", "Sprachen/Schriften",
				"Handwerkstalente", "Sondertalente/Gaben" };

		TalentListe gruppe1[] = new TalentListe[4];
		TalentListe gruppe2[] = new TalentListe[4];
		TalentListe metatalente = new TalentListe();

		int sf_offset, uebrig;

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

		/* kalkuliere Platz für Sonderfertigkeiten */
		PDFSonderfertigkeiten.Kategorie kat[] = { Kategorie.TALENT };
		sfList = PDFSonderfertigkeiten.extrahiereKategorien(alleSF, kat);
		Collections.sort(sfList);

		sf_offset = (PDFSonderfertigkeiten.anzeigeGroesse(sfList) + 1) / 2 + 1;
		uebrig = restZeilen(gruppe2, sf_offset) - 4;
		if (uebrig < 0) {
			sf_offset = sf_offset - (-uebrig);
		}

		stream = new PDPageContentStream(doc, page);

		/* Titelzeile */
		titelzeile(guteEigenschaften);

		/* linke spalte */
		talentSpalte(gruppe1, kategorien1, 0, halbeBreite, 6, true);

		/* Metatalente */
		zeichneTalentKategorie(metatalente, cellCountY - 6, 0, halbeBreite,
				"Metatalente", 5);

		/* rechte spalte */
		talentSpalte(gruppe2, kategorien2, halbeBreite + 1, cellCountX,
				sf_offset, false);

		/* Sonderfertigkeiten */
		PDFSonderfertigkeiten.zeichneTabelle(this, halbeBreite + 1, cellCountY
				- sf_offset, cellCountX - (viertelBreite + 1), cellCountY,
				"Sonderfertigkeiten (1)", sfList);
		PDFSonderfertigkeiten.zeichneTabelle(this, cellCountX - viertelBreite,
				cellCountY - sf_offset, cellCountX, cellCountY,
				"Sonderfertigkeiten (2)", sfList);
		stream.close();

		return uebrig < 0;
	}

	private void zeichneKampfTalentKategorie(TalentListe tl, int offset,
			int x1, int x2, int anzahl) throws IOException {
		Collections.sort(tl, new TalentComparator());

		/* leerzeilen hinzufügen */
		while (tl.size() < anzahl) {
			tl.add(null);
		}
		drawTabelle(x1, x2, offset, tl.toArray(), new KampfTalentTabelle(x2
				- x1));
	}

	private void zeichneTalentKategorie(TalentListe tl, int offset, int x1,
			int x2, String titel, int anzahl) throws IOException {
		Collections.sort(tl, new TalentComparator());

		/* leerzeilen hinzufügen */
		while (tl.size() < anzahl) {
			tl.add(null);
		}
		drawTabelle(x1, x2, offset, tl.toArray(), new TalentTabelle(titel, x2
				- x1));
	}

	private int restZeilen(TalentListe[] talentListen, int footer) {
		int uebrig;

		uebrig = cellCountY - 2 * talentListen.length - 2 - footer;
		;
		for (TalentListe tl : talentListen) {
			uebrig -= tl.size();
		}
		return uebrig;
	}

	private void talentSpalte(TalentListe[] talentListen, String[] kategorien,
			int x1, int x2, int footer, boolean links) throws IOException {
		int uebrig;
		int offset[], leer[];

		uebrig = restZeilen(talentListen, footer);

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
					if (ts.getSpezReferenz().getAt().length() > 0) {
						int at = Integer.parseInt(ts.getSpezReferenz().getAt());

						at++;

						if (ts.getSpezReferenz().getPa().length() == 0) {
							/* Fernkampf/Lanzenreiten hat keinen PA-Wert */
							at++;
						}
						return Integer.toString(at);
					}
					return "";
				case 3:
					if (ts.getSpezReferenz().getPa().length() > 0) {
						return Integer.toString(Integer.parseInt(ts
								.getSpezReferenz().getPa()) + 1);
					}
					return "";
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
			return (o instanceof TalentSpezialisierung) && x == 0 ? 2 : 0;
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
				if (t.getBereich().contains("Liturgiekenntnis")) {
					name = "Liturgiekenntnis: " + name;
				}
				if (t.getBereich().contains("Ritualkenntnis")) {
					name = "Ritualkenntnis: " + name;
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

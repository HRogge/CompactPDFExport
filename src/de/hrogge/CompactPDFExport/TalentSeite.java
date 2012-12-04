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
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import de.hrogge.CompactPDFExport.PDFSonderfertigkeiten.Kategorie;
import de.hrogge.CompactPDFExport.gui.Konfiguration;

public class TalentSeite extends PDFSeite {
	public TalentSeite(PDDocument d, float marginX, float marginY,
			float textMargin) throws IOException {
		super(d, marginX, marginY, textMargin);
	}

	public void erzeugeSeite(Daten daten, String[] guteEigenschaften,
			List<PDFSonderfertigkeiten> alleSF, Konfiguration k)
			throws IOException {
		List<TalentGruppe> gruppen;
		List<PDFSonderfertigkeiten> sfListe;
		int sfLaenge, linksFrei, rechtsFrei;
		int links;
		int hoehe, beideFrei;

		hoehe = 72;

		gruppen = new ArrayList<TalentSeite.TalentGruppe>();

		gruppen.add(new TalentGruppe("Kampftalente", "Kampf", false));
		gruppen.add(new TalentGruppe("Körperliche Talente", "Körperlich", false));
		gruppen.add(new TalentGruppe("Gesellschaftliche Talente",
				"Gesellschaft", false));
		gruppen.add(new TalentGruppe("Naturtalente", "Natur", false));
		if (daten.getConfig().isMetatalente()) {
			gruppen.add(new TalentGruppe("Metatalente", "Natur", true));
		}
		gruppen.add(new TalentGruppe("Wissenstalente", "Wissen", false));
		gruppen.add(new TalentGruppe("Sprachen/Schriften", "Sprachen",
				"Schriften", false));
		gruppen.add(new TalentGruppe("Handwerkstalente", "Handwerk", false));
		gruppen.add(new TalentGruppe("Sondertalente/Gaben", null, false));

		/* Talente in Gruppen einsortieren */
		for (Talent t : daten.getTalentliste().getTalent()) {
			TalentGruppe gruppe = null;
			for (TalentGruppe g : gruppen) {
				if (g.passendesTalent(t)) {
					gruppe = g;
					break;
				}
			}

			assert (gruppe != null);

			gruppe.add(t);

			if (t.getSpezialisierungen() != null
					&& t.getSpezialisierungen().length() > 0) {
				for (String s : t.getSpezialisierungen().split(",")) {
					s = s.trim();

					gruppe.add(new TalentSpezialisierung(t, s));
				}
			}
		}

		/* Extrahiere Sonderfertigkeiten */
		PDFSonderfertigkeiten.Kategorie kat1[] = { Kategorie.TALENT };
		sfListe = PDFSonderfertigkeiten.extrahiereKategorien(alleSF, kat1);

		/* diese Spezialisierungen werden direkt in der Talentliste angezeigt */
		PDFSonderfertigkeiten.Kategorie kat2[] = { Kategorie.TALENTSPEZ };
		for (PDFSonderfertigkeiten sf : PDFSonderfertigkeiten
				.extrahiereKategorien(alleSF, kat2)) {
			sf.gedruckt();
		}

		/* kalkuliere Platz für Sonderfertigkeiten */
		sfLaenge = (PDFSonderfertigkeiten.anzeigeGroesse(sfListe) + 1) / 2 + 2;
		if (sfLaenge < 6) {
			sfLaenge = 6;
		}

		/* Sortiere Gruppen */
		for (TalentGruppe g : gruppen) {
			Collections.sort(g, new TalentComparator());
		}

		/* berechne Layout */
		links = berechneLayout(gruppen, hoehe, daten.getConfig()
				.isMetatalente());

		linksFrei = (hoehe - 2) - gesammtLaenge(gruppen, 0, links);
		rechtsFrei = (hoehe - 2)
				- gesammtLaenge(gruppen, links, gruppen.size());

		/* Platz für Sonderfertigkeiten? */
		if (rechtsFrei > sfLaenge + 4) {
			rechtsFrei -= (sfLaenge + 1);
		} else {
			sfLaenge = 0;
		}

		/* Eventuell Platz um Zeilenhöhe zu vergrößern? */
		beideFrei = Math.min(linksFrei, rechtsFrei);
		if (beideFrei > 8) {
			beideFrei = Math.min(12, beideFrei - 8);

			linksFrei -= beideFrei;
			rechtsFrei -= beideFrei;
			hoehe -= beideFrei;
		}

		leerzeilenVerteilen(gruppen, 0, links, linksFrei);
		leerzeilenVerteilen(gruppen, links, gruppen.size(), rechtsFrei);

		/* Seite erzeugen */
		initPDFStream(hoehe);

		/* Titelzeile */
		titelzeile(guteEigenschaften);

		/* linke spalte */
		talentSpalte(gruppen, 0, links, 0, halbeBreite, true, k);

		/* rechte spalte */
		talentSpalte(gruppen, links, gruppen.size(), halbeBreite + 1,
				cellCountX, false, k);

		/* Sonderfertigkeiten */
		if (sfLaenge > 0) {
			PDFSonderfertigkeiten.zeichneTabelle(this, halbeBreite + 1,
					cellCountY - sfLaenge, cellCountX - (viertelBreite + 1),
					cellCountY, "Sonderfertigkeiten (1)", sfListe);
			PDFSonderfertigkeiten.zeichneTabelle(this, cellCountX
					- viertelBreite, cellCountY - sfLaenge, cellCountX,
					cellCountY, "Sonderfertigkeiten (2)", sfListe);
		}

		stream.close();
	}

	private int berechneLayout(List<TalentGruppe> gruppen, int hoehe,
			boolean metatalente) {
		TalentGruppe gruppeL, gruppeR;
		boolean genugPlatz;
		int unterschied, besteVerteilung;
		int linkeBloecke;
		int l1, l2;
		int stdLayout;

		if (gesammtLaenge(gruppen, 0, gruppen.size()) + 3 > 2 * (hoehe - 2)) {
			/* okay, es ist einfach zu viel */
			return -1;
		}

		if (metatalente) {
			stdLayout = 5;
		} else {
			stdLayout = 4;
		}

		unterschied = Integer.MAX_VALUE;
		besteVerteilung = -1;
		genugPlatz = false;
		for (linkeBloecke = 1; linkeBloecke < gruppen.size() - 1; linkeBloecke++) {
			l1 = gesammtLaenge(gruppen, 0, linkeBloecke);
			l2 = gesammtLaenge(gruppen, linkeBloecke, gruppen.size());

			if (linkeBloecke == stdLayout && Math.max(l1, l2) < hoehe - 10) {
				/* Standardlayout bevorzugen */
				return linkeBloecke;
			}

			if (Math.abs(l1 - l2) < unterschied) {
				besteVerteilung = linkeBloecke;
				unterschied = Math.abs(l1 - l2);
				genugPlatz = Math.max(l1, l2) < hoehe - 10;
			}
		}

		assert (besteVerteilung != -1);

		if (genugPlatz) {
			return besteVerteilung;
		}

		/* nochmal auf die genauen Werte sehen */
		l1 = gesammtLaenge(gruppen, 0, besteVerteilung);
		l2 = gesammtLaenge(gruppen, besteVerteilung, gruppen.size());

		if (l2 > l1) {
			/* lieber immer von links nach rechts arbeiten */
			besteVerteilung++;

			l1 = gesammtLaenge(gruppen, 0, besteVerteilung);
			l2 = gesammtLaenge(gruppen, besteVerteilung, gruppen.size());
		}

		/* Gruppe splitten */
		gruppeL = gruppen.get(besteVerteilung - 1);
		gruppeL.leerzeilen = false;
		gruppeR = new TalentGruppe(gruppeL.titel, gruppeL.bereich1,
				gruppeL.bereich2, gruppeL.metatalent);
		gruppen.add(besteVerteilung, gruppeR);

		unterschied = l1 - (l2 + 2);

		do {
			Talent t;

			t = gruppeL.remove(gruppeL.size() - 1);
			gruppeR.add(0, t);

			unterschied -= 2;
		} while (unterschied > 1);

		if (gruppeR.get(0) instanceof TalentSpezialisierung) {
			TalentSpezialisierung ts = (TalentSpezialisierung) gruppeR.get(0);

			gruppeR.add(0, ts.getSpezReferenz());
		}

		return besteVerteilung;
	}

	private int gesammtLaenge(List<TalentSeite.TalentGruppe> gruppen, int from,
			int to) {
		int laenge = 0;

		for (int i = from; i < to; i++) {
			if (i > from) {
				/* Leerzeile */
				laenge++;
			}

			/* Titel */
			laenge++;

			/* Einträge */
			laenge += gruppen.get(i).size();

		}
		return laenge;
	}

	private void leerzeilenVerteilen(List<TalentSeite.TalentGruppe> gruppen,
			int from, int to, int zeilen) {
		int i;

		i = from;
		while (zeilen > 0) {
			TalentGruppe g = gruppen.get(i);

			if (g.leerzeilen) {
				gruppen.get(i).add(null);
				zeilen--;
			}

			i++;
			if (i == to) {
				i = from;
			}
		}
	}

	private void talentSpalte(List<TalentGruppe> gruppen, int from, int to,
			int x1, int x2, boolean links, Konfiguration ko) throws IOException {
		boolean basis;
		int talentBreite[], kampfBreite[];
		int y;

		basis = ko.getOptionsDaten(Konfiguration.TALENT_BASISTALENTE);

		/* talentgruppen zeichnen */
		talentBreite = new int[] { 0, 0, 0, 0, 0, 6, 2, 2, 3, 3, 2 };
		kampfBreite = new int[] { 0, 2, 2, 2, 2, 0, 2, 2, 3, 3, 2 };

		if (!ko.getOptionsDaten(Konfiguration.TALENT_IMMER_LEERESPALTEN)) {
			boolean behinderungLeer = true;
			boolean sternLeer = true;
			
			for (int gIndex = from; gIndex < to; gIndex++) {
				TalentGruppe g = gruppen.get(gIndex);
				for (Talent t : g) {
					if (t == null) {
						continue;
					}
					
					if (t.getBehinderung().length() > 0) {
						behinderungLeer = false;
					}
					if (TalentSeite.getStern(t).length() > 0) {
						sternLeer = false;
					}
				}
			}
			
			if (behinderungLeer) {
				talentBreite[8] = 0;
				kampfBreite[8] = 0;
			}
			if (sternLeer) {
				talentBreite[9] = 0;
				kampfBreite[9] = 0;
			}
		}
		
		y = 2;
		for (int gIndex = from; gIndex < to; gIndex++) {
			TalentGruppe g = gruppen.get(gIndex);

			if (links && gIndex == 0) {
				drawTabelle(x1, x2, y, g.toArray(), new TalentTabelle(g.titel,
						kampfBreite, x2 - x1, basis));
			} else {
				drawTabelle(x1, x2, y, g.toArray(), new TalentTabelle(g.titel,
						talentBreite, x2 - x1, basis));
			}

			y += g.size() + 2;
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

	private class TalentGruppe extends ArrayList<Talent> {
		/**
		 * random serialUID
		 */
		private static final long serialVersionUID = -8391017860638548671L;

		public final String titel;
		public final String bereich1, bereich2;
		public final boolean metatalent;
		public boolean leerzeilen;

		public TalentGruppe(String titel, String bereich, boolean metatalent) {
			super();
			this.titel = titel;
			this.bereich1 = bereich;
			this.bereich2 = bereich;
			this.metatalent = metatalent;
			this.leerzeilen = !metatalent;
		}

		public TalentGruppe(String titel, String bereich1, String bereich2,
				boolean metatalent) {
			super();
			this.titel = titel;
			this.bereich1 = bereich1;
			this.bereich2 = bereich2;
			this.metatalent = metatalent;
			this.leerzeilen = !metatalent;
		}

		public boolean passendesTalent(Talent t) {
			if (this.bereich1 == null) {
				return true;
			}
			if (t.isMetatalent() != this.metatalent) {
				return false;
			}
			return t.getBereich().equals(this.bereich1)
					|| t.getBereich().equals(this.bereich2);
		}
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

	static public String getStern(Talent t) {
		String stern = "";
		
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
	}

	private class TalentTabelle extends AbstractTabellenZugriff {
		boolean markiereBasis;

		public TalentTabelle(String titel, int[] columns, int width,
				boolean markiereBasis) {
			super(new String[] { null, "AT", "", "PA", "", "Probe", "TaW", "",
					"BE", "*", "SKT" }, columns, 0, titel, width);
			this.markiereBasis = markiereBasis;
		}

		@Override
		public String get(Object obj, int x) {
			Talent t = (Talent) obj;
			String name;

			if (t instanceof TalentSpezialisierung) {
				return getSpez(x, t);
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
				return t.getAt();
			case 3:
				return t.getPa();
			case 5:
				return t.getProbe();
			case 6:
				if (t.getWert() != null) {
					return t.getWert().toString();
				}
				return "";
			case 8:
				if (t.getBehinderung() != null) {
					return t.getBehinderung();
				}
				return "";
			case 9:
				return TalentSeite.getStern(t);
			case 10:
				if (t.getLernkomplexität() != null && !t.isMetatalent()) {
					return t.getLernkomplexität();
				}
				return "";
			default:
				return "";
			}
		}

		
		private String getSpez(int x, Talent t) {
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
			case 6:
				return Integer.toString(ts.getSpezValue());
			}
			return "";
		}

		@Override
		public PDFont getFont(Object o, int x) {
			Talent t;

			if (o instanceof TalentSpezialisierung) {
				TalentSpezialisierung ts = (TalentSpezialisierung) o;
				t = ts.getSpezReferenz();
			} else {
				t = (Talent) o;
			}

			if (this.markiereBasis && t.isBasis() && x == 0) {
				return PDType1Font.HELVETICA_BOLD;
			}
			return PDType1Font.HELVETICA;
		}

		@Override
		public int getColumnSpan(int x) {
			if (x == 1 || x == 3 || x == 6) {
				return 2;
			}
			return 1;
		}

		@Override
		public int getIndent(Object o, int x) {
			return (o instanceof TalentSpezialisierung) && x == 0 ? 2 : 0;
		}

	}
}

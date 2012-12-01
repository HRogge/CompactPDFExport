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
import jaxbGenerated.datenxml.Zauber;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import de.hrogge.CompactPDFExport.PDFSonderfertigkeiten.Kategorie;

public class ZauberSeite extends PDFSeite {
	public ZauberSeite(PDDocument d, float marginX, float marginY,
			float textMargin) throws IOException {
		super(d, marginX, marginY, textMargin);
	}

	public void erzeugeSeite(Daten daten, String[] guteEigenschaften,
			List<PDFSonderfertigkeiten> alleSF, boolean notizen)
			throws IOException {
		List<PDFSonderfertigkeiten> sfListe;
		List<Zauber> zauberListe;
		int sfBreite, hoehe;
		sfBreite = 15;

		/* Generiere Liste der magischen Sonderfertigkeiten */
		PDFSonderfertigkeiten.Kategorie kat1[] = { Kategorie.MAGISCH };
		sfListe = PDFSonderfertigkeiten.extrahiereKategorien(alleSF, kat1);

		for (PDFSonderfertigkeiten sf : sfListe) {
			sf.gedruckt();
		}

		/* diese Spezialisierungen werden direkt in der Zauberliste angezeigt */
		PDFSonderfertigkeiten.Kategorie kat2[] = { Kategorie.ZAUBERSPEZ };
		for (PDFSonderfertigkeiten sf : PDFSonderfertigkeiten
				.extrahiereKategorien(alleSF, kat2)) {
			sf.gedruckt();
		}

		/* Generiere Liste der Zauber */
		zauberListe = new ArrayList<Zauber>();

		for (Zauber z : daten.getZauberliste().getZauber()) {
			zauberListe.add(z);

			if (z.getSpezialisierungen() != null
					&& z.getSpezialisierungen().length() > 0) {
				for (String s : z.getSpezialisierungen().split(",")) {
					s = s.trim();

					zauberListe.add(new ZauberSpezialisierung(z, s));
				}
			}
		}
		Collections.sort(zauberListe, new ZauberComparator());

		/* Höhe der Seite bestimmen */
		hoehe = Math.max(zauberListe.size(), sfListe.size());
		if (hoehe > 72) {
			/* Maximum 72 */
			hoehe = 72;
		} else if (hoehe < 72 - 5) {
			/* Minimum 60, aber 5 Plätze frei */
			hoehe = Math.max(60, hoehe + 5);
		}

		while (zauberListe.size() > 0) {
			initPDFStream(hoehe);

			titelzeile(guteEigenschaften);

			if (notizen) {
				zeichneZauber(zauberListe, cellCountX,
						new ZauberTabelleNotizen());
			} else {
				zeichneZauber(zauberListe, cellCountX - sfBreite - 1,
						new ZauberTabelle(cellCountX - sfBreite - 1));

				PDFSonderfertigkeiten.zeichneTabelle(this, cellCountX
						- sfBreite, 2, cellCountX, cellCountY,
						"Sonderfertigkeiten", sfListe);
			}

			stream.close();
		}
	}

	private void zeichneZauber(List<Zauber> zauberListe, int breite,
			AbstractTabellenZugriff tabelle) throws IOException {
		List<Zauber> seitenListe;
		int count;

		seitenListe = new ArrayList<Zauber>();

		count = Math.min(zauberListe.size(), cellCountY - 3);

		/* Liste von Spezialisierungen aufgespalten? */
		if (count < zauberListe.size()) {
			while (zauberListe.get(count) instanceof ZauberSpezialisierung) {
				count--;
			}
		}

		for (int i = 0; i < count; i++) {
			seitenListe.add(zauberListe.remove(0));
		}

		/* zu wenige Zauber ? */
		while (seitenListe.size() < cellCountY - 3) {
			seitenListe.add(null);
		}

		drawTabelle(0, breite, 2, seitenListe.toArray(), tabelle);

		stream.closeAndStroke();
	}

	private class ZauberComparator implements Comparator<Zauber> {
		@Override
		public int compare(Zauber o1, Zauber o2) {
			ZauberSpezialisierung s1 = null, s2 = null;
			int comp;

			if (o1 instanceof ZauberSpezialisierung) {
				s1 = (ZauberSpezialisierung) o1;
				o1 = s1.getSpezReferenz();
			}
			if (o2 instanceof ZauberSpezialisierung) {
				s2 = (ZauberSpezialisierung) o2;
				o2 = s2.getSpezReferenz();
			}

			if (o1.isHauszauber() && !o2.isHauszauber()) {
				return -1;
			}
			if (!o1.isHauszauber() && o2.isHauszauber()) {
				return 1;
			}
			comp = o1.getName().compareTo(o2.getName());
			if (comp != 0) {
				return comp;
			}

			comp = o1.getRepräsentation().compareTo(o2.getRepräsentation());
			if (comp != 0) {
				return comp;
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

	private class ZauberSpezialisierung extends Zauber {
		private String spezName;
		private int spezValue;
		private Zauber referenz;

		public ZauberSpezialisierung(Zauber t, String spezialisierung) {
			super();
			referenz = t;
			spezName = spezialisierung;
			spezValue = t.getWert().intValue() + 2;
		}

		public String getSpezName() {
			return spezName;
		}

		public Zauber getSpezReferenz() {
			return referenz;
		}

		public int getSpezValue() {
			return spezValue;
		}
	}

	private class ZauberTabelle extends AbstractTabellenZugriff {
		public ZauberTabelle(int breite) {
			super(new String[] { null, "Probe", "MR", "ZfW", "", "Seite",
					"SKT", "Merkmal", "Repräs." }, new int[] { 0, 6, 3, 2, 2,
					3, 2, 9, 4 }, 0, "Zaubername", breite);
		}

		protected ZauberTabelle(String[] spaltenTitel, int[] spaltenBreite,
				int breite) {
			super(spaltenTitel, spaltenBreite, 0, "Zaubername", breite);
		}

		@Override
		public String get(Object obj, int x) {
			Zauber z;

			if (obj instanceof ZauberSpezialisierung) {
				ZauberSpezialisierung zs = (ZauberSpezialisierung) obj;
				if (x == 0) {
					return zs.getSpezName();
				} else if (x == 3) {
					return Integer.toString(zs.getSpezValue());
				}
				return "";
			}

			z = (Zauber) obj;

			switch (x) {
			case 0:
				return z.getNamemitvariante();
			case 1:
				return z.getProbe();
			case 2:
				return z.getMr();
			case 3:
				return z.getWert().toString();
			case 5:
				return z.getQuelle().getSeite().toString();
			case 6:
				return z.getLernkomplexität();
			case 7:
				return z.getMerkmale();
			case 8:
				return z.getRepräsentation();
			}
			return "";
		}

		@Override
		public int getColumnSpan(int x) {
			return x == 3 ? 2 : 1;
		}

		@Override
		public PDFont getFont(Object o, int x) {
			Zauber z = (Zauber) o;
			if (z instanceof ZauberSpezialisierung) {
				ZauberSpezialisierung zs = (ZauberSpezialisierung) z;
				z = zs.getSpezReferenz();
			}
			if (z != null && x == 0 && z.isHauszauber()) {
				return PDType1Font.HELVETICA_BOLD;
			} else {
				return PDType1Font.HELVETICA;
			}
		}

		@Override
		public int getIndent(Object o, int x) {
			if (x == 0 && o instanceof ZauberSpezialisierung) {
				return 2;
			}
			return 0;
		}
	}

	private class ZauberTabelleNotizen extends ZauberTabelle {
		public ZauberTabelleNotizen() {
			super(new String[] { null, "Probe", "MR", "ZfW", "", "Seite", "ZD",
					"RW", "AsP", "WD", "SKT", "Merkmal", "Repräs.", },
					new int[] { 0, 6, 3, 2, 2, 3, 4, 4, 4, 4, 2, 9, 4 },
					cellCountX);
		}

		@Override
		public String get(Object obj, int x) {
			Zauber z;

			if (obj instanceof ZauberSpezialisierung) {
				return super.get(obj, x);
			}
			if (x < 6) {
				return super.get(obj, x);
			}
			if (x > 9) {
				return super.get(obj, x - 4);
			}

			z = (Zauber) obj;

			switch (x) {
			case 6:
				return z.getZauberdauer();
			case 7:
				return z.getReichweite();
			case 8:
				return z.getKosten();
			case 9:
				return z.getWirkungsdauer();
			}
			return "";
		}
	}
}

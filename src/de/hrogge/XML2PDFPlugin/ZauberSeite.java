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

import java.io.IOException;
import java.util.*;

import jaxbGenerated.datenxml.Daten;
import jaxbGenerated.datenxml.Zauber;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import de.hrogge.XML2PDFPlugin.PDFSonderfertigkeiten.Kategorie;

public class ZauberSeite extends PDFSeite {
	public ZauberSeite(PDDocument d, float marginX, float marginY,
			float textMargin) throws IOException {
		super(d, marginX, marginY, textMargin, 72);
	}

	public void erzeugeSeite(Daten daten, String[] guteEigenschaften,
			List<PDFSonderfertigkeiten> alleSF) throws IOException {
		List<PDFSonderfertigkeiten> sfList;
		int sfBreite;

		sfBreite = 16;
		PDFSonderfertigkeiten.Kategorie kat[] = { Kategorie.MAGISCH };
		sfList = PDFSonderfertigkeiten.extrahiereKategorien(alleSF, kat);
		Collections.sort(sfList);

		stream = new PDPageContentStream(doc, page);

		titelzeile(guteEigenschaften);
		zauber(daten, cellCountX - sfBreite - 1);

		PDFSonderfertigkeiten.zeichneTabelle(this, cellCountX - sfBreite, 2,
				cellCountX, cellCountY, "Sonderfertigkeiten", sfList);

		stream.close();
	}

	private void titelzeile(String[] guteEigenschaften) throws IOException {
		String[] titel = { "MU:", "KL:", "IN:", "CH:", "FF:", "GE:", "KK:",
				"KO:" };

		for (int i = 0; i < titel.length; i++) {
			int x = i * 8 + 1;

			drawText(PDType1Font.HELVETICA_BOLD, x + 0, x + 3, 0, 2, titel[i],
					true);
			drawText(PDType1Font.HELVETICA_BOLD, x + 3, x + 6, 0, 2,
					guteEigenschaften[i], true);
		}
	}

	private void zauber(Daten daten, int breite) throws IOException {
		List<Zauber> zauberListe;

		/* lade Zauber */
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

		/* zu viele Zauber ? */
		while (zauberListe.size() >= cellCountY - 3) {
			while (zauberListe.remove(zauberListe.size() - 1) instanceof ZauberSpezialisierung)
				;
		}

		/* zu wenige Zauber ? */
		while (zauberListe.size() < cellCountY - 3) {
			zauberListe.add(null);
		}

		drawTabelle(0, breite, 2, zauberListe.toArray(), new ZauberTabelle(
				cellCountX - 17));

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
			super(new String[] { null, "Probe", "ZfW", "", "Seite", "*", "SKT",
					"Merkmal", "Repräsentation" }, new int[] { 0, 6, 2, 2, 3,
					2, 2, 8, 6 }, 0, "Zaubername", breite);
		}

		@Override
		public String get(Object obj, int x) {
			Zauber z = (Zauber) obj;
			String stern;

			if (obj instanceof ZauberSpezialisierung) {
				if (z instanceof ZauberSpezialisierung) {
					ZauberSpezialisierung zs = (ZauberSpezialisierung) z;
					if (x == 0) {
						return zs.getSpezName();
					} else if (x == 2) {
						return Integer.toString(zs.getSpezValue());
					} else {
						return "";
					}
				}
			}

			switch (x) {
			case 0:
				return z.getNamemitvariante();
			case 1:
				return z.getProbe();
			case 2:
				return z.getWert().toString();
			case 4:
				return z.getQuelle().getSeite().toString();
			case 5:
				stern = "";
				if (z.isHauszauber()) {
					stern += "H";
				}
				return stern;
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

		@Override
		public int getColumnSpan(int x) {
			return x == 2 ? 2 : 1;
		}
	}
}

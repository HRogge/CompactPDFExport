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
import java.text.Collator;
import java.util.*;

import javax.xml.xpath.XPath;

import jaxbGenerated.datenxml.Daten;
import jaxbGenerated.datenxml.Zauber;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import org.w3c.dom.Element;

import de.hrogge.CompactPDFExport.PDFSonderfertigkeiten.Kategorie;
import de.hrogge.CompactPDFExport.gui.Konfiguration;

public class ZauberSeite extends PDFSeite {
	public ZauberSeite(PDDocument d) throws IOException {
		super(d);
	}

	public void erzeugeSeite(ExtXPath xpath, PDJpeg hintergrund,
			String[] guteEigenschaften, List<PDFSonderfertigkeiten> alleSF,
			Hausregeln hausregeln, List<String> commands, Konfiguration k)
			throws IOException {
		List<PDFSonderfertigkeiten> sfListe;
		List<Zauber> zauberListe;
		int zauberBreite, sfBreite, hoehe, bonus;
		int zauberSpalten[];
		boolean mehrereRepr, first;
		String repr;

		sfBreite = 15;

		/* Generiere Liste der magischen Sonderfertigkeiten */
		PDFSonderfertigkeiten.Kategorie kat1[] = { Kategorie.MAGISCH };
		sfListe = PDFSonderfertigkeiten.extrahiereKategorien(alleSF, kat1);
		Collections.sort(sfListe);

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
		mehrereRepr = false;
		repr = null;

		for (Zauber z : daten.getZauberliste().getZauber()) {
			zauberListe.add(z);

			/* Prüfe auf mehrere Repräsentationen */
			if (repr != null) {
				mehrereRepr |= !repr.equals(z.getRepräsentation());
			}
			repr = z.getRepräsentation();

			if (z.getSpezialisierungen() != null
					&& z.getSpezialisierungen().length() > 0) {
				for (String s : z.getSpezialisierungen().split(",")) {
					s = s.trim();

					zauberListe.add(new ZauberSpezialisierung(z, s));
				}
			}
		}
		for (String cmd : commands) {
			String[] split = cmd.split(":");

			if (split.length != 3) {
				continue;
			}

			Zauber z = hausregeln.getEigenenZauber(split[0], split[1], split[2]);
			if (z != null) {
				zauberListe.add(z);
			}
		}

		Collections.sort(
				zauberListe,
				new ZauberComparator(k
						.getOptionsDaten(Konfiguration.ZAUBER_HAUSZAUBEROBEN)));

		/* Höhe der Seite bestimmen */
		hoehe = Math.max(zauberListe.size(), sfListe.size());
		if (hoehe > 72) {
			/* Maximum 72 */
			hoehe = 72;
		} else if (hoehe < 72 - 5) {
			/* Minimum 60, aber 5 Plätze frei */
			hoehe = Math.max(60, hoehe + 5);
		}

		bonus = 0;
		zauberSpalten = new int[] { 0, 6, 2, 2, 3, 5, 5, 5, 5, 2, 2, 9, 0 };
		/*
		 * 0: Name 1: Probe 2: Wert 3: Wert 2 4: Seitenzahl 5: Zauberdauer 6:
		 * Reichweite 7: Kosten 8: Wirkungsdauer 9: Lernkomplexität 10:
		 * Repräsentation 11: Merkmal 12: Anmerkung
		 */

		if (!mehrereRepr
				&& !k.getOptionsDaten(Konfiguration.ZAUBER_IMMER_REPRAESENTATION)) {
			// Keine Repräsentations-Spalte
			bonus += zauberSpalten[10];
			zauberSpalten[10] = 0;
		}

		if (!k.getOptionsDaten(Konfiguration.ZAUBER_SEITENZAHLEN)) {
			// Keine Seitenzahlen
			bonus += zauberSpalten[4];
			zauberSpalten[4] = 0;
		}

		if (k.getOptionsDaten(Konfiguration.ZAUBER_NOTIZEN_WERTE)) {
			for (int i = 5; i <= 8; i++) {
				zauberSpalten[i] += bonus / 4;
			}
			for (int i = 8, j = bonus % 4; j > 0; i--, j--) {
				zauberSpalten[i]++;
			}
		} else if (k.getOptionsDaten(Konfiguration.ZAUBER_NOTIZEN_ANMERKUNGEN)) {
			for (int i = 5; i <= 8; i++) {
				zauberSpalten[i] = 0;
			}
			zauberSpalten[12] = sfBreite;
		} else { // k.getOptionsDaten(Konfiguration.ZAUBER_NOTIZEN_KEINE
			for (int i = 5; i <= 8; i++) {
				zauberSpalten[i] = 0;
			}
		}

		if (k.getOptionsDaten(Konfiguration.ZAUBER_NOTIZEN_KEINE)) {
			zauberBreite = cellCountX - sfBreite - 1;
		} else {
			zauberBreite = cellCountX;
		}

		first = true;
		while (zauberListe.size() > 0) {
			if (first) {
				first = false;
			} else {
				neueSeite();
			}
			initPDFStream(hoehe);

			titelzeile(guteEigenschaften);
			zeichneZauber(zauberListe, zauberBreite, zauberSpalten, k);

			if (k.getOptionsDaten(Konfiguration.ZAUBER_NOTIZEN_KEINE)) {
				PDFSonderfertigkeiten.zeichneTabelle(this, cellCountX
						- sfBreite, 2, cellCountX, cellCountY,
						"Sonderfertigkeiten", sfListe);
			}

			stream.close();
		}
	}

	private void zeichneZauber(List<Zauber> zauberListe, int breite,
			int[] spaltenBreite, Konfiguration ko) throws IOException {
		boolean probenwerte;
		List<Zauber> seitenListe;
		int count;

		probenwerte = ko.getOptionsDaten(Konfiguration.GLOBAL_PROBENWERTE);

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

		drawTabelle(0, breite, 2, seitenListe.toArray(), new ZauberTabelle(
				spaltenBreite, breite, probenwerte));
	}

	private class ZauberComparator implements Comparator<Zauber> {
		private Collator col;
		boolean hauszauberOben;

		public ZauberComparator(boolean hauszauberOben) {
			this.hauszauberOben = hauszauberOben;
			this.col = Collator.getInstance();
		}

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
			if (this.hauszauberOben) {
				if (o1.isHauszauber() && !o2.isHauszauber()) {
					return -1;
				}
				if (!o1.isHauszauber() && o2.isHauszauber()) {
					return 1;
				}
			}
			comp = col.compare(o1.getName(), o2.getName());
			if (comp != 0) {
				return comp;
			}

			comp = col.compare(o1.getRepräsentation(), o2.getRepräsentation());
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
				return col.compare(s1.getSpezName(), s2.getSpezName());
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
		boolean probenWerte;

		public ZauberTabelle(int[] spaltenBreite, int breite,
				boolean probenWerte) {
			super(new String[] { null, "Probe", "ZfW", "", "Seite", "ZD", "RW",
					"AsP", "WD", "SKT", "Rep", "Merkmal", "Anmerkung" },
					spaltenBreite, 0, "Zaubername", breite);
			this.probenWerte = probenWerte;
		}

		@Override
		public String get(Object obj, int x) {
			Zauber z;

			if (obj instanceof ZauberSpezialisierung) {
				ZauberSpezialisierung zs = (ZauberSpezialisierung) obj;
				if (x == 0) {
					return zs.getSpezName();
				} else if (x == 2) {
					return Integer.toString(zs.getSpezValue());
				}
				return "";
			}

			z = (Zauber) obj;

			switch (x) {
			case 0:
				if (z.getMr().length() > 0) {
					return z.getNamemitvariante() + " (" + z.getMr() + ")";
				}
				return z.getNamemitvariante();
			case 1:
				if (probenWerte && !z.getProbe().equals("--/--/--")) {
					return z.getProbenwerte();
				} else {
					return z.getProbe();
				}
			case 2:
				return z.getWert().toString();
			case 4:
				if (z.getQuelle() == null) {
					return "";
				}
				return z.getQuelle().getSeite().toString();
			case 5:
				return z.getZauberdauer();
			case 6:
				return z.getReichweite();
			case 7:
				return z.getKosten();
			case 8:
				return z.getWirkungsdauer();
			case 9:
				return z.getLernkomplexität();
			case 10:
				if (z.getRepräsentation().length() < 3) {
					return z.getRepräsentation();
				}
				return z.getRepräsentation().substring(0, 3);
			case 11:
				return z.getMerkmale();
			case 12:
				return z.getAnmerkung();

			}
			return "";
		}

		@Override
		public int getColumnSpan(int x) {
			return x == 2 ? 2 : 1;
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
}

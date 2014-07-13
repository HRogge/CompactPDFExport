package de.hrogge.CompactPDFExport;

import java.io.IOException;
import java.util.*;

import jaxbGenerated.datenxml.Gegenstand;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;

public class SonstigesSeite extends PDFSeite {
	public SonstigesSeite(PDDocument d) throws IOException {
		super(d);
	}

	public void erzeugeSeite(PDJpeg hintergrund, String[] guteEigenschaften,
			List<PDFSonderfertigkeiten> alleSF, List<Gegenstand> ausruestung) throws IOException {
		List<PDFSonderfertigkeiten> sfListe;
		int col;
		int x;
		
		sfListe = new ArrayList<PDFSonderfertigkeiten>(alleSF);

		initPDFStream(60);
		titelzeile(guteEigenschaften);
		col = 3;
		
		x = 0;
		while (col > 0 && sfFilter(sfListe) > 0) {
			PDFSonderfertigkeiten.zeichneTabelle(this, x, 2, x+20, cellCountY,
					"Sonderfertigkeiten 1", sfListe);
			col--;
			x+=21;
			sfFilter(sfListe);
		}

		while (col > 0 && ausruestung.size() > 0) {
			zeichneAusruestung(x, ausruestung);
			col--;
			x+=21;
		}
		stream.close();
	}

	private int sfFilter(List<PDFSonderfertigkeiten> alleSF) {
		while (alleSF.size() > 0 && alleSF.get(0) == null) {
			alleSF.remove(0);
		}
		return alleSF.size();
	}
	
	private void zeichneAusruestung(int x, List<Gegenstand> alleGegenstaende) throws IOException {
		List<Gegenstand> seite;
		
		seite = new ArrayList<>();
		while(seite.size() < cellCountY && alleGegenstaende.size() > 0) {
			seite.add(alleGegenstaende.remove(0));
		}
		
		/* zu wenige Zauber ? */
		while (seite.size() < cellCountY - 3) {
			seite.add(null);
		}
		drawTabelle(x, x+20, 2, seite.toArray(), new AusruestungsZabelle());
	}
	
	private class AusruestungsZabelle extends AbstractTabellenZugriff {
		public AusruestungsZabelle() {
			super(new String[] { "#", null, "Unzen" },
					new int[] { 2, 0, 3 }, 3, "Ausrüstung", 20);
		}
		
		@Override
		public String get(Object obj, int x) {
			Gegenstand g = (Gegenstand)obj;
			String gew;
			
			switch (x) {
			case 0:
				return g.getAnzahl().toString();
			case 1:
				return g.getName();
			case 2:
				gew = g.getGewicht().toString();
				if (gew.endsWith(".0")) {
					gew = gew.substring(0, gew.length()-2);
				}
				return gew;
			}
			return "";
		}
	}
}

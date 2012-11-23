package de.hrogge.CompactPDFExport;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jaxbGenerated.datenxml.Sonderfertigkeit;

import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class PDFSonderfertigkeiten implements Comparable<PDFSonderfertigkeiten> {
	static public int anzeigeGroesse(List<PDFSonderfertigkeiten> sflist) {
		String typ = null;
		int count = 0;
		
		for (PDFSonderfertigkeiten sf : sflist) {
			if (sf.getTyp().length() == 0) {
				typ = null;
			} else {
				if (!sf.getTyp().equals(typ)) {
					typ = sf.getTyp();
					count++;
				}
			}
			count++;
		}
		return count;
	}
	
	static public List<PDFSonderfertigkeiten> extrahiereKategorien(
			List<PDFSonderfertigkeiten> sflist, Kategorie[] k) {
		List<Kategorie> katlist;
		List<PDFSonderfertigkeiten> res;

		res = new ArrayList<PDFSonderfertigkeiten>();
		katlist = Arrays.asList(k);
		for (PDFSonderfertigkeiten sf : sflist) {
			if (katlist.contains(sf.getKategorie())) {
				res.add(sf);
			}
		}
		return res;
	}

	static public void zeichneTabelle(PDFSeite seite, int x1, int y1, int x2,
			int y2, String titel, List<PDFSonderfertigkeiten> sflist)
			throws IOException {
		String typ;
		PDFSonderfertigkeiten s;

		seite.drawLabeledBox(x1, y1, x2, y2, titel);

		seite.getStream().setStrokingColor(Color.BLACK);
		seite.getStream().setLineWidth(0.1f);
		for (int y = y1 + 2; y < y2; y++) {
			seite.addLine(x1, y, x2, y);
		}
		seite.getStream().closeAndStroke();

		typ = null;
		for (int y = y1 + 1; !sflist.isEmpty() && y < y2; y++) {
			s = sflist.get(0);

			if (s.getTyp().length() == 0) {
				typ = null;
			
				seite.drawText(PDType1Font.HELVETICA, x1, x2, y, s.getName(),
						false);
			} else {
				if (!s.getTyp().equals(typ)) {
					if (y == y2 - 1) {
						break;
					}
					typ = s.getTyp();
					
					seite.drawText(PDType1Font.HELVETICA, x1, x2, y,
							s.getTyp(), false);
					y++;
				}

				seite.drawText(PDType1Font.HELVETICA, x1 + 2, x2, y,
						s.getName(), false);
			}
			
			sflist.remove(0);
		}
	}

	private Kategorie kategorie;
	private String typ;
	private String name;;

	public PDFSonderfertigkeiten(Sonderfertigkeit sf) {
		this.kategorie = berechneKategorie(sf);
		berechneTypName(sf, this.kategorie, null);
	}

	public PDFSonderfertigkeiten(Sonderfertigkeit sf, String auswahl) {
		this.kategorie = berechneKategorie(sf);
		berechneTypName(sf, this.kategorie, auswahl);
	}

	@Override
	public int compareTo(PDFSonderfertigkeiten o2) {
		int result;
		String typ1, typ2;
		
		result = this.getKategorie().compareTo(o2.getKategorie());
		if (result != 0) {
			return result;
		}

		typ1 = this.getTyp();
		if (typ1.length() == 0) {
			typ1 = this.getName();
		}
		typ2 = o2.getTyp();
		if (typ2.length() == 0) {
			typ2 = o2.getName();
		}
		result = typ1.compareTo(typ2);
		if (result != 0) {
			return result;
		}

		return this.getName().compareTo(o2.getName());
	}

	public String getName() {
		return this.name;
	}

	public String getTyp() {
		return this.typ;
	}

	private Kategorie berechneKategorie(Sonderfertigkeit sf) {
		List<String> bereich = sf.getBereich();
		if (bereich.contains("Kampf")) {
			return Kategorie.KAMPF;
		}

		if (bereich.contains("Geweiht")) {
			if (bereich.contains("Liturgie")) {
				return Kategorie.LITURGIE;
			} else {
				return Kategorie.GEWEIHT;
			}
		}

		if (bereich.contains("Magisch")) {
			if (bereich.contains("Talentspezialisierung")) {
				return Kategorie.ZAUBERSPEZ;
			} else {
				return Kategorie.MAGISCH;
			}
		}

		if (bereich.contains("Talentspezialisierung")) {
			return Kategorie.TALENTSPEZ;
		}
		if (bereich.contains("Sonst")) {
			return Kategorie.TALENT;
		}

		return Kategorie.UNBEKANNT;
	}

	private void berechneTypName(Sonderfertigkeit sf, Kategorie k, String auswahl) {
		String n;
		int idx;

		n = sf.getNameausfuehrlich();
		idx = n.indexOf(": ");

		switch (k) {
		case KAMPF:
			this.typ = "Kampfsonderfertigkeit:";
			this.name = sf.getNameausfuehrlich();
			break;
		case MAGISCH:
			if (auswahl != null) {
				this.typ = sf.getBezeichner();
				this.name = auswahl;
			}
			else if (idx != -1) {
				this.typ = n.substring(0, idx+1);
				this.name = n.substring(idx + 2);
			} else {
				this.typ = "";
				this.name = n;
			}
			break;
		case ZAUBERSPEZ:
			this.typ = "Zauberspezialisierung:";
			this.name = n.substring(n.indexOf(' ') + 1);
			break;
		case GEWEIHT:
			this.typ = "";
			this.name = n;
			break;
		case LITURGIE:
			this.typ = "Liturgie Grad " + sf.getGrad().toString() + ":";
			this.name = sf.getName();
			break;
		case TALENTSPEZ:
			this.typ = "Talentspezialisierung:";
			this.name = n.substring(n.indexOf(' ') + 1);
			break;
		case TALENT:
			if (sf.getBereich().contains("Geländekunde")) {
				this.typ = "Geländekunde:";
				this.name = n;
				break;
			}
			if (sf.getBezeichner().equals("Kulturkunde")) {
				this.typ = "Kulturkunde:";
				this.name = auswahl;
				break;
			}
			this.typ = "";
			this.name = n;
			break;
		case UNBEKANNT:
			this.typ = "Unbekannt";
			this.name = n;
			break;
		}
	}

	private Kategorie getKategorie() {
		return this.kategorie;
	}

	public enum Kategorie {
		KAMPF, MAGISCH, ZAUBERSPEZ, GEWEIHT, LITURGIE, TALENTSPEZ, TALENT, UNBEKANNT;
	}
}

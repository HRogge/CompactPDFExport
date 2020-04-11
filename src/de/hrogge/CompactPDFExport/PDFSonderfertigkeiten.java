package de.hrogge.CompactPDFExport;

import java.awt.Color;
import java.io.IOException;
import java.text.Collator;
import java.util.*;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PDFSonderfertigkeiten implements Comparable<PDFSonderfertigkeiten> {
	private static final Collator col = Collator.getInstance();

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
		if (y1 + 2 < y2) {
			seite.getStream().closeAndStroke();
		}

		typ = null;
		for (int y = y1 + 1; !sflist.isEmpty() && y < y2; y++) {
			s = sflist.get(0);

			if (s != null) {
				s.gedruckt();
				if (s.getTyp().length() == 0) {
					typ = null;

					seite.drawText(PDType1Font.HELVETICA, x1, x2, y,
							s.getName(), false);
				} else {
					if (!s.getTyp().equals(typ)) {
						if (y == y2 - 1) {
							break;
						}
						typ = s.getTyp();

						seite.drawText(PDType1Font.HELVETICA_BOLD, x1, x2, y,
								s.getTyp(), false);
						y++;
					}

					seite.drawText(PDType1Font.HELVETICA, x1 + 2, x2, y,
							s.getName(), false);
				}
			}
			sflist.remove(0);
		}
	}

	private Kategorie kategorie;
	private String typ;
	private String name;;
	private boolean _gedruckt;

	public PDFSonderfertigkeiten(ExtXPath xpath, Node sf) throws XPathExpressionException {
		List<String> bereich = berechneBereiche(xpath, sf);
		this.kategorie = berechneKategorie(bereich);
		berechneTypName(xpath, sf, this.kategorie, null, bereich);
		_gedruckt = false;
	}

	public PDFSonderfertigkeiten(ExtXPath xpath, Node sf, String auswahl) throws XPathExpressionException {
		List<String> bereich = berechneBereiche(xpath, sf);
		this.kategorie = berechneKategorie(bereich);
		berechneTypName(xpath, sf, this.kategorie, auswahl, bereich);
		_gedruckt = false;
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
		result = col.compare(typ1, typ2);
		if (result != 0) {
			return result;
		}

		return col.compare(this.getName(), o2.getName());
	}

	public void gedruckt() {
		_gedruckt = true;
	}

	public Kategorie getKategorie() {
		return this.kategorie;
	}

	public String getName() {
		return this.name;
	}

	public String getTyp() {
		return this.typ;
	}

	public boolean istGedruckt() {
		return _gedruckt;
	}

	private List<String> berechneBereiche(ExtXPath xpath, Node sf) throws XPathExpressionException {
		NodeList bereiche = xpath.evaluateList("bereich", sf);
		List<String> bereich = new ArrayList<String>();
		for (int i=0; i<bereiche.getLength(); i++) {
			bereich.add(bereiche.item(i).getFirstChild().getNodeValue());
		}
		return bereich;
	}
	private Kategorie berechneKategorie(List<String> bereich) {
		if (bereich.contains("Manöver")) {
			return Kategorie.WAFFENLOS;
		}
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

	private void berechneTypName(ExtXPath xpath, Node sf, Kategorie k,
			String auswahl, List<String> bereich) throws XPathExpressionException {
		String nameausfuehrlich;
		int idx;

		nameausfuehrlich = xpath.evaluate("nameausfuehrlich",  sf);
		idx = nameausfuehrlich.indexOf(": ");

		switch (k) {
		case KAMPF:
			this.typ = "Kampfsonderfertigkeit:";
			this.name = nameausfuehrlich;
			break;
		case WAFFENLOS:
			this.typ = "Waffenloses Manöver:";
			this.name = nameausfuehrlich;
			break;
		case MAGISCH:
			if (auswahl != null) {
				this.typ = xpath.evaluate("bezeichner", sf);
				this.name = auswahl;
			} else if (idx != -1) {
				this.typ = nameausfuehrlich.substring(0, idx + 1);
				this.name = nameausfuehrlich.substring(idx + 2);
			} else {
				this.typ = "Generische Magische SF:";
				this.name = nameausfuehrlich;
			}
			break;
		case ZAUBERSPEZ:
			this.typ = "Zauberspezialisierung:";
			this.name = nameausfuehrlich.substring(nameausfuehrlich.indexOf(' ') + 1);
			break;
		case GEWEIHT:
			this.typ = "Generische Karmale SF:";
			this.name = nameausfuehrlich;
			break;
		case LITURGIE:
			this.typ = "Liturgie Grad " + xpath.evaluate("grad", sf) + ":";
			this.name = xpath.evaluate("name",  sf);
			break;
		case TALENTSPEZ:
			this.typ = "Talentspezialisierung:";
			this.name = nameausfuehrlich.substring(nameausfuehrlich.indexOf(' ') + 1);
			break;
		case TALENT:
			if (bereich.contains("Geländekunde")) {
				this.typ = "Geländekunde:";
				this.name = nameausfuehrlich;
				break;
			}
			if (bereich.contains("Kulturkunde")) {
				this.typ = "Kulturkunde:";
				this.name = auswahl;
				break;
			}
			this.typ = "Generische Talent-SF:";
			if (xpath.evaluate("name", sf).contains("Berufsgeheimnis")) {
				int split;
				this.typ = "Berufsgeheimnis";
				
				split = auswahl.lastIndexOf(';');
				this.name = auswahl.substring(split+1).trim() + " (" + auswahl.substring(0, split).trim() + ")";
				break;
			}
			this.name = nameausfuehrlich;
			break;
		case UNBEKANNT:
			this.typ = "Unbekannt:";
			this.name = nameausfuehrlich;
			break;
		}
	}

	public enum Kategorie {
		KAMPF, GEWEIHT, LITURGIE, TALENT, TALENTSPEZ, MAGISCH, ZAUBERSPEZ, WAFFENLOS, UNBEKANNT;
	}
}

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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.hrogge.CompactPDFExport.gui.Konfiguration;

public class PDFGenerator {
	private final float marginX = 5f;
	private final float marginY = 10f;
	private final float textMargin = 0.5f;

	public PDDocument erzeugePDFDokument(Konfiguration k, Document doc) throws IOException, XPathExpressionException, ParserConfigurationException {
		return this.internErzeugePDFDokument(k, new ExtXPath(doc.getDocumentElement()));
	}

	public void exportierePDF(JFrame frame, File output, Document input, Konfiguration k, boolean speichernDialog)
			throws IOException, COSVisitorException, XPathExpressionException, ParserConfigurationException {
		PDDocument doc = null;
		Element root = input.getDocumentElement();

		ExtXPath xpath = new ExtXPath(root);

		try {
			doc = internErzeugePDFDokument(k, xpath);

			if (output == null) {
				String ordner = k.getTextDaten(Konfiguration.GLOBAL_ZIELORDNER);
				if (speichernDialog) {
					String name = xpath.evaluate("angaben/name");
					output = waehlePDFFile(frame, name, ordner);
					if (output == null) {
						return;
					}
				} else {
					String name = xpath.evaluate("angaben/name");
					output = new File(ordner, name + ".pdf");
				}
			}

			if (output.exists()) {
				int result = JOptionPane.showConfirmDialog(frame,
						"Die Datei " + output.getAbsolutePath() + " existiert schon.\nSoll sie überschrieben werden?",
						"Datei überschreiben?", JOptionPane.YES_NO_OPTION);

				if (result != JOptionPane.YES_OPTION) {
					return;
				}
			}

			doc.save(new FileOutputStream(output));
		} finally {
			if (doc != null) {
				doc.close();
			}
		}
	}

	private void extrahiereKommandos(List<String> list, String notiz) {
		if (notiz == null || !notiz.startsWith("@")) {
			return;
		}

		StringTokenizer st = new StringTokenizer(notiz.substring(1), " \n\r");
		while (st.hasMoreTokens()) {
			list.add(st.nextToken());
		}
	}

	private PDDocument internErzeugePDFDokument(Konfiguration k, ExtXPath xpath)
			throws IOException, XPathExpressionException, ParserConfigurationException {
		String[] guteEigenschaften;
		boolean tzm;
		PDDocument doc;
		String pfad;
		PDJpeg charakterBild;
		PDJpeg hintergrundBild;
		Hausregeln hausregeln;
		List<String> commands;
		doc = null;

		hausregeln = new Hausregeln(k);

		charakterBild = null;
		hintergrundBild = null;

		tzm = xpath.evaluate("config/rsmodel").equals("zone");

		/*
		 * Gute Eigenschaften auslesen, da sie seitenübergreifend gebraucht
		 * werden
		 */
		guteEigenschaften = new String[8];
		guteEigenschaften[0] = xpath.evaluate("eigenschaften/mut/akt");
		guteEigenschaften[1] = xpath.evaluate("eigenschaften/klugheit/akt");
		guteEigenschaften[2] = xpath.evaluate("eigenschaften/intuition/akt");
		guteEigenschaften[3] = xpath.evaluate("eigenschaften/charisma/akt");
		guteEigenschaften[4] = xpath.evaluate("eigenschaften/fingerfertigkeit/akt");
		guteEigenschaften[5] = xpath.evaluate("eigenschaften/gewandtheit/akt");
		guteEigenschaften[6] = xpath.evaluate("eigenschaften/konstitution/akt");
		guteEigenschaften[7] = xpath.evaluate("eigenschaften/koerperkraft/akt");

		List<PDFSonderfertigkeiten> sflist = new ArrayList<PDFSonderfertigkeiten>();
		NodeList sfNodes = xpath.evaluateList("sonderfertigkeiten/*");
		for (int i = 0; i < sfNodes.getLength(); i++) {
			Node sf = sfNodes.item(i);

			NodeList auswahlen = xpath.evaluateList("auswahlen/*/name", sf);
			if (auswahlen.getLength() > 0) {
				for (int j = 0; j < auswahlen.getLength(); j++) {
					sflist.add(new PDFSonderfertigkeiten(xpath, sf, auswahlen.item(j).getFirstChild().getNodeValue()));
				}
			} else {
				sflist.add(new PDFSonderfertigkeiten(xpath, sf));
			}
		}

		NodeList ausruestungNodes = xpath.evaluateList("gegenstaende/gegenstand");
		List <Gegenstand> ausruestung = new ArrayList<>();
		for (int idx=0; idx<ausruestungNodes.getLength(); idx++) {
			Gegenstand g = new Gegenstand();
			g.name = xpath.evaluate("name", ausruestungNodes.item(idx));
			g.anzahl = xpath.evaluate("anzahl", ausruestungNodes.item(idx));
			g.gewicht = xpath.evaluate("gewicht", ausruestungNodes.item(idx));
			ausruestung.add(g);
		}

		/* Kommandos aus Notizen extrahieren */

		String nxml = xpath.evaluate("angaben/notizen/text");
		commands = new ArrayList<>();

		StringTokenizer st = new StringTokenizer(nxml, "\r\n");
		while (st.hasMoreTokens()) {
			extrahiereKommandos(commands, st.nextToken());
		}

		try {
			/* PDF erzeugen */
			doc = new PDDocument();

			/*
			 * Bilder müssen bei PDFBox geladen werden bevor die Content-Streams
			 * erzeugt werden
			 */
			pfad = xpath.evaluate("angaben/bildPfad");
			if (pfad != null && pfad.length() > 0) {
				try {
					BufferedImage img = ImageIO.read(new File(pfad));
					charakterBild = new PDJpeg(doc, img);
				} catch (Exception e) {
					System.err.println("Konnte das Bild '" + pfad + "' nicht laden.");
				}
			}

			pfad = k.getTextDaten(Konfiguration.GLOBAL_HINTERGRUND);
			if (pfad != null && pfad.length() > 0) {
				try {
					BufferedImage img = ImageIO.read(new File(pfad));
					hintergrundBild = new PDJpeg(doc, img);
				} catch (Exception e) {
					System.err.println("Konnte das Bild '" + pfad + "' nicht laden.");
				}
			}

			/* globale Settings für Seite festlegen */
			PDFSeite.init(marginX, marginY, textMargin, hintergrundBild,
					k.getOptionsDaten(Konfiguration.GLOBAL_HINTERGRUND_VERZERREN));

			/* Sonderfertigkeiten sortieren */
			Collections.sort(sflist);

			/* Seiten erzeugen */
			FrontSeite page1 = new FrontSeite(doc);
			page1.erzeugeSeite(xpath, charakterBild, hintergrundBild, guteEigenschaften, sflist, hausregeln, commands, tzm,
					k);

			TalentSeite page2 = new TalentSeite(doc);
			page2.erzeugeSeite(xpath, hintergrundBild, guteEigenschaften, sflist, hausregeln, commands, k);

			if (xpath.evaluateBool("angaben/magisch")) {
				ZauberSeite page3 = new ZauberSeite(doc);
				page3.erzeugeSeite(xpath, hintergrundBild, guteEigenschaften, sflist, hausregeln, commands, k);
			}

			// Leerzeilen zu Sonderfertigkeitsliste hinzufügen
			for (int i = 1; i < sflist.size(); i++) {
				if (sflist.get(i - 1).getKategorie() != sflist.get(i).getKategorie()) {
					sflist.add(i, null);
					i++;
				}
			}

			while (hatNichtGedruckteSonderfertigkeit(sflist) || ausruestung.size() > 0) {
				SonstigesSeite page4 = new SonstigesSeite(doc);
				page4.erzeugeSeite(xpath, hintergrundBild, guteEigenschaften, sflist, ausruestung);
			}
		} catch (IOException e) {
			if (doc != null) {
				doc.close();
				doc = null;
			}
			throw e;
		}
		return doc;
	}

	private boolean hatNichtGedruckteSonderfertigkeit(List<PDFSonderfertigkeiten> sflist) {
		for (PDFSonderfertigkeiten sf : sflist) {
			if (sf != null && !sf.istGedruckt()) {
				return true;
			}
		}
		return false;
	}

	private File waehlePDFFile(JFrame frame, String name, String zielverzeichnis) {
		JFileChooser chooser = new JFileChooser();
		chooser.setApproveButtonText("PDF Export");
		chooser.setApproveButtonToolTipText("Aktuellen Helden als PDF exportieren");
		chooser.setCurrentDirectory(new File(zielverzeichnis));
		FileFilter filter = new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().endsWith(".pdf");
			}



			@Override
			public String getDescription() {
				return "PDF Dateien";
			}
		};
		chooser.setFileFilter(filter);

		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setMultiSelectionEnabled(false);
		chooser.setDialogType(JFileChooser.SAVE_DIALOG);
		chooser.setDialogTitle("PDF Export speichern...");
		chooser.setSelectedFile(new File(name + ".pdf"));
		if (chooser.showSaveDialog(frame) != JFileChooser.APPROVE_OPTION) {
			return null;
		}

		return chooser.getSelectedFile();
	}
}

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
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

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

	public void exportierePDF(JFrame frame, File output, Document input, Konfiguration k, boolean speichernDialog)
			throws IOException, COSVisitorException {
		PDDocument doc = null;
		Element root = input.getDocumentElement();

		XPathFactory xpathfactory = XPathFactory.newInstance();
		XPath xpath = xpathfactory.newXPath();
		
		try {
			doc = internErzeugePDFDokument(k, xpath, root);

			if (output == null) {
				String ordner = k.getTextDaten(Konfiguration.GLOBAL_ZIELORDNER);
				if (speichernDialog) {
					output = waehlePDFFile(frame, xpath, root, ordner);
					if (output == null) {
						return;
					}
				} else {
					String name = xpath.compile("angaben/name").evaluate(root);
					output = new File(ordner, name + ".pdf");
				}
			}

			if (output.exists()) {
				int result = JOptionPane.showConfirmDialog(frame, "Die Datei " + output.getAbsolutePath()
						+ " existiert schon.\nSoll sie überschrieben werden?", "Datei überschreiben?",
						JOptionPane.YES_NO_OPTION);

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

	private PDDocument internErzeugePDFDokument(Konfiguration k, XPath xpath, Element root) throws IOException {
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
		
		tzm = xpath.compile("config/rsmodel").evaluate(root).equals("zone");

		/*
		 * Gute Eigenschaften auslesen, da sie seitenübergreifend gebraucht
		 * werden
		 */
		guteEigenschaften = new String[8];
		guteEigenschaften[0] = xpath.compile("eigenschaften/mut/akt").evaluate(root); 
		guteEigenschaften[1] = xpath.compile("eigenschaften/klugheit/akt").evaluate(root);
		guteEigenschaften[2] = xpath.compile("eigenschaften/intuition/akt").evaluate(root);
		guteEigenschaften[3] = xpath.compile("eigenschaften/charisma/akt").evaluate(root);
		guteEigenschaften[4] = xpath.compile("eigenschaften/fingerfertigkeit/akt").evaluate(root);
		guteEigenschaften[5] = xpath.compile("eigenschaften/gewandheit/akt").evaluate(root);
		guteEigenschaften[6] = xpath.compile("eigenschaften/konstitution/akt").evaluate(root);
		guteEigenschaften[7] = xpath.compile("eigenschaften/koerperkraft/akt").evaluate(root);

		List<PDFSonderfertigkeiten> sflist = new ArrayList<PDFSonderfertigkeiten>();
		NodeList sfNodes = (NodeList)xpath.compile("sonderfertigkeiten/*").evaluate(root, XPathConstants.NODESET);
		for (int i=0; i<sfNodes.getLength(); i++) {
			Node sf = sfNodes.item(i);
			
			NodeList auswahlen = (NodeList)xpath.compile("auswahlen/*/name").evaluate(sf, XPathConstants.NODESET);
			if (auswahlen.getLength() > 0) {
				for (int j=0; j<auswahlen.getLength(); j++) {
					sflist.add(new PDFSonderfertigkeiten(sf, auswahlen.item(j).getNodeValue()));
				}
			} else {
				sflist.add(new PDFSonderfertigkeiten(sf));
			}
		}

		ausruestung = new ArrayList<>(daten.getGegenstaende().getGegenstand());

		/* Kommandos aus Notizen extrahieren */
		Notizen n = daten.getAngaben().getNotizen();
		commands = new ArrayList<>();
		
		StringTokenizer st = new StringTokenizer(n.getN0(), "\r\n");
		if (st.countTokens() > 1) {
			if (st.hasMoreTokens()) {
				n.setN0(st.nextToken());
			}
			if (st.hasMoreTokens()) {
				n.setN1(st.nextToken());
			}
			if (st.hasMoreTokens()) {
				n.setN2(st.nextToken());
			}
			if (st.hasMoreTokens()) {
				n.setN3(st.nextToken());
			}
			if (st.hasMoreTokens()) {
				n.setN4(st.nextToken());
			}
			if (st.hasMoreTokens()) {
				n.setN5(st.nextToken());
			}
			if (st.hasMoreTokens()) {
				n.setN6(st.nextToken());
			}
			if (st.hasMoreTokens()) {
				n.setN7(st.nextToken());
			}
			if (st.hasMoreTokens()) {
				n.setN8(st.nextToken());
			}
			if (st.hasMoreTokens()) {
				n.setN9(st.nextToken());
			}
			if (st.hasMoreTokens()) {
				n.setN10(st.nextToken());
			}
			if (st.hasMoreTokens()) {
				n.setN11(st.nextToken());
			}
		}
		extrahiereKommandos(commands, n.getN0());
		extrahiereKommandos(commands, n.getN1());
		extrahiereKommandos(commands, n.getN2());
		extrahiereKommandos(commands, n.getN3());
		extrahiereKommandos(commands, n.getN4());
		extrahiereKommandos(commands, n.getN5());
		extrahiereKommandos(commands, n.getN6());
		extrahiereKommandos(commands, n.getN7());
		extrahiereKommandos(commands, n.getN8());
		extrahiereKommandos(commands, n.getN9());
		extrahiereKommandos(commands, n.getN10());
		extrahiereKommandos(commands, n.getN11());

		try {
			/* PDF erzeugen */
			doc = new PDDocument();

			/*
			 * Bilder müssen bei PDFBox geladen werden bevor die Content-Streams
			 * erzeugt werden
			 */
			pfad = daten.getAngaben().getBildPfad();
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
			page1.erzeugeSeite(daten, charakterBild, hintergrundBild, guteEigenschaften, sflist, hausregeln, commands,
					tzm, k);

			TalentSeite page2 = new TalentSeite(doc);
			page2.erzeugeSeite(daten, hintergrundBild, guteEigenschaften, sflist, hausregeln, commands, k);

			if (daten.getAngaben().isMagisch()) {
				ZauberSeite page3 = new ZauberSeite(doc);
				page3.erzeugeSeite(daten, hintergrundBild, guteEigenschaften, sflist, hausregeln, commands, k);
			}

			/* Leerzeilen zu Sonderfertigkeitsliste hinzufügen */
			for (int i = 1; i < sflist.size(); i++) {
				if (sflist.get(i - 1).getKategorie() != sflist.get(i).getKategorie()) {
					sflist.add(i, null);
					i++;
				}
			}

			while (hatNichtGedruckteSonderfertigkeit(sflist) || ausruestung.size() > 0) {
				SonstigesSeite page4 = new SonstigesSeite(doc);
				page4.erzeugeSeite(hintergrundBild, guteEigenschaften, sflist, ausruestung);
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

	private File waehlePDFFile(JFrame frame, Daten daten, String zielverzeichnis) {
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
		chooser.setSelectedFile(new File(daten.getAngaben().getName() + ".pdf"));
		if (chooser.showSaveDialog(frame) != JFileChooser.APPROVE_OPTION) {
			return null;
		}

		return chooser.getSelectedFile();
	}
}

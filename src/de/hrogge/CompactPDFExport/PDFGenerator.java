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
import javax.xml.bind.*;

import jaxbGenerated.datenxml.*;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import org.w3c.dom.Document;

import de.hrogge.CompactPDFExport.gui.Konfiguration;

public class PDFGenerator {
	private final float marginX = 5f;
	private final float marginY = 10f;
	private final float textMargin = 0.5f;

	public PDDocument erzeugePDFDokument(Document doc, Konfiguration k)
			throws IOException, COSVisitorException, JAXBException {
		/* JAXB Repräsentation des XML-Dokuments erzeugen */
		JAXBContext jaxbContext = JAXBContext
				.newInstance(jaxbGenerated.datenxml.Daten.class);

		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		Daten daten = (Daten) jaxbUnmarshaller.unmarshal(doc
				.getDocumentElement());

		return internErzeugePDFDokument(k, daten);
	}

	public void exportierePDF(JFrame frame, File output, Document input,
			Konfiguration k, boolean speichernDialog) throws IOException,
			COSVisitorException, JAXBException {
		PDDocument doc = null;

		/* JAXB Repräsentation des XML-Dokuments erzeugen */
		JAXBContext jaxbContext = JAXBContext
				.newInstance(jaxbGenerated.datenxml.Daten.class);

		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		Daten daten = (Daten) jaxbUnmarshaller.unmarshal(input
				.getDocumentElement());

		try {
			doc = internErzeugePDFDokument(k, daten);

			if (output == null) {
				String ordner = k.getTextDaten(Konfiguration.GLOBAL_ZIELORDNER);
				if (speichernDialog) {
					output = waehlePDFFile(frame, daten, ordner);
					if (output == null) {
						return;
					}
				} else {
					output = new File(ordner, daten.getAngaben().getName()
							+ ".pdf");
				}
			}

			if (output.exists()) {
				int result = JOptionPane.showConfirmDialog(frame, "Die Datei "
						+ output.getAbsolutePath()
						+ " existiert schon.\nSoll sie überschrieben werden?",
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
		if (!notiz.startsWith("@")) {
			return;
		}

		StringTokenizer st = new StringTokenizer(notiz.substring(1));

		while (st.hasMoreTokens()) {
			list.add(st.nextToken());
		}
	}

	private PDDocument internErzeugePDFDokument(Konfiguration k, Daten daten)
			throws IOException {
		String[] guteEigenschaften;
		List<PDFSonderfertigkeiten> sflist;
		List<Gegenstand> ausruestung;
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
		tzm = daten.getConfig().getRsmodell().equals("zone");

		/*
		 * Gute Eigenschaften auslesen, da sie seitenübergreifend gebraucht
		 * werden
		 */
		Eigenschaften eigenschaften = daten.getEigenschaften();
		guteEigenschaften = new String[8];
		guteEigenschaften[0] = eigenschaften.getMut().getAkt().toString();
		guteEigenschaften[1] = eigenschaften.getKlugheit().getAkt().toString();
		guteEigenschaften[2] = eigenschaften.getIntuition().getAkt().toString();
		guteEigenschaften[3] = eigenschaften.getCharisma().getAkt().toString();
		guteEigenschaften[4] = eigenschaften.getFingerfertigkeit().getAkt()
				.toString();
		guteEigenschaften[5] = eigenschaften.getGewandtheit().getAkt()
				.toString();
		guteEigenschaften[6] = eigenschaften.getKonstitution().getAkt()
				.toString();
		guteEigenschaften[7] = eigenschaften.getKoerperkraft().getAkt()
				.toString();

		sflist = new ArrayList<PDFSonderfertigkeiten>();
		for (Sonderfertigkeit sf : daten.getSonderfertigkeiten()
				.getSonderfertigkeit()) {
			if (sf.getAuswahlen() != null
					&& sf.getAuswahlen().getAuswahl().size() > 0) {
				for (Sonderfertigkeit.Auswahlen.Auswahl a : sf.getAuswahlen()
						.getAuswahl()) {
					sflist.add(new PDFSonderfertigkeiten(sf, a.getName()));
				}
			} else {
				sflist.add(new PDFSonderfertigkeiten(sf));
			}
		}

		ausruestung = new ArrayList<>(daten.getGegenstaende().getGegenstand());
		
		/* Kommandos aus Notizen extrahieren */
		Notizen n = daten.getAngaben().getNotizen();
		commands = new ArrayList<>();
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
					System.err.println("Konnte das Bild '" + pfad
							+ "' nicht laden.");
				}
			}

			pfad = k.getTextDaten(Konfiguration.GLOBAL_HINTERGRUND);
			if (pfad != null && pfad.length() > 0) {
				try {
					BufferedImage img = ImageIO.read(new File(pfad));
					hintergrundBild = new PDJpeg(doc, img);
				} catch (Exception e) {
					System.err.println("Konnte das Bild '" + pfad
							+ "' nicht laden.");
				}
			}

			/* globale Settings für Seite festlegen */
			PDFSeite.init(
					marginX,
					marginY,
					textMargin,
					hintergrundBild,
					k.getOptionsDaten(Konfiguration.GLOBAL_HINTERGRUND_VERZERREN));

			/* Sonderfertigkeiten sortieren */
			Collections.sort(sflist);

			/* Seiten erzeugen */
			FrontSeite page1 = new FrontSeite(doc);
			page1.erzeugeSeite(daten, charakterBild, hintergrundBild,
					guteEigenschaften, sflist, tzm, k);

			TalentSeite page2 = new TalentSeite(doc);
			page2.erzeugeSeite(daten, hintergrundBild, guteEigenschaften,
					sflist, hausregeln, commands, k);

			if (daten.getAngaben().isMagisch()) {
				ZauberSeite page3 = new ZauberSeite(doc);
				page3.erzeugeSeite(daten, hintergrundBild, guteEigenschaften,
						sflist, hausregeln, commands, k);
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
				page4.erzeugeSeite(hintergrundBild, guteEigenschaften,
						sflist, ausruestung);
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

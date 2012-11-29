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

public class PDFGenerator {
	public void erzeugePDF(JFrame frame, File output, Document input,
			float marginX, float marginY, float textMargin, boolean notizen)
			throws IOException, COSVisitorException, JAXBException {
		String[] guteEigenschaften;
		TreeMap<PDFSonderfertigkeiten, Boolean> sflist;
		boolean tzm;
		PDDocument doc = null;

		/* JAXB Repräsentation des XML-Dokuments erzeugen */
		JAXBContext jaxbContext = JAXBContext
				.newInstance(jaxbGenerated.datenxml.Daten.class);

		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		Daten daten = (Daten) jaxbUnmarshaller.unmarshal(input
				.getDocumentElement());

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

		sflist = new TreeMap<PDFSonderfertigkeiten, Boolean>();
		for (Sonderfertigkeit sf : daten.getSonderfertigkeiten()
				.getSonderfertigkeit()) {
			if (sf.getAuswahlen() != null
					&& sf.getAuswahlen().getAuswahl().size() > 0) {
				for (Sonderfertigkeit.Auswahlen.Auswahl a : sf.getAuswahlen()
						.getAuswahl()) {
					sflist.put(new PDFSonderfertigkeiten(sf, a.getName()), false);
				}
			} else {
				sflist.put(new PDFSonderfertigkeiten(sf), false);
			}
		}

		try {
			String pfad;
			PDJpeg bild = null;

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
					bild = new PDJpeg(doc, img);
				} catch (Exception e) {
					System.err.println("Konnte das Bild '" + pfad
							+ "' nicht laden.");
				}
			}

			FrontSeite page1 = new FrontSeite(doc, marginX, marginY, textMargin);
			page1.erzeugeSeite(daten, bild, guteEigenschaften,
					sflist, tzm);

			TalentSeite page2 = new TalentSeite(doc, marginX, marginY,
					textMargin);
			page2.erzeugeSeite(daten, guteEigenschaften, sflist);

			if (daten.getAngaben().isMagisch()) {
				ZauberSeite page3 = new ZauberSeite(doc, marginX, marginY,
						textMargin);
				page3.erzeugeSeite(daten, guteEigenschaften,
							sflist, notizen);
			}

			for (PDFSonderfertigkeiten sf : sflist.keySet()) {
				if (!sflist.get(sf)) {
					System.out.println(sf.getName());
					SFSeite page4 = new SFSeite(doc, marginX, marginY, textMargin);
					page4.erzeugeSeite(guteEigenschaften, sflist);
					break;
				}
			}

			if (output == null) {
				output = waehlePDFFile(frame, daten);
			}

			if (output == null) {
				return;
			}

			doc.save(new FileOutputStream(output));
		} finally {
			if (doc != null) {
				doc.close();
			}
		}
	}

	private File waehlePDFFile(JFrame frame, Daten daten) {
		File output;

		JFileChooser chooser = new JFileChooser();
		chooser.setApproveButtonText("PDF Export");
		chooser.setApproveButtonToolTipText("Aktuellen Helden als PDF exportieren");

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

		output = chooser.getSelectedFile();

		if (output.exists()) {
			int result = JOptionPane.showConfirmDialog(frame, "Die Datei "
					+ output.getAbsolutePath()
					+ " existiert schon.\nSoll sie überschrieben werden?",
					"Datei überschreiben?", JOptionPane.YES_NO_OPTION);

			if (result != JOptionPane.YES_OPTION) {
				return null;
			}
		}
		return output;
	}
}

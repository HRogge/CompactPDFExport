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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class MainStart {
	public static void main(String[] args) throws ParserConfigurationException,
			SAXException, IOException {
		File input, output;
		
		if (args.length != 2) {
			/* User wählt Eingabefile */
			input = waehleEingabeDatei();
			if (input == null) {
				return;
			}
			
			output = new File(input.getAbsolutePath() + ".pdf");
		}
		else {
			/* Steuerung über Kommandozeilenparameter */
			input = new File(args[0]);
			output = new File(args[1]);
		}
		
		/* XML-Dokument des Eingabefiles erzeugen */
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
		
		FileReader reader = new FileReader(input);
		Document doc = documentBuilder.parse(new InputSource(reader));

		try {
			PDFGenerator creator = new PDFGenerator();
			creator.erzeugePDF(null, output, doc, 5f, 10f, 0.5f, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static File waehleEingabeDatei() {
		JFileChooser chooser;
		
		chooser = new JFileChooser();
		chooser.setApproveButtonText("Konvertieren");
		chooser.setApproveButtonToolTipText("Konvertiert das selektierte Helden XML-Dokument in ein PDF");

		FileFilter filter = new FileFilter() {
			@Override
			public String getDescription() {
				return "Helden XML-Dateien";
			}
			@Override
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().endsWith(".xml");
			}
		};
		chooser.setFileFilter(filter);

		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setMultiSelectionEnabled(false);
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
			return null;
		}
		
		return chooser.getSelectedFile();
	}

}

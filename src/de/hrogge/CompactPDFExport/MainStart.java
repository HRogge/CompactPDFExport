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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import de.hrogge.CompactPDFExport.gui.Konfiguration;

public class MainStart {
	public static void main(String[] args) throws ParserConfigurationException,
			SAXException, IOException {
		File input, output;

		if (args.length != 2) {
			System.err
					.println("Bitte Ein-, Ausgabedatei als Parameter angeben.");
			System.exit(1);
		}

		/* Steuerung über Kommandozeilenparameter */
		input = new File(args[0]);
		output = new File(args[1]);

		/* XML-Dokument des Eingabefiles erzeugen */
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

		FileReader reader = new FileReader(input);
		Document doc = documentBuilder.parse(new InputSource(reader));

		Konfiguration k = new Konfiguration();
//		if (JOptionPane.OK_OPTION != JOptionPane.showOptionDialog(null,
//				k.getPanel(), "Einstellungen für kompakten Heldenbogen",
//				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
//				null, 0)) {
//			return;
//		}

		try {
			PDFGenerator creator = new PDFGenerator();
			creator.exportierePDF(null, output, doc, k, false);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public static final void prettyPrint(Node node) {
		Transformer tf;
		try {
			tf = TransformerFactory.newInstance().newTransformer();
			tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			tf.setOutputProperty(OutputKeys.INDENT, "yes");
			Writer out = new StringWriter();
			tf.transform(new DOMSource(node), new StreamResult(out));
			System.out.println(out.toString());
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

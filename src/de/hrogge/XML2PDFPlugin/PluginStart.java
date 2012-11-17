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

import helden.plugin.HeldenXMLDatenPlugin;
import helden.plugin.datenxmlplugin.DatenAustauschImpl;

import java.awt.Dialog.ModalityType;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PluginStart implements HeldenXMLDatenPlugin {
	public PluginStart() {
	}

	@Override
	public void doWork(JFrame arg0) {
		/* Nicht benutzt in diesem Plugin */
	}

	@Override
	public void doWork(JFrame frame, Integer menuIdx, DatenAustauschImpl dai) {
		try {
			doMyWork(frame, menuIdx, dai);
		} catch (Exception e) {
			/*
			 * Kann an dieser Stelle die Exception zum Hauptprogramm
			 * weitergeworfen werden ?
			 */
			e.printStackTrace();
		}
	}

	@Override
	public ImageIcon getIcon() {
		/* Kein Icon */
		return new ImageIcon();
	}

	@Override
	public String getMenuName() {
		return "Kompakter PDF-Export";
	}

	@Override
	public String getToolTipText() {
		return "PDF-Export für kompakten Heldenbogen";
	}

	@Override
	public String getType() {
		return DATEN;
	}

	@Override
	public ArrayList<String> getUntermenus() {
		ArrayList<String> l = new ArrayList<String>();
		
		l.add("Einfache Rüstungen");
		l.add("Trefferzonen Rüstungen");
		return l;
	}

	protected void doMyWork(JFrame frame, int menuIdx, DatenAustauschImpl dai)
			throws Exception {
		DocumentBuilder documentBuilder;
		org.w3c.dom.Document request;
		org.w3c.dom.Document doc;
		Object obj;

		/* Baue die nötigen XML-Objekte auf */
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory
				.newInstance();
		documentBuilder = documentFactory.newDocumentBuilder();

		/* Generiere XML-Request */
		request = documentBuilder.newDocument();

		/* Frage die Daten zum aktuellen Helden im XML Format ab */
		Element requestElement = request.createElement("action");
		request.appendChild(requestElement);
		requestElement.setAttribute("action", "held");
		requestElement.setAttribute("id", "selected");
		requestElement.setAttribute("format", "xml");
		requestElement.setAttribute("version", "2");

		obj = dai.exec(request);
		if (obj == null || !(obj instanceof org.w3c.dom.Document)) {
			throw new Exception("Unbekannter Rückgabewert auf Request: "
					+ obj.getClass().getCanonicalName());
		}

		doc = (org.w3c.dom.Document) obj;

		/*
		 * Aktivieren für Debug-Output:
		 * 
		 * zeigeXML(frame, doc);
		 */

		PDFGenerator creator = new PDFGenerator();
		creator.erzeugePDF(frame, null, doc, 5f, 10f, 0.5f, menuIdx == 1);
	}

	protected void zeigeXML(JFrame frame, Document doc)
			throws TransformerFactoryConfigurationError, TransformerException {

		TransformerFactory transformerFactory;
		Transformer transformer;
		StringWriter writer;

		/* Transformer initialisieren */
		transformerFactory = TransformerFactory.newInstance();
		transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(
				"{http://xml.apache.org/xslt}indent-amount", "4");

		/* Textform des XMLs generieren */
		writer = new StringWriter();
		transformer.transform(new DOMSource(doc), new StreamResult(writer));

		/*
		 * Anzeige in Java Dialog
		 */
		JTextArea textarea = new JTextArea(writer.toString());
		JScrollPane scrollpane = new JScrollPane(textarea);

		JDialog dialog = new JDialog(frame);
		dialog.add(scrollpane);
		dialog.setModalityType(ModalityType.MODELESS);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.pack();
		dialog.setVisible(true);
	}
}

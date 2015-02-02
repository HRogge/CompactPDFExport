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

import helden.plugin.HeldenXMLDatenPlugin3;
import helden.plugin.datenxmlplugin.DatenAustausch3Interface;

import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import de.hrogge.CompactPDFExport.gui.DruckAnsicht;
import de.hrogge.CompactPDFExport.gui.Konfiguration;

public class PluginStart implements HeldenXMLDatenPlugin3, ChangeListener {
	private DatenAustausch3Interface dai;
	private JFrame frame;

	private Konfiguration konfig;
	private JMenuItem exportMenuItem;
	private JMenuItem exportAlsMenuItem;
	private JMenuItem einstellungenMenuItem;

	private DruckAnsicht druckAnsicht;

	private boolean debug;

	private boolean tabHatFokus;

	private boolean datenGeaendert;

	private boolean zwangsUpdate;

	private VorschauUpdaten updater;

	public PluginStart() throws URISyntaxException {
		druckAnsicht = null;
		tabHatFokus = false;
		datenGeaendert = false;
		zwangsUpdate = false;

		konfig = new Konfiguration();
		updater = new VorschauUpdaten();

		/* set to true to see some debugging XML output */
		debug = false;
	}

	@Override
	public void click() {
		/* Nicht benutzt in diesem Plugin */
	}

	@Override
	public void doWork(JFrame arg0) {
		/* Nicht benutzt in diesem Plugin */
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
	public JComponent getPanel() {
		zwangsUpdate = true;
		new Thread(updater).start();

		return druckAnsicht.getPanel();
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
	public ArrayList<JComponent> getUntermenus() {
		ArrayList<JComponent> l = new ArrayList<JComponent>();

		exportMenuItem = new JMenuItem("Exportieren");
		exportMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exportierenAktion(false);
			}
		});
		l.add(exportMenuItem);

		exportAlsMenuItem = new JMenuItem("Exportieren als ...");
		exportAlsMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exportierenAktion(true);
			}
		});
		l.add(exportAlsMenuItem);

		l.add(new JSeparator());

		einstellungenMenuItem = new JMenuItem("Einstellungen");
		einstellungenMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				einstellungenAction();
			}
		});
		l.add(einstellungenMenuItem);

		return l;
	}

	@Override
	public boolean hatMenu() {
		return true;
	}

	@Override
	public boolean hatTab() {
		return true;
	}

	@Override
	public void init(DatenAustausch3Interface dai, JFrame frame) {
		Runnable k, s, su;

		this.dai = dai;
		this.frame = frame;

		dai.addChangeListener(this);

		k = new Runnable() {
			@Override
			public void run() {
				einstellungenAction();
			}
		};

		s = new Runnable() {
			@Override
			public void run() {
				exportierenAktion(false);
			}
		};

		su = new Runnable() {
			@Override
			public void run() {
				exportierenAktion(true);
			}
		};

		druckAnsicht = new DruckAnsicht(k, s, su);

		try {
			ladeKonfiguration();
		} catch (Exception e) {
			e.printStackTrace();
		}

		SwingUtilities.invokeLater(k);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource().equals("neuer Held")) {
			datenGeaendert = true;
		} else if (e.getSource().equals("Kein Focus")) {
			tabHatFokus = false;
		} else if (e.getSource().equals("Focus")) {
			tabHatFokus = true;
		} else if (e.getSource().equals("Änderung")) {
			datenGeaendert = true;
		}
		new Thread(updater).start();
	}

	protected void einstellungenAction() {
		int result = -1;
		
		try {
			result = JOptionPane.showOptionDialog(frame, konfig.getPanel(), "Einstellungen für kompakten Heldenbogen",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, 0);
		} catch (RuntimeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}

		if (result == JOptionPane.OK_OPTION) {
			datenGeaendert = true;
			new Thread(updater).start();

			try {
				speichereKonfiguration();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				ladeKonfiguration();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
		return;
	}

	protected void exportierenAktion(boolean dialog) {
		try {
			org.w3c.dom.Document doc = heldEinlesen();
			if (doc == null) {
				return;
			}

			zeigeXML(frame, null, doc);

			PDFGenerator creator = new PDFGenerator();
			creator.exportierePDF(frame, null, doc, konfig, dialog);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void ladeKonfiguration() throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory;
		DocumentBuilder builder;
		Document request, result, localxml;
		Element requestElement;
		NodeList nodeList;
		Element element;
		Properties p;
		String key, value, xml;

		factory = DocumentBuilderFactory.newInstance();
		builder = factory.newDocumentBuilder();
		request = builder.newDocument();

		requestElement = request.createElement("action");
		request.appendChild(requestElement);
		requestElement.setAttribute("action", "listProperties");
		requestElement.setAttribute("pluginName", "CompactPDFExport");

		/* Parameter-Dokument vom Hauptprogramm laden */
		result = (Document) this.dai.exec(request);
		if (result == null) {
			try {
				zeigeXML(frame, "Got null from dai.exec", request);
			} catch (Exception e) {
			}
			return;
		}

		nodeList = result.getElementsByTagName("prop");

		/* lade properties dieses Plugins */
		p = new Properties();
		for (int i = 0; i < nodeList.getLength(); i++) {
			element = (Element) nodeList.item(i);

			key = element.getAttribute("key");
			value = element.getAttribute("value");

			p.setProperty(key, value);
		}

		xml = p.getProperty("xmlsettings");
		if (xml != null) {
			/* use XML configuration */
			localxml = builder.parse(new InputSource(new StringReader(xml)));

			konfig.readFromXML((Element) localxml.getFirstChild());
		} else {
			/* fall back on old settings */
			konfig.konfigurationAnwenden(p);
		}
	}

	protected void speichereKonfiguration() throws ParserConfigurationException, TransformerException {
		DocumentBuilderFactory factory;
		DocumentBuilder builder;
		Document request, localxml;
		TransformerFactory transformerFactory;
		Transformer transformer;
		StringWriter writer;
		Element actionElement, propElement, rootElement;

		factory = DocumentBuilderFactory.newInstance();
		builder = factory.newDocumentBuilder();
		request = builder.newDocument();

		actionElement = request.createElement("action");
		request.appendChild(actionElement);
		actionElement.setAttribute("action", "saveProperties");
		actionElement.setAttribute("pluginName", "CompactPDFExport");

		/* generate local settings */
		localxml = builder.newDocument();
		rootElement = localxml.createElement("settings");
		localxml.appendChild(rootElement);
		konfig.appendToXml(localxml, rootElement);

		/* Transformer initialisieren */
		transformerFactory = TransformerFactory.newInstance();
		transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "false");

		/* Textform des XMLs generieren */
		writer = new StringWriter();
		transformer.transform(new DOMSource(localxml), new StreamResult(writer));

		/* store in Heldentool Settings */
		propElement = request.createElement("prop");
		propElement.setAttribute("key", "xmlsettings");
		propElement.setAttribute("value", writer.toString());
		actionElement.appendChild(propElement);

		if (this.dai.exec(request) == null) {
			try {
				zeigeXML(frame, "Got null from dai.exec", request);
			} catch (Exception e) {
			}
		}
	}

	private org.w3c.dom.Document heldEinlesen() throws ParserConfigurationException, Exception {
		DocumentBuilder documentBuilder;
		org.w3c.dom.Document request;
		org.w3c.dom.Document doc;
		Object obj;

		/* Baue die nötigen XML-Objekte auf */
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		documentBuilder = documentFactory.newDocumentBuilder();

		/* Generiere XML-Request */
		request = documentBuilder.newDocument();

		/* Frage die Daten zum aktuellen Helden im XML Format ab */
		Element requestElement = request.createElement("action");
		request.appendChild(requestElement);
		requestElement.setAttribute("action", "held");
		requestElement.setAttribute("id", "selected");
		requestElement.setAttribute("format", "xml");
		requestElement.setAttribute("version", "3");

		obj = dai.exec(request);
		if (obj == null) {
			try {
				zeigeXML(frame, "Got null from dai.exec:\n", request);
			} catch (Exception e) {
			}
			return null;
		}
		if (!(obj instanceof org.w3c.dom.Document)) {
			throw new Exception("Unbekannter Rückgabewert auf Request: " + obj.getClass().getCanonicalName());
		}

		doc = (org.w3c.dom.Document) obj;
		return doc;
	}

	private void zeigeXML(JFrame frame, String errorMessage, Document doc) throws TransformerFactoryConfigurationError,
			TransformerException {

		TransformerFactory transformerFactory;
		Transformer transformer;
		StringWriter writer;

		if (!debug) {
			return;
		}

		/* Transformer initialisieren */
		transformerFactory = TransformerFactory.newInstance();
		transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

		/* Textform des XMLs generieren */
		writer = new StringWriter();
		if (errorMessage != null) {
			new RuntimeException(errorMessage).printStackTrace(new PrintWriter(writer));
		}

		transformer.transform(new DOMSource(doc), new StreamResult(writer));

		/*
		 * Anzeige in Java Dialog
		 */
		JTextArea textarea = new JTextArea(writer.toString());
		JScrollPane scrollpane = new JScrollPane(textarea);

		JDialog dialog = new JDialog(frame, "Debug output for CompactPDFExport");
		dialog.add(scrollpane);
		dialog.setModalityType(ModalityType.MODELESS);
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		dialog.pack();
		dialog.setVisible(true);
	}

	private class VorschauUpdaten implements Runnable {
		@Override
		public void run() {
			PDDocument pddoc = null;

			synchronized (this) {
				if (!zwangsUpdate && (!tabHatFokus || !datenGeaendert)) {
					return;
				}

				datenGeaendert = false;
				zwangsUpdate = false;

				try {
					org.w3c.dom.Document doc = heldEinlesen();
					if (doc == null) {
						return;
					}
					PDFGenerator creator = new PDFGenerator();
					pddoc = creator.erzeugePDFDokument(doc, konfig);

					druckAnsicht.updateAnsicht(pddoc);
				} catch (Exception e1) {
					e1.printStackTrace();
				} finally {
					if (pddoc != null) {
						try {
							pddoc.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
}

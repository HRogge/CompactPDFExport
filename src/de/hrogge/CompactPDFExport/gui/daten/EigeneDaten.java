package de.hrogge.CompactPDFExport.gui.daten;

import java.util.Arrays;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.hrogge.CompactPDFExport.gui.ISerializeXML;

public abstract class EigeneDaten implements ISerializeXML {
	protected String[] xmlAttributes;
	protected String xmlName;

	String[] values;

	public EigeneDaten(String[] xmlNames, String parentName) {
		this.xmlAttributes = xmlNames;
		this.xmlName = parentName;

		this.values = new String[xmlNames.length];
		Arrays.fill(this.values, "");
	}

	@Override
	public void appendToXml(Document d, Element parent) {
		Element e = d.createElement(xmlName);

		for (int i = 0; i < xmlAttributes.length; i++) {
			e.setAttribute(xmlAttributes[i], values[i]);
		}

		parent.appendChild(e);
	}

	public String getValue(int i) {
		return values[i];
	}

	@Override
	public void readFromXML(Element e) {
		if (!e.getNodeName().equals(xmlName)) {
			throw new IllegalArgumentException("Ungültiger Nodename für " + xmlName + ": " + e.getNodeName());
		}

		for (int i = 0; i < xmlAttributes.length; i++) {
			values[i] = e.getAttribute(xmlAttributes[i]);
		}
	}

	public void setValue(int i, String value) {
		values[i] = value;
	}
}

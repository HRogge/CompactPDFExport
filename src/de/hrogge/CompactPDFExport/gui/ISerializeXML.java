package de.hrogge.CompactPDFExport.gui;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface ISerializeXML {
	public void appendToXml(Document d, Element parent);

	public void readFromXML(Element parent);
}

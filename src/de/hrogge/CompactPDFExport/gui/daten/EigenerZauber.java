package de.hrogge.CompactPDFExport.gui.daten;

import java.util.Random;

public class EigenerZauber extends EigeneDaten {
	private static final String[] xmlAttributes = new String[] { "name", "probe", "skt", "merkmal" };

	public static String XML_NODENAME = "zauber";
	public static String XML_LISTNAME = "zauberlist";

	public EigenerZauber() {
		super(xmlAttributes, XML_NODENAME);
		values[0] = "<eigener Zauber> " + new Random().nextInt();
		values[1] = "KL/IN/CH";
		values[2] = "C";
		values[3] = "";
	}
}

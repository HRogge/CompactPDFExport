package de.hrogge.CompactPDFExport.gui.daten;

public class TalentInstanz extends EigeneDaten {
	private static final String[] xmlAttributes = new String[] { "referenz", "wert" };

	public static String XML_NODENAME = "talentinstanz";
	public static String XML_LISTNAME = "talentinstanzlist";

	public TalentInstanz() {
		super(xmlAttributes, XML_NODENAME);
	}
}

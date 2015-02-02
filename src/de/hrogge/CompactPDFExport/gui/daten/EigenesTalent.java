package de.hrogge.CompactPDFExport.gui.daten;

import java.util.Random;

import de.hrogge.CompactPDFExport.gui.editor.TalentBereichCellEditor;

public class EigenesTalent extends EigeneDaten {
	private static final String[] xmlAttributes = new String[] { "name", "bereich", "probe", "be", "skt" };

	public static String XML_NODENAME = "talent";
	public static String XML_LISTNAME = "talentlist";

	public EigenesTalent() {
		super(xmlAttributes, XML_NODENAME);
		values[0] = "<eigenes Talent> " + new Random().nextInt();
		values[1] = TalentBereichCellEditor.SPRACHEN;
		values[2] = "KL/IN/CH";
		values[3] = "";
		values[4] = "B";
	}
}

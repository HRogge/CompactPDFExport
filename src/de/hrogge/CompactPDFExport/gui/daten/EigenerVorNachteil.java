package de.hrogge.CompactPDFExport.gui.daten;

import java.util.Random;

import de.hrogge.CompactPDFExport.gui.editor.VorNachteilTypEditor;

public class EigenerVorNachteil extends EigeneDaten {
	private static final String[] xmlAttributes = new String[] { "name", "typ" };

	public static String XML_NODENAME = "vornachteil";
	public static String XML_LISTNAME = "vornachteillist";

	public EigenerVorNachteil() {
		super(xmlAttributes, XML_NODENAME);
		values[0] = "<Vor/Nachteil> " + new Random().nextInt();
		values[1] = VorNachteilTypEditor.SCHLECHTE_EIGENSCHAFT;
	}
}

package de.hrogge.CompactPDFExport.gui.editor;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;

public class VorNachteilTypEditor extends DefaultCellEditor {
	public static final String SCHLECHTE_EIGENSCHAFT = "Schlechte Eigenschaft";

	private static final long serialVersionUID = 548864263495547356L;

	public static final List<String> valid_typ;

	static {
		valid_typ = new ArrayList<String>();
		valid_typ.add("Vorteil");
		valid_typ.add("Nachteil");
		valid_typ.add(SCHLECHTE_EIGENSCHAFT);
	}

	public VorNachteilTypEditor(JTable table) {
		super(new JComboBox<String>(valid_typ.toArray(new String[0])));
	}
}

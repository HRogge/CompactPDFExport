package de.hrogge.CompactPDFExport.gui.editor;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;

public class SKTCellEditor extends DefaultCellEditor {
	private static final long serialVersionUID = 548864263495547356L;

	public static final List<String> valid_skt;

	static {
		valid_skt = new ArrayList<String>();
		valid_skt.add("A+");
		valid_skt.add("A");
		valid_skt.add("B");
		valid_skt.add("C");
		valid_skt.add("D");
		valid_skt.add("E");
		valid_skt.add("F");
		valid_skt.add("G");
		valid_skt.add("H");
	}

	public SKTCellEditor(JTable table) {
		super(new JComboBox<String>(valid_skt.toArray(new String[0])));
	}
}

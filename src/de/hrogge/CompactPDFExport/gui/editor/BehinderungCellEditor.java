package de.hrogge.CompactPDFExport.gui.editor;

import javax.swing.JTable;

public class BehinderungCellEditor extends AbstractTextCellEditor {
	private static final long serialVersionUID = -3177246127563392845L;

	public BehinderungCellEditor(JTable table) {
		super(table);
	}

	@Override
	boolean checkValue(String value) {
		return value.length() == 0 || value.matches("(2*)?BE-[1-9][0-9]*");
	}

}

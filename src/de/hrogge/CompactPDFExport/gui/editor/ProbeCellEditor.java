package de.hrogge.CompactPDFExport.gui.editor;

import javax.swing.JTable;

public class ProbeCellEditor extends AbstractTextCellEditor {
	private static final long serialVersionUID = 319928586397459991L;

	public ProbeCellEditor(JTable table) {
		super(table);
	}

	@Override
	boolean checkValue(String value) {
		String p = value.toUpperCase() + "/";
		return p.matches("((MU|KL|IN|CH|FF|GE|KK|KO)/){3}");
	}
}

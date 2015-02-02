package de.hrogge.CompactPDFExport.gui.handler;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import de.hrogge.CompactPDFExport.gui.editor.BehinderungCellEditor;

public class BehinderungHandler extends AbstractTableColumnHandler {
	public BehinderungHandler() {
		super();
	}

	@Override
	public TableCellEditor getEditor(JTable table) {
		return new BehinderungCellEditor(table);
	}
}

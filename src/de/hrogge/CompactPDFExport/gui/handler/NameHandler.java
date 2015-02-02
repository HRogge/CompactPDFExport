package de.hrogge.CompactPDFExport.gui.handler;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import de.hrogge.CompactPDFExport.gui.editor.NameCellEditor;

public class NameHandler extends AbstractTableColumnHandler {
	public NameHandler() {
		super();
	}

	@Override
	public TableCellEditor getEditor(JTable table) {
		return new NameCellEditor(table);
	}
}

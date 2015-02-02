package de.hrogge.CompactPDFExport.gui.handler;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import de.hrogge.CompactPDFExport.gui.editor.SKTCellEditor;

public class SKTHandler extends AbstractTableColumnHandler {
	@Override
	public TableCellEditor getEditor(JTable table) {
		return new SKTCellEditor(table);
	}
}

package de.hrogge.CompactPDFExport.gui.handler;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import de.hrogge.CompactPDFExport.gui.editor.WertCellEditor;

public class WertHandler extends AbstractTableColumnHandler {
	@Override
	public TableCellEditor getEditor(JTable table) {
		return new WertCellEditor();
	}
}

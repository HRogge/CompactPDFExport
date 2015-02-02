package de.hrogge.CompactPDFExport.gui.handler;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import de.hrogge.CompactPDFExport.gui.editor.VorNachteilTypEditor;

public class VorNachteilTypHandler extends AbstractTableColumnHandler {
	@Override
	public TableCellEditor getEditor(JTable table) {
		return new VorNachteilTypEditor(table);
	}
}

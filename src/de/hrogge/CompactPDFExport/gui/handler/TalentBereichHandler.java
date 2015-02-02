package de.hrogge.CompactPDFExport.gui.handler;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import de.hrogge.CompactPDFExport.gui.editor.TalentBereichCellEditor;

public class TalentBereichHandler extends AbstractTableColumnHandler {
	public TalentBereichHandler() {
		super();
	}

	@Override
	public TableCellEditor getEditor(JTable table) {
		return new TalentBereichCellEditor(table);
	}
}

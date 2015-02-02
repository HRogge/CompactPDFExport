package de.hrogge.CompactPDFExport.gui.handler;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import de.hrogge.CompactPDFExport.gui.editor.ProbeCellEditor;

public class ProbeHandler extends AbstractTableColumnHandler {
	public ProbeHandler() {
		super();
	}

	@Override
	public TableCellEditor getEditor(JTable table) {
		return new ProbeCellEditor(table);
	}
}

package de.hrogge.CompactPDFExport.gui.handler;

import javax.swing.JTable;
import javax.swing.table.*;

public abstract class AbstractTableColumnHandler {
	abstract public TableCellEditor getEditor(JTable table);

	public TableCellRenderer getRenderer(JTable table) {
		return new DefaultTableCellRenderer();
	}

	public void initTableColumn(JTable table, TableColumn tableColumn) {
		tableColumn.setCellEditor(getEditor(table));
		tableColumn.setCellRenderer(getRenderer(table));
	}
}

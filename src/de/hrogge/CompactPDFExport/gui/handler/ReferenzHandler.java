package de.hrogge.CompactPDFExport.gui.handler;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

import de.hrogge.CompactPDFExport.gui.editor.ReferenzCellEditor;

public class ReferenzHandler extends AbstractTableColumnHandler {
	private TableModel referenzModel;
	
	public ReferenzHandler(TableModel referenzModel) {
		this.referenzModel = referenzModel;
	}

	@Override
	public TableCellEditor getEditor(JTable table) {
		return new ReferenzCellEditor(referenzModel);
	}
}

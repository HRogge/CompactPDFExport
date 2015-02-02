package de.hrogge.CompactPDFExport.gui.editor;

import javax.swing.JTable;
import javax.swing.table.TableModel;

public class NameCellEditor extends AbstractTextCellEditor {
	private static final long serialVersionUID = -8113947290061655798L;

	private JTable table;
	
	public NameCellEditor(JTable table) {
		super();
		
		this.table = table;
	}

	@Override
	boolean checkValue(String value) {
		TableModel model = table.getModel();
		
		/* prevent same name from being used twice */
		for (int row = 0; row < model.getRowCount(); row++) {
			if (row != table.getSelectedRow() && model.getValueAt(row, 0).equals(value)) {
				return false;
			}
		}
		return true;
	}
}

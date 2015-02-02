package de.hrogge.CompactPDFExport.gui.editor;

import javax.swing.JTable;
import javax.swing.table.TableModel;

public class NameCellEditor extends AbstractTextCellEditor {
	private static final long serialVersionUID = -8113947290061655798L;

	public NameCellEditor(JTable table) {
		super(table);
	}

	@Override
	boolean checkValue(String value) {
		/* prevent same name from being used twice */
		TableModel model = table.getModel();

		for (int row = 0; row < model.getRowCount(); row++) {
			if (row != table.getSelectedRow() && model.getValueAt(row, column).equals(value)) {
				return false;
			}
		}
		return true;
	}
}

package de.hrogge.CompactPDFExport.gui.editor;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;

public abstract class AbstractTextCellEditor extends DefaultCellEditor implements AbstractTableCellEditor {
	private static final long serialVersionUID = -5116436245073549058L;

	protected int column;
	protected JTable table;

	public AbstractTextCellEditor(JTable table) {
		super(new JTextField());
		this.table = table;
	}

	@Override
	public JTable getTable() {
		return table;
	}

	@Override
	public boolean stopCellEditing() {
		JTextField textfield;

		textfield = (JTextField) super.getComponent();
		if (!checkValue(textfield.getText())) {
			return false;
		}
		return super.stopCellEditing();
	}

	abstract boolean checkValue(String value);
}

package de.hrogge.CompactPDFExport.gui.editor;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;

public abstract class AbstractTextCellEditor extends DefaultCellEditor {
	private static final long serialVersionUID = -5116436245073549058L;

	public AbstractTextCellEditor() {
		super(new JTextField());
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

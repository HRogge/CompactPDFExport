package de.hrogge.CompactPDFExport.gui.renderer;

import java.awt.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import de.hrogge.CompactPDFExport.gui.daten.MerkmalDaten;

public class MerkmalRenderer extends DefaultTableCellRenderer implements ListCellRenderer<String> {
	private static final long serialVersionUID = -6601870611876210467L;

	@Override
	public Component getListCellRendererComponent(JList<? extends String> list, String value, int index,
			boolean isSelected, boolean cellHasFocus) {
		return new JLabel(MerkmalDaten.decode(value));
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		return new JLabel(MerkmalDaten.decode((String) value));
	}
}

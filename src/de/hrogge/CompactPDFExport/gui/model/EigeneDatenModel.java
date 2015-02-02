package de.hrogge.CompactPDFExport.gui.model;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import de.hrogge.CompactPDFExport.gui.daten.EigeneDaten;

public abstract class EigeneDatenModel<E extends EigeneDaten> extends AbstractTableModel {
	private static final long serialVersionUID = 1178564981062599294L;

	String[] columnNames;
	List<E> values;

	public EigeneDatenModel(String[] columnNames, List<E> values) {
		this.columnNames = columnNames;
		this.values = values;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public int getRowCount() {
		return values.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		E daten = values.get(rowIndex);
		return daten.getValue(columnIndex);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}

	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		E daten = values.get(rowIndex);
		daten.setValue(columnIndex, (String) value);
	}
}

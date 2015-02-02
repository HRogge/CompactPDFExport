package de.hrogge.CompactPDFExport.gui.editor;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class ReferenzCellEditor extends DefaultCellEditor implements TableModelListener {
	private static final long serialVersionUID = 548864263495547356L;

	private TableModel referenceModel;
	
	public ReferenzCellEditor(TableModel referenceModel) {
		super(new JComboBox<String>());
		this.referenceModel = referenceModel;
		this.referenceModel.addTableModelListener(this);
		initBox();
	}
	
	@SuppressWarnings("unchecked")
	private void initBox() {
		String values[] = new String[referenceModel.getRowCount()];
		JComboBox<String> box = (JComboBox<String>) getComponent();
		
		for (int i=0; i<referenceModel.getRowCount(); i++) {
			values[i] = (String)referenceModel.getValueAt(i, 0);
		}
		
		box.setModel(new DefaultComboBoxModel<String>(values));
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				initBox();
			}
		});
	}
}

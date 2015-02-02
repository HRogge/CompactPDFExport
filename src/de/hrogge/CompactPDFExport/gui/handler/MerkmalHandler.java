package de.hrogge.CompactPDFExport.gui.handler;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import de.hrogge.CompactPDFExport.gui.editor.MerkmalBox;
import de.hrogge.CompactPDFExport.gui.renderer.MerkmalRenderer;

public class MerkmalHandler extends AbstractTableColumnHandler {
	public MerkmalHandler() {
		super();
	}

	@Override
	public TableCellEditor getEditor(JTable table) {
		return new DefaultCellEditor(new MerkmalBox(table));
	}

	@Override
	public TableCellRenderer getRenderer(JTable table) {
		return new MerkmalRenderer();
	}
}

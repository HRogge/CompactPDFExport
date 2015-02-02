package de.hrogge.CompactPDFExport.gui.editor;

public class WertCellEditor extends AbstractTextCellEditor {
	private static final long serialVersionUID = 6393507270837974933L;

	public WertCellEditor() {
		super();
	}

	@Override
	boolean checkValue(String value) {
		int i;
		
		try {
			i = Integer.parseInt(value);
			if (i>=-10 || i<=30) {
				return true;
			}
		} catch (NumberFormatException e) {
		}
		return false;
	}

}

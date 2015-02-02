package de.hrogge.CompactPDFExport.gui.editor;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;

public class TalentBereichCellEditor extends DefaultCellEditor {
	public static final String SPRACHEN = "Sprachen";

	private static final long serialVersionUID = 142826153127876751L;

	public static final List<String> valid_talentbereich;

	static {
		valid_talentbereich = new ArrayList<String>();
		valid_talentbereich.add("Kampf");
		valid_talentbereich.add("Körperlich");
		valid_talentbereich.add("Gesellschaft");
		valid_talentbereich.add("Natur");
		valid_talentbereich.add("Metatalente");
		valid_talentbereich.add("Wissen");
		valid_talentbereich.add(SPRACHEN);
		valid_talentbereich.add("Handwerk");
		valid_talentbereich.add("Sondertalente");
		valid_talentbereich.add("Liturgiekenntnis");
		valid_talentbereich.add("Ritualkenntnis");
	}

	public TalentBereichCellEditor(JTable table) {
		super(new JComboBox<String>(valid_talentbereich.toArray(new String[0])));
	}
}

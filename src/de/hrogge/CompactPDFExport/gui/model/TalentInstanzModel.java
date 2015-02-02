package de.hrogge.CompactPDFExport.gui.model;

import java.util.List;

import de.hrogge.CompactPDFExport.gui.daten.TalentInstanz;

public class TalentInstanzModel extends EigeneDatenModel<TalentInstanz> {
	private static final long serialVersionUID = -5887823177791312741L;
	private static String[] columnNames = new String[] { "Talent", "Wert" };

	public TalentInstanzModel(List<TalentInstanz> values) {
		super(columnNames, values);
	}
}

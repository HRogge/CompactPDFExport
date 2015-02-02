package de.hrogge.CompactPDFExport.gui.model;

import java.util.List;

import de.hrogge.CompactPDFExport.gui.daten.EigenesTalent;

public class TalentModel extends EigeneDatenModel<EigenesTalent> {
	private static final long serialVersionUID = -2937720835192027987L;

	private static String[] columnNames = new String[] { "Talent", "Bereich", "Probe", "BE", "SKT" };

	public TalentModel(List<EigenesTalent> values) {
		super(columnNames, values);
	}
}

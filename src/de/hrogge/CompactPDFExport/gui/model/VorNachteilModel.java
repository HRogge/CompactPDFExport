package de.hrogge.CompactPDFExport.gui.model;

import java.util.List;

import de.hrogge.CompactPDFExport.gui.daten.EigenerVorNachteil;

public class VorNachteilModel extends EigeneDatenModel<EigenerVorNachteil> {
	private static final long serialVersionUID = -7651768907969599111L;

	private static String[] columnNames = { "Vor-/Nachteil", "Typ", };

	public VorNachteilModel(List<EigenerVorNachteil> values) {
		super(columnNames, values);
	}
}

package de.hrogge.CompactPDFExport.gui.model;

import java.util.List;

import de.hrogge.CompactPDFExport.gui.daten.EigenerZauber;

public class ZauberModel extends EigeneDatenModel<EigenerZauber> {
	private static final long serialVersionUID = 8869124736835973919L;

	private static String[] columnNames = new String[] { "Zauber", "Probe", "SKT", "Merkmal" };

	public ZauberModel(List<EigenerZauber> values) {
		super(columnNames, values);
	}
}

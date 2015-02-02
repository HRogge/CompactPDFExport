package de.hrogge.CompactPDFExport.gui;

import javax.swing.JPanel;

public interface IKonfigurationsSeite extends ISerializeXML {
	public JPanel getPanel();

	public String getTitel();
}

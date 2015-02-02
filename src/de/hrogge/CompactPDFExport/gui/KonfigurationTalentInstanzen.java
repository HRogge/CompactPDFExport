package de.hrogge.CompactPDFExport.gui;

import java.util.List;

import javax.swing.table.TableModel;

import de.hrogge.CompactPDFExport.gui.daten.TalentInstanz;
import de.hrogge.CompactPDFExport.gui.handler.AbstractTableColumnHandler;
import de.hrogge.CompactPDFExport.gui.handler.ReferenzHandler;
import de.hrogge.CompactPDFExport.gui.handler.WertHandler;
import de.hrogge.CompactPDFExport.gui.model.TalentInstanzModel;

public class KonfigurationTalentInstanzen extends KonfigurationElemente<TalentInstanz> {
	public KonfigurationTalentInstanzen(TableModel referenzModel) {
		super(new AbstractTableColumnHandler[] {
					new ReferenzHandler(referenzModel),
					new WertHandler(),
				}, 
				"Talente", "Neues Talent", "Talent entfernen", TalentInstanz.XML_LISTNAME,
				TalentInstanz.XML_NODENAME);
	}

	@Override
	protected TalentInstanz createElement() {
		return new TalentInstanz();
	}

	@Override
	protected TalentInstanzModel createModel(List<TalentInstanz> daten) {
		return new TalentInstanzModel(daten);
	}
}

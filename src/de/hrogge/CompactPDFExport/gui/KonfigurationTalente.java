package de.hrogge.CompactPDFExport.gui;

import java.util.List;

import de.hrogge.CompactPDFExport.gui.daten.EigenesTalent;
import de.hrogge.CompactPDFExport.gui.handler.*;
import de.hrogge.CompactPDFExport.gui.model.TalentModel;

public class KonfigurationTalente extends KonfigurationElemente<EigenesTalent> {
	private static final AbstractTableColumnHandler handler[] = { new NameHandler(), new TalentBereichHandler(),
			new ProbeHandler(), new BehinderungHandler(), new SKTHandler() };

	
	public KonfigurationTalente() {
		super(handler, "Talente", "Neues Talent", "Talent entfernen", EigenesTalent.XML_LISTNAME,
				EigenesTalent.XML_NODENAME);
	}

	@Override
	protected EigenesTalent createElement() {
		return new EigenesTalent();
	}

	@Override
	protected TalentModel createModel(List<EigenesTalent> daten) {
		return new TalentModel(daten);
	}
}

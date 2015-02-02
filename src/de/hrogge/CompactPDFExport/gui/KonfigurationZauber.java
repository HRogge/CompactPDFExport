package de.hrogge.CompactPDFExport.gui;

import java.util.List;

import de.hrogge.CompactPDFExport.gui.daten.EigenerZauber;
import de.hrogge.CompactPDFExport.gui.handler.*;
import de.hrogge.CompactPDFExport.gui.model.ZauberModel;

public class KonfigurationZauber extends KonfigurationElemente<EigenerZauber> {
	private static final AbstractTableColumnHandler handler[] = { new NameHandler(), new ProbeHandler(),
			new SKTHandler(), new MerkmalHandler(), };

	public KonfigurationZauber() {
		super(handler, "Zauber", "Neue Zauber", "Zauber entfernen", EigenerZauber.XML_LISTNAME,
				EigenerZauber.XML_NODENAME);
	}

	@Override
	protected EigenerZauber createElement() {
		return new EigenerZauber();
	}

	@Override
	protected ZauberModel createModel(List<EigenerZauber> daten) {
		return new ZauberModel(daten);
	}
}

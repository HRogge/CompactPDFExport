package de.hrogge.CompactPDFExport.gui;

import java.util.List;

import de.hrogge.CompactPDFExport.gui.daten.EigenerVorNachteil;
import de.hrogge.CompactPDFExport.gui.handler.AbstractTableColumnHandler;
import de.hrogge.CompactPDFExport.gui.handler.NameHandler;
import de.hrogge.CompactPDFExport.gui.handler.VorNachteilTypHandler;
import de.hrogge.CompactPDFExport.gui.model.VorNachteilModel;

public class KonfigurationVorNachteile extends KonfigurationElemente<EigenerVorNachteil> {
	private static final AbstractTableColumnHandler handler[] = { new NameHandler(), new VorNachteilTypHandler() };

	public KonfigurationVorNachteile() {
		super(handler, "Vor-/Nachteile", "Neue Vor-/Nachteil", "Vor-/Nachteil entfernen",
				EigenerVorNachteil.XML_LISTNAME, EigenerVorNachteil.XML_NODENAME);
	}

	@Override
	protected EigenerVorNachteil createElement() {
		return new EigenerVorNachteil();
	}

	@Override
	protected VorNachteilModel createModel(List<EigenerVorNachteil> daten) {
		return new VorNachteilModel(daten);
	}
}

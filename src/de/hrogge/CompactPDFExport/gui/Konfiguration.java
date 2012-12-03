package de.hrogge.CompactPDFExport.gui;

import java.io.File;

public class Konfiguration {
	private File zielOrdner;
	private File hintergrund;
	private boolean fBBRuestungen;
	private boolean fBBFernkampf;
	private boolean fBBParrierwaffen;
	private boolean fAnfangsEigenschaften;
	private boolean fMehrSonderfertigkeiten;
	private boolean tBBLeereSpalten;
	private boolean tMarkiereBasistalente;
	private boolean zBBRepraesentation;
	private Notizen zNotizen;
	private boolean zHauszauberOben;

	public Konfiguration() {
		zielOrdner = new File(".");
		hintergrund = null;
		
		fBBRuestungen = false;
		fBBFernkampf = false;
		fBBParrierwaffen = true;
		fAnfangsEigenschaften = false;
		fMehrSonderfertigkeiten = false;
		
		tBBLeereSpalten = false;
		tMarkiereBasistalente = true;
		
		zBBRepraesentation = true;
		zNotizen = Notizen.KEINE;
		zHauszauberOben = true;
	}
	
	public File getHintergrund() {
		return hintergrund;
	}

	public File getZielOrdner() {
		return zielOrdner;
	}

	public Notizen getzNotizen() {
		return zNotizen;
	}

	public boolean isfAnfangsEigenschaften() {
		return fAnfangsEigenschaften;
	}

	public boolean isfBBFernkampf() {
		return fBBFernkampf;
	}

	public boolean isfBBParrierwaffen() {
		return fBBParrierwaffen;
	}

	public boolean isfBBRuestungen() {
		return fBBRuestungen;
	}

	public boolean isfMehrSonderfertigkeiten() {
		return fMehrSonderfertigkeiten;
	}

	public boolean istBBLeereSpalten() {
		return tBBLeereSpalten;
	}

	public boolean istMarkiereBasistalente() {
		return tMarkiereBasistalente;
	}

	public boolean iszBBRepraesentation() {
		return zBBRepraesentation;
	}

	public boolean iszHauszauberOben() {
		return zHauszauberOben;
	}

	public void setfAnfangsEigenschaften(boolean fAnfangsEigenschaften) {
		this.fAnfangsEigenschaften = fAnfangsEigenschaften;
	}
	public void setfBBFernkampf(boolean fBBFernkampf) {
		this.fBBFernkampf = fBBFernkampf;
	}
	
	public void setfBBParrierwaffen(boolean fBBParrierwaffen) {
		this.fBBParrierwaffen = fBBParrierwaffen;
	}
	public void setfBBRuestungen(boolean fBBRuestungen) {
		this.fBBRuestungen = fBBRuestungen;
	}
	public void setfMehrSonderfertigkeiten(boolean fMehrSonderfertigkeiten) {
		this.fMehrSonderfertigkeiten = fMehrSonderfertigkeiten;
	}
	public void setHintergrund(File hintergrund) {
		this.hintergrund = hintergrund;
	}
	public void settBBLeereSpalten(boolean tBBLeereSpalten) {
		this.tBBLeereSpalten = tBBLeereSpalten;
	}
	
	public void settMarkiereBasistalente(boolean tMarkiereBasistalente) {
		this.tMarkiereBasistalente = tMarkiereBasistalente;
	}
	public void setzBBRepraesentation(boolean zBBRepraesentation) {
		this.zBBRepraesentation = zBBRepraesentation;
	}
	
	public void setzHauszauberOben(boolean zHauszauberOben) {
		this.zHauszauberOben = zHauszauberOben;
	}
	public void setZielOrdner(File zielOrdner) {
		this.zielOrdner = zielOrdner;
	}
	public void setzNotizen(Notizen zNotizen) {
		this.zNotizen = zNotizen;
	}
	
	enum Notizen {
		KEINE,
		WERTE,
		ANMERKUNGEN
	}
}

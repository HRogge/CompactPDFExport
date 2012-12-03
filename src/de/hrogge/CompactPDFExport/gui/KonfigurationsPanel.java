package de.hrogge.CompactPDFExport.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.*;
import java.util.Properties;

import javax.swing.*;

public class KonfigurationsPanel extends JPanel {
	private static final String ZAUBER_HAUSZAUBEROBEN = "zauber.hauszauberoben";
	private static final String ZAUBER_NOTIZEN_ANMERKUNGEN = "zauber.notizen.anmerkungen";
	private static final String ZAUBER_NOTIZEN_WERTE = "zauber.notizen.werte";
	private static final String ZAUBER_NOTIZEN_KEINE = "zauber.notizen.keine";
	private static final String ZAUBER_BEIBEDARF_REPRAESENTATION = "zauber.beibedarf.repraesentation";
	private static final String TALENT_BASISTALENTE = "talent.basistalente";
	private static final String TALENT_BEIBEDARF_LEERESPALTEN = "talent.beibedarf.leerespalten";
	private static final String FRONT_MEHRSF = "front.mehrsf";
	private static final String FRONT_ANFANGSEIGENSCHAFTEN = "front.anfangseigenschaften";
	private static final String FRONT_BEIBEDARF_PARRIERWAFFEN = "front.beibedarf.parrierwaffen";
	private static final String FRONT_BEIBEDARF_FERNKAMPF = "front.beibedarf.fernkampf";
	private static final String FRONT_BEIBEDARF_RUESTUNGEN = "front.beibedarf.ruestungen";
	private static final String HINTERGRUND = "hintergrund";
	private static final String ZIELORDNER = "zielordner";

	/**
	 * Zufällige ID
	 */
	private static final long serialVersionUID = 5251222479651706547L;

	private JPanel speichernPanel;
	private JPanel sNamePanel;
	private JLabel snZielOrdner;
	private JLabel snHintergrund;
	private JPanel sPfadPanel;
	private JTextField spZielOrdner;
	private JTextField spHintergrund;
	private JPanel sDialogPanel;
	private JButton sdZielOrdner;
	private JButton sdHintergrund;

	private JPanel einstellungenPanel;

	private JPanel frontPanel;
	private JPanel fBeiBedarfPanel;
	private JCheckBox fbRuestungen;
	private JCheckBox fbFernkampf;
	private JCheckBox fbParrierw;
	private JCheckBox fAnfangsEigenschaften;
	private JCheckBox fMehrSF;

	private JPanel talentPanel;
	private JPanel tBeiBedarfPanel;
	private JCheckBox tbLeereSpalten;
	private JCheckBox tBasisTalente;

	private JPanel zauberPanel;
	private JPanel zBeiBedarfPanel;
	private JCheckBox zbRepraesentation;
	private JPanel zNotizenPanel;
	private ButtonGroup znGroup;
	private JRadioButton znKeine;
	private JRadioButton znWerte;
	private JRadioButton znAnmerkungen;
	private JCheckBox zHauszauberOben;

	public KonfigurationsPanel() {
		super(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		erzeugeFilePanel();

		einstellungenPanel = new JPanel(new GridLayout(0, 3));
		add(einstellungenPanel, BorderLayout.CENTER);

		erzeugeFrontPanel();
		erzeugeTalentPanel();
		erzeugeZauberPanel();

		/* Standardkonfiguration */
		konfiguriere(new Properties());
	}

	public void ladeKonfiguration(File f) throws IOException {
		FileInputStream fis = new FileInputStream(f);

		Properties p = new Properties();
		p.load(fis);

		konfiguriere(p);
	}

	public void schreibeKonfig(File f) throws IOException {
		Properties p = new Properties();

		p.setProperty(ZIELORDNER, spZielOrdner.getText());
		p.setProperty(HINTERGRUND, spHintergrund.getText());

		p.setProperty(FRONT_BEIBEDARF_RUESTUNGEN,
				Boolean.toString(fbRuestungen.isSelected()));
		p.setProperty(FRONT_BEIBEDARF_FERNKAMPF,
				Boolean.toString(fbFernkampf.isSelected()));
		p.setProperty(FRONT_BEIBEDARF_PARRIERWAFFEN,
				Boolean.toString(fbParrierw.isSelected()));
		p.setProperty(FRONT_ANFANGSEIGENSCHAFTEN,
				Boolean.toString(fAnfangsEigenschaften.isSelected()));
		p.setProperty(FRONT_MEHRSF, Boolean.toString(fMehrSF.isSelected()));

		p.setProperty(TALENT_BEIBEDARF_LEERESPALTEN,
				Boolean.toString(tbLeereSpalten.isSelected()));
		p.setProperty(TALENT_BASISTALENTE,
				Boolean.toString(tBasisTalente.isSelected()));

		p.setProperty(ZAUBER_BEIBEDARF_REPRAESENTATION,
				Boolean.toString(zbRepraesentation.isSelected()));
		p.setProperty(ZAUBER_NOTIZEN_KEINE,
				Boolean.toString(znKeine.isSelected()));
		p.setProperty(ZAUBER_NOTIZEN_WERTE,
				Boolean.toString(znWerte.isSelected()));
		p.setProperty(ZAUBER_NOTIZEN_ANMERKUNGEN,
				Boolean.toString(znAnmerkungen.isSelected()));
		p.setProperty(ZAUBER_HAUSZAUBEROBEN,
				Boolean.toString(zHauszauberOben.isSelected()));

		FileOutputStream fos = new FileOutputStream(f);
		p.store(fos, "CompactPDFExportPlugin v1.0");
	}

	private void erzeugeFilePanel() {
		speichernPanel = new JPanel(new BorderLayout());
		add(speichernPanel, BorderLayout.NORTH);

		sNamePanel = new JPanel(new GridLayout(0, 1));
		speichernPanel.add(sNamePanel, BorderLayout.WEST);

		snZielOrdner = new JLabel("Zielordner:");
		sNamePanel.add(snZielOrdner);
		snHintergrund = new JLabel("Hintergrund:");
		sNamePanel.add(snHintergrund);

		sPfadPanel = new JPanel(new GridLayout(0, 1));
		speichernPanel.add(sPfadPanel, BorderLayout.CENTER);

		spZielOrdner = new JTextField();
		sPfadPanel.add(spZielOrdner);
		spHintergrund = new JTextField();
		sPfadPanel.add(spHintergrund);

		sDialogPanel = new JPanel(new GridLayout(0, 1));
		speichernPanel.add(sDialogPanel, BorderLayout.EAST);

		sdZielOrdner = new JButton("...");
		sDialogPanel.add(sdZielOrdner);
		sdHintergrund = new JButton("...");
		sDialogPanel.add(sdHintergrund);
	}

	private void erzeugeFrontPanel() {
		frontPanel = new JPanel();
		frontPanel.setLayout(new BoxLayout(frontPanel, BoxLayout.PAGE_AXIS));
		frontPanel.setBorder(BorderFactory.createTitledBorder("Vorderseite"));
		einstellungenPanel.add(frontPanel);

		fBeiBedarfPanel = new JPanel();
		fBeiBedarfPanel.setLayout(new BoxLayout(fBeiBedarfPanel,
				BoxLayout.PAGE_AXIS));
		fBeiBedarfPanel.setBorder(BorderFactory
				.createTitledBorder("bei Bedarf"));
		frontPanel.add(fBeiBedarfPanel);

		fbRuestungen = new JCheckBox("Rüstungen");
		fbRuestungen.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		fBeiBedarfPanel.add(fbRuestungen);

		fbFernkampf = new JCheckBox("Fernkampf");
		fbFernkampf.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		fBeiBedarfPanel.add(fbFernkampf);

		fbParrierw = new JCheckBox("Parrierwaffen");
		fbParrierw.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		fBeiBedarfPanel.add(fbParrierw);

		fAnfangsEigenschaften = new JCheckBox("Anfangs-Eigenschaften");
		fAnfangsEigenschaften.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		frontPanel.add(fAnfangsEigenschaften);

		fMehrSF = new JCheckBox("Mehr Sonderfertigkeiten");
		fMehrSF.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		frontPanel.add(fMehrSF);
	}

	private void erzeugeTalentPanel() {
		talentPanel = new JPanel();
		talentPanel.setLayout(new BoxLayout(talentPanel, BoxLayout.PAGE_AXIS));
		talentPanel.setBorder(BorderFactory.createTitledBorder("Talente"));
		einstellungenPanel.add(talentPanel);

		tBeiBedarfPanel = new JPanel();
		tBeiBedarfPanel.setLayout(new BoxLayout(tBeiBedarfPanel,
				BoxLayout.PAGE_AXIS));
		tBeiBedarfPanel.setBorder(BorderFactory
				.createTitledBorder("bei Bedarf"));
		talentPanel.add(tBeiBedarfPanel);

		tbLeereSpalten = new JCheckBox("leere Spalten");
		tbLeereSpalten.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		tBeiBedarfPanel.add(tbLeereSpalten);

		tBasisTalente = new JCheckBox("Markiere Basistalente");
		tBasisTalente.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		talentPanel.add(tBasisTalente);
	}

	private void erzeugeZauberPanel() {
		zauberPanel = new JPanel();
		zauberPanel.setLayout(new BoxLayout(zauberPanel, BoxLayout.PAGE_AXIS));
		zauberPanel.setBorder(BorderFactory.createTitledBorder("Zauber"));
		einstellungenPanel.add(zauberPanel);

		zBeiBedarfPanel = new JPanel();
		zBeiBedarfPanel.setLayout(new BoxLayout(zBeiBedarfPanel,
				BoxLayout.PAGE_AXIS));
		zBeiBedarfPanel.setBorder(BorderFactory
				.createTitledBorder("bei Bedarf"));
		zauberPanel.add(zBeiBedarfPanel);

		zbRepraesentation = new JCheckBox("Repräsentation");
		zbRepraesentation.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		zBeiBedarfPanel.add(zbRepraesentation);

		zNotizenPanel = new JPanel();
		zNotizenPanel.setLayout(new BoxLayout(zNotizenPanel,
				BoxLayout.PAGE_AXIS));
		zNotizenPanel.setBorder(BorderFactory.createTitledBorder("Notizen"));
		zauberPanel.add(zNotizenPanel);

		znGroup = new ButtonGroup();

		znKeine = new JRadioButton("Keine");
		znKeine.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		znGroup.add(znKeine);
		zNotizenPanel.add(znKeine);

		znWerte = new JRadioButton("Werte");
		znWerte.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		znGroup.add(znWerte);
		zNotizenPanel.add(znWerte);

		znAnmerkungen = new JRadioButton("Anmerkungen");
		znAnmerkungen.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		znGroup.add(znAnmerkungen);
		zNotizenPanel.add(znAnmerkungen);

		zHauszauberOben = new JCheckBox("Hauszauber oben");
		zHauszauberOben.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		zauberPanel.add(zHauszauberOben);
	}

	private void konfiguriere(Properties p) {
		spZielOrdner.setText(p.getProperty(ZIELORDNER, "."));
		spHintergrund.setText(p.getProperty(HINTERGRUND, ""));

		fbRuestungen.setSelected(Boolean.parseBoolean(p.getProperty(
				FRONT_BEIBEDARF_RUESTUNGEN, Boolean.toString(false))));
		fbFernkampf.setSelected(Boolean.parseBoolean(p.getProperty(
				FRONT_BEIBEDARF_FERNKAMPF, Boolean.toString(false))));
		fbParrierw.setSelected(Boolean.parseBoolean(p.getProperty(
				FRONT_BEIBEDARF_PARRIERWAFFEN, Boolean.toString(true))));
		fAnfangsEigenschaften.setSelected(Boolean.parseBoolean(p.getProperty(
				FRONT_ANFANGSEIGENSCHAFTEN, Boolean.toString(false))));
		fMehrSF.setSelected(Boolean.parseBoolean(p.getProperty(FRONT_MEHRSF,
				Boolean.toString(false))));

		tbLeereSpalten.setSelected(Boolean.parseBoolean(p.getProperty(
				TALENT_BEIBEDARF_LEERESPALTEN, Boolean.toString(false))));
		tBasisTalente.setSelected(Boolean.parseBoolean(p.getProperty(
				TALENT_BASISTALENTE, Boolean.toString(false))));

		zbRepraesentation.setSelected(Boolean.parseBoolean(p.getProperty(
				ZAUBER_BEIBEDARF_REPRAESENTATION, Boolean.toString(false))));
		znKeine.setSelected(Boolean.parseBoolean(p.getProperty(
				ZAUBER_NOTIZEN_KEINE, Boolean.toString(true))));
		znWerte.setSelected(Boolean.parseBoolean(p.getProperty(
				ZAUBER_NOTIZEN_WERTE, Boolean.toString(false))));
		znAnmerkungen.setSelected(Boolean.parseBoolean(p.getProperty(
				ZAUBER_NOTIZEN_ANMERKUNGEN, Boolean.toString(false))));
		zHauszauberOben.setSelected(Boolean.parseBoolean(p.getProperty(
				ZAUBER_HAUSZAUBEROBEN, Boolean.toString(true))));
	}
}

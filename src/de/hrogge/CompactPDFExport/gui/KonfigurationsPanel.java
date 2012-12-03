package de.hrogge.CompactPDFExport.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.*;

public class KonfigurationsPanel extends JPanel {
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
		fbParrierw.setSelected(true);
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
		tBasisTalente.setSelected(true);
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
		zbRepraesentation.setSelected(true);
		zBeiBedarfPanel.add(zbRepraesentation);

		zNotizenPanel = new JPanel();
		zNotizenPanel.setLayout(new BoxLayout(zNotizenPanel,
				BoxLayout.PAGE_AXIS));
		zNotizenPanel.setBorder(BorderFactory.createTitledBorder("Notizen"));
		zauberPanel.add(zNotizenPanel);

		znGroup = new ButtonGroup();

		znKeine = new JRadioButton("Keine");
		znKeine.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		znKeine.setSelected(true);
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
		zHauszauberOben.setSelected(true);
		zauberPanel.add(zHauszauberOben);
	}
}

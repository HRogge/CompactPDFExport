package de.hrogge.CompactPDFExport.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

public class Konfiguration {
	public static final String ZAUBER_SEITENZAHLEN = "zauber.seitenzahlen";
	public static final String ZAUBER_HAUSZAUBEROBEN = "zauber.hauszauberoben";
	public static final String ZAUBER_NOTIZEN_ANMERKUNGEN = "zauber.notizen.anmerkungen";
	public static final String ZAUBER_NOTIZEN_WERTE = "zauber.notizen.werte";
	public static final String ZAUBER_NOTIZEN_KEINE = "zauber.notizen.keine";
	public static final String ZAUBER_IMMER_REPRAESENTATION = "zauber.immer.repraesentation";
	public static final String TALENT_BASISTALENTE = "talent.basistalente";
	public static final String TALENT_IMMER_LEERESPALTEN = "talent.immer.leerespalten";
	public static final String FRONT_MEHRSF = "front.mehrsf";
	public static final String FRONT_KAUFBAREEIGENSCHAFTEN = "front.kaufbareeigenschaften";
	public static final String FRONT_IMMER_FERNKAMPF = "front.immer.fernkampf";
	public static final String FRONT_IMMER_RUESTUNGEN = "front.immer.ruestungen";
	public static final String FRONT_IMMER_SCHILDE = "front.immer.schilde";
	public static final String SPEICHERN_HINTERGRUND = "speichern.hintergrund";
	public static final String SPEICHERN_ZIELORDNER = "speichern.zielordner";

	private JPanel panel;
	
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
	private JPanel fImmerPanel;
	private JCheckBox fiFernkampf;
	private JCheckBox fiRuestungen;
	private JCheckBox fiSchilde;
	private JCheckBox fKaufbareEigenschaften;
	private JCheckBox fMehrSF;

	private JPanel talentPanel;
	private JPanel tImmerPanel;
	private JCheckBox tiLeereSpalten;
	private JCheckBox tBasisTalente;

	private JPanel zauberPanel;
	private JPanel zImmerPanel;
	private JCheckBox ziRepraesentation;
	private JPanel zNotizenPanel;
	private ButtonGroup znGroup;
	private JRadioButton znKeine;
	private JRadioButton znWerte;
	private JRadioButton znAnmerkungen;
	private JCheckBox zHauszauberOben;
	private JCheckBox zSeitenzahlen;

	private Map<String, JTextField> textMap;
	private Map<String, JToggleButton> optionenMap;
	private Map<String, String> textStandardMap;
	private Map<String, Boolean> optionenStandardMap;
	
	public Konfiguration() {
		textMap = new HashMap<String, JTextField>();
		optionenMap = new HashMap<String, JToggleButton>();

		textStandardMap = new HashMap<String, String>();
		textStandardMap.put(SPEICHERN_ZIELORDNER, ".");
		textStandardMap.put(SPEICHERN_HINTERGRUND, "");
		
		optionenStandardMap = new HashMap<String, Boolean>();
		optionenStandardMap.put(FRONT_IMMER_FERNKAMPF, false);
		optionenStandardMap.put(FRONT_IMMER_RUESTUNGEN, true);
		optionenStandardMap.put(FRONT_IMMER_SCHILDE, false);
		optionenStandardMap.put(FRONT_KAUFBAREEIGENSCHAFTEN, false);
		optionenStandardMap.put(FRONT_MEHRSF, false);

		optionenStandardMap.put(TALENT_IMMER_LEERESPALTEN, true);
		optionenStandardMap.put(TALENT_BASISTALENTE, true);

		optionenStandardMap.put(ZAUBER_IMMER_REPRAESENTATION, false);
		optionenStandardMap.put(ZAUBER_NOTIZEN_KEINE, true);
		optionenStandardMap.put(ZAUBER_NOTIZEN_WERTE, false);
		optionenStandardMap.put(ZAUBER_NOTIZEN_ANMERKUNGEN, false);
		optionenStandardMap.put(ZAUBER_HAUSZAUBEROBEN, true);
		optionenStandardMap.put(ZAUBER_SEITENZAHLEN, true);

		panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		erzeugeFilePanel();

		einstellungenPanel = new JPanel(new GridLayout(0, 3));
		panel.add(einstellungenPanel, BorderLayout.CENTER);

		erzeugeFrontPanel();
		erzeugeTalentPanel();
		erzeugeZauberPanel();

		/* Standardkonfiguration */
		konfiguriere(new Properties());
	}

	public boolean getOptionsDaten(String key) {
		return optionenMap.get(key).isSelected();
	}

	public JPanel getPanel() {
		return panel;
	}

	public String getTextDaten(String key) {
		return textMap.get(key).getText();
	}
	
	public void ladeKonfiguration(File f) throws IOException {
		FileInputStream fis = new FileInputStream(f);

		Properties p = new Properties();
		p.load(fis);

		konfiguriere(p);
	}
	
	public void schreibeKonfig(File f) throws IOException {
		Properties p = new Properties();

		for (String key : textMap.keySet()) {
			JTextField tf = textMap.get(key);
			
			p.setProperty(key, tf.getText());
		}
		
		for (String key : optionenMap.keySet()) {
			JToggleButton tb = optionenMap.get(key);
			
			p.setProperty(key, Boolean.toString(tb.isSelected()));
		}
		
		FileOutputStream fos = new FileOutputStream(f);
		p.store(fos, "CompactPDFExportPlugin v1.0");
	}
	
	private void erzeugeFilePanel() {
		speichernPanel = new JPanel(new BorderLayout());
		panel.add(speichernPanel, BorderLayout.NORTH);

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
		textMap.put(SPEICHERN_ZIELORDNER, spZielOrdner);
		spHintergrund = new JTextField();
		sPfadPanel.add(spHintergrund);
		textMap.put(SPEICHERN_HINTERGRUND, spHintergrund);

		sDialogPanel = new JPanel(new GridLayout(0, 1));
		speichernPanel.add(sDialogPanel, BorderLayout.EAST);

		sdZielOrdner = new JButton("...");
		sdZielOrdner.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				zielOrdnerDialog();
			}
		});
		sDialogPanel.add(sdZielOrdner);
		sdHintergrund = new JButton("...");
		sdHintergrund.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				hintergrundDialog();
			}
		});
		sDialogPanel.add(sdHintergrund);
	}

	protected void zielOrdnerDialog() {
		JFileChooser chooser = new JFileChooser();
		chooser.setApproveButtonText("Auswählen");
		chooser.setApproveButtonToolTipText("Dieses Verzeichnis als Standard für PDF-Export festlegen");
		FileFilter filter = new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.isDirectory();
			}

			@Override
			public String getDescription() {
				return "Verzeichnis für PDF-Export";
			}
		};
		chooser.setFileFilter(filter);

		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setMultiSelectionEnabled(false);
		chooser.setDialogType(JFileChooser.SAVE_DIALOG);
		chooser.setDialogTitle("PDF Export speichern...");
		chooser.setSelectedFile(new File(spZielOrdner.getText()));
		if (chooser.showSaveDialog(panel) != JFileChooser.APPROVE_OPTION) {
			return;
		}

		spZielOrdner.setText(chooser.getSelectedFile().getAbsolutePath());
	}
	
	protected void hintergrundDialog() {
		JFileChooser chooser = new JFileChooser();
		chooser.setApproveButtonText("Auswählen");
		chooser.setApproveButtonToolTipText("Dieses Bild als Hintergrundbild festlegen");
		FileFilter filter = new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().endsWith(".jpg")
						|| f.getName().endsWith(".jpeg");
			}

			@Override
			public String getDescription() {
				return "JPEG Hintergrundbild für PDF-Export";
			}
		};
		chooser.setFileFilter(filter);

		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setMultiSelectionEnabled(false);
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		chooser.setDialogTitle("PDF Export speichern...");
		chooser.setSelectedFile(new File(spHintergrund.getText()));
		if (chooser.showOpenDialog(panel) != JFileChooser.APPROVE_OPTION) {
			return;
		}

		spHintergrund.setText(chooser.getSelectedFile().getAbsolutePath());
	}
	
	private void erzeugeFrontPanel() {
		frontPanel = new JPanel();
		frontPanel.setLayout(new BoxLayout(frontPanel, BoxLayout.PAGE_AXIS));
		frontPanel.setBorder(BorderFactory.createTitledBorder("Vorderseite"));
		einstellungenPanel.add(frontPanel);

		fImmerPanel = new JPanel();
		fImmerPanel.setLayout(new BoxLayout(fImmerPanel,
				BoxLayout.PAGE_AXIS));
		fImmerPanel.setBorder(BorderFactory
				.createTitledBorder("Immer"));
		frontPanel.add(fImmerPanel);

		fiFernkampf = new JCheckBox("Fernkampf");
		fiFernkampf.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		fImmerPanel.add(fiFernkampf);
		optionenMap.put(FRONT_IMMER_FERNKAMPF, fiFernkampf);
		
		fiRuestungen = new JCheckBox("Rüstungen");
		fiRuestungen.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		fImmerPanel.add(fiRuestungen);
		optionenMap.put(FRONT_IMMER_RUESTUNGEN, fiRuestungen);
		
		fiSchilde = new JCheckBox("Schilde");
		fiSchilde.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		fImmerPanel.add(fiSchilde);
		optionenMap.put(FRONT_IMMER_SCHILDE, fiSchilde);
		
		fKaufbareEigenschaften = new JCheckBox("Kaufbare Eigenschaften");
		fKaufbareEigenschaften.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		frontPanel.add(fKaufbareEigenschaften);
		optionenMap.put(FRONT_KAUFBAREEIGENSCHAFTEN, fKaufbareEigenschaften);
		
		fMehrSF = new JCheckBox("Mehr Sonderfertigkeiten");
		fMehrSF.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		frontPanel.add(fMehrSF);
		optionenMap.put(FRONT_MEHRSF, fMehrSF);
	}

	private void erzeugeTalentPanel() {
		talentPanel = new JPanel();
		talentPanel.setLayout(new BoxLayout(talentPanel, BoxLayout.PAGE_AXIS));
		talentPanel.setBorder(BorderFactory.createTitledBorder("Talente"));
		einstellungenPanel.add(talentPanel);

		tImmerPanel = new JPanel();
		tImmerPanel.setLayout(new BoxLayout(tImmerPanel,
				BoxLayout.PAGE_AXIS));
		tImmerPanel.setBorder(BorderFactory
				.createTitledBorder("Immer"));
		talentPanel.add(tImmerPanel);

		tiLeereSpalten = new JCheckBox("leere Spalten");
		tiLeereSpalten.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		tImmerPanel.add(tiLeereSpalten);
		optionenMap.put(TALENT_IMMER_LEERESPALTEN, tiLeereSpalten);
		
		tBasisTalente = new JCheckBox("Markiere Basistalente");
		tBasisTalente.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		talentPanel.add(tBasisTalente);
		optionenMap.put(TALENT_BASISTALENTE, tBasisTalente);
	}

	private void erzeugeZauberPanel() {
		zauberPanel = new JPanel();
		zauberPanel.setLayout(new BoxLayout(zauberPanel, BoxLayout.PAGE_AXIS));
		zauberPanel.setBorder(BorderFactory.createTitledBorder("Zauber"));
		einstellungenPanel.add(zauberPanel);

		zImmerPanel = new JPanel();
		zImmerPanel.setLayout(new BoxLayout(zImmerPanel,
				BoxLayout.PAGE_AXIS));
		zImmerPanel.setBorder(BorderFactory
				.createTitledBorder("Immer"));
		zauberPanel.add(zImmerPanel);

		ziRepraesentation = new JCheckBox("Repräsentation");
		ziRepraesentation.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		zImmerPanel.add(ziRepraesentation);
		optionenMap.put(ZAUBER_IMMER_REPRAESENTATION, ziRepraesentation);
		
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
		optionenMap.put(ZAUBER_NOTIZEN_KEINE, znKeine);
		
		znWerte = new JRadioButton("Werte");
		znWerte.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		znGroup.add(znWerte);
		zNotizenPanel.add(znWerte);
		optionenMap.put(ZAUBER_NOTIZEN_WERTE, znWerte);
		
		znAnmerkungen = new JRadioButton("Anmerkungen");
		znAnmerkungen.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		znGroup.add(znAnmerkungen);
		zNotizenPanel.add(znAnmerkungen);
		optionenMap.put(ZAUBER_NOTIZEN_ANMERKUNGEN, znAnmerkungen);
		
		zHauszauberOben = new JCheckBox("Hauszauber oben");
		zHauszauberOben.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		zauberPanel.add(zHauszauberOben);
		optionenMap.put(ZAUBER_HAUSZAUBEROBEN, zHauszauberOben);
		
		zSeitenzahlen = new JCheckBox("Seitenzahlen");
		zSeitenzahlen.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		zauberPanel.add(zSeitenzahlen);
		optionenMap.put(ZAUBER_SEITENZAHLEN, zSeitenzahlen);
	}

	private void konfiguriere(Properties p) {
		for (String key : textMap.keySet()) {
			JTextField tf = textMap.get(key);
			
			if (p.containsKey(key)) {
				tf.setText(p.getProperty(key));
			}
			else {
				tf.setText(textStandardMap.get(key));
			}
		}

		for (String key : optionenMap.keySet()) {
			JToggleButton tb = optionenMap.get(key);
			
			if (p.containsKey(key)) {
				tb.setSelected(Boolean.parseBoolean(p.getProperty(key)));
			}
			else {
				tb.setSelected(optionenStandardMap.get(key));
			}
		}
	}
}

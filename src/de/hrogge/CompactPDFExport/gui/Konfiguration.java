package de.hrogge.CompactPDFExport.gui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Konfiguration implements ISerializeXML {
	private JPanel panel;
	private JTabbedPane tabbedPane;

	private List<IKonfigurationsSeite> seiten;
	private KonfigurationGlobal seite1;
	private KonfigurationTalente seite2;
	private KonfigurationZauber seite3;
	private KonfigurationVorNachteile seite4;

	public Konfiguration() {
		seite1 = new KonfigurationGlobal();
		seite2 = new KonfigurationTalente();
		seite3 = new KonfigurationZauber();
		seite4 = new KonfigurationVorNachteile();

		seiten = new ArrayList<IKonfigurationsSeite>();
		seiten.add(seite1);
		seiten.add(seite2);
		seiten.add(seite3);
		seiten.add(seite4);
		seiten.add(new KonfigurationTalentInstanzen(seite2.getModel()));
		tabbedPane = new JTabbedPane();

		panel = new JPanel(new BorderLayout());
		panel.add(BorderLayout.CENTER, tabbedPane);

		for (IKonfigurationsSeite s : seiten) {
			tabbedPane.addTab(s.getTitel(), s.getPanel());
		}
	}

	@Override
	public void appendToXml(Document d, Element parent) {
		for (IKonfigurationsSeite seite : seiten) {
			seite.appendToXml(d, parent);
		}
	}

	public boolean getOptionsDaten(String key) {
		return seite1.getOptionsDaten(key);
	}

	public JPanel getPanel() {
		return panel;
	}

	public String getTextDaten(String key) {
		return seite1.getTextDaten(key);
	}

	public void konfigurationAnwenden(Properties p) {
		seite1.konfigurationAnwenden(p);
	}

	@Override
	public void readFromXML(Element parent) {
		for (IKonfigurationsSeite seite : seiten) {
			seite.readFromXML(parent);
		}
	}
}

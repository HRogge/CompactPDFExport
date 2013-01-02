package de.hrogge.CompactPDFExport.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.VolatileImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

import org.apache.pdfbox.pdfviewer.PageDrawer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

public class DruckAnsicht {
	private Runnable konfiguriere, speichern, speichernUnter;

	private JPanel panel;
	private JPanel kontrollPanel;

	private JButton konfigurieren;
	private JPanel kontrollButtonsPanel;
	private JButton exportierenButton;
	private JButton exportierenUnterButton;
	private JButton vorherigeSeite;
	private JButton naechsteSeite;

	private ImageIcon vorherBild, nachherBild, konfigurierenBild;

	private JLabel seite;
	private ArrayList<VolatileImage> seitenCache;
	private int angezeigteSeite = 0, letzteSeite = 0;

	private JLabel infoText;

	public DruckAnsicht(Runnable k, Runnable s, Runnable su) {
		konfiguriere = k;
		speichern = s;
		speichernUnter = su;

		vorherBild = new ImageIcon(
				DruckAnsicht.class.getResource("VCRBack.gif"));
		nachherBild = new ImageIcon(
				DruckAnsicht.class.getResource("VCRForward.gif"));
		konfigurierenBild = new ImageIcon(
				DruckAnsicht.class.getResource("guiuse.gif"));

		panel = new JPanel(new BorderLayout());

		erzeugeKontrollButtonPanel();

		seite = new JLabel();
		
		HandScrollListener scrollListener = new HandScrollListener(seite);
		JScrollPane scroll = new JScrollPane(seite);
		scroll.getViewport().addMouseMotionListener(scrollListener);
		scroll.getViewport().addMouseListener(scrollListener);
		scroll.setWheelScrollingEnabled(true);
		scroll.getVerticalScrollBar().setUnitIncrement(20);
		
		panel.add(scroll, BorderLayout.CENTER);

		seitenCache = new ArrayList<VolatileImage>();
	}

	public JPanel getPanel() {
		return panel;
	}

	@SuppressWarnings("unchecked")
	public void updateAnsicht(PDDocument pddoc) throws IOException {
		java.util.List<PDPage> seiten;
		int resolution;

		resolution = Toolkit.getDefaultToolkit().getScreenResolution();
		seitenCache.clear();

		seiten = (java.util.List<PDPage>) pddoc.getDocumentCatalog()
				.getAllPages();
		letzteSeite = seiten.size() - 1;
		if (angezeigteSeite > letzteSeite) {
			angezeigteSeite = letzteSeite;
		}

		for (int i = 0; i < seiten.size(); i++) {
			if (i != angezeigteSeite) {
				seitenCache.add(null);
			} else {
				seitenCache.add(seiteZeichnen(resolution,
						seiten.get(angezeigteSeite)));
			}
		}

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				zeigeSeite(angezeigteSeite);
			}
		});

		/* Rest des Caches füllen */
		for (int i = 0; i < seiten.size(); i++) {
			if (i == angezeigteSeite) {
				continue;
			}

			seitenCache.set(i, seiteZeichnen(resolution, seiten.get(i)));
		}
	}

	private void erzeugeKontrollButtonPanel() {
		kontrollPanel = new JPanel(new BorderLayout());
		panel.add(kontrollPanel, BorderLayout.NORTH);

		konfigurieren = new JButton(konfigurierenBild);
		konfigurieren.setMargin(new Insets(1, 1, 1, 1));
		konfigurieren.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(konfiguriere).start();
			}
		});
		kontrollPanel.add(konfigurieren, BorderLayout.WEST);

		infoText = new JLabel("");
		kontrollPanel.add(infoText, BorderLayout.CENTER);

		kontrollButtonsPanel = new JPanel();
		kontrollButtonsPanel.setLayout(new BoxLayout(kontrollButtonsPanel,
				BoxLayout.LINE_AXIS));
		kontrollPanel.add(kontrollButtonsPanel, BorderLayout.EAST);

		exportierenButton = new JButton("Exportieren");
		exportierenButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(speichern).start();
			}
		});
		kontrollButtonsPanel.add(exportierenButton);

		exportierenUnterButton = new JButton("Exportieren unter...");
		exportierenUnterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(speichernUnter).start();
			}
		});
		kontrollButtonsPanel.add(exportierenUnterButton);

		kontrollButtonsPanel.add(Box.createHorizontalStrut(10));

		vorherigeSeite = new JButton(vorherBild);
		vorherigeSeite.setMargin(new Insets(1, 1, 1, 1));
		vorherigeSeite.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				zeigeVorherigesBild();
			}
		});

		kontrollButtonsPanel.add(vorherigeSeite);

		naechsteSeite = new JButton(nachherBild);
		naechsteSeite.setMargin(new Insets(1, 1, 1, 1));
		naechsteSeite.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				zeigeNaechstesBild();
			}
		});
		kontrollButtonsPanel.add(naechsteSeite);
	}
	private void zeigeNaechstesBild() {
		if (angezeigteSeite < letzteSeite) {
			if (seitenCache.get(angezeigteSeite + 1) != null) {
				angezeigteSeite++;
				zeigeSeite(angezeigteSeite);
			}
		}
	}
	
	private void zeigeVorherigesBild() {
		if (angezeigteSeite > 0) {
			if (seitenCache.get(angezeigteSeite - 1) != null) {
				angezeigteSeite--;
				zeigeSeite(angezeigteSeite);
			}
		}
	}

	private VolatileImage seiteZeichnen(int resolution, PDPage page)
			throws IOException {
		VolatileImage img;
		/* from PDFBox convertToImage */
		PDRectangle cropBox = page.findCropBox();
		float widthPt = cropBox.getWidth();
		float heightPt = cropBox.getHeight();

		float scaling = (float) resolution / (float) 72;
		int widthPx = Math.round(widthPt * scaling);
		int heightPx = Math.round(heightPt * scaling);

		Dimension pageDimension = new Dimension((int) widthPt, (int) heightPt);

		img = seite.getGraphicsConfiguration().createCompatibleVolatileImage(
				widthPx, heightPx);

		Graphics2D graphics = img.createGraphics();
		graphics.setBackground(Color.WHITE);
		graphics.clearRect(0, 0, img.getWidth(), img.getHeight());
		graphics.scale(scaling, scaling);
		PageDrawer drawer = new PageDrawer();
		drawer.drawPage(graphics, page, pageDimension);
		graphics.dispose();
		return img;
	}

	private void zeigeSeite(int i) {
		seite.setIcon(new ImageIcon(seitenCache.get(angezeigteSeite)));
		infoText.setText("Seite " + (angezeigteSeite+1) + "/" + (letzteSeite+1));
	}
}

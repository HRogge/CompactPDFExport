package de.hrogge.CompactPDFExport.gui.editor;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

import de.hrogge.CompactPDFExport.gui.daten.MerkmalDaten;
import de.hrogge.CompactPDFExport.gui.renderer.MerkmalRenderer;

public class MerkmalBox extends JComboBox<String> implements PopupMenuListener, ItemListener {
	public static ComponentUI createUI(JComponent c) {
		return new MerkmalUI((MerkmalBox) c);
	}

	private static final long serialVersionUID = -4947903688324223943L;

	private JPopupMenu merkmalMenu;

	private JCheckBoxMenuItem merkmalBoxen[];
	private JMenu daemonischMenu;

	private JCheckBoxMenuItem daemonischBoxen[];
	private JMenu elementarMenu;

	private JCheckBoxMenuItem elementarBoxen[];
	private String encodedValue;

	private JTable table;

	private static final String uiClassID = "CustomComboBoxPopupUI";

	static {
		UIManager.put(uiClassID, MerkmalBox.class.getName());
	}

	public MerkmalBox(JTable table) {
		super();

		this.table = table;

		merkmalMenu = new JPopupMenu();

		merkmalBoxen = new JCheckBoxMenuItem[MerkmalDaten.merkmale.size()];
		for (int i = 0; i < MerkmalDaten.merkmale.size(); i++) {
			merkmalBoxen[i] = new JCheckBoxMenuItem(MerkmalDaten.merkmale.get(i));
			merkmalBoxen[i].addItemListener(this);
			merkmalMenu.add(merkmalBoxen[i]);
		}

		daemonischMenu = new JMenu("Dämonisch (speziell)");
		daemonischBoxen = new JCheckBoxMenuItem[MerkmalDaten.daemonisch.size()];
		for (int i = 0; i < MerkmalDaten.daemonisch.size(); i++) {
			daemonischBoxen[i] = new JCheckBoxMenuItem(MerkmalDaten.daemonisch.get(i));
			daemonischBoxen[i].addItemListener(this);
			daemonischMenu.add(daemonischBoxen[i]);
		}

		elementarMenu = new JMenu("Elementar (speziell)");
		elementarBoxen = new JCheckBoxMenuItem[MerkmalDaten.elementar.size()];
		for (int i = 0; i < MerkmalDaten.elementar.size(); i++) {
			elementarBoxen[i] = new JCheckBoxMenuItem(MerkmalDaten.elementar.get(i));
			elementarBoxen[i].addItemListener(this);
			elementarMenu.add(elementarBoxen[i]);
		}

		merkmalMenu.add(daemonischMenu, 3);
		merkmalMenu.add(elementarMenu, 7);

		merkmalMenu.pack();
		merkmalMenu.addPopupMenuListener(this);

		super.setRenderer(new MerkmalRenderer());
	}

	public JPopupMenu getPopup() {
		return merkmalMenu;
	}

	@Override
	public Object getSelectedItem() {
		return encodedValue;
	}

	@Override
	public String getUIClassID() {
		return uiClassID;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		JCheckBoxMenuItem item;
		int index;
		String value;

		item = (JCheckBoxMenuItem) e.getSource();
		index = MerkmalDaten.getIndex(item.getText());
		if (index >= 0) {
			value = "";
			if (index > 0) {
				value = encodedValue.substring(0, index);
			}
			value = value + (item.isSelected() ? "1" : "0");
			if (index < encodedValue.length() - 1) {
				value = value + encodedValue.substring(index + 1);
			}
			encodedValue = value;
		}
	}

	@Override
	public void popupMenuCanceled(PopupMenuEvent e) {
	}

	@Override
	public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
		final JTable t = table;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				t.getCellEditor().stopCellEditing();
			};
		});
	}

	@Override
	public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
	}

	@Override
	public void setSelectedItem(Object anObject) {
		int index;

		encodedValue = (String) anObject;
		if (encodedValue.length() != MerkmalDaten.getEncodedLength()) {
			encodedValue = MerkmalDaten.defaultEncoded;
		}

		for (JCheckBoxMenuItem item : merkmalBoxen) {
			index = MerkmalDaten.getIndex(item.getText());
			item.setSelected(index >= 0 && encodedValue.charAt(index) == '1');
		}
		for (JCheckBoxMenuItem item : daemonischBoxen) {
			index = MerkmalDaten.getIndex(item.getText());
			item.setSelected(index >= 0 && encodedValue.charAt(index) == '1');
		}
		for (JCheckBoxMenuItem item : elementarBoxen) {
			index = MerkmalDaten.getIndex(item.getText());
			item.setSelected(index >= 0 && encodedValue.charAt(index) == '1');
		}

		super.setSelectedItem(MerkmalDaten.decode(encodedValue));
	}

	private static class MerkmalEditorPopup extends BasicComboPopup {
		private final MerkmalBox editorPopup;
		private static final long serialVersionUID = 1854473185401556540L;

		protected MerkmalEditorPopup(MerkmalBox editorPopup) {
			super(editorPopup);
			this.editorPopup = editorPopup;
		}

		@Override
		public void hide() {
			editorPopup.getPopup().setVisible(false);
		}

		@Override
		public void show() {
			editorPopup.getPopup().show(editorPopup, 0, editorPopup.getHeight());
		}
	}

	private static class MerkmalUI extends BasicComboBoxUI {
		private final MerkmalBox editorPopup;

		private MerkmalUI(MerkmalBox editorPopup) {
			this.editorPopup = editorPopup;
		}

		@Override
		protected ComboPopup createPopup() {
			return new MerkmalEditorPopup(editorPopup);
		}
	}
}

package de.hrogge.CompactPDFExport.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.hrogge.CompactPDFExport.gui.daten.EigeneDaten;
import de.hrogge.CompactPDFExport.gui.handler.AbstractTableColumnHandler;
import de.hrogge.CompactPDFExport.gui.model.EigeneDatenModel;

public abstract class KonfigurationElemente<E extends EigeneDaten> implements IKonfigurationsSeite {
	private String titel;
	private String xmlListName;
	private String xmlNodeName;

	private List<E> datenListe;

	private JPanel panel;
	private JScrollPane scrollpane;
	private JTable table;
	private EigeneDatenModel<E> tableModel;

	private JPanel buttonPanel;
	protected JButton removeButton;
	private JButton newButton;

	public KonfigurationElemente(AbstractTableColumnHandler[] handler, String titel, String addText, String removeText,
			String xmlList, String xmlNodeName) {
		TableColumnModel cm;

		this.titel = titel;
		this.xmlListName = xmlList;
		this.xmlNodeName = xmlNodeName;

		this.datenListe = new ArrayList<>();

		tableModel = createModel(this.datenListe);
		table = new JTable(tableModel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				fireSelectionChanged(e);
			}
		});

		cm = table.getColumnModel();
		for (int i = 0; i < handler.length; i++) {
			handler[i].initTableColumn(table, cm.getColumn(i));
		}

		scrollpane = new JScrollPane(table);

		panel = new JPanel(new BorderLayout());
		panel.add(scrollpane, BorderLayout.CENTER);

		buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panel.add(buttonPanel, BorderLayout.SOUTH);

		newButton = new JButton(addText);
		newButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireNewButton();
			}
		});
		buttonPanel.add(newButton);

		removeButton = new JButton(removeText);
		removeButton.setEnabled(false);
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireRemoveButton();
			}
		});
		buttonPanel.add(removeButton);

	}

	@Override
	public void appendToXml(Document d, Element parent) {
		Element listElement = d.createElement(xmlListName);
		parent.appendChild(listElement);

		for (E daten : datenListe) {
			daten.appendToXml(d, listElement);
		}
	}

	public EigeneDatenModel<E> getModel() {
		return tableModel;
	}

	@Override
	public JPanel getPanel() {
		return panel;
	}
	
	@Override
	public String getTitel() {
		return titel;
	}

	@Override
	public void readFromXML(Element parent) {
		datenListe.clear();
		if (table.getCellEditor() != null) {
			table.getCellEditor().cancelCellEditing();
		}

		NodeList list = parent.getElementsByTagName(xmlListName);
		if (list.getLength() == 1 && list.item(0) instanceof Element) {
			Element listElement = (Element) list.item(0);

			NodeList elements = listElement.getElementsByTagName(xmlNodeName);
			for (int i = 0; i < elements.getLength(); i++) {
				if (elements.item(i) instanceof Element) {
					E daten = createElement();
					try {
						daten.readFromXML((Element) elements.item(i));
						datenListe.add(daten);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}
				}
			}

			tableModel.fireTableDataChanged();
		}
	}

	protected abstract E createElement();

	protected abstract EigeneDatenModel<E> createModel(List<E> daten);
	
	protected void fireNewButton() {
		datenListe.add(createElement());
		tableModel.fireTableRowsInserted(datenListe.size() - 1, datenListe.size() - 1);
	}

	protected void fireRemoveButton() {
		int index = table.getSelectedRow();

		if (index != -1) {
			datenListe.remove(index);
			tableModel.fireTableRowsDeleted(index, index);
			removeButton.setEnabled(false);
		}
	}

	protected void fireSelectionChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting() && e.getFirstIndex() >= 0 && e.getFirstIndex() < datenListe.size()) {
			removeButton.setEnabled(true);
		}
	}
}
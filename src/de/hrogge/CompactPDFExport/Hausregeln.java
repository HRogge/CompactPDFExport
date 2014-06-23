package de.hrogge.CompactPDFExport;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import jaxbGenerated.datenxml.Talent;
import jaxbGenerated.datenxml.Zauber;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import de.hrogge.CompactPDFExport.gui.Konfiguration;

public class Hausregeln {
	private Map<String, Properties> hauszauber;
	private Map<String, Properties> haustalente;

	public Hausregeln(Konfiguration k) {
		hauszauber = new HashMap<>();
		haustalente = new HashMap<>();

		String sourceFile = k.getTextDaten(Konfiguration.GLOBAL_HAUSREGELN);
		if (sourceFile.length() > 0) {
			try {
				ladeHausregeln(sourceFile);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void ladeHausregeln(String filename)
			throws ParserConfigurationException, SAXException, IOException {
		File input = new File(filename);

		/* XML-Dokument des Eingabefiles erzeugen */
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

		FileReader reader = new FileReader(input);
		Document doc = documentBuilder.parse(new InputSource(reader));

		Node hausregeln = doc.getFirstChild();

		for (int i = 0; i < hausregeln.getChildNodes().getLength(); i++) {
			Node child = hausregeln.getChildNodes().item(i);
			NamedNodeMap map = child.getAttributes();

			if (map == null) {
				continue;
			}

			Properties p = new Properties();
			String key = null;

			for (int j = 0; j < map.getLength(); j++) {
				Node a = map.item(j);
				if (a.getNodeName().equals("key")) {
					key = map.item(j).getNodeValue();
				} else {
					p.setProperty(a.getNodeName(), a.getNodeValue());
				}
			}

			if (child.getNodeName().equals("zauber")) {
				hauszauber.put(key, p);
			} else if (child.getNodeName().equals("talent")) {
				haustalente.put(key, p);
			}
		}
	}

	public Zauber getHauszauber(String key, String wert) {
		Properties p = hauszauber.get(key);

		if (p == null) {
			return null;
		}

		/*
		 * <zauber key="RHuGg" name="Resincendo Harz und Glut" hauszauber="true"
		 * probe="MU/IN/FF" zd="" rw="" asp="" wd="" skt="" rep="Magier"
		 * merkmal="Elem (Feu), Objk, Scha" anmerkung=""
		 */

		Zauber z = new Zauber();
		
		z.setNamemitvariante(p.getProperty("name", "-"));
		z.setName(p.getProperty("name", "-"));
		z.setHauszauber(Boolean.valueOf(p.getProperty("hauszauber", "false")));
		z.setProbe(p.getProperty("probe", "--/--/--"));
		z.setProbenwerte(p.getProperty("probe", "--/--/--"));
		z.setZauberdauer(p.getProperty("zd", ""));
		z.setReichweite(p.getProperty("rw", ""));
		z.setKosten(p.getProperty("asp", ""));
		z.setWirkungsdauer(p.getProperty("wd", ""));
		z.setLernkomplexität(p.getProperty("skt", ""));
		z.setRepräsentation(p.getProperty("rep", ""));
		z.setMerkmale(p.getProperty("merkmal", ""));
		z.setAnmerkung(p.getProperty("anmerkung", ""));

		z.setMr("");
		z.setWert(new BigInteger(wert));

		return z;
	}

	public Talent getHaustalent(String key, String wert) {
		Properties p = haustalente.get(key);

		if (p == null) {
			return null;
		}

		/*
		 * <talent key="Pulver" name="Pulverwaffen" bereich="Kampf" probe=""
		 * be="" skt="D"/>
		 */
		Talent t = new Talent();
		t.setName(p.getProperty("name", "-"));
		t.setBereich(p.getProperty("bereich", "Sondertalente/Gaben"));
		t.setProbe(p.getProperty("probe", "--/--/--"));
		t.setProbenwerte(p.getProperty("probe", "--/--/--"));
		t.setBehinderung(p.getProperty("be", ""));
		t.setLernkomplexität(p.getProperty("skt", ""));

		t.setAt("");
		t.setPa("");
		t.setWert(new BigInteger(wert));
		return t;
	}
}

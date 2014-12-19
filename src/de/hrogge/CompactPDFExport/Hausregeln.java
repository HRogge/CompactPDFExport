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
	private Map<String, Properties> eigeneZauber;
	private Map<String, Properties> eigeneTalente;
	private Map<String, Properties> eigeneVorteile;
	private Map<String, Properties> eigeneNachteile;

	static private Map<String, String> repraesentation;
	
	static {
		// A(ch), E(lf), M(ag), D(ru), H(ex), G(eo), S(rl), K(Schelm = Kobold), B(or)
		repraesentation = new HashMap<String, String>();
		repraesentation.put("a", "Achaz");
		repraesentation.put("e", "Elf");
		repraesentation.put("m", "Magier");
		repraesentation.put("d", "Druide");
		repraesentation.put("g", "Geode");
		repraesentation.put("s", "Scharlatan");
		repraesentation.put("k", "Schelm");
		repraesentation.put("b", "Borbaradianer");
		repraesentation.put("f", "Fee");
		repraesentation.put("he", "Hochelf");
	}
	
	public Hausregeln(Konfiguration k) {
		eigeneZauber = new HashMap<String, Properties>();
		eigeneTalente = new HashMap<String, Properties>();
		eigeneVorteile = new HashMap<String, Properties>();
		eigeneNachteile = new HashMap<String, Properties>();

		String sourceFile = k.getTextDaten(Konfiguration.GLOBAL_HAUSREGELN);
		if (sourceFile != null && sourceFile.length() > 0) {
			try {
				ladeHausregeln(sourceFile);
			} catch (Exception e) {
			}
		}
	}

	private void ladeHausregeln(String filename)
			throws ParserConfigurationException, SAXException, IOException {
		File input = new File(filename);

		if (!input.exists()) {
			return;
		}
		
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
				eigeneZauber.put(key, p);
			} else if (child.getNodeName().equals("talent")) {
				eigeneTalente.put(key, p);
			} else if (child.getNodeName().equals("vorteil")) {
				eigeneVorteile.put(key, p);
			} else if (child.getNodeName().equals("nachteil")) {
				eigeneNachteile.put(key, p);
			}
		}
	}

	public Zauber getEigenenZauber(String key, String wert, String rep) {
		boolean hauszauber;
		
		hauszauber = key.endsWith("*");
		if (hauszauber) {
			key = key.substring(0, key.length()-1);
		}
		
		Properties p = eigeneZauber.get(key);

		if (p == null) {
			return null;
		}

		Zauber z = new Zauber();
		z.setNamemitvariante(p.getProperty("name", "-"));
		z.setName(p.getProperty("name", "-"));
		z.setHauszauber(hauszauber);
		z.setProbe(p.getProperty("probe", "--/--/--"));
		z.setProbenwerte(p.getProperty("probe", "--/--/--"));
		z.setZauberdauer(p.getProperty("zd", ""));
		z.setReichweite(p.getProperty("rw", ""));
		z.setKosten(p.getProperty("asp", ""));
		z.setWirkungsdauer(p.getProperty("wd", ""));
		z.setLernkomplexität(p.getProperty("skt", ""));
		if (repraesentation.containsKey(rep)) {
			z.setRepräsentation(repraesentation.get(rep));
		}
		else {
			z.setRepräsentation("");
		}
		z.setMerkmale(p.getProperty("merkmal", ""));
		z.setAnmerkung(p.getProperty("anmerkung", ""));

		z.setMr("");
		z.setWert(new BigInteger(wert));

		return z;
	}

	public Talent getEigenesTalent(String key, String wert) {
		Properties p = eigeneTalente.get(key);
		
		if (p == null) {
			return null;
		}

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

	public PDFVorteil getEigenenVorteil(String key, String wert) {
		return getVorteil(eigeneVorteile, key, wert);
	}
	
	public PDFVorteil getEigenenNachteil(String key, String wert) {
		return getVorteil(eigeneNachteile, key, wert);
	}
	
	private PDFVorteil getVorteil(Map<String, Properties> map,
			String key, String wert) {
		Properties p = map.get(key);
		String name;
		
		if (p == null) {
			return null;
		}
		
		name = p.getProperty("name", "-");
		
		if (p.getProperty("wert", "n").equalsIgnoreCase("n")) {
			wert = "";
		}
		
		return new PDFVorteil(name, wert);
	}	
}

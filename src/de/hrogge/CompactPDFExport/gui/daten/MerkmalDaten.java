package de.hrogge.CompactPDFExport.gui.daten;

import java.util.*;

public class MerkmalDaten {
	public static String decode(String encoded) {
		StringBuilder decoded = new StringBuilder();
		int j = 0;

		if (encoded.length() != getEncodedLength()) {
			return "";
		}

		for (int i = 0; i < merkmale.size(); i++, j++) {
			if (encoded.charAt(j) == '1') {
				if (decoded.length() > 0) {
					decoded.append(", ");
				}
				decoded.append(kurzerName.get(merkmale.get(i)));
			}
		}
		for (int i = 0; i < daemonisch.size(); i++, j++) {
			if (encoded.charAt(j) == '1') {
				if (decoded.length() > 0) {
					decoded.append(", ");
				}
				decoded.append(kurzerName.get(daemonisch.get(i)));
			}
		}
		for (int i = 0; i < elementar.size(); i++, j++) {
			if (encoded.charAt(j) == '1') {
				if (decoded.length() > 0) {
					decoded.append(", ");
				}
				decoded.append(kurzerName.get(elementar.get(i)));
			}
		}
		return decoded.toString();
	}

	public static int getEncodedLength() {
		return merkmale.size() + daemonisch.size() + elementar.size();
	}

	public static int getIndex(String name) {
		int index;

		index = merkmale.indexOf(name);
		if (index != -1) {
			return index;
		}

		index = daemonisch.indexOf(name);
		if (index != -1) {
			return index + merkmale.size();
		}

		index = elementar.indexOf(name);
		if (index != -1) {
			return index + merkmale.size() + daemonisch.size();
		}

		return -1;
	}

	private static String addKurzerName(Map<String, String> map, String key, String value) {
		map.put(key, value);
		return key;
	}

	private static String addKurzerNameD(Map<String, String> map, String key, String value) {
		map.put(key, "Dämo (" + value + ")");
		return key;
	}

	private static String addKurzerNameE(Map<String, String> map, String key, String value) {
		map.put(key, "Elem (" + value + ")");
		return key;
	}

	public static final List<String> merkmale;

	public static final List<String> daemonisch;

	public static final List<String> elementar;

	public static final Map<String, String> kurzerName;

	public static final String defaultEncoded;

	static {
		ArrayList<String> l;
		Map<String, String> m;
		StringBuilder s;

		m = new TreeMap<String, String>();

		l = new ArrayList<String>();
		l.add(addKurzerName(m, "Antimagie", "Anti"));
		l.add(addKurzerName(m, "Beschwörung", "Besw"));
		l.add(addKurzerName(m, "Dämonisch", "Dämo"));
		l.add(addKurzerName(m, "Eigenschaften", "Eign"));
		l.add(addKurzerName(m, "Einfluss", "Einfl"));
		l.add(addKurzerName(m, "Elementar", "Elem"));
		l.add(addKurzerName(m, "Form", "Form"));
		l.add(addKurzerName(m, "Geisterwesen", "Geis"));
		l.add(addKurzerName(m, "Heilung", "Heil"));
		l.add(addKurzerName(m, "Hellsicht", "Hell"));
		l.add(addKurzerName(m, "Herbeirufung", "Rufe"));
		l.add(addKurzerName(m, "Herrschaft", "Herr"));
		l.add(addKurzerName(m, "Illusion", "Illu"));
		l.add(addKurzerName(m, "Kraft", "Krft"));
		l.add(addKurzerName(m, "Limbus", "Limb"));
		l.add(addKurzerName(m, "Metamagie", "Meta"));
		l.add(addKurzerName(m, "Objekt", "Objk"));
		l.add(addKurzerName(m, "Schaden", "Scha"));
		l.add(addKurzerName(m, "Telekinese", "Tele"));
		l.add(addKurzerName(m, "Temporal", "Temp"));
		l.add(addKurzerName(m, "Umwelt", "Umwt"));
		l.add(addKurzerName(m, "Verständigung", "Vers"));
		merkmale = Collections.unmodifiableList(l);

		l = new ArrayList<String>();
		l.add(addKurzerNameD(m, "Blakharaz", "Blk"));
		l.add(addKurzerNameD(m, "Belhalhar", "Blh"));
		l.add(addKurzerNameD(m, "Charyptoroth", "Cha"));
		l.add(addKurzerNameD(m, "Lolgramoth", "Lol"));
		l.add(addKurzerNameD(m, "Thargunitoth", "Tar"));
		l.add(addKurzerNameD(m, "Amazeroth", "Amz"));
		l.add(addKurzerNameD(m, "Belshirash", "Bls"));
		l.add(addKurzerNameD(m, "Asfaloth", "Asf"));
		l.add(addKurzerNameD(m, "Tasfarelel", "Tas"));
		l.add(addKurzerNameD(m, "Belzhorash", "Blz"));
		l.add(addKurzerNameD(m, "Agrimoth", "Agm"));
		l.add(addKurzerNameD(m, "Belkelel", "Bel"));
		daemonisch = Collections.unmodifiableList(l);

		l = new ArrayList<String>();
		l.add(addKurzerNameE(m, "Feuer", "Feu"));
		l.add(addKurzerNameE(m, "Wasser", "Was"));
		l.add(addKurzerNameE(m, "Erz", "Erz"));
		l.add(addKurzerNameE(m, "Luft", "Luf"));
		l.add(addKurzerNameE(m, "Humus", "Hum"));
		l.add(addKurzerNameE(m, "Eis", "Eis"));
		elementar = Collections.unmodifiableList(l);

		kurzerName = Collections.unmodifiableMap(m);

		s = new StringBuilder();
		for (int i = 0; i < merkmale.size() + daemonisch.size() + elementar.size(); i++) {
			s.append('0');
		}
		defaultEncoded = s.toString();
	}
}

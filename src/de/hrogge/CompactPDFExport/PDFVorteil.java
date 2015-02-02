package de.hrogge.CompactPDFExport;

public class PDFVorteil implements Comparable<PDFVorteil> {
	private String name;
	private String wert;

	public PDFVorteil(String name, String wert) {
		this.name = name;
		this.wert = wert;
	}

	@Override
	public int compareTo(PDFVorteil v2) {
		int result;

		result = this.getName().compareTo(v2.getName());
		if (result != 0) {
			return result;
		}

		return this.getWert().compareTo(v2.getWert());
	}

	public String getName() {
		return name;
	}

	public String getWert() {
		return wert;
	}
}

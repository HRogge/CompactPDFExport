package de.hrogge.CompactPDFExport;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;

public class SFSeite extends PDFSeite {
	public SFSeite(PDDocument d, float marginX, float marginY, float textMargin)
			throws IOException {
		super(d, marginX, marginY, textMargin);
	}

	public boolean erzeugeSeite(String[] guteEigenschaften,
			List<PDFSonderfertigkeiten> alleSF) throws IOException {

		Collections.sort(alleSF);

		for (int i = 1; i < alleSF.size(); i++) {
			if (alleSF.get(i - 1).getKategorie() != alleSF.get(i)
					.getKategorie()) {
				alleSF.add(i, null);
				i++;
			}
		}

		if (alleSF.size()/3 > 55) {
			initPDFStream(72);
		}
		else {
			initPDFStream(60);
		}
		
		titelzeile(guteEigenschaften);

		filter(alleSF);
		PDFSonderfertigkeiten.zeichneTabelle(this, 0, 2, 20, cellCountY,
				"Sonderfertigkeiten 1", alleSF);

		filter(alleSF);
		PDFSonderfertigkeiten.zeichneTabelle(this, 21, 2, 42, cellCountY,
				"Sonderfertigkeiten 2", alleSF);

		filter(alleSF);
		PDFSonderfertigkeiten.zeichneTabelle(this, 43, 2, 63, cellCountY,
				"Sonderfertigkeiten 3", alleSF);

		stream.close();

		return alleSF.size() > 0;
	}

	private void filter(List<PDFSonderfertigkeiten> alleSF) {
		while (alleSF.size() > 0 && alleSF.get(0) == null) {
			alleSF.remove(0);
		}
	}
}

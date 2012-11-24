package de.hrogge.CompactPDFExport;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

public class SFSeite extends PDFSeite {
	public SFSeite(PDDocument d, float marginX, float marginY, float textMargin)
			throws IOException {
		super(d, marginX, marginY, textMargin, 72);
	}

	public boolean erzeugeSeite(String[] guteEigenschaften,
			List<PDFSonderfertigkeiten> alleSF) throws IOException {

		stream = new PDPageContentStream(doc, page);

		titelzeile(guteEigenschaften);

		Collections.sort(alleSF);

		for (int i = 1; i < alleSF.size(); i++) {
			if (alleSF.get(i - 1).getKategorie() != alleSF.get(i)
					.getKategorie()) {
				alleSF.add(i, null);
				i++;
			}
		}

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

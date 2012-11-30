package de.hrogge.CompactPDFExport;

import java.io.IOException;
import java.util.*;

import org.apache.pdfbox.pdmodel.PDDocument;

public class SFSeite extends PDFSeite {
	public SFSeite(PDDocument d, float marginX, float marginY, float textMargin)
			throws IOException {
		super(d, marginX, marginY, textMargin);
	}

	public void erzeugeSeite(String[] guteEigenschaften,
			List<PDFSonderfertigkeiten> alleSF) throws IOException {
		List<PDFSonderfertigkeiten> sfList;

		sfList = new ArrayList<PDFSonderfertigkeiten>(alleSF);
		Collections.sort(sfList);

		for (int i = 1; i < sfList.size(); i++) {
			if (sfList.get(i - 1).getKategorie() != sfList.get(i)
					.getKategorie()) {
				sfList.add(i, null);
				i++;
			}
		}

		if (sfList.size() / 3 > 55) {
			initPDFStream(72);
		} else {
			initPDFStream(60);
		}

		titelzeile(guteEigenschaften);

		filter(sfList);
		PDFSonderfertigkeiten.zeichneTabelle(this, 0, 2, 20, cellCountY,
				"Sonderfertigkeiten 1", sfList);

		filter(sfList);
		PDFSonderfertigkeiten.zeichneTabelle(this, 21, 2, 42, cellCountY,
				"Sonderfertigkeiten 2", sfList);

		filter(sfList);
		PDFSonderfertigkeiten.zeichneTabelle(this, 43, 2, 63, cellCountY,
				"Sonderfertigkeiten 3", sfList);

		stream.close();
	}

	private void filter(List<PDFSonderfertigkeiten> alleSF) {
		while (alleSF.size() > 0 && alleSF.get(0) == null) {
			alleSF.remove(0);
		}
	}
}

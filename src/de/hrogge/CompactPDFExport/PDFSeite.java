/*
 *    Copyright 2012 Henning Rogge
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.hrogge.CompactPDFExport;

import java.awt.Color;
import java.awt.geom.PathIterator;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDFontDescriptor;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;

public class PDFSeite {
	private static final int DEFAULT_USER_SPACE_UNIT_DPI = 72;
	private static final float MM_TO_UNITS = 1 / (10 * 2.54f)
			* DEFAULT_USER_SPACE_UNIT_DPI;

	protected final int cellCountX = 63;
	protected final int halbeBreite, viertelBreite;

	protected final float pageWidth, pageHeight;

	protected final float leftEdge, topEdge;
	protected final float rightEdge, bottomEdge;

	protected final float textMargin;

	protected int cellCountY;
	protected float cellWidth, cellHeight;

	protected PDPage page;
	protected final PDDocument doc;
	protected PDPageContentStream stream;

	public PDFSeite(PDDocument d, float lrPageMargin, float tbPageMargin,
			float tMargin) {
		this.doc = d;
		this.stream = null;

		neueSeite();

		this.halbeBreite = 31;
		this.viertelBreite = 15;

		this.leftEdge = lrPageMargin * MM_TO_UNITS;
		this.bottomEdge = tbPageMargin * MM_TO_UNITS;

		this.textMargin = tMargin;
		this.pageWidth = page.findMediaBox().getWidth() - this.leftEdge * 2;
		this.pageHeight = page.findMediaBox().getHeight() - this.bottomEdge * 2;

		this.rightEdge = this.leftEdge + this.pageWidth;
		this.topEdge = this.bottomEdge + this.pageHeight;
	}

	public void initPDFStream(int cy, PDJpeg hintergrund) throws IOException {
		this.cellCountY = cy;

		this.cellWidth = this.pageWidth / this.cellCountX;
		this.cellHeight = this.pageHeight / this.cellCountY;
		
		this.stream = new PDPageContentStream(doc, page);
		
		if (hintergrund != null) {
			drawImage(0, 0, cellCountX, cellCountY, hintergrund);
		}
	}
	
	public void addLine(int x1, int y1, int x2, int y2) throws IOException {
		stream.addLine(getX(x1), getY(y1), getX(x2), getY(y2));
	}

	public void addRect(int x1, int y1, int x2, int y2) throws IOException {
		stream.addRect(getX(x1), getY(y1), getX(x2) - getX(x1), getY(y2)
				- getY(y1));
	}

	public void drawImage(int x1, int y1, int x2, int y2, PDJpeg bild)
			throws IOException {
		float x, y, w, h, tmp;

		x = getX(x1);
		y = getY(y2);

		w = getX(x2) - getX(x1);
		h = getY(y1) - getY(y2);

		if (w / bild.getWidth() > h / bild.getHeight()) {
			tmp = h * bild.getWidth() / bild.getHeight();
			x = x + (w - tmp) / 2;
			w = tmp;
		} else {
			tmp = w * bild.getHeight() / bild.getWidth();
			y = y + (h - tmp) / 2;
			h = tmp;
		}

		stream.drawXObject(bild, x, y, w, h);
	}

	public void drawLabeledBox(int x1, int y1, int x2, int y2, String label)
			throws IOException {
		stream.setStrokingColor(Color.BLACK);
		stream.setNonStrokingColor(Color.GRAY);
		stream.setLineWidth(1f);

		addRect(x1, y1, x2, y1 + 1);
		stream.fill(PathIterator.WIND_NON_ZERO);

		addRect(x1, y1, x2, y2);
		addLine(x1, y1 + 1, x2, y1 + 1);
		stream.closeAndStroke();

		stream.setNonStrokingColor(Color.BLACK);
		if (label != null) {
			drawText(PDType1Font.HELVETICA_BOLD, x1, x2, y1, label, true);
		}
	}

	public int drawTabelle(int x1, int x2, int y1, Object[] objects,
			ITabellenZugriff table) throws IOException {
		int i, x, span;
		boolean newSpan;
		int colX[];

		/* calculate absolute coordinates */
		colX = new int[table.getColumnCount() + 1];
		colX[0] = x1;
		for (i = 1; i < table.getColumnCount() + 1; i++) {
			colX[i] = colX[i - 1] + table.getWidth(i - 1);
		}

		if (objects != null) {
			for (int j = 0; j < objects.length; j++) {
				if (objects[j] == null) {
					continue;
				}

				for (i = 0; i < table.getColumnCount(); i++) {
					if (table.getBackgroundColor(objects[j], i) == null || table.getWidth(i) == 0) {
						continue;
					}

					x = colX[i + 1];

					stream.setNonStrokingColor(table.getBackgroundColor(
							objects[j], i));
					addRect(colX[i], y1 + j + 1, x, y1 + j + 2);
					stream.fill(PathIterator.WIND_NON_ZERO);
				}
			}
		}

		drawLabeledBox(x1, y1, x2, y1 + objects.length + 1, null);

		stream.setStrokingColor(Color.BLACK);

		span = 1;
		for (i = 0; i < table.getColumnCount(); i++) {
			span--;
			newSpan = span == 0;
			if (newSpan) {
				span = table.getColumnSpan(i);

				drawText(PDType1Font.HELVETICA_BOLD, colX[i], colX[i + span],
						y1, table.getColumn(i), true);
			}

			if (colX[i + 1] == x2) {
				break;
			}
			if (table.getWidth(i) == 0) {
				continue;
			}
			if (span > 1) {
				stream.setLineWidth(0.1f);
				addLine(colX[i + 1], y1 + 1, colX[i + 1], y1 + objects.length
						+ 1);
			} else {
				stream.setLineWidth(1f);
				addLine(colX[i + 1], y1, colX[i + 1], y1 + objects.length + 1);
			}
			stream.closeAndStroke();
		}

		stream.setLineWidth(0.1f);
		for (x = 0; x < objects.length - 1; x++) {
			addLine(x1, y1 + 2 + x, x2, y1 + 2 + x);
		}
		if (objects.length-1 > 0) {
			stream.closeAndStroke();
		}

		if (objects != null) {
			for (int j = 0; j < objects.length; j++) {
				if (objects[j] == null) {
					continue;
				}

				for (i = 0; i < table.getColumnCount(); i++) {
					x = colX[i + 1];

					if (table.getWidth(i) > 0) {
						drawText(table.getFont(objects[j], i),
								colX[i] + table.getIndent(objects[j], i), x, y1 + j
								+ 1, table.get(objects[j], i),
								table.getCentered(objects[j], i));
					}
				}
			}
		}
		return y1 + objects.length + 2;
	}

	public void drawText(PDFont font, int x1, int x2, int y1, int y2,
			String text, boolean center) throws IOException {
		PDFontDescriptor descr = font.getFontDescriptor();
		float boxProp, textProp;
		float boxWidth, boxHeight;
		float textWidth, textHeight;
		float shiftX, shiftY;

		boxHeight = (getY(y1) - getY(y2)) - 2 * textMargin;
		boxWidth = (getX(x2) - getX(x1)) - 2 * textMargin;
		boxProp = boxWidth / boxHeight;

		textHeight = (descr.getFontBoundingBox().getHeight()) / 1000f;
		textWidth = font.getStringWidth(text) / 1000f;
		textProp = textWidth / textHeight;

		/* begrenze vertikale Skalierung */
		while (textProp / boxProp > 1.75 && text.contains(" ")) {
			text = text.substring(0, text.lastIndexOf(' ')) + "...";
			textWidth = font.getStringWidth(text) / 1000f;
			textProp = textWidth / textHeight;
		}

		stream.beginText();

		if (textProp > boxProp) {
			/* scale over width */
			stream.setFont(font, boxWidth / textWidth);

			shiftX = 0f;
			shiftY = boxHeight / 2 - (textHeight * boxWidth / textWidth) / 2;
			shiftY += -descr.getDescent() / 1000 * (boxWidth / textWidth);
		} else {
			/* scale over height */
			stream.setFont(font, boxHeight / textHeight);

			if (center) {
				shiftX = boxWidth / 2 - textWidth * (boxHeight / textHeight)
						/ 2;
			} else {
				shiftX = 0f;
			}
			shiftY = -descr.getDescent() / 1000 * (boxHeight / textHeight);
		}

		stream.moveTextPositionByAmount(getX(x1) + textMargin + shiftX,
				getY(y2) + textMargin + shiftY);
		stream.drawString(text);
		stream.endText();
	}

	public void drawText(PDFont font, int x1, int x2, int y1, String text,
			boolean center) throws IOException {
		drawText(font, x1, x2, y1, y1 + 1, text, center);
	}

	public PDPageContentStream getStream() {
		return stream;
	}

	public float getX(int x) {
		assert (x <= cellCountX);
		return leftEdge + (pageWidth * x) / cellCountX;
	}

	public float getY(int y) {
		assert (y <= cellCountY);
		return topEdge - (pageHeight * y) / cellCountY;
	}

	protected void neueSeite() {
		this.page = new PDPage(PDPage.PAGE_SIZE_A4);
		this.doc.addPage(page);
	}

	protected void titelzeile(String[] guteEigenschaften) throws IOException {
		String[] titel = { "MU:", "KL:", "IN:", "CH:", "FF:", "GE:", "KO:",
				"KK:" };

		for (int i = 0; i < titel.length; i++) {
			int x = i * 8 + 1;

			drawText(PDType1Font.HELVETICA_BOLD, x + 0, x + 3, 0, 2, titel[i],
					true);
			drawText(PDType1Font.HELVETICA_BOLD, x + 3, x + 6, 0, 2,
					guteEigenschaften[i], true);
		}
	}
}

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

package de.hrogge.XML2PDFPlugin;

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

	protected final int cellCountX, cellCountY;

	protected final float pageWidth, pageHeight;
	protected final float cellWidth, cellHeight;

	protected final float leftEdge, topEdge;
	protected final float rightEdge, bottomEdge;

	protected final float textMargin;

	protected final PDPage page;
	protected final PDDocument doc;
	
	public PDFSeite(PDDocument d, float lrPageMargin, float tbPageMargin, float tMargin,
			int cx, int cy) {
		this.page = new PDPage(PDPage.PAGE_SIZE_A4);
		this.doc = d;
		this.doc.addPage(page);
		
		cellCountX = cx;
		cellCountY = cy;

		leftEdge = lrPageMargin * MM_TO_UNITS;
		bottomEdge = tbPageMargin * MM_TO_UNITS;

		textMargin = tMargin;
		pageWidth = page.findMediaBox().getWidth() - leftEdge * 2;
		pageHeight = page.findMediaBox().getHeight() - bottomEdge * 2;

		rightEdge = leftEdge + pageWidth;
		topEdge = bottomEdge + pageHeight;

		cellWidth = pageWidth / cellCountX;
		cellHeight = pageHeight / cellCountY;
	}

	public void addLine(PDPageContentStream stream, int x1, int y1, int x2,
			int y2) throws IOException {
		stream.addLine(getX(x1), getY(y1), getX(x2), getY(y2));
	}

	public void addRect(PDPageContentStream stream, int x1, int y1, int x2,
			int y2) throws IOException {
		stream.addRect(getX(x1), getY(y1), getX(x2) - getX(x1), getY(y2)
				- getY(y1));
	}

	public void drawImage(PDPageContentStream stream, int x1, int y1, int x2,
			int y2, PDJpeg bild) throws IOException {
		float x, y, w, h, tmp;

		x = getX(x1);
		y = getY(y2);

		w = getX(x2) - getX(x1);
		h = getY(y1) - getY(y2);
		
		if (w/bild.getWidth() > h/bild.getHeight()) {
			tmp = h * bild.getWidth()/bild.getHeight();
			x = x + (w-tmp)/2;
			w = tmp;
		}
		else {
			tmp = w * bild.getHeight()/bild.getWidth();
			y = y + (h-tmp)/2;
			h = tmp;
		}
		
		stream.drawXObject(bild, x, y, w,h);
	}

	public void drawLabeledBox(PDPageContentStream stream, int x1, int y1,
			int x2, int y2, String label) throws IOException {
		stream.setStrokingColor(Color.BLACK);
		stream.setNonStrokingColor(Color.GRAY);
		stream.setLineWidth(1f);

		addRect(stream, x1, y1, x2, y1 + 1);
		stream.fill(PathIterator.WIND_NON_ZERO);

		addRect(stream, x1, y1, x2, y2);
		addLine(stream, x1, y1 + 1, x2, y1 + 1);
		stream.closeAndStroke();

		stream.setNonStrokingColor(Color.BLACK);
		if (label != null) {
			drawText(stream, PDType1Font.HELVETICA_BOLD, x1, x2, y1, label,
					true);
		}
	}

	public int drawTabelle(PDPageContentStream stream, int x1, int x2, int y1,
			Object[] objects, ITabellenZugriff table)
			throws IOException {
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
					if (table.getBackgroundColor(objects[j], i) == null) {
						continue;
					}

					x = colX[i + 1];

					stream.setNonStrokingColor(table.getBackgroundColor(
							objects[j], i));
					addRect(stream, colX[i], y1 + j + 1, x, y1 + j + 2);
					stream.fill(PathIterator.WIND_NON_ZERO);
				}
			}
		}

		drawLabeledBox(stream, x1, y1, x2, y1 + objects.length + 1, null);

		stream.setStrokingColor(Color.BLACK);

		span = 1;
		for (i = 0; i < table.getColumnCount(); i++) {
			span--;
			newSpan = span == 0;
			if (newSpan) {
				span = table.getColumnSpan(i);
				
				drawText(stream, PDType1Font.HELVETICA_BOLD, colX[i], colX[i + span],
						y1, table.getColumn(i), true);
			}

			if (colX[i + 1] == x2) {
				break;
			}

			if (span > 1) {
				stream.setLineWidth(0.1f);
				addLine(stream, colX[i + 1], y1+1, colX[i + 1], y1 + objects.length
						+ 1);
			}
			else {
				stream.setLineWidth(1f);
				addLine(stream, colX[i + 1], y1, colX[i + 1], y1 + objects.length
						+ 1);
			}
			stream.closeAndStroke();
		}

		stream.setLineWidth(0.1f);
		for (x = 0; x < objects.length - 1; x++) {
			addLine(stream, x1, y1 + 2 + x, x2, y1 + 2 + x);
		}
		stream.closeAndStroke();

		if (objects != null) {
			for (int j = 0; j < objects.length; j++) {
				if (objects[j] == null) {
					continue;
				}

				for (i = 0; i < table.getColumnCount(); i++) {
					x = colX[i + 1];

					drawText(stream, table.getFont(objects[j], i), colX[i]
							+ table.getIndent(objects[j], i), x, y1 + j + 1,
							table.get(objects[j], i),
							table.getCentered(objects[j], i));
				}
			}
		}
		return y1 + objects.length + 2;
	}

	public void drawText(PDPageContentStream stream, PDFont font, int x1,
			int x2, int y1, int y2, String text, boolean center)
			throws IOException {
		PDFontDescriptor descr = font.getFontDescriptor();
		float boxProp, textProp;
		float boxWidth, boxHeight;
		float textWidth, textHeight;
		float shiftX, shiftY;

		boxWidth = (getX(x2) - getX(x1)) - 2 * textMargin;
		boxHeight = (getY(y1) - getY(y2)) - 2 * textMargin;
		boxProp = boxWidth / boxHeight;

		textWidth = font.getStringWidth(text) / 1000f;
		textHeight = (descr.getFontBoundingBox().getHeight()) / 1000f;
		textProp = textWidth / textHeight;

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

	public void drawText(PDPageContentStream stream, PDFont font, int x1,
			int x2, int y1, String text, boolean center) throws IOException {
		drawText(stream, font, x1, x2, y1, y1 + 1, text, center);
	}

	public float getX(int x) {
		assert (x <= cellCountX);
		return leftEdge + (pageWidth * x) / cellCountX;
	}

	public float getY(int y) {
		assert (y <= cellCountY);
		return topEdge - (pageHeight * y) / cellCountY;
	}
}
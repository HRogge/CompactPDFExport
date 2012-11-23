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

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public abstract class AbstractTabellenZugriff implements ITabellenZugriff {
	protected String columns[];
	protected int columnWidth[];
	protected int variabel, colCount;

	public AbstractTabellenZugriff(String[] col, int[] colwidth, int colcount,
			String titel, int width) {
		int uebrig;

		if (colcount == 0) {
			colcount = col.length;
		}
		this.columns = col;
		this.columnWidth = colwidth;
		this.colCount = colcount;

		variabel = -1;

		uebrig = width;
		for (int i = 0; i < colCount; i++) {
			uebrig -= columnWidth[i];
		}
		for (int i = 0; i < colCount; i++) {
			if (columns[i] == null) {
				columns[i] = titel;
				columnWidth[i] = uebrig;
				variabel = i;
				break;
			}
		}
	}

	@Override
	public String getColumn(int x) {
		return columns[x];
	}

	@Override
	public int getWidth(int x) {
		return columnWidth[x];
	}

	@Override
	public int getColumnCount() {
		return colCount;
	}

	@Override
	public PDFont getFont(Object o, int x) {
		return PDType1Font.HELVETICA;
	}

	@Override
	public boolean getCentered(Object o, int x) {
		return x != variabel;
	}

	@Override
	public Color getBackgroundColor(Object o, int x) {
		return null;
	}

	@Override
	public int getIndent(Object o, int x) {
		return 0;
	}

	@Override
	public int getColumnSpan(int x) {
		return 1;
	}

	@Override
	abstract public String get(Object obj, int x);
}

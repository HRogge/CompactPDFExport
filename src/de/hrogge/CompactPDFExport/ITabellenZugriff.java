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

public interface ITabellenZugriff {
	public String get(Object obj, int x);

	public Color getBackgroundColor(Object o, int x);

	public boolean getCentered(Object o, int x);

	public String getColumn(int x);

	public int getColumnCount();

	public int getColumnSpan(int x);

	public PDFont getFont(Object o, int x);

	public int getIndent(Object o, int x);

	public int getWidth(int x);
}

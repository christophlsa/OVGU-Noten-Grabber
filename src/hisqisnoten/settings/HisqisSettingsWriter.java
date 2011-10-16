/* Copyright (C) 2010-2011 Christoph Giesel
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package hisqisnoten.settings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import hisqisnoten.HQNContainer;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class HisqisSettingsWriter {

	public static Document generateDocument(HisqisSettings settings) {
		Element logindataElement = new Element("logindata");
		logindataElement.setAttribute("save", String.valueOf(settings.saveLogin));

		if (settings.saveLogin) {
			logindataElement.setAttribute("username", settings.username);
			logindataElement.setAttribute("password", settings.password);
		}

		Element settingsElement = new Element("settings");
		settingsElement.addContent(logindataElement);


		Element marksElement = new Element("marks");
		marksElement.setAttribute("save", String.valueOf(settings.saveMarks));

		if (settings.saveMarks) {
			marksElement.setAttribute("averagegrade", settings.averageGrade);
			marksElement.setAttribute("totalcreditpoints", settings.totalCreditPoints);

			for (HQNContainer mark : settings.marks) {
				Element markElement = new Element("mark");
				markElement.setAttribute("subject", mark.getFach());
				markElement.setAttribute("term", mark.getSemester());
				markElement.setAttribute("creditpoints", mark.getCreditpoints());
				markElement.setAttribute("mark", mark.getNote());
				markElement.setAttribute("passed", mark.getBestanden());

				marksElement.addContent(markElement);
			}
		}

		Element rootElement = new Element("config");
		rootElement.setAttribute("version", HisqisSettings.VERSION);
		rootElement.addContent(settingsElement);
		rootElement.addContent(marksElement);

		Document document = new Document(rootElement);

		return document;
	}

	public static boolean saveDocument(File res, Document document) {
		try {
			FileOutputStream out = new FileOutputStream(res);

			XMLOutputter fmt = new XMLOutputter();
			fmt.setFormat(Format.getPrettyFormat());

			fmt.output(document, out);
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}

		return true;
	}

	public static boolean saveDocument(HisqisSettings settings, File res) {
		return saveDocument(res, generateDocument(settings));
	}
}

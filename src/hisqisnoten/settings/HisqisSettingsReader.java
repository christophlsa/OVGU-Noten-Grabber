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

import hisqisnoten.HQNContainer;

import java.util.ArrayList;

import org.jdom.Document;
import org.jdom.Element;

public class HisqisSettingsReader {

	public static HisqisSettings parseDocument(Document document) {
		HisqisSettings settings = new HisqisSettings();

		Element rootElement = document.getRootElement();
		//final String VERSION = rootElement.getAttributeValue("version", HisqisSettings.VERSION);

		Element settingsElement = rootElement.getChild("settings");

		if (settingsElement != null) {
			Element logindataElement = settingsElement.getChild("logindata");

			if (logindataElement != null) {
				settings.saveLogin = Boolean.parseBoolean(logindataElement.getAttributeValue("save", "false"));

				if (settings.saveLogin) {
					settings.username = logindataElement.getAttributeValue("username");
					settings.password = logindataElement.getAttributeValue("password");
				}
			}
		}

		Element marksElement = rootElement.getChild("marks");

		if (marksElement != null) {
			settings.saveMarks = Boolean.parseBoolean(marksElement.getAttributeValue("save", "false"));

			if (settings.saveMarks) {
				settings.marks = new ArrayList<HQNContainer>();

				for (Object mark : marksElement.getChildren("mark")) {
					Element markElement = (Element) mark;

					String subject = markElement.getAttributeValue("subject");
					String term = markElement.getAttributeValue("term");
					String creditpoints = markElement.getAttributeValue("creditpoints");
					String markValue = markElement.getAttributeValue("mark");
					String passed = markElement.getAttributeValue("passed");

					settings.marks.add(new HQNContainer(subject, term, markValue, passed, creditpoints));
				}
			}
		}

		return settings;
	}

}

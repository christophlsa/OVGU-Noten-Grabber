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

import java.io.File;
import java.util.ArrayList;

public class HisqisSettings {

	public static final String VERSION = "0.4.0";

	public static final String CONFIGNAME = "ovgugrabber.xml";

	public static ArrayList<File> configpath = new ArrayList<File>();

	public File configpathUsed;

	public String username;

	public String password;

	public boolean saveLogin = false;

	public ArrayList<HQNContainer> marks;

	public String averageGrade;

	public String totalCreditPoints;

	public boolean saveMarks = false;

	public static void genConfigPath() {
		configpath.clear();

		// working path
		configpath.add(new File(CONFIGNAME));

		// same path as jar
		configpath.add(new File(
				new File(HisqisSettings.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent(),
				CONFIGNAME
		));

		// user home path
		configpath.add(new File(new File(System.getProperty("user.home")), CONFIGNAME));
	}

}

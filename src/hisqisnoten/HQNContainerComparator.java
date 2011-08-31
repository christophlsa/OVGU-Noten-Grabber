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

package hisqisnoten;

import java.util.Comparator;

/**
 * 
 * @author Christoph Giesel
 *
 */
public class HQNContainerComparator implements Comparator<HQNContainer> {

	@Override
	public int compare(HQNContainer t1, HQNContainer t2) {
		int compSem =  t1.getSemester().substring(5).compareTo(t2.getSemester().substring(5));

		if (compSem == 0) {
			return t1.getFach().compareTo(t2.getFach());
		}

		return compSem;
	}

}

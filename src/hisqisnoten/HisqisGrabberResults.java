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

import java.util.ArrayList;

public class HisqisGrabberResults {
	
	protected String averageGrade;
	protected String totalCreditPoints;
	protected ArrayList<HQNContainer> marks;
	
	public HisqisGrabberResults(String averageGrade, String totalCreditPoints, ArrayList<HQNContainer> marks) {
		this.averageGrade = averageGrade;
		this.totalCreditPoints = totalCreditPoints;
		this.marks = marks;
	}

	public String getAverageGrade() {
		return averageGrade;
	}

	public void setAverageGrade(String averageGrade) {
		this.averageGrade = averageGrade;
	}

	public String getTotalCreditPoints() {
		return totalCreditPoints;
	}

	public void setTotalCreditPoints(String totalCreditPoints) {
		this.totalCreditPoints = totalCreditPoints;
	}

	public ArrayList<HQNContainer> getMarks() {
		return marks;
	}

	public void setMarks(ArrayList<HQNContainer> marks) {
		this.marks = marks;
	}
}

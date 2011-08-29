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

/**
 * 
 * @author Christoph Giesel
 *
 */
public class HQNContainer {

    private String fach;
    private String semester;
    private String note;
    private String bestanden;
    private String creditpoints;

    public HQNContainer(String fach, String semester, String note, String bestanden, String creditpoints) {
        this.fach = fach;
        this.semester = semester;
        this.note = note;
        this.bestanden = bestanden;
        this.creditpoints = creditpoints;
    }

    public String getFach() {
        return fach;
    }

    public void setFach(String fach) {
        this.fach = fach;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
    
    public String getBestanden() {
        return bestanden;
    }

    public void setBestanden(String bestanden) {
        this.bestanden = bestanden;
    }

	public String getCreditpoints() {
		return creditpoints;
	}

	public void setCreditpoints(String cp) {
		this.creditpoints = cp;
	}
}

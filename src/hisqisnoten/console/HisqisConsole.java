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

package hisqisnoten.console;

import hisqisnoten.HQNContainer;
import hisqisnoten.HQNContainerComparator;
import hisqisnoten.HisqisGrabber;
import hisqisnoten.HisqisGrabberResults;

import java.io.Console;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javax.security.auth.login.LoginException;

/**
 * 
 * @author Christoph Giesel
 *
 */
public class HisqisConsole {
	
	static final String GETUSERNAME = "Enter Username: ";
	static final String GETPASSWORD = "Enter Password: ";
	static final String outputFormat = "%-60s | %-10s | %-4s | %3s | %s";
	
	private String username;
	private String password;

	public HisqisConsole(String user, String password) {
		this.username = user;
		this.password = password;
		
		if (this.username == null || this.password == null) {
			Console console = System.console();
			
			if (console != null) {
				if (this.username == null)
					this.username = String.valueOf(console.readLine(GETUSERNAME)).trim();
				if (this.password == null)
					this.password = String.valueOf(console.readPassword(GETPASSWORD)).trim();
			} else {
				Scanner sc = new Scanner(System.in);
				if (this.username == null) {
				    System.out.print(GETUSERNAME);
				    this.username = sc.next().trim();
				}
				if (this.password == null) {
				    System.out.print(GETPASSWORD);
				    this.password = sc.next().trim();
				}
			}
		}

        System.out.println();
		
		System.out.println("Bitte warten. Dies kann ein paar Sekunden dauern...");
        System.out.println();

        HisqisGrabber grabber = new HisqisGrabber(this.username, this.password);
        
        HisqisGrabberResults results = null;
		try {
			results = grabber.process();
		} catch (LoginException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
        
        ArrayList<HQNContainer> marks = results.getMarks();
            
        // nach Semester sortieren
        Collections.sort(marks, new HQNContainerComparator());

        System.out.printf(outputFormat, "Fach", "Semester", "Note", "CP", "Bestanden");
        System.out.println();
        System.out.println("--------------------------------------------"
                + "----------------------------------------------------------");
        
        for (HQNContainer hqnc : marks) {
            System.out.printf(outputFormat, hqnc.getFach(), hqnc.getSemester(), hqnc.getNote(), hqnc.getCreditpoints(), hqnc.getBestanden());
            System.out.println();
        }
        
        System.out.println("--------------------------------------------"
                + "----------------------------------------------------------");
        
        System.out.printf(outputFormat, "total", "", results.getAverageGrade(), results.getTotalCreditPoints(), "");
        System.out.println();
	}
	
}

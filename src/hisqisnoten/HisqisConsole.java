package hisqisnoten;

import java.io.Console;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class HisqisConsole {
	
	static final String GETUSERNAME = "Enter Username: ";
	static final String GETPASSWORD = "Enter Password: ";
	static final String outputFormat = "%-60s | %-10s | %-4s | %s";
	
	private String user;
	private String password;

	public HisqisConsole(String user, String password) {
		if (user == null || password == null) {
			Console console = System.console();
			
			if (console != null) {
				this.user = String.valueOf(console.readLine(GETUSERNAME)).trim();
			    this.password = String.valueOf(console.readPassword(GETPASSWORD)).trim();
			} else {
				Scanner sc = new Scanner(System.in);
			    System.out.print(GETUSERNAME);
			    this.user = sc.next().trim();
			    System.out.print(GETPASSWORD);
			    this.password = sc.next().trim();
			}
		} else {
			this.user = user;
			this.password = password;
		}

        System.out.println();
		
		System.out.println("Bitte warten. Dies kann ein paar Sekunden dauern...");
        System.out.println();

        HisqisGrabber grabber = new HisqisGrabber(this.user, this.password);
        ArrayList<HQNContainer> marks = grabber.process();
            
        // nach Semester sortieren
        Collections.sort(marks, new HQNContainerComparator());

        System.out.printf(outputFormat, "Fach", "Semester", "Note", "Bestanden");
        System.out.println();
        System.out.println("--------------------------------------------"
                + "------------------------------------------------------");
        
        for (HQNContainer hqnc : marks) {
            System.out.printf(outputFormat, hqnc.getFach(), hqnc.getSemester(), hqnc.getNote(), hqnc.getBestanden());
            System.out.println();
        }
	}
	
}

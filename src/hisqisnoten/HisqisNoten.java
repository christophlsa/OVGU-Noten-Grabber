package hisqisnoten;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HisqisNoten {

    public static void main(String[] args) {
        final String user;
        final String pass;

        if (args.length != 2) {
            Scanner sc = new Scanner(System.in);
            System.out.print("Username: ");
            user = sc.next().trim();
            System.out.print("Passwort: ");
            pass = sc.next().trim();

            System.out.println();
        } else {
            user = args[0];
            pass = args[1];
        }

        System.out.println("Bitte warten. Dies kann ein paar Sekunden dauern...");
        System.out.println();

        final String URLSTR1 = "https://vhisqis.uni-magdeburg.de/qisserver/rds?state=user&type=1";
        final String URLSTR2 = "https://idp.uni-magdeburg.de/idp/Authn/UserPassword";
        final String URLSTR3 = "https://vhisqis.uni-magdeburg.de/Shibboleth.sso/SAML2/POST";
        final String URLSTR4 = "https://vhisqis.uni-magdeburg.de/qisserver/rds?state=user&type=1";
        final String URLSTR5 = "https://vhisqis.uni-magdeburg.de/qisserver/rds?state=notenspiegelStudent&next=tree.vm&nextdir=qispos/notenspiegel/student&menuid=notenspiegelStudent&breadcrumb=notenspiegel&breadCrumbSource=menu&asi=";

        try {
            // Cookiemanager - keine Ahnung ob noetig
            CookieManager manager = new CookieManager();
            manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            CookieHandler.setDefault(manager);

            // -- Seite 1 (leitet zur Login Seite) --
            URL url1 = new URL(URLSTR1);
            url1.openConnection().getInputStream();

            // -- Seite 2 (Login Seite) --
            URL url2 = new URL(URLSTR2);
            URLConnection is2 = url2.openConnection();
            is2.setRequestProperty("Referer", url1.toExternalForm());
            is2.setDoOutput(true);
            OutputStream outStream2 = is2.getOutputStream();
            outStream2.write(("j_username=" + URLEncoder.encode(user, "UTF-8") + "&j_password=" + URLEncoder.encode(pass, "UTF-8")).getBytes());
            outStream2.flush();
            InputStream inStream2 = is2.getInputStream();

            String output2 = new Scanner(inStream2).useDelimiter("\\Z").next();

            Matcher matcher1 = Pattern.compile("<input type=\"hidden\" name=\"RelayState\" value=\"(.+)\"/>").matcher(output2);
            matcher1.find();
            final String RELAYSTATE = matcher1.group(1);

            Matcher matcher2 = Pattern.compile("<input type=\"hidden\" name=\"SAMLResponse\" value=\"(.+)\"/>").matcher(output2);
            matcher2.find();
            final String SAMLRESPONSE = matcher2.group(1);

            // -- Seite 3 (Seite, wo Login Daten hingeschickt werden) --
            URL url3 = new URL(URLSTR3);
            URLConnection is3 = url3.openConnection();
            is3.setRequestProperty("Referer", url2.toExternalForm());
            is3.setDoOutput(true);
            OutputStream outStream3 = is3.getOutputStream();
            outStream3.write(("RelayState=" + URLEncoder.encode(RELAYSTATE, "UTF-8") + "&SAMLResponse=" + URLEncoder.encode(SAMLRESPONSE, "UTF-8")).getBytes());
            outStream3.flush();
            is3.getInputStream();

            // -- Seite 4 (eingeloggte Hauptseite) --
            URL url4 = new URL(URLSTR4);
            URLConnection is4 = url4.openConnection();
            is4.setRequestProperty("Referer", url3.toExternalForm());
            InputStream inStream4 = is4.getInputStream();

            String output4 = new Scanner(inStream4).useDelimiter("\\Z").next();

            Matcher matcher4 = Pattern.compile("asi=(.+?)\"").matcher(output4);
            matcher4.find();
            final String ASI = matcher4.group(1);

            // -- Seite 5 (Uebersicht ueber die Studiengaenge) --
            URL url5 = new URL(URLSTR5 + URLEncoder.encode(ASI, "UTF-8"));
            URLConnection is5 = url5.openConnection();
            is5.setRequestProperty("Referer", url4.toExternalForm());
            InputStream inStream5 = is5.getInputStream();

            String output5 = new Scanner(inStream5).useDelimiter("\\Z").next();

            Pattern pattern5 = Pattern.compile("<a href=\"(.+?)\" title=\"Leistungen f");
            Matcher matcher5 = pattern5.matcher(output5);
            matcher5.find();
            final String NOTENLINK = matcher5.group(1).replaceAll("&amp;", "&");

            // -- Seite 6 (Notenuebersicht) --
            URL url6 = new URL(NOTENLINK);
            URLConnection is6 = url6.openConnection();
            is6.setRequestProperty("Referer", url5.toExternalForm());
            InputStream inStream6 = is6.getInputStream();

            String output6 = new Scanner(inStream6).useDelimiter("\\Z").next();

            Matcher matcher6 = Pattern.compile("<table border=\"0\">(.+?)<\\/table>", Pattern.MULTILINE | Pattern.DOTALL).matcher(output6);
            matcher6.find();
            final String TABELLE = matcher6.group(1);

            Matcher matcher7 = Pattern.compile("<tr>(.+?)<\\/tr>", Pattern.MULTILINE | Pattern.DOTALL).matcher(TABELLE);

            ArrayList<HQNContainer> noten = new ArrayList<HQNContainer>();
            Pattern tdPattern = Pattern.compile("<td (.+?)>(.+?)<\\/td>", Pattern.MULTILINE | Pattern.DOTALL);

            matcher7.find();
            matcher7.find();

            while (matcher7.find()) {
                Matcher tdMatcher = tdPattern.matcher(matcher7.group(1));
                tdMatcher.find();

                if (tdMatcher.group().contains("<b>")) {
                    continue;
                }

                tdMatcher.find();
                String fach = tdMatcher.group(2).trim();

                tdMatcher.find();
                String semester = tdMatcher.group(2).trim();

                tdMatcher.find();
                String note = Pattern.compile("<!--(.+?)-->", Pattern.MULTILINE | Pattern.DOTALL).matcher(tdMatcher.group(2)).replaceAll("").trim();

                noten.add(new HQNContainer(fach, semester, note));
            }

            System.out.printf("%-50s | %-15s | %s", "Fach", "Semester", "Note");
            System.out.println();
            System.out.println("------------------------------------------------------------------------------");

            for (HQNContainer hqnc : noten) {
                System.out.printf("%-50s | %-15s | %s", hqnc.getFach(), hqnc.getSemester(), hqnc.getNote());
                System.out.println();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

package hisqisnoten;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

public class HisqisGrabber {

	private String user;
	private String password;

	private String keystoreFile = "/resources/keystore";
	private char[] keystorePassword = "password".toCharArray();

	static final String PREDECESSORLOGINPAGEURL = "https://vhisqis.uni-magdeburg.de/qisserver/rds?state=user&type=1";
	static final String LOGINPAGEURL = "https://idp.uni-magdeburg.de/idp/Authn/UserPassword";
	static final String LOGINPAGEDATAGETTERURL = "https://vhisqis.uni-magdeburg.de/Shibboleth.sso/SAML2/POST";
	static final String STARTPAGEURL = "https://vhisqis.uni-magdeburg.de/qisserver/rds?state=user&type=1";
	static final String STUDYCOURSESURL = "https://vhisqis.uni-magdeburg.de/qisserver/rds?state=notenspiegelStudent&next=tree.vm&nextdir=qispos/notenspiegel/student&menuid=notenspiegelStudent&breadcrumb=notenspiegel&breadCrumbSource=menu&asi=";

	static final Pattern relaystatePattern = Pattern.compile("<input type=\"hidden\" name=\"RelayState\" value=\"(.+)\"/>");
	static final Pattern samlresponsePattern = Pattern.compile("<input type=\"hidden\" name=\"SAMLResponse\" value=\"(.+)\"/>");
	static final Pattern asiPattern = Pattern.compile("asi=(.+?)\"");
	static final Pattern studyCoursePattern = Pattern.compile("<a href=\"(.+?)\" title=\"Leistungen f");
	static final Pattern tablePattern = Pattern.compile("<table border=\"0\">(.+?)<\\/table>", Pattern.MULTILINE | Pattern.DOTALL);
	static final Pattern marksPattern = Pattern.compile("<tr>(.+?)<\\/tr>", Pattern.MULTILINE | Pattern.DOTALL);
	static final Pattern tdPattern = Pattern.compile("<td (.+?)>(.+?)<\\/td>", Pattern.MULTILINE | Pattern.DOTALL);
	static final Pattern markPattern = Pattern.compile("<!--(.+?)-->", Pattern.MULTILINE | Pattern.DOTALL);

	private String relaystate = "";
	private String samlresponse = "";
	private String asi = "";
	private String studyCourseURL = "";
	
	private ArrayList<HQNContainer> marks;

	public HisqisGrabber() {
		this(null, null);
	}

	public HisqisGrabber(String user, String password) {
		this.user = user;
		this.password = password;

		prepareCookies();

		prepareSSL();
	}

	/**
	 * Cookiemanager - keine Ahnung ob noetig
	 */
	private void prepareCookies() {
		CookieManager manager = new CookieManager();
		manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
		CookieHandler.setDefault(manager);
	}

	/**
	 * SSL Zeug - hoffentlich hilft es
	 * 
	 * @return
	 */
	private boolean prepareSSL() {
		try {
			KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());

			InputStream is = Class.class.getResourceAsStream(keystoreFile);

			if (is == null) {
				return false;
			}

			ks.load(is, keystorePassword);

			KeyManagerFactory km = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			km.init(ks, keystorePassword);

			SSLContext sc = SSLContext.getInstance("SSLv3");
			sc.init(km.getKeyManagers(), null, null);

			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public ArrayList<HQNContainer> process() {
		try {
			URL url1 = doStep1();
			URL url2 = doStep2(url1);
			URL url3 = doStep3(url2);
			URL url4 = doStep4(url3);
			URL url5 = doStep5(url4);
			doStep6(url5);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return marks;
	}

	/**
	 * forwards to login page
	 * 
	 * @return URL to first page
	 * @throws IOException
	 *             invalid URL or I/O Error
	 */
	private URL doStep1() throws IOException {
		URL url = new URL(PREDECESSORLOGINPAGEURL);
		url.openConnection().getInputStream();

		return url;
	}

	/**
	 * shows login page
	 * 
	 * @param referer
	 *            URL to predecessor page
	 * @throws IOException
	 *             invalid URL or I/O Error
	 */
	private URL doStep2(URL referer) throws IOException {
		URL url = new URL(LOGINPAGEURL);
		URLConnection inputConnection = url.openConnection();

		inputConnection.setRequestProperty("Referer", referer.toExternalForm());

		inputConnection.setDoOutput(true);
		OutputStream outputStream = inputConnection.getOutputStream();
		outputStream.write(("j_username=" + URLEncoder.encode(user, "UTF-8")
				+ "&j_password=" + URLEncoder.encode(password, "UTF-8"))
				.getBytes());
		outputStream.flush();

		InputStream inputResponseStream = inputConnection.getInputStream();

		String responseContent = new Scanner(inputResponseStream).useDelimiter("\\Z").next();

		Matcher relaystateMatcher = relaystatePattern.matcher(responseContent);
		relaystateMatcher.find();
		relaystate = relaystateMatcher.group(1);

		Matcher samlresponseMatcher = samlresponsePattern.matcher(responseContent);
		samlresponseMatcher.find();
		samlresponse = samlresponseMatcher.group(1);

		return url;
	}

	/**
	 * gets login page data
	 * 
	 * @param referer
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	private URL doStep3(URL referer) throws UnsupportedEncodingException, IOException {
		URL url3 = new URL(LOGINPAGEDATAGETTERURL);
		URLConnection inputConnection = url3.openConnection();

		inputConnection.setRequestProperty("Referer", referer.toExternalForm());

		inputConnection.setDoOutput(true);
		OutputStream outStream = inputConnection.getOutputStream();
		outStream.write(("RelayState=" + URLEncoder.encode(relaystate, "UTF-8")
				+ "&SAMLResponse=" + URLEncoder.encode(samlresponse, "UTF-8"))
				.getBytes());
		outStream.flush();

		inputConnection.getInputStream();

		return url3;
	}

	/**
	 * startpage
	 * 
	 * @param referer
	 * @return
	 * @throws IOException
	 */
	private URL doStep4(URL referer) throws IOException {
        URL url = new URL(STARTPAGEURL);
        URLConnection inputStream = url.openConnection();
        
        inputStream.setRequestProperty("Referer", referer.toExternalForm());
        
        InputStream responseStream = inputStream.getInputStream();

        String responseContent = new Scanner(responseStream).useDelimiter("\\Z").next();

        Matcher asiMatcher = asiPattern.matcher(responseContent);
        asiMatcher.find();
        asi = asiMatcher.group(1);
        
        return url;
	}
	
	/**
	 * overview of study courses
	 * 
	 * @param referer
	 * @return
	 * @throws IOException
	 */
	private URL doStep5(URL referer) throws IOException {
        URL url = new URL(STUDYCOURSESURL + URLEncoder.encode(asi, "UTF-8"));
        URLConnection inputStream = url.openConnection();
        
        inputStream.setRequestProperty("Referer", referer.toExternalForm());
        
        InputStream responseStream = inputStream.getInputStream();

        String responseContent = new Scanner(responseStream).useDelimiter("\\Z").next();

        Matcher studeCourseMatcher = studyCoursePattern.matcher(responseContent);
        studeCourseMatcher.find();
        studyCourseURL = studeCourseMatcher.group(1).replaceAll("&amp;", "&");
        
        return url;
	}
	
	/**
	 * marks overview
	 * 
	 * @param referer
	 * @return
	 * @throws IOException
	 */
	private URL doStep6(URL referer) throws IOException {
        URL url = new URL(studyCourseURL);
        URLConnection inputStream = url.openConnection();
        
        inputStream.setRequestProperty("Referer", referer.toExternalForm());
        
        InputStream responseStream = inputStream.getInputStream();

        String responseContent = new Scanner(responseStream).useDelimiter("\\Z").next();

        Matcher tableMatcher = tablePattern.matcher(responseContent);
        tableMatcher.find();
        final String table = tableMatcher.group(1);

        Matcher marksMatcher = marksPattern.matcher(table);

        marks = new ArrayList<HQNContainer>();

        marksMatcher.find();
        marksMatcher.find();

        while (marksMatcher.find()) {
            Matcher tdMatcher = tdPattern.matcher(marksMatcher.group(1));
            tdMatcher.find();

            if (tdMatcher.group().contains("<b>")) {
                continue;
            }

            tdMatcher.find();
            String subject = tdMatcher.group(2).trim();

            tdMatcher.find();
            String term = tdMatcher.group(2).trim();

            tdMatcher.find();
            String mark = markPattern.matcher(tdMatcher.group(2)).replaceAll("").trim();
            
            tdMatcher.find();
            String passed = tdMatcher.group(2).trim();

            marks.add(new HQNContainer(subject, term, mark, passed));
        }
        
        return url;
	}
	
	/**
	 * @return the username
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user the username to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the path to key store
	 */
	public String getKeystoreFile() {
		return keystoreFile;
	}

	/**
	 * @param keystoreFile the path to key store to set
	 */
	public void setKeystoreFile(String keystoreFile) {
		this.keystoreFile = keystoreFile;
	}

	/**
	 * @return the key store password
	 */
	public char[] getKeystorePassword() {
		return keystorePassword;
	}

	/**
	 * @param keystorePassword the key store password to set
	 */
	public void setKeystorePassword(char[] keystorePassword) {
		this.keystorePassword = keystorePassword;
	}

	/**
	 * @return the marks
	 */
	public ArrayList<HQNContainer> getMarks() {
		return marks;
	}
}

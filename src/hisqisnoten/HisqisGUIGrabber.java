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

import java.net.URL;

import javax.security.auth.login.LoginException;
import javax.swing.SwingWorker;

public class HisqisGUIGrabber extends SwingWorker<HisqisGrabberResults, Object> {

	HisqisGrabber grabber;

	public HisqisGUIGrabber(String user, String password) {
		grabber = new HisqisGrabber(user, password);
	}

	public HisqisGrabberResults process() {
		try {
			grabber.init();

			URL url1 = grabber.doStep1();
			setProgress(1);
			URL url2 = grabber.doStep2(url1);
			setProgress(2);
			URL url3 = grabber.doStep3(url2);
			setProgress(3);
			URL url4 = grabber.doStep4(url3);
			setProgress(4);
			URL url5 = grabber.doStep5(url4);
			setProgress(5);
			grabber.doStep6(url5);
			setProgress(6);
		} catch (LoginException e) {
			setProgress(0);
			cancel(false);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return new HisqisGrabberResults(grabber.averageGrade, grabber.totalCreditPoints, grabber.marks);
	}

	@Override
	protected HisqisGrabberResults doInBackground() throws Exception {
		return process();
	}

	/**
	 * @return the username
	 */
	public String getUser() {
		return grabber.user;
	}

	/**
	 * @param user the username to set
	 */
	public void setUser(String user) {
		grabber.user = user;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return grabber.password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		grabber.password = password;
	}
}

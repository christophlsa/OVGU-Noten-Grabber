package hisqisnoten;

import java.net.URL;

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
}

package eu.dzhw.zofar.mailReturns.main;

import java.util.Map;
import java.util.Set;

import eu.dzhw.zofar.mailReturns.feedback.FeedbackClient;
import eu.dzhw.zofar.mailReturns.scanning.ScanningClient;

public class MainApp {

	public static void main(String[] args) {
		ScanningClient scanner = ScanningClient.getInstance();

		try {
			final Map<Set<String>, Set<String>> result = scanner.scan("xxx", "xxx", "xxx", "INBOX/xxx", null);
			FeedbackClient.getInstance().send("xxx", "xxx", "xxx", "xxx", 25, "xxx", "xxx", result);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

}

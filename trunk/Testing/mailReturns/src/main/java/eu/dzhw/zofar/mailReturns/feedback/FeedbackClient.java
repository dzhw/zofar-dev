package eu.dzhw.zofar.mailReturns.feedback;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.dzhw.zofar.management.comm.mail.SendMailClient;
import eu.dzhw.zofar.management.comm.mail.components.EmailAuthenticator;

public class FeedbackClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(FeedbackClient.class);

	private static final FeedbackClient INSTANCE = new FeedbackClient();

	private FeedbackClient() {
		super();
	}

	public static FeedbackClient getInstance() {
		return INSTANCE;
	}
	
	public void send(final String sendMailFromAdress, final String sendMailFromUser, final String sendMailFromPass, final String sendMailFromServer, final int sendMailFromPort, final String sendMailToAdress,
			final String sourceMailPath, final Map<Set<String>, Set<String>> analyze) throws AddressException,
			MessagingException, IOException {
		String result = null;
		if (analyze != null) {
			final Set<Set<String>> keys = analyze.keySet();
			if (keys != null) {
				final Iterator<Set<String>> it = keys.iterator();
				final StringBuffer buffer = new StringBuffer();
				boolean flag = false;
				while (it.hasNext()) {
					Set<String> mail = it.next();
					Set<String> reasons = analyze.get(mail);
					buffer.append(mail + "XX" + reasons + "XX\n");
					flag = true;
				}
				if (flag) result = buffer.toString();
			}
		}
		if (result != null) {
			try {
				SendMailClient.getInstance().sendMail(sendMailFromAdress, sendMailToAdress,
						"Bericht_" + sourceMailPath.replace('/', '_'), result,
						new EmailAuthenticator(sendMailFromUser, sendMailFromPass), sendMailFromServer, sendMailFromPort);
			}
			catch (AddressException e) {e.printStackTrace();}
			catch (MessagingException e) {e.printStackTrace();}
		}
	}

}

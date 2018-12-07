package eu.dzhw.zofar.management.comm.mail;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.dzhw.zofar.management.comm.mail.components.EmailAuthenticator;

public class SendMailClient {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SendMailClient.class);

	private static final SendMailClient INSTANCE = new SendMailClient();

	private static class BulkThread extends Thread {

		Session session = null;
		String smtp = null;
		String user = null;
		String pass = null;

		String mailTo = null;
		String mailFrom = null;
		String mailSubject = null;
		String mailText = null;

		private BulkThread(Session session, String smtp, String user,
				String pass, String mailFrom, String mailTo,
				String mailSubject, String mailText) {
			super();
			this.session = session;
			this.smtp = smtp;
			this.user = user;
			this.pass = pass;

			this.mailFrom = mailFrom;
			this.mailTo = mailTo;
			this.mailSubject = mailSubject;
			this.mailText = mailText;
		}

		public synchronized static BulkThread newInstance(Session session,
				String smtp, String user, String pass, String mailFrom,
				String mailTo, String mailSubject, String mailText) {
			return new BulkThread(session, smtp, user, pass, mailFrom, mailTo,
					mailSubject, mailText);
		}

		@Override
		public void run() {
			try {
				// Define message
				MimeMessage message = new MimeMessage(session);
				message.setFrom(new InternetAddress(mailFrom));
				message.addRecipient(Message.RecipientType.TO,
						new InternetAddress(mailTo));
				message.setSubject(mailSubject);
				message.setText(mailText);

				// Send message
				// Transport.send(message);
				message.saveChanges(); // implicit with Transport.send()
				Transport tr = session.getTransport("smtp");
				tr.connect(smtp, user, pass);
				message.saveChanges(); // don't forget this
				tr.sendMessage(message, message.getAllRecipients());
				tr.close();
				System.out.println("send mail to " + mailTo);
			} catch (MessagingException e) {
				System.out.println("failed to send mail to " + mailTo);
			}

		}
	};

	private SendMailClient() {
		super();
	}

	public static SendMailClient getInstance() {
		return INSTANCE;
	}

	public boolean sendBulkMail(String from,
			HashSet<String> to, HashMap<String, String> subject,
			HashMap<String, String> text, final EmailAuthenticator auth,
			String smtp, String testTo) throws AddressException,
			MessagingException {
		if (from == null)
			return false;
		if ((from.trim()).equals(""))
			return false;
		if (to == null)
			return false;
		if (to.isEmpty())
			return false;
		if (subject == null)
			return false;
		if (subject.isEmpty())
			return false;
		if (subject.size() != to.size())
			return false;
		if (text == null)
			return false;
		if (text.isEmpty())
			return false;
		if (text.size() != to.size())
			return false;
		if (auth == null)
			return false;
		if (smtp == null)
			return false;
		if ((smtp.trim()).equals(""))
			return false;

		Properties props = System.getProperties();

		// Setup mail server
		props.put("mail.smtp.host", smtp);
		props.put("mail.smtp.auth", "true");
		props.setProperty("mail.smtp.socketFactory.port", "25");

		// Get session
		Session session = Session.getInstance(props, auth);

		Iterator<String> toIt = to.iterator();
		if (toIt != null) {
			BlockingQueue<Runnable> bulkQueue = new LinkedBlockingQueue<Runnable>();

			while (toIt.hasNext()) {
				String mailTo = toIt.next();
				String mailSubject = subject.get(mailTo);
				String mailText = text.get(mailTo);
				if ((testTo != null) && (!(testTo.trim()).equals("")))
					mailTo = testTo.trim();
				bulkQueue.add(BulkThread.newInstance(session, smtp, auth.getPasswordAuthentication().getUserName(), auth.getPasswordAuthentication().getPassword(),
						from, mailTo, mailSubject, mailText));
			}

			int cpus = Runtime.getRuntime().availableProcessors();
			int maxThreads = cpus * 2;
			maxThreads = (maxThreads > 0 ? maxThreads : 1);

			System.out.println("Maximum threads : " + maxThreads);

			ThreadPoolExecutor pool = new ThreadPoolExecutor(1, maxThreads, 60,
					TimeUnit.MILLISECONDS, bulkQueue);
			pool.shutdown();
		}
		return true;
	}
	
	public boolean sendMail(final String from, final String to, final String subject,
			final String text, final EmailAuthenticator auth, final String smtp, final int port)
			throws AddressException, MessagingException {
		final Properties props = System.getProperties();
		// Setup mail server
		props.put("mail.smtp.host", smtp);
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.auth", "true");
		props.setProperty("mail.smtp.socketFactory.port", port+"");

		// Get session
		final Session session = Session.getInstance(props, auth);

		// Define message
		final MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		message.setSubject(subject);
		message.setText(text);

		// Send message
		Transport.send(message);
		return true;
	}

}

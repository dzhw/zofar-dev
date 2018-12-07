package eu.dzhw.zofar.management.comm.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated
public class MailClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(MailClient.class);

	private static final MailClient INSTANCE = new MailClient();

//	private Store store;
//	private Set<Folder> folders;

	private MailClient() {
		super();
	}

	public static MailClient getInstance() {
		return INSTANCE;
	}
	
//	public boolean sendMail(final String from, final String to, final String subject,
//			final String text, final EmailAuthenticator auth, final String smtp, final int port)
//			throws AddressException, MessagingException {
//		final Properties props = System.getProperties();
//		// Setup mail server
//		props.put("mail.smtp.host", smtp);
//		props.put("mail.smtp.port", port);
//		props.put("mail.smtp.auth", "true");
//		props.setProperty("mail.smtp.socketFactory.port", port+"");
//
//		// Get session
//		final Session session = Session.getInstance(props, auth);
//
//		// Define message
//		final MimeMessage message = new MimeMessage(session);
//		message.setFrom(new InternetAddress(from));
//		message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
//		message.setSubject(subject);
//		message.setText(text);
//
//		// Send message
//		Transport.send(message);
//		return true;
//	}

//	private void openStore(final String smtp, final int port, final EmailAuthenticator auth) throws MessagingException {
//		final Properties props = System.getProperties();
//		// Setup mail server
//		props.put("mail.smtp.host", smtp);
//		props.put("mail.smtp.port", port);
//		props.put("mail.smtp.auth", "true");
//		props.setProperty("mail.smtp.socketFactory.port", port+"");
//		
//		final Session session = Session.getDefaultInstance(props, auth);
//
//		// Get the store
//		store = session.getStore("imaps");
//		store.connect(smtp, auth.getPasswordAuthentication().getUserName(), auth.getPasswordAuthentication().getPassword());
//	}

//	public Folder openFolder(final String folderPath) throws MessagingException {
//		if (store == null)
//			throw new MessagingException("Store is closed");
//
//		// Get folder
//		final Folder folder = store.getFolder(folderPath);
//		return openFolder(folder);
//	}

//	private Folder openFolder(final Folder folder) throws MessagingException {
//		if (store == null)
//			throw new MessagingException("Store is closed");
//		if (folders == null)
//			folders = new HashSet<Folder>();
//		if (!folders.contains(folder)) {
//			if ((folder != null) && (!folder.isOpen()))
//				folder.open(Folder.READ_WRITE);
//			folders.add(folder);
//		}
//		return folder;
//	}

//	public void closeFolders() throws MessagingException {
//		if (store == null)
//			throw new MessagingException("Store is closed");
//
//		if (folders == null)
//			folders = new HashSet<Folder>();
//
//		final Set<Folder> removedFolders = new HashSet<Folder>();
//		for (final Folder folder : folders) {
//			if ((folder != null) && (folder.isOpen())) {
//				try {
//					folder.close(false);
//				} catch (MessagingException e) {
//					LOGGER.error("Failed to close Folder {}", folder.getFullName());
//				}
//			}
//			removedFolders.add(folder);
//		}
//		folders.removeAll(removedFolders);
//	}

//	public void closeStore() {
//		if (store != null)
//			try {
//				store.close();
//			} catch (MessagingException e) {
//			} finally {
//				store = null;
//			}
//	}

//	public Map<String, List<Message>> loadMessages(final Folder folder, final SearchTerm searchTerm) throws MessagingException {
//		return loadMessages(folder, searchTerm, folder);
//	}

//	private Map<String, List<Message>> loadMessages(final Folder folder, final SearchTerm searchTerm, final Folder baseFolder) throws MessagingException {
//
//		openFolder(folder);
//
//		// // Get directory
//		Message[] messages = null;
//		if (searchTerm == null)
//			messages = folder.getMessages();
//		else {
//			LOGGER.info("Searching...");
//			messages = folder.search(searchTerm);
//		}
//		Map<String, List<Message>> back = new HashMap<String, List<Message>>();
//		if (messages != null) {
//			for (Message message : messages) {
//				String folderName = "UNKOWN";
//				final Folder parentFolder = message.getFolder();
//				if (parentFolder != null)
//					folderName = parentFolder.getFullName();
//				folderName = folderName.replaceAll(baseFolder.getFullName(), "");
//				if(folderName.equals(""))folderName="EMPTY";
//				List<Message> messageBag = null;
//				if (back.containsKey(folderName))
//					messageBag = back.get(folderName);
//				if (messageBag == null)
//					messageBag = new ArrayList<Message>();
//				messageBag.add(message);
//				back.put(folderName, messageBag);
//			}
//		}
//
//		final Folder[] subFolders = folder.list("*");
//
//		if (subFolders != null) {
//			for (Folder subFolder : subFolders) {
//				back.putAll(loadMessages(subFolder, searchTerm, baseFolder));
//			}
//		}
//		return back;
//	}

//	public int getMessageId(final Message message) throws MessagingException {
//		if (message == null)
//			return -1;
//
//		// final Folder folder = message.getFolder();
//		// openFolder(folder);
//		return message.getMessageNumber();
//	}

//	public String getMessageSender(final Message message) throws MessagingException {
//		if (message == null)
//			return null;
//
//		// final Folder folder = message.getFolder();
//		// openFolder(folder);
//
//		Address[] adresses = message.getFrom();
//
//		if (adresses != null) {
//			final StringBuffer back = new StringBuffer();
//			for (Address address : adresses) {
//				back.append(address.toString() + " ");
//			}
//			return back.toString().trim();
//		}
//
//		return null;
//	}

//	public String getMessageFolder(final Message message) throws IOException, MessagingException {
//		if (message == null)
//			return null;
//		final Folder folder = message.getFolder();
//		openFolder(folder);
//
//		String group = "UNKOWN";
//		if (folder != null)
//			group = folder.getName();
//		return group;
//	}

//	public String getMessageBody(final Part message) throws IOException, MessagingException {
//		if (message == null)
//			return null;
//		if (message.isMimeType("text/*")) {
//			String s = (String) message.getContent();
//			return ReplaceClient.getInstance().stripHTML(s);
//		}
//
//		if (message.isMimeType("multipart/alternative")) {
//			// prefer html text over plain text
//			Multipart mp = (Multipart) message.getContent();
//			String text = null;
//			for (int i = 0; i < mp.getCount(); i++) {
//				Part bp = mp.getBodyPart(i);
//				if (bp.isMimeType("text/plain")) {
//					if (text == null)
//						text = getMessageBody(bp);
//					continue;
//				} else if (bp.isMimeType("text/html")) {
//					String s = getMessageBody(bp);
//					if (s != null)
//						return ReplaceClient.getInstance().stripHTML(s);
//				} else {
//					return getMessageBody(bp);
//				}
//			}
//			return text;
//		} else if (message.isMimeType("multipart/*")) {
//			Multipart mp = (Multipart) message.getContent();
//			for (int i = 0; i < mp.getCount(); i++) {
//				String s = getMessageBody(mp.getBodyPart(i));
//				if (s != null)
//					return ReplaceClient.getInstance().stripHTML(s);
//			}
//		}
//
//		return null;
//	}
}

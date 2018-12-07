package eu.dzhw.zofar.management.comm.mail;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeMessage;

import eu.dzhw.zofar.management.utils.string.ReplaceClient;

public abstract class AbstractMailClient {

	protected AbstractMailClient() {
		super();
	}
	
	public abstract void markFolder(final Object folder,final Message[] messages,final Flags flags, final boolean value) throws MessagingException;
	
	public int getMessageId(final Message message) throws MessagingException {
		if (message == null)
			return -1;
		return message.getMessageNumber();
	}

	public String getMessageSender(final Message message) throws Exception {
		if (message == null)
			return null;

		Address[] adresses = message.getFrom();

		if (adresses != null) {
			final StringBuffer back = new StringBuffer();
			for (Address address : adresses) {
				back.append(address.toString() + " ");
			}
			return back.toString().trim();
		}

		return null;
	}


	public String getMessageBody(final Part message) throws Exception {
		if (message == null)
			return null;
		
		try{
			if (message.isMimeType("text/*")) {
				String s = (String) message.getContent();
				return ReplaceClient.getInstance().stripHTML(s);
			}

			if (message.isMimeType("multipart/alternative")) {
				// prefer html text over plain text
				Multipart mp = (Multipart) message.getContent();
				String text = null;
				for (int i = 0; i < mp.getCount(); i++) {
					Part bp = mp.getBodyPart(i);
					if (bp.isMimeType("text/plain")) {
						if (text == null)
							text = getMessageBody(bp);
						continue;
					} else if (bp.isMimeType("text/html")) {
						String s = getMessageBody(bp);
						if (s != null)
							return ReplaceClient.getInstance().stripHTML(s);
					} else {
						return getMessageBody(bp);
					}
				}
				return text;
			} else if (message.isMimeType("multipart/*")) {
				Multipart mp = (Multipart) message.getContent();
				for (int i = 0; i < mp.getCount(); i++) {
					String s = getMessageBody(mp.getBodyPart(i));
					if (s != null)
						return ReplaceClient.getInstance().stripHTML(s);
				}
			}
		}
		catch(MessagingException e){
			final String msg = e.getMessage();
			if((msg != null)&&(msg.equals("Unable to load BODYSTRUCTURE"))&&((MimeMessage.class).isAssignableFrom(message.getClass()))){
				final Object content = new MimeMessage((MimeMessage) message).getContent();
				if (content != null)
					return ReplaceClient.getInstance().stripHTML(content+"");
			}
			else throw e;
		}
		return null;
	}

}

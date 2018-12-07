package eu.dzhw.zofar.mailReturns.scanning;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.search.SearchTerm;

import org.apache.poi.hsmf.exceptions.ChunkNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.dzhw.zofar.mailReturns.scanning.util.analyze.Analyzer;
import eu.dzhw.zofar.mailReturns.scanning.util.analyze.Analyzer.TYPE;
import eu.dzhw.zofar.management.comm.mail.AbstractMailClient;
import eu.dzhw.zofar.management.comm.mail.ReceiveFileMailClient;
import eu.dzhw.zofar.management.comm.mail.ReceiveMailClient;
import eu.dzhw.zofar.management.comm.mail.components.EmailAuthenticator;

public class ScanningClient {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ScanningClient.class);

	private static final ScanningClient INSTANCE = new ScanningClient();
	
	final Message[] dummy = new Message[0];

	private ScanningClient() {
		super();
	}

	public static ScanningClient getInstance() {
		return INSTANCE;
	}
	
	public Map<Set<String>, Set<String>> scan(final File folder) throws MessagingException, IOException, ChunkNotFoundException  {
		if(folder == null)return null;
		if(!folder.canRead())throw new IOException("folder not readable");
		
		ReceiveFileMailClient fileMailClient = ReceiveFileMailClient.getInstance();
		Map<String, List<Message>> messages = fileMailClient.loadMessages(new File("xxx"));

		final Map<Set<String>, Set<String>> back = analyze(folder,messages,fileMailClient);
		return back;
	}

	public Map<Set<String>, Set<String>> scan(final String user,
			final String pass, final String imap, final String folderPath,
			final SearchTerm searchTerm) throws AddressException,
			MessagingException, IOException {
//		final Map<Set<String>, Set<String>> back = new LinkedHashMap<Set<String>, Set<String>>();
		final ReceiveMailClient receiveMailClient = ReceiveMailClient.getInstance();
		try {
			receiveMailClient.openStore(imap, 25, new EmailAuthenticator(user, pass));
			final Folder folder = receiveMailClient.openFolder(folderPath);
			final Map<String, List<Message>> messages = receiveMailClient
					.loadMessages(folder, searchTerm);
			
			final Map<Set<String>, Set<String>> back = analyze(folder,messages,receiveMailClient);

//			for (final Map.Entry<String, List<Message>> entry : messages.entrySet()) {
//				final List<Message> messagesInFolder = entry.getValue();
//				for (final Message msg : messagesInFolder) {
//						try {
//							final String messageBody = receiveMailClient.getMessageBody(msg);
//							final String cleanedMessageBody = cleanMessageBody(messageBody);
//							final Map<TYPE, Set<String>> analyze = Analyzer.getInstance().analyze(receiveMailClient.getMessageSender(msg),cleanedMessageBody);
//							final Set<Message> set = new HashSet<Message>();
//							set.add(msg);
//							final Flags tmpflags = new Flags();
//							tmpflags.add(Flag.SEEN);
//
//							if(!analyze.get(TYPE.REASONS).isEmpty()){
//								tmpflags.add(Flag.FLAGGED);
//								folder.setFlags(set.toArray(dummy),tmpflags, true);
//							}
//							else{
//								folder.setFlags(set.toArray(dummy),tmpflags, false);
//							}
//							
//							if((!analyze.get(TYPE.ADDRESS).isEmpty())&&(!analyze.get(TYPE.REASONS).isEmpty()))back.put(analyze.get(TYPE.ADDRESS), analyze.get(TYPE.REASONS));
//							else{
//								LOGGER.info("==> {}",cleanedMessageBody);
//							}
//						} catch (Exception e) {
//							LOGGER.error("{}",e);
//						}
////						LOGGER.info("{}",analyze);
//				}
//			}
			return back;
		} catch (final MessagingException e) {
			e.printStackTrace();
		}
//		finally{
//			receiveMailClient.closeFolders();
//			receiveMailClient.closeStore();
//		}
		return null;
	}
	
	private Map<Set<String>, Set<String>> analyze(final Object folder,final Map<String, List<Message>> messages,final AbstractMailClient client){
		final Map<Set<String>, Set<String>> back = new LinkedHashMap<Set<String>, Set<String>>();
		for (final Map.Entry<String, List<Message>> entry : messages.entrySet()) {
			final List<Message> messagesInFolder = entry.getValue();
			for (final Message msg : messagesInFolder) {
					try {
						final String messageBody = client.getMessageBody(msg);
						final String cleanedMessageBody = cleanMessageBody(messageBody);
						final Map<TYPE, Set<String>> analyze = Analyzer.getInstance().analyze(client.getMessageSender(msg),cleanedMessageBody);
						final Set<Message> set = new HashSet<Message>();
						set.add(msg);
						final Flags tmpflags = new Flags();
						tmpflags.add(Flag.SEEN);

						if(!analyze.get(TYPE.REASONS).isEmpty()){
							tmpflags.add(Flag.FLAGGED);
							client.markFolder(folder, set.toArray(dummy),tmpflags, true);
//							folder.setFlags(set.toArray(dummy),tmpflags, true);
						}
						else{
//							folder.setFlags(set.toArray(dummy),tmpflags, false);
							client.markFolder(folder, set.toArray(dummy),tmpflags, false);
						}
						
						if((!analyze.get(TYPE.ADDRESS).isEmpty())&&(!analyze.get(TYPE.REASONS).isEmpty()))back.put(analyze.get(TYPE.ADDRESS), analyze.get(TYPE.REASONS));
						else{
							LOGGER.info("() ==> (Adresses : "+analyze.get(TYPE.ADDRESS).isEmpty()+", Reasons : "+analyze.get(TYPE.REASONS).isEmpty()+") {}",cleanedMessageBody);
						}
					} catch (Exception e) {
						LOGGER.error("{}",e);
					}
//					LOGGER.info("{}",analyze);
			}
		}
		return back;
	}
	
	private String cleanMessageBody(final String uncleaned){
		if(uncleaned == null )return null;
		if(uncleaned.equals(""))return "";
		String cleaned = uncleaned.replaceAll("\\r\\n|\\r|\\n", " ");
		cleaned = cleaned.replaceAll("=", "");
		cleaned = cleaned.replaceAll(" {2,}", " ");
		return cleaned;
	}
}

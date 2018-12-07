package eu.dzhw.zofar.management.utils.audio;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class TalkClient {
	
	/** The Constant INSTANCE. */
	private static final TalkClient INSTANCE = new TalkClient();

	/** The Constant LOGGER. */
	final static Logger LOGGER = LoggerFactory.getLogger(TalkClient.class);
	
	private static final String VOICENAME_kevin = "kevin16";

	private  TalkClient() {
		super();
	}
	
	/**
	 * Gets the single instance of TalkClient.
	 * 
	 * @return single instance of TalkClient
	 */
	public static synchronized TalkClient getInstance() {
		return INSTANCE;
	}
	
	public void talk(final String text){
	    VoiceManager voiceManager = VoiceManager.getInstance();
//	    Voice voice = voiceManager.getVoice(VOICENAME_kevin);    
	    Voice[] voices = voiceManager.getVoices();
	    System.out.println("Voices found : "+voices.length);
	    for (int i = 0; i < voices.length; i++) {
	     System.out.println("    " + voices[i].getName() + " ("
	       + voices[i].getDomain() + " domain)");
	    }
	    Voice voice = voiceManager.getVoice("kevin16"); // switch to voiceName
		voice.allocate();
		voice.speak(text);
	}

}

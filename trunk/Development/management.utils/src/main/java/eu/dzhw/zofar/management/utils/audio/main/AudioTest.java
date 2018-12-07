package eu.dzhw.zofar.management.utils.audio.main;

import eu.dzhw.zofar.management.utils.audio.TalkClient;

public class AudioTest {

	public static void main(String[] args) {
		TalkClient talker = TalkClient.getInstance();
		talker.talk("huschelpuschel");

	}

}

package presentation.feedback.format.txt;

import java.util.Map;

import presentation.feedback.format.AbstractFormat;


public class TxtFormat extends AbstractFormat {

	private static TxtFormat INSTANCE;

	
	private TxtFormat() {
		super();
	}
	
	public static AbstractFormat getInstance(){
		if(INSTANCE == null)INSTANCE = new TxtFormat();
		return INSTANCE;
	}

	@Override
	public Object formatExits(final Map<String,Integer> data) {
		if (data == null)
			return null;
		if (data.isEmpty())
			return null;
		final StringBuffer back = new StringBuffer();
		for (final Map.Entry<String,Integer> exit : data.entrySet()) {
			back.append("" + exit.getKey() + " = " + exit.getValue()+"\n");
		}
		return back.toString();
	}

	@Override
	public Object formatCounts(Map<String, Integer> data) {
		int participated = 0;
		int finished = 0;
		for (final Map.Entry<String,Integer> exit : data.entrySet()) {
			if(exit.getKey().equals("/end.xhtml"))finished = exit.getValue();
			else participated = participated + exit.getValue();
		}
		final StringBuffer back = new StringBuffer();
		back.append("Participated : "+participated+"\n");
		back.append("Finished : "+finished+"");
		return back;
	}

}

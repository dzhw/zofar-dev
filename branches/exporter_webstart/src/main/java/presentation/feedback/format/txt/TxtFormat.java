package presentation.feedback.format.txt;

import java.util.Map;

import presentation.feedback.format.AbstractFormat;

// TODO: Auto-generated Javadoc
/**
 * The Class TxtFormat.
 */
public class TxtFormat extends AbstractFormat {

	/** The instance. */
	private static TxtFormat INSTANCE;

	/**
	 * Instantiates a new txt format.
	 */
	private TxtFormat() {
		super();
	}

	/**
	 * Gets the single instance of TxtFormat.
	 * 
	 * @return single instance of TxtFormat
	 */
	public static AbstractFormat getInstance() {
		if (INSTANCE == null)
			INSTANCE = new TxtFormat();
		return INSTANCE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * presentation.feedback.format.AbstractFormat#formatExits(java.util.Map)
	 */
	@Override
	public Object formatExits(final Map<String, Integer> data) {
		if (data == null)
			return null;
		if (data.isEmpty())
			return null;
		final StringBuffer back = new StringBuffer();
		for (final Map.Entry<String, Integer> exit : data.entrySet()) {
			back.append("" + exit.getKey() + " = " + exit.getValue() + "\n");
		}
		return back.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * presentation.feedback.format.AbstractFormat#formatCounts(java.util.Map)
	 */
	@Override
	public Object formatCounts(final Map<String, Integer> data) {
//		int participated = 0;
//		int finished = 0;
//		for (final Map.Entry<String, Integer> exit : data.entrySet()) {
//			if (exit.getKey().equals("/end.xhtml"))
//				finished = exit.getValue();
//			else
//				participated = participated + exit.getValue();
//		}
		final StringBuffer back = new StringBuffer();
		back.append("Invited : " + data.get("invited") + "\n");
		back.append("Participated : " + data.get("participated") + "\n");
		back.append("Finished : " + data.get("finished") + "");
		return back.toString();
	}

}

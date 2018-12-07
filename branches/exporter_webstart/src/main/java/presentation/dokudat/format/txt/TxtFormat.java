package presentation.dokudat.format.txt;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import model.HeaderEntry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import presentation.dokudat.format.AbstractFormat;
import eu.dzhw.zofar.management.utils.objects.CollectionClient;
import eu.dzhw.zofar.management.utils.string.StringUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class TxtFormat.
 */
public class TxtFormat extends AbstractFormat {

	private static final Logger LOGGER = LoggerFactory.getLogger(TxtFormat.class);

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

	/**
	 * Builds the export.
	 * 
	 * @param map
	 *            the data
	 * @param path
	 *            the path
	 * @param mapping
	 *            the mapping
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	private Object buildExport(final Map<HeaderEntry, Object> data, final Map<String, String> mapping) throws Exception {
		Map<String, Object> back = new HashMap<String, Object>();
		if (data == null)
			return back;
		final StringBuffer questionsBack = new StringBuffer();
		final StringBuffer answersBack = new StringBuffer();

		final Set<HeaderEntry> headers = data.keySet();
		final Iterator<HeaderEntry> headerIt = headers.iterator();

		while (headerIt.hasNext()) {
			final HeaderEntry header = headerIt.next();
			final Map<String, Object> tmp = (Map<String, Object>) data.get(header);

			final String questionText = CollectionClient.getInstance().implode(header.getText().toArray(), " ");
			// questionsBack.append(header.getVariable()+"\t\t"+questionText+"\n");
			final String questionColumn1 = StringUtils.getInstance().fillup(header.getVariable(), ' ', 12);
			final String questionColumn2 = " ";
			final String questionColumn3 = questionText;
			questionsBack.append(questionColumn1 + questionColumn2 + questionColumn3 + "\n");

			final Map<String, String> values = (Map<String, String>) tmp.get("values");
			for (final Map.Entry<String, String> value : values.entrySet()) {
				// answersBack.append(header.getVariable()+"\t\t" +
				// value.getKey() + "\t"+ value.getValue()+"\n");
				final String answerColumn1 = StringUtils.getInstance().fillup(header.getVariable(), ' ', 12);
				String tmpValue = value.getKey();
				if (mapping.containsKey(value.getKey()))
					tmpValue = mapping.get(value.getKey());

				final String answerColumn2 = StringUtils.getInstance().fillup(tmpValue, ' ', 6);
				final String answerColumn3 = StringUtils.getInstance().fillup("", ' ', 16);
				final String answerColumn4 = value.getValue();
				answersBack.append(answerColumn1 + answerColumn2 + answerColumn3+ answerColumn4 + "\n");
			}
		}
		back.put("questions", questionsBack.toString());
		back.put("answers", answersBack.toString());
		return back;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see presentation.codebook.format.AbstractFormat#format(java.util.Map,
	 * java.util.Map)
	 */
	@Override
	public Object format(final Map<HeaderEntry, Object> data, final Map<String, String> mapping) {
		if (data == null)
			return null;
		if (data.isEmpty())
			return null;

		try {
			return this.buildExport(data, mapping);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

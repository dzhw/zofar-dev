package service.feedback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import model.SurveyHistory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class FeedbackService.
 */
public class FeedbackService {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(FeedbackService.class);

	/** The instance. */
	private static FeedbackService INSTANCE;

	/**
	 * Instantiates a new feedback service.
	 */
	private FeedbackService() {
		super();
	}

	/**
	 * Gets the single instance of FeedbackService.
	 * 
	 * @return single instance of FeedbackService
	 */
	public static FeedbackService getInstance() {
		if (INSTANCE == null)
			INSTANCE = new FeedbackService();
		return INSTANCE;
	}

	/**
	 * Gets the exit pages.
	 * 
	 * @param history
	 *            the history
	 * @param back
	 *            the back
	 * @return the exit pages
	 */
	public Map<String, Integer> getExitPages(final Map<String, List<SurveyHistory>> history, Map<String, Integer> back) {
		// final Map<String,Integer> back = new LinkedHashMap<String,Integer>();
		if (back == null)
			back = new LinkedHashMap<String, Integer>();
		final List<String> pages = new ArrayList<String>(history.keySet());
		Collections.sort(pages);
		for (final String token : pages) {
			final String page = this.lastPage(history.get(token));
			Integer count = 0;
			if (back.containsKey(page))
				count = back.get(page);
			back.put(page, count + 1);
		}
		return back;
	}
	
	/**
	 * Gets the exit pages.
	 * 
	 * @param history
	 *            the history
	 * @param back
	 *            the back
	 * @return the exit pages
	 */
	public Map<String, Integer> getCockpitInformations(final Map<String, List<SurveyHistory>> history) {
		final Map<String, Integer> back = new HashMap<String, Integer>();
		for(final Map.Entry<String, List<SurveyHistory>> token : history.entrySet()){
			boolean participated = false;
			boolean finished = false;
			final List<SurveyHistory> historyData = token.getValue();
			if((historyData != null)&&(!historyData.isEmpty())){
				participated = true;
				for(final SurveyHistory tmp: historyData){
					final String page = tmp.getPage();
					if(page.equals("/end.xhtml")){
						finished = true;
						break;
					}
				}
			}
			if(finished){
				Integer counter = 0;
				if(back.containsKey("finished"))counter = back.get("finished");
				counter = counter + 1;
				back.put("finished", counter);
			}
			else if(participated){
				Integer counter = 0;
				if(back.containsKey("participated"))counter = back.get("participated");
				counter = counter + 1;
				back.put("participated", counter);
			}
		}
		return back;
	}

	/**
	 * Last page.
	 * 
	 * @param history
	 *            the history
	 * @return the string
	 */
	private String lastPage(final List<SurveyHistory> history) {
		if (history == null)
			return "NOHISTORY";
		if (history.size() == 0)
			return "NOHISTORY";

		String lastPage = "UNKOWN";
		// Date date = new Date(0);
		Date date = new Date(Long.MIN_VALUE);
		for (final SurveyHistory item : history) {
			if (date.before(item.getTimestamp())) {
				date = item.getTimestamp();
				lastPage = item.getPage();
			}
		}
		return lastPage;
	}

}

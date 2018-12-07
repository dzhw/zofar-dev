package service.feedback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import model.SurveyHistory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FeedbackService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(FeedbackService.class);

	private static FeedbackService INSTANCE;

	private FeedbackService() {
		super();
	}

	public static FeedbackService getInstance() {
		if (INSTANCE == null)
			INSTANCE = new FeedbackService();
		return INSTANCE;
	}

	public Map<String,Integer> getExitPages(final Map<String,List<SurveyHistory>> history){
		final Map<String,Integer> back = new LinkedHashMap<String,Integer>();
		final List<String> pages = new ArrayList<String>(history.keySet());
		Collections.sort(pages);
		for(final String pageName: pages){
			final String page = lastPage(history.get(pageName));
			Integer count =0;
			if(back.containsKey(page))count =  back.get(page);
			back.put(page, count + 1);
		}
		return back;
	}
	
	private String lastPage(final List<SurveyHistory> history){
		String lastPage = "UNKOWN";
		Date date = new Date(0);
		for(final SurveyHistory item:history){
			if(date.before(item.getTimestamp())){
				date = item.getTimestamp();
				lastPage = item.getPage();
			}
		}
		return lastPage;
	}

}

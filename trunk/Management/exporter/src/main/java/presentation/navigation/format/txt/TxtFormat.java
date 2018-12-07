package presentation.navigation.format.txt;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import eu.dzhw.zofar.management.utils.files.FileClient;
import model.HeaderEntry;
import model.TransitionEntry;
import presentation.navigation.format.AbstractFormat;

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
	public Object format(Map<String, Set<TransitionEntry>> data) {
		if (data == null)
			return null;
		if (data.isEmpty())
			return null;

		try {
			return buildExport(data,FileClient.getInstance().createTempFile("transitions", "txt").getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Object buildExport(Map<String, Set<TransitionEntry>> data,
			String absolutePath) {
		final StringBuffer buffer = new StringBuffer();
		final Set<String> sourcePages = data.keySet();
		final Iterator<String> sourceIt = sourcePages.iterator();
		
		while (sourceIt.hasNext()) {
			final String sourcePage = sourceIt.next();
			final Set<TransitionEntry> transitions = data.get(sourcePage);
			buffer.append("Source : "+sourcePage+"\n");
			for(TransitionEntry transition:transitions){
				if(transition.getCondition() != null)buffer.append("\t =("+transition.getCondition()+")=> "+transition.getTargetPage()+" \n");
				else buffer.append("\t ==> "+transition.getTargetPage()+" \n");
			}
			buffer.append("\n");
		}

		return buffer.toString();
	}


}

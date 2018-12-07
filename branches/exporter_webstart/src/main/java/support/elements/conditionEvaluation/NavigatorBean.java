package support.elements.conditionEvaluation;

import java.util.List;
import java.util.Map;

public class NavigatorBean {
	
	final List<String> currentPages;
	final List<String> pagesFromHistory;
	final Map<String, List<String>> pageTree;

	public NavigatorBean(List<String> currentPages, List<String> pagesFromHistory,Map<String, List<String>> pageTree) {
		super();
		this.currentPages = currentPages;
		this.pagesFromHistory = pagesFromHistory;
		this.pageTree = pageTree;
	}
	
//	public boolean isBackward() {
//		return false;
//	}
//
//	public boolean isForward() {
//		return false;
//	}
//	
	public boolean isSame() {
		if(this.currentPages == null)return false;
		if(this.currentPages.isEmpty())return false;
		if(this.pagesFromHistory == null)return false;
		if(this.pagesFromHistory.isEmpty())return false;
		
		boolean back = false;
		for(final String currentPage: this.currentPages){
			final int index = this.pagesFromHistory.indexOf(currentPage);
			if((index != -1)&&(this.pagesFromHistory.size() > (index+1))){
				if(this.pagesFromHistory.get(index+1).equals(currentPage))back = true;
			}
		}
		return back;
	}
}

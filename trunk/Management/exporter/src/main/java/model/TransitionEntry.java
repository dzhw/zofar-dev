package model;

public class TransitionEntry {
	
	private String sourcePage;
	private String condition;
	private String targetPage;
	
	public TransitionEntry(String sourcePage, String condition,
			String targetPage) {
		super();
		this.sourcePage = sourcePage;
		this.condition = condition;
		this.targetPage = targetPage;
	}

	public String getSourcePage() {
		return sourcePage;
	}

	public void setSourcePage(String sourcePage) {
		this.sourcePage = sourcePage;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getTargetPage() {
		return targetPage;
	}

	public void setTargetPage(String targetPage) {
		this.targetPage = targetPage;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((condition == null) ? 0 : condition.hashCode());
		result = prime * result
				+ ((sourcePage == null) ? 0 : sourcePage.hashCode());
		result = prime * result
				+ ((targetPage == null) ? 0 : targetPage.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TransitionEntry other = (TransitionEntry) obj;
		if (condition == null) {
			if (other.condition != null)
				return false;
		} else if (!condition.equals(other.condition))
			return false;
		if (sourcePage == null) {
			if (other.sourcePage != null)
				return false;
		} else if (!sourcePage.equals(other.sourcePage))
			return false;
		if (targetPage == null) {
			if (other.targetPage != null)
				return false;
		} else if (!targetPage.equals(other.targetPage))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TransitionEntry [sourcePage=" + sourcePage + ", condition="
				+ condition + ", targetPage=" + targetPage + "]";
	}


}

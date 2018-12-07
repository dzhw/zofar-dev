package model;

// TODO: Auto-generated Javadoc
/**
 * The Class TransitionEntry.
 */
public class TransitionEntry {

	/** The source page. */
	private String sourcePage;

	/** The condition. */
	private String condition;

	/** The target page. */
	private String targetPage;

	/**
	 * Instantiates a new transition entry.
	 * 
	 * @param sourcePage
	 *            the source page
	 * @param condition
	 *            the condition
	 * @param targetPage
	 *            the target page
	 */
	public TransitionEntry(final String sourcePage, final String condition, final String targetPage) {
		super();
		this.sourcePage = sourcePage;
		this.condition = condition;
		this.targetPage = targetPage;
	}

	/**
	 * Gets the source page.
	 * 
	 * @return the source page
	 */
	public String getSourcePage() {
		return this.sourcePage;
	}

	/**
	 * Sets the source page.
	 * 
	 * @param sourcePage
	 *            the new source page
	 */
	public void setSourcePage(final String sourcePage) {
		this.sourcePage = sourcePage;
	}

	/**
	 * Gets the condition.
	 * 
	 * @return the condition
	 */
	public String getCondition() {
		return this.condition;
	}

	/**
	 * Sets the condition.
	 * 
	 * @param condition
	 *            the new condition
	 */
	public void setCondition(final String condition) {
		this.condition = condition;
	}

	/**
	 * Gets the target page.
	 * 
	 * @return the target page
	 */
	public String getTargetPage() {
		return this.targetPage;
	}

	/**
	 * Sets the target page.
	 * 
	 * @param targetPage
	 *            the new target page
	 */
	public void setTargetPage(final String targetPage) {
		this.targetPage = targetPage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.condition == null) ? 0 : this.condition.hashCode());
		result = prime * result + ((this.sourcePage == null) ? 0 : this.sourcePage.hashCode());
		result = prime * result + ((this.targetPage == null) ? 0 : this.targetPage.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		final TransitionEntry other = (TransitionEntry) obj;
		if (this.condition == null) {
			if (other.condition != null)
				return false;
		} else if (!this.condition.equals(other.condition))
			return false;
		if (this.sourcePage == null) {
			if (other.sourcePage != null)
				return false;
		} else if (!this.sourcePage.equals(other.sourcePage))
			return false;
		if (this.targetPage == null) {
			if (other.targetPage != null)
				return false;
		} else if (!this.targetPage.equals(other.targetPage))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TransitionEntry [sourcePage=" + this.sourcePage + ", condition=" + this.condition + ", targetPage=" + this.targetPage + "]";
	}

}

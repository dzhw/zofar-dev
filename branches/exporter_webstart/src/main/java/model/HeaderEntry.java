package model;

import java.util.Set;

// TODO: Auto-generated Javadoc
/**
 * The Class HeaderEntry.
 */
public class HeaderEntry {

	/** The variable. */
	private String variable;

	/** The text. */
	private Set<String> text;

	/**
	 * Instantiates a new header entry.
	 * 
	 * @param variable
	 *            the variable
	 * @param text
	 *            the text
	 */
	public HeaderEntry(final String variable, final Set<String> text) {
		super();
		this.variable = variable;
		this.text = text;
	}

	/**
	 * Gets the variable.
	 * 
	 * @return the variable
	 */
	public String getVariable() {
		return this.variable;
	}

	/**
	 * Sets the variable.
	 * 
	 * @param variable
	 *            the new variable
	 */
	public void setVariable(final String variable) {
		this.variable = variable;
	}

	/**
	 * Gets the text.
	 * 
	 * @return the text
	 */
	public Set<String> getText() {
		return this.text;
	}

	/**
	 * Sets the text.
	 * 
	 * @param text
	 *            the new text
	 */
	public void setText(final Set<String> text) {
		this.text = text;
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
		result = prime * result + ((this.text == null) ? 0 : this.text.hashCode());
		result = prime * result + ((this.variable == null) ? 0 : this.variable.hashCode());
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
		final HeaderEntry other = (HeaderEntry) obj;
		if (this.text == null) {
			if (other.text != null)
				return false;
		} else if (!this.text.equals(other.text))
			return false;
		if (this.variable == null) {
			if (other.variable != null)
				return false;
		} else if (!this.variable.equals(other.variable))
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
		return "HeaderEntry [variable=" + this.variable + ", text=" + this.text + "]";
	}
}

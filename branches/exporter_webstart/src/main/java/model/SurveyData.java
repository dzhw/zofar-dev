package model;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class SurveyData.
 */
public class SurveyData implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1474890185705248359L;

	/** The variable name. */
	private String variableName;

	/** The value. */
	private String value;

	/**
	 * Instantiates a new survey data.
	 */
	public SurveyData() {
		super();
	}

	/**
	 * Gets the variable name.
	 * 
	 * @return the variable name
	 */
	public String getVariableName() {
		return this.variableName;
	}

	/**
	 * Sets the variable name.
	 * 
	 * @param variableName
	 *            the new variable name
	 */
	public void setVariableName(final String variableName) {
		this.variableName = variableName;
	}

	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * Sets the value.
	 * 
	 * @param value
	 *            the new value
	 */
	public void setValue(final String value) {
		this.value = value;
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
		result = prime * result + ((this.value == null) ? 0 : this.value.hashCode());
		result = prime * result + ((this.variableName == null) ? 0 : this.variableName.hashCode());
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
		final SurveyData other = (SurveyData) obj;
		if (this.value == null) {
			if (other.value != null)
				return false;
		} else if (!this.value.equals(other.value))
			return false;
		if (this.variableName == null) {
			if (other.variableName != null)
				return false;
		} else if (!this.variableName.equals(other.variableName))
			return false;
		return true;
	}

}

package model;

// TODO: Auto-generated Javadoc
/**
 * The Class ValueEntry.
 */
public class ValueEntry {

	/** The value. */
	private String value;

	/** The label. */
	private String label;
	
	/** Missing flag **/
	private boolean missing;

	/**
	 * Instantiates a new value entry.
	 * 
	 * @param value
	 *            the value
	 * @param label
	 *            the label
	 */
	public ValueEntry(final String value, final String label,final boolean missing) {
		super();
		this.value = value;
		this.label = label;
		this.missing = missing;
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

	/**
	 * Gets the label.
	 * 
	 * @return the label
	 */
	public String getLabel() {
		return this.label;
	}

	/**
	 * Sets the label.
	 * 
	 * @param label
	 *            the new label
	 */
	public void setLabel(final String label) {
		this.label = label;
	}
	
	
	public boolean isMissing() {
		return missing;
	}

	public void setMissing(boolean missing) {
		this.missing = missing;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + (missing ? 1231 : 1237);
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ValueEntry))
			return false;
		ValueEntry other = (ValueEntry) obj;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (missing != other.missing)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ValueEntry [" + (value != null ? "value=" + value + ", " : "") + (label != null ? "label=" + label + ", " : "") + "missing=" + missing + "]";
	}
}

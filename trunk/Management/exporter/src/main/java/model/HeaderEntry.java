package model;

import java.util.Set;

public class HeaderEntry {

	private String variable;
	private Set<String> text;
	
	public HeaderEntry(String variable, Set<String> text) {
		super();
		this.variable = variable;
		this.text = text;
	}

	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

	public Set<String> getText() {
		return text;
	}

	public void setText(Set<String> text) {
		this.text = text;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		result = prime * result
				+ ((variable == null) ? 0 : variable.hashCode());
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
		HeaderEntry other = (HeaderEntry) obj;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		if (variable == null) {
			if (other.variable != null)
				return false;
		} else if (!variable.equals(other.variable))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "HeaderEntry [variable=" + variable + ", text=" + text + "]";
	}
}

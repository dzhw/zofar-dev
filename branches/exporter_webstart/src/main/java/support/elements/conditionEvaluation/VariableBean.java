package support.elements.conditionEvaluation;

import java.io.Serializable;

public class VariableBean implements Serializable{

	private static final long serialVersionUID = 4629341269129052858L;
	private final String variableName;
	private final Object value;
	private final Object valueId;
	

    public VariableBean(String variableName, Object value, Object valueId) {
		super();
		this.variableName = variableName;
		this.value = value;
		this.valueId = valueId;
	}

	public String getVariableName() {
		return variableName;
	}

	public Object getValue() {
		return value;
	}

	public Object getValueId() {
		return valueId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		result = prime * result + ((valueId == null) ? 0 : valueId.hashCode());
		result = prime * result + ((variableName == null) ? 0 : variableName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof VariableBean))
			return false;
		VariableBean other = (VariableBean) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		if (valueId == null) {
			if (other.valueId != null)
				return false;
		} else if (!valueId.equals(other.valueId))
			return false;
		if (variableName == null) {
			if (other.variableName != null)
				return false;
		} else if (!variableName.equals(other.variableName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "VariableBean [" + (variableName != null ? "variableName=" + variableName + ", " : "") + (value != null ? "value=" + value + ", " : "") + (valueId != null ? "valueId=" + valueId : "") + "]";
	}
}

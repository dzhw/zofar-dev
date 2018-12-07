package eu.dzhw.zofar.testing.condition.term.elements;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ElementVector {
	
	private Map<String,Set<Element>> tupels;
	
	public ElementVector() {
		super();
		this.tupels = new LinkedHashMap<String,Set<Element>>();
	}

	public Map<String,Set<Element>> getTupels() {
		return tupels;
	}

	public void setTupels(Map<String,Set<Element>> tupels) {
		this.tupels = tupels;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tupels == null) ? 0 : tupels.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ElementVector))
			return false;
		ElementVector other = (ElementVector) obj;
		if (tupels == null) {
			if (other.tupels != null)
				return false;
		} 
		else{
			for(Entry<String, Set<Element>> item : tupels.entrySet()){
				final String key = item.getKey();
				final Set<Element> thisSet = item.getValue();
				final Set<Element> otherSet = other.tupels.get(key);
				if (thisSet == null) {
					if (otherSet != null)
						return false;
				} 
				else{
					if (otherSet == null) return false;
					else if(!thisSet.equals(otherSet))return false;
				}
			}
		}
//		else if (!tupels.equals(other.tupels))
//			return false;
		return true;
	}

	@Override
	public String toString() {
//		return "ElementVector [" + (tupels != null ? "tupels=" + tupels : "") + "]";
		return "" + (tupels != null ? "" + tupels : "") + "";
	}
}

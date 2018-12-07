package eu.dzhw.zofar.management.utils.reflection.main;

import java.io.Serializable;
import java.util.Date;

public class TestObj implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6423056227055560841L;
	private Date timestamp;
	private Long id;
	private Object value;

	
	
	public TestObj(long time) {
		super();
	}



	public Date getTimestamp() {
		return timestamp;
	}



	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public Object getValue() {
		return value;
	}



	public void setValue(Object value) {
		this.value = value;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TestObj other = (TestObj) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (timestamp == null) {
			if (other.timestamp != null) {
				return false;
			}
		} else if (!timestamp.equals(other.timestamp)) {
			return false;
		}
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}



	@Override
	public String toString() {
		return "TestObj [" + (timestamp != null ? "timestamp=" + timestamp + ", " : "")
				+ (id != null ? "id=" + id + ", " : "") + (value != null ? "value=" + value : "") + "]";
	}


	
	
	
}

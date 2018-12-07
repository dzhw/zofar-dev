package eu.zofar.cockpit.service.transfer;

import java.io.Serializable;

public class Transfer implements Serializable {

	private static final long serialVersionUID = 6779516382664160111L;

	private String id;
	private long timestamp;

	public Transfer() {
		super();
	}

	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(final long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = prime * result
				+ (int) (this.timestamp ^ (this.timestamp >>> 32));
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Transfer))
			return false;
		final Transfer other = (Transfer) obj;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		if (this.timestamp != other.timestamp)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Transfer [id=" + this.id + ", timestamp=" + this.timestamp
				+ "]";
	}
}

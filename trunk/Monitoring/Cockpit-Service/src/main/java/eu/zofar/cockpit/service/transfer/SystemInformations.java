package eu.zofar.cockpit.service.transfer;

import java.io.Serializable;

public class SystemInformations implements Serializable {

	private static final long serialVersionUID = 5593929805430487822L;

	private String location;
	private double load;
	private long usedHeap;

	public SystemInformations() {
		super();
	}

	public void setLoad(final double load) {
		this.load = load;
	}

	public void setUsedHeap(final long usedHeap) {
		this.usedHeap = usedHeap;
	}

	public double getLoad() {
		return this.load;
	}

	public long getUsedHeap() {
		return this.usedHeap;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(final String location) {
		this.location = location;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(this.load);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((this.location == null) ? 0 : this.location.hashCode());
		result = prime * result
				+ (int) (this.usedHeap ^ (this.usedHeap >>> 32));
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SystemInformations))
			return false;
		final SystemInformations other = (SystemInformations) obj;
		if (Double.doubleToLongBits(this.load) != Double
				.doubleToLongBits(other.load))
			return false;
		if (this.location == null) {
			if (other.location != null)
				return false;
		} else if (!this.location.equals(other.location))
			return false;
		if (this.usedHeap != other.usedHeap)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SystemInformations [location=" + this.location + ", load="
				+ this.load + ", usedHeap=" + this.usedHeap + "]";
	}

}

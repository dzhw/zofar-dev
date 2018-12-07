package eu.zofar.cockpit.master.objects;

import java.io.Serializable;
import java.util.Arrays;

import eu.zofar.cockpit.service.transfer.SystemInformations;

public class ServerEntry implements Serializable {

	private static final long serialVersionUID = 5180798724047640321L;

	private final String survey_id;
	private long lastContact;
	private final String address;

	private Double finished;
	private Double participated;
	private String[][] exitPages;

	private SystemInformations healthStatus;

	public ServerEntry(final String survey_id, final String address) {
		super();
		this.survey_id = survey_id;
		this.address = address;
	}

	public long getLastContact() {
		return this.lastContact;
	}

	public void setLastContact(final long lastContact) {
		this.lastContact = lastContact;
	}

	public Double getFinished() {
		return this.finished;
	}

	public void setFinished(final Double finished) {
		this.finished = finished;
	}

	public Double getParticipated() {
		return this.participated;
	}

	public void setParticipated(final Double participated) {
		this.participated = participated;
	}

	public String[][] getExitPages() {
		return this.exitPages;
	}

	public void setExitPages(final String[][] exitPages) {
		this.exitPages = exitPages;
	}

	public String getSurvey_id() {
		return this.survey_id;
	}

	public String getAddress() {
		return this.address;
	}

	public SystemInformations getHealthStatus() {
		return this.healthStatus;
	}

	public void setHealthStatus(final SystemInformations healthStatus) {
		this.healthStatus = healthStatus;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.address == null) ? 0 : this.address.hashCode());
		result = prime * result
				+ ((this.survey_id == null) ? 0 : this.survey_id.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ServerEntry))
			return false;
		final ServerEntry other = (ServerEntry) obj;
		if (this.address == null) {
			if (other.address != null)
				return false;
		} else if (!this.address.equals(other.address))
			return false;
		if (this.survey_id == null) {
			if (other.survey_id != null)
				return false;
		} else if (!this.survey_id.equals(other.survey_id))
			return false;
		return true;
	}

	private String showArray(final String[][] array) {
		final StringBuffer buffer = new StringBuffer();
		boolean first = true;
		for (final String[] item : array) {
			if (!first)
				buffer.append(",");
			buffer.append(Arrays.toString(item));
			first = false;
		}
		return "[" + buffer.toString() + "]";
	}

	@Override
	public String toString() {
		return "ServerEntry [survey_id=" + this.survey_id + ", lastContact="
				+ this.lastContact + ", address=" + this.address
				+ ", finished=" + this.finished + ", participated="
				+ this.participated + ", exitPages="
				+ this.showArray(this.exitPages) + ", healthStatus="
				+ this.healthStatus + "]";
	}

}

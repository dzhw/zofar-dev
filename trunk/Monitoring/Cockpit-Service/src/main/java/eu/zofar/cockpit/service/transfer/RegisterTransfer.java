package eu.zofar.cockpit.service.transfer;

public class RegisterTransfer extends Transfer {

	private static final long serialVersionUID = 7944847685573311379L;

	private String address;
	private String host;
	private String survey;
	private int port;

	public RegisterTransfer() {
		super();
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	public String getHost() {
		return this.host;
	}

	public void setHost(final String host) {
		this.host = host;
	}

	public String getSurvey() {
		return this.survey;
	}

	public void setSurvey(final String survey) {
		this.survey = survey;
	}

	public int getPort() {
		return this.port;
	}

	public void setPort(final int port) {
		this.port = port;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((this.address == null) ? 0 : this.address.hashCode());
		result = prime * result
				+ ((this.host == null) ? 0 : this.host.hashCode());
		result = prime * result + this.port;
		result = prime * result
				+ ((this.survey == null) ? 0 : this.survey.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof RegisterTransfer))
			return false;
		final RegisterTransfer other = (RegisterTransfer) obj;
		if (this.address == null) {
			if (other.address != null)
				return false;
		} else if (!this.address.equals(other.address))
			return false;
		if (this.host == null) {
			if (other.host != null)
				return false;
		} else if (!this.host.equals(other.host))
			return false;
		if (this.port != other.port)
			return false;
		if (this.survey == null) {
			if (other.survey != null)
				return false;
		} else if (!this.survey.equals(other.survey))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RegisterTransfer [getAddress()=" + this.getAddress()
				+ ", getHost()=" + this.getHost() + ", getPort()="
				+ this.getPort() + ", getId()=" + this.getId()
				+ ", getSurvey()=" + this.getSurvey() + ", getTimestamp()="
				+ this.getTimestamp() + "]";
	}
}

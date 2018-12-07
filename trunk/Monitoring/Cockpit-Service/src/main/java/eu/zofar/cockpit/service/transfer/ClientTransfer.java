package eu.zofar.cockpit.service.transfer;

import java.util.Arrays;

public class ClientTransfer extends Transfer {

	private static final long serialVersionUID = 8828939475694562964L;
	private Double finished;
	private Double participated;
	private String[][] exitPages;

	private SystemInformations system;
	private Feedback feedback;

	public ClientTransfer() {
		super();
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

	public SystemInformations getSystem() {
		return this.system;
	}

	public void setSystem(final SystemInformations system) {
		this.system = system;
	}

	public Feedback getFeedback() {
		return feedback;
	}

	public void setFeedback(Feedback feedback) {
		this.feedback = feedback;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(exitPages);
		result = prime * result
				+ ((feedback == null) ? 0 : feedback.hashCode());
		result = prime * result
				+ ((finished == null) ? 0 : finished.hashCode());
		result = prime * result
				+ ((participated == null) ? 0 : participated.hashCode());
		result = prime * result + ((system == null) ? 0 : system.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientTransfer other = (ClientTransfer) obj;
		if (!Arrays.deepEquals(exitPages, other.exitPages))
			return false;
		if (feedback == null) {
			if (other.feedback != null)
				return false;
		} else if (!feedback.equals(other.feedback))
			return false;
		if (finished == null) {
			if (other.finished != null)
				return false;
		} else if (!finished.equals(other.finished))
			return false;
		if (participated == null) {
			if (other.participated != null)
				return false;
		} else if (!participated.equals(other.participated))
			return false;
		if (system == null) {
			if (other.system != null)
				return false;
		} else if (!system.equals(other.system))
			return false;
		return true;
	}

	private String showArray(final String[][] array) {
		if (array == null)
			return null;
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
		return "ClientTransfer [finished=" + this.finished + ", participated="
				+ this.participated + ", exitPages="
				+ this.showArray(this.getExitPages()) + ", system="
				+ this.system + "]";
	}

}

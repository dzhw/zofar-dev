package eu.zofar.cockpit.master.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SurveyEntry implements Serializable {

	private static final long serialVersionUID = -8208808183443172864L;
	private final String survey_id;
	private final List<ServerEntry> server;

	public SurveyEntry(final String survey_id) {
		super();
		this.survey_id = survey_id;
		this.server = new ArrayList<ServerEntry>();
	}

	public List<ServerEntry> getServer() {
		return this.server;
	}

	public String getSurvey_id() {
		return this.survey_id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.server == null) ? 0 : this.server.hashCode());
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
		if (!(obj instanceof SurveyEntry))
			return false;
		final SurveyEntry other = (SurveyEntry) obj;
		if (this.server == null) {
			if (other.server != null)
				return false;
		} else if (!this.server.equals(other.server))
			return false;
		if (this.survey_id == null) {
			if (other.survey_id != null)
				return false;
		} else if (!this.survey_id.equals(other.survey_id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SurveyEntry [survey_id=" + this.survey_id + ", server="
				+ this.server + "]";
	}

}

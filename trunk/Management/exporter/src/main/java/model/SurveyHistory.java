package model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class SurveyHistory implements Serializable {

	private static final long serialVersionUID = 2470143867447105097L;
	private ParticipantEntity participant;
	private String page;
	private Date timestamp;

	public SurveyHistory() {
		super();
	}
	
//  public SurveyHistory(final ParticipantEntity participant, final String page) {
//  this();
//  this.participant = participant;
//  this.page = page;
//}

	public ParticipantEntity getParticipant() {
		return participant;
	}

	public void setParticipant(ParticipantEntity participant) {
		this.participant = participant;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((page == null) ? 0 : page.hashCode());
		result = prime * result
				+ ((participant == null) ? 0 : participant.hashCode());
		result = prime * result
				+ ((timestamp == null) ? 0 : timestamp.hashCode());
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
		SurveyHistory other = (SurveyHistory) obj;
		if (page == null) {
			if (other.page != null)
				return false;
		} else if (!page.equals(other.page))
			return false;
		if (participant == null) {
			if (other.participant != null)
				return false;
		} else if (!participant.equals(other.participant))
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		return true;
	}
	


	
	
}

package model;

import java.io.Serializable;
import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * The Class SurveyHistory.
 */
public class SurveyHistory implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2470143867447105097L;

	/** The participant. */
	private ParticipantEntity participant;
	
	private String id;

	/** The page. */
	private String page;

	/** The timestamp. */
	private Date timestamp;

	/**
	 * Instantiates a new survey history.
	 */
	public SurveyHistory() {
		super();
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the participant.
	 * 
	 * @return the participant
	 */
	public ParticipantEntity getParticipant() {
		return this.participant;
	}

	/**
	 * Sets the participant.
	 * 
	 * @param participant
	 *            the new participant
	 */
	public void setParticipant(final ParticipantEntity participant) {
		this.participant = participant;
	}

	/**
	 * Gets the page.
	 * 
	 * @return the page
	 */
	public String getPage() {
		return this.page;
	}

	/**
	 * Sets the page.
	 * 
	 * @param page
	 *            the new page
	 */
	public void setPage(final String page) {
		this.page = page;
	}

	/**
	 * Gets the timestamp.
	 * 
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return this.timestamp;
	}

	/**
	 * Sets the timestamp.
	 * 
	 * @param timestamp
	 *            the new timestamp
	 */
	public void setTimestamp(final Date timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((page == null) ? 0 : page.hashCode());
		result = prime * result + ((participant == null) ? 0 : participant.hashCode());
		result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
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

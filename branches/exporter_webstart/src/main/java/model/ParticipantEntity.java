package model;

import java.io.Serializable;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class ParticipantEntity.
 */
public class ParticipantEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3758524766842419279L;

	/** The id. */
	private String id;

	/** The token. */
	private String token;

	/** The password. */
	private String password;

	/** The survey data. */
	private Map<String, SurveyData> surveyData;

	/**
	 * Instantiates a new participant entity.
	 */
	public ParticipantEntity() {
		super();
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(final String id) {
		this.id = id;
	}

	/**
	 * Gets the token.
	 * 
	 * @return the token
	 */
	public String getToken() {
		return this.token;
	}

	/**
	 * Sets the token.
	 * 
	 * @param token
	 *            the new token
	 */
	public void setToken(final String token) {
		this.token = token;
	}

	/**
	 * Gets the password.
	 * 
	 * @return the password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Sets the password.
	 * 
	 * @param password
	 *            the new password
	 */
	public void setPassword(final String password) {
		this.password = password;
	}

	/**
	 * Gets the survey data.
	 * 
	 * @return the survey data
	 */
	public Map<String, SurveyData> getSurveyData() {
		return this.surveyData;
	}

	/**
	 * Sets the survey data.
	 * 
	 * @param surveyData
	 *            the survey data
	 */
	public void setSurveyData(final Map<String, SurveyData> surveyData) {
		this.surveyData = surveyData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = prime * result + ((this.password == null) ? 0 : this.password.hashCode());
		result = prime * result + ((this.surveyData == null) ? 0 : this.surveyData.hashCode());
		result = prime * result + ((this.token == null) ? 0 : this.token.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		final ParticipantEntity other = (ParticipantEntity) obj;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		if (this.password == null) {
			if (other.password != null)
				return false;
		} else if (!this.password.equals(other.password))
			return false;
		if (this.surveyData == null) {
			if (other.surveyData != null)
				return false;
		} else if (!this.surveyData.equals(other.surveyData))
			return false;
		if (this.token == null) {
			if (other.token != null)
				return false;
		} else if (!this.token.equals(other.token))
			return false;
		return true;
	}

}

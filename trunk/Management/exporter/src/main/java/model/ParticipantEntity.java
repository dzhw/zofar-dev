package model;

import java.io.Serializable;
import java.util.Map;

public class ParticipantEntity implements Serializable {

	private static final long serialVersionUID = 3758524766842419279L;
	private String id;
	private String token;
	private String password;
//	private Map<String, SurveyData> surveyData;
	

	public ParticipantEntity() {
		super();
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


//	public Map<String, SurveyData> getSurveyData() {
//		return surveyData;
//	}
//
//
//	public void setSurveyData(Map<String, SurveyData> surveyData) {
//		this.surveyData = surveyData;
//	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
//		result = prime * result
//				+ ((surveyData == null) ? 0 : surveyData.hashCode());
		result = prime * result + ((token == null) ? 0 : token.hashCode());
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
		ParticipantEntity other = (ParticipantEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
//		if (surveyData == null) {
//			if (other.surveyData != null)
//				return false;
//		} else if (!surveyData.equals(other.surveyData))
//			return false;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		return true;
	}

	
	

}

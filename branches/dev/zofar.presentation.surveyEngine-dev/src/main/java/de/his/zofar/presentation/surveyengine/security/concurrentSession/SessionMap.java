package de.his.zofar.presentation.surveyengine.security.concurrentSession;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SessionMap implements Serializable {

	private static final long serialVersionUID = -8875862762825577506L;

    @Id
    private String token;

    private String sessionId;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
}

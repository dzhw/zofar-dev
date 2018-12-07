package de.his.zofar.presentation.surveyengine.security.concurrentSession;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SessionMap implements Serializable {

	private static final long serialVersionUID = -8875862762825577506L;

    @Id
    private String token;

    private String sessionId;
    
    @Column(name = "MODIFIED_DATE", insertable=true,updatable=true)
    private Date modifiedDate;
 
    @Column(name = "CREATED_DATE", insertable=true,updatable=false)
    private Date createdDate;

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
	
	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
}

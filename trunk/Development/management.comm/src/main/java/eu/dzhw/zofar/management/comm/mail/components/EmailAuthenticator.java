package eu.dzhw.zofar.management.comm.mail.components;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class EmailAuthenticator extends Authenticator {

	private String user;
	private String pw;

	public EmailAuthenticator (String username, String password) {
		super();
		this.user = username;
		this.pw = password;
	}

	public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(user, pw);
	}

}

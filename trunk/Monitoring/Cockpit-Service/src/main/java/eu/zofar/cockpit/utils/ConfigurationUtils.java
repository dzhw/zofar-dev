package eu.zofar.cockpit.utils;



public class ConfigurationUtils extends eu.dzhw.zofar.management.utils.system.ConfigurationUtils{
	
	/** The instance. */
	private static ConfigurationUtils INSTANCE;
	
	/**
	 * Gets the single instance of ConfigurationUtils.
	 *
	 * @return single instance of ConfigurationUtils
	 */
	public static ConfigurationUtils getInstance() {
		if (INSTANCE == null)
			INSTANCE = new ConfigurationUtils();
		return INSTANCE;
	}
	
	public String getSurveyId() {
		String back = this.getSystemConfiguration().getProperty("survey_id");
		if (back == null)
			back = "UNKOWN";
		return back;
	}

	public String getMasterId() {
		String back = this.getSystemConfiguration().getProperty("master_id");
		if (back == null)
			back = "UNKOWN";
		return back;
	}

	public String getManualClients(final String surveyId) {
		String back = this.getSystemConfiguration().getProperty(
				"manual_clients_" + surveyId);
		if (back == null)
			back = "";
		return back;
	}

	public Long getRefreshStatus() {
		String back = this.getSystemConfiguration().getProperty(
				"refresh_status");
		if (back == null)
			back = "0";
		return Long.parseLong(back) * 1000;
	}

	public String getMasterProtocol() {
		String back = this.getSystemConfiguration().getProperty(
				"master_protocol");
		if (back == null)
			back = "http";
		return back;
	}

	public String getMasterHost() {
		String back = this.getSystemConfiguration().getProperty("master_host");
		if (back == null)
			back = "UNKOWN";
		return back;
	}

	public String getMasterPort() {
		String back = this.getSystemConfiguration().getProperty("master_port");
		if (back == null)
			back = "8080";
		return back;
	}

	public String getMasterAppname() {
		String back = this.getSystemConfiguration().getProperty(
				"master_appname");
		if (back == null)
			back = "UNKOWN";
		return back;
	}

	public String getClientProtocol() {
		String back = this.getSystemConfiguration().getProperty(
				"client_protocol");
		if (back == null)
			back = "http";
		return back;
	}

	public String getClientPort() {
		String back = this.getSystemConfiguration().getProperty("client_port");
		if (back == null)
			back = "8080";
		return back;
	}

	public String getClientAppname() {
		String back = this.getSystemConfiguration().getProperty(
				"client_appname");
		if (back == null)
			back = "UNKOWN";
		return back;
	}

	public Integer getTimeout() {
		String back = this.getSystemConfiguration().getProperty("timeout");
		if (back == null)
			back = "0";
		return Integer.parseInt(back);
	}

	public Long getRefreshRegistration() {
		String back = this.getSystemConfiguration().getProperty(
				"refresh_registration");
		if (back == null)
			back = "0";
		return Long.parseLong(back);
	}
	
	public String getWebService() {
		String back = this.getSystemConfiguration().getProperty(
				"cockpit_ws_url");
		if (back == null)
			back = "http://localhost/";
		return back;
	}
	
	

}

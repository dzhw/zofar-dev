package eu.zofar.cockpit.master.ui;

import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.zofar.cockpit.master.objects.ServerEntry;
import eu.zofar.cockpit.master.objects.SurveyEntry;
import eu.zofar.cockpit.master.service.MasterProxy;
import eu.zofar.cockpit.service.StatusService;
import eu.zofar.cockpit.service.transfer.ClientTransfer;
import eu.zofar.cockpit.service.transfer.RegisterTransfer;
import eu.zofar.cockpit.utils.ConfigurationUtils;

//@ManagedBean(eager=true)
//@ApplicationScoped
public class ClientManager implements Serializable, MasterProxy {

	private static final long serialVersionUID = -9198016402330607899L;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ClientManager.class);

	private class ClientData implements Serializable {
		private static final long serialVersionUID = 7431265281392896673L;

		private long lastContact;
		private final String address;

		private final String surveyId;
		private ClientTransfer data;

		public ClientData(final String address, final String surveyId) {
			super();
			this.lastContact = -1L;
			this.address = address;
			this.surveyId = surveyId;
		}

		public ClientTransfer getData() {
			return this.data;
		}

		public void setData(final ClientTransfer data) {
			this.lastContact = System.currentTimeMillis();
			this.data = data;
		}

		public long getLastContact() {
			return this.lastContact;
		}

		public String getAddress() {
			return this.address;
		}

		public String getSurveyId() {
			return this.surveyId;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + this.getOuterType().hashCode();
			result = prime * result
					+ ((this.address == null) ? 0 : this.address.hashCode());
			result = prime * result
					+ ((this.surveyId == null) ? 0 : this.surveyId.hashCode());
			return result;
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof ClientData))
				return false;
			final ClientData other = (ClientData) obj;
			if (!this.getOuterType().equals(other.getOuterType()))
				return false;
			if (this.address == null) {
				if (other.address != null)
					return false;
			} else if (!this.address.equals(other.address))
				return false;
			if (this.surveyId == null) {
				if (other.surveyId != null)
					return false;
			} else if (!this.surveyId.equals(other.surveyId))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "ClientData [getData()=" + this.getData()
					+ ", getLastContact()=" + this.getLastContact()
					+ ", getAddress()=" + this.getAddress()
					+ ", getSurveyId()=" + this.getSurveyId() + "]";
		}

		private ClientManager getOuterType() {
			return ClientManager.this;
		}

	};

	private Map<String, ClientData> clients;
	private Map<String, List<String>> surveyClientMap;

	public ClientManager() {
		super();
	}

	private String getKey(final ClientData client) {
		if (client == null)
			return null;
		return client.getSurveyId() + "_" + client.getAddress();
	}

	private String getKey(final String surveyId, final String address) {
		if (surveyId == null)
			return null;
		if (address == null)
			return null;
		if (surveyId.equals(""))
			return null;
		if (address.equals(""))
			return null;
		return surveyId + "_" + address;
	}

	public List<SurveyEntry> getSurveys(final List<String> survey_ids) {
		LOGGER.info("get Surveys {}", survey_ids);
		for (final String survey_id : survey_ids) {
			this.forceRegistration(survey_id);
			this.update(survey_id, ConfigurationUtils.getInstance()
					.getRefreshStatus());
		}

		if (this.surveyClientMap == null) {
			this.surveyClientMap = new HashMap<String, List<String>>();
		}
		final List<SurveyEntry> back = new ArrayList<SurveyEntry>();
		for (final String survey_id : survey_ids) {
			if (!this.surveyClientMap.containsKey(survey_id))
				continue;
			final List<String> clientIds = this.surveyClientMap.get(survey_id);
			if (clientIds == null)
				continue;
			if (clientIds.isEmpty())
				continue;

			final SurveyEntry survey = new SurveyEntry(survey_id);
			final Iterator<String> it = clientIds.iterator();
			while (it.hasNext()) {
				final String clientId = it.next();
				final ClientData client = this.clients.get(clientId);
				if (client == null)
					continue;
				if (!client.getSurveyId().equals(survey_id))
					continue;
				final ServerEntry server = new ServerEntry(survey_id,
						client.getAddress());
				server.setLastContact(client.getLastContact());

				if (client.getData() != null) {
					server.setExitPages(client.getData().getExitPages());
					server.setFinished(client.getData().getFinished());
					server.setParticipated(client.getData().getParticipated());
					server.setHealthStatus(client.getData().getSystem());
				}
				survey.getServer().add(server);
			}
			back.add(survey);
		}
		return back;
	}

	@Override
	public RegisterTransfer register(final String survey_id,
			final HttpServletRequest request) {
		LOGGER.info("register ({}) : {}", this.hashCode(), survey_id);

		if (this.clients == null) {
			this.clients = new HashMap<String, ClientData>();
		}
		if (this.surveyClientMap == null) {
			this.surveyClientMap = new HashMap<String, List<String>>();
		}

		final String remoteAddress = request.getRemoteAddr();
		final String remoteHost = request.getRemoteHost();
		final int remotePort = request.getRemotePort();

		final RegisterTransfer back = new RegisterTransfer();
		back.setTimestamp(System.currentTimeMillis());
		back.setId(ConfigurationUtils.getInstance().getMasterId());
		back.setSurvey(survey_id);
		back.setAddress(remoteAddress);
		back.setHost(remoteHost);
		back.setPort(remotePort);

		final String key = this.getKey(survey_id, remoteAddress);

		if (this.clients.containsKey(key)) {
			LOGGER.info("Client still alive ({}) {}", key,
					this.clients.get(key));
		} else {
			final ClientData clientData = new ClientData(remoteAddress,
					survey_id);
			this.clients.put(key, clientData);
			if (!this.surveyClientMap.containsKey(survey_id))
				this.surveyClientMap.put(survey_id, new ArrayList<String>());
			this.surveyClientMap.get(survey_id).add(key);
			LOGGER.info("Client registered ({}): {}", key, clientData);
		}
		return back;
	}

	@Override
	public RegisterTransfer forceRegistration(final String survey_id) {
		final String clientList = ConfigurationUtils.getInstance()
				.getManualClients(survey_id);
		if (clientList != null) {
			final String[] manualClients = clientList.split(",");
			if (manualClients != null) {
				if (manualClients.length == 0)
					return null;

				for (String manualClient : manualClients) {
					manualClient = manualClient.trim();
					if (manualClient.equals(""))
						continue;
					try {
						final URL url = new URL(null, ConfigurationUtils
								.getInstance().getClientProtocol()
								+ "://"
								+ manualClient
								+ ":"
								+ ConfigurationUtils.getInstance()
										.getClientPort()
								+ "/"
								+ ConfigurationUtils.getInstance()
										.getClientAppname()
								+ "/statusService?wsdl",
								new URLStreamHandler() {
									@Override
									protected URLConnection openConnection(
											final URL url) throws IOException {
										final URL clone_url = new URL(url
												.toString());
										final HttpURLConnection clone_urlconnection = (HttpURLConnection) clone_url
												.openConnection();
										// TimeOut settings
										clone_urlconnection
												.setConnectTimeout(ConfigurationUtils
														.getInstance()
														.getTimeout());
										clone_urlconnection
												.setReadTimeout(ConfigurationUtils
														.getInstance()
														.getTimeout());
										return (clone_urlconnection);
									}
								});

						this.forceRegistrationHelper(survey_id, url);
					} catch (final MalformedURLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

	private void forceRegistrationHelper(final String survey_id,
			final URL client) {
		if (this.clients == null) {
			this.clients = new HashMap<String, ClientData>();
		}
		if (this.surveyClientMap == null) {
			this.surveyClientMap = new HashMap<String, List<String>>();
		}

		final String remoteAddress = client.getHost();
		final String remoteHost = client.getHost();
		final int remotePort = client.getPort();

		final RegisterTransfer back = new RegisterTransfer();
		back.setTimestamp(System.currentTimeMillis());
		back.setId(ConfigurationUtils.getInstance().getMasterId());
		back.setSurvey(survey_id);
		back.setAddress(remoteAddress);
		back.setHost(remoteHost);
		back.setPort(remotePort);

		final String key = this.getKey(survey_id, remoteAddress);

		if (this.clients.containsKey(key)) {
			LOGGER.info("forced Client still alive ({}) {}", key,
					this.clients.get(key));
		} else {
			final ClientData clientData = new ClientData(remoteAddress,
					survey_id);
			this.clients.put(key, clientData);
			if (!this.surveyClientMap.containsKey(survey_id))
				this.surveyClientMap.put(survey_id, new ArrayList<String>());
			this.surveyClientMap.get(survey_id).add(key);
			LOGGER.info("forced Client registered ({}): {}", key, clientData);
		}
	}

	private void update(final String survey_id, final long limit) {
		LOGGER.info("update {}", survey_id);
		if (this.clients == null) {
			this.clients = new HashMap<String, ClientData>();
		}
		if (this.surveyClientMap == null) {
			this.surveyClientMap = new HashMap<String, List<String>>();
			LOGGER.info("init surveyClientMap 1");
		}
		if (!this.surveyClientMap.containsKey(survey_id))
			return;
		final List<String> clientIds = this.surveyClientMap.get(survey_id);
		if (clientIds == null)
			return;
		if (clientIds.isEmpty())
			return;

		final Iterator<String> it = clientIds.iterator();
		final long now = System.currentTimeMillis();
		while (it.hasNext()) {
			final String clientId = it.next();
			final ClientData client = this.clients.get(clientId);
			if (client == null)
				continue;
			if ((client.getLastContact() < (now - limit))) {
				final ClientTransfer tmp = this.requestStatus(client);
				if (tmp == null)
					this.removeClient(client);
				else {
					client.setData(tmp);
					this.clients.put(clientId, client);
				}
			} else {
				LOGGER.info("Data fresh enough for {}", clientId);
			}

			// if (clients.get(survey_id).add(requestStatus(client))) {
			// removeClient(client);
			// }
			// if ((client.getLastContact() < (now - limit))) {
			// if (!requestStatus(client)) {
			// removeClient(client);
			// }
			// } else {
			// // Data fresh enough
			// }

		}
	}

	private ClientTransfer requestStatus(final ClientData client) {
		LOGGER.info("request Status {}", client);
		try {
			final URL url = new URL(null, ConfigurationUtils.getInstance()
					.getClientProtocol()
					+ "://"
					+ client.getAddress()
					+ ":"
					+ ConfigurationUtils.getInstance().getClientPort()
					+ "/"
					+ ConfigurationUtils.getInstance().getClientAppname()
					+ "/statusService?wsdl", new URLStreamHandler() {
				@Override
				protected URLConnection openConnection(final URL url)
						throws IOException {
					final URL clone_url = new URL(url.toString());
					final HttpURLConnection clone_urlconnection = (HttpURLConnection) clone_url
							.openConnection();
					// TimeOut settings
					clone_urlconnection.setConnectTimeout(ConfigurationUtils
							.getInstance().getTimeout());
					clone_urlconnection.setReadTimeout(ConfigurationUtils
							.getInstance().getTimeout());
					return (clone_urlconnection);
				}
			});
			final QName qname = new QName(
					"http://impl.service.cockpit.zofar.eu/", "ClientService");
			final Service service = Service.create(url, qname);
			final StatusService statusService = service
					.getPort(StatusService.class);

			final ClientTransfer clientTransfer = statusService.status();
			LOGGER.info("ClientTransfer received : {}", clientTransfer);
			if (clientTransfer != null) {
				return clientTransfer;
			}
		} catch (final MalformedURLException e) {
			e.printStackTrace();
		} catch (final WebServiceException e) {
			LOGGER.info("Webservice call timed out");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	private void removeClient(final ClientData client) {
		final String key = this.getKey(client);
		LOGGER.info("Client do not answer => removed : {}", client);
		this.clients.remove(key);
	}
}

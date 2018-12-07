package eu.zofar.cockpit.service.ui;

import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.zofar.cockpit.service.RegisterService;
import eu.zofar.cockpit.service.transfer.RegisterTransfer;
import eu.zofar.cockpit.utils.ConfigurationUtils;

@ManagedBean(eager = true)
@ApplicationScoped
public class Initialisation implements Serializable {

	private static final long serialVersionUID = -3556693777022367061L;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(Initialisation.class);

	private RegisterTransfer registerTransfer;

	private ScheduledExecutorService scheduler;

	private class ReregisterJob implements Runnable {

		@Override
		public void run() {
			LOGGER.info("trigger timer");
			Initialisation.this.doRegistration();
		}

	}

	public Initialisation() {
		super();
	}

	@PostConstruct
	public void register() {
		LOGGER.info("register at Cockpit Master Client {}", this.hashCode());
		this.registerTransfer = this.doRegistration();
		if (this.registerTransfer != null) {
			LOGGER.info("RegisterTransfer received : {}", this.registerTransfer);
			if (this.scheduler == null) {
				LOGGER.info("start scheduler");
				this.scheduler = Executors.newSingleThreadScheduledExecutor();
				this.scheduler.scheduleAtFixedRate(new ReregisterJob(), 0,
						ConfigurationUtils.getInstance()
								.getRefreshRegistration(), TimeUnit.SECONDS);
			}

		}
	}

	private RegisterTransfer doRegistration() {
		try {
			final URL url = new URL(null, ConfigurationUtils.getInstance()
					.getMasterProtocol()
					+ "://"
					+ ConfigurationUtils.getInstance().getMasterHost()
					+ ":"
					+ ConfigurationUtils.getInstance().getMasterPort()
					+ "/"
					+ ConfigurationUtils.getInstance().getMasterAppname()
					+ "/register?wsdl", new URLStreamHandler() {
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
					"http://impl.service.master.cockpit.zofar.eu/",
					"MasterService");
			final Service service = Service.create(url, qname);
			final RegisterService registerService = service
					.getPort(RegisterService.class);
			return registerService.register(ConfigurationUtils.getInstance()
					.getSurveyId());

		} catch (final MalformedURLException e) {
			e.printStackTrace();
		} catch (final WebServiceException e) {
			LOGGER.info("Webservice call timed out");
		}
		return null;
	}

	@PreDestroy
	public void destroy() {
		if (this.scheduler != null)
			this.scheduler.shutdownNow();
	}

}

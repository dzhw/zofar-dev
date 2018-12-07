package eu.zofar.cockpit.client.standalone;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.zofar.cockpit.service.StatusService;
import eu.zofar.cockpit.utils.ConfigurationUtils;

public class MainApp {

	private static final Logger LOGGER = LoggerFactory.getLogger(MainApp.class);

	public static void main(final String[] args) {
		try {
			final URL url = new URL(
					null,
					ConfigurationUtils.getInstance().getWebService(),
					new URLStreamHandler() {
						@Override
						protected URLConnection openConnection(final URL url)
								throws IOException {
							final URL clone_url = new URL(url.toString());
							final HttpURLConnection clone_urlconnection = (HttpURLConnection) clone_url
									.openConnection();
							// TimeOut settings
							clone_urlconnection.setConnectTimeout(10);
							clone_urlconnection.setReadTimeout(10);
							return (clone_urlconnection);
						}
					});

			final QName qname = new QName("http://impl.service.cockpit.zofar.eu/", "ClientService");
			final Service service = Service.create(url, qname);
			LOGGER.info("retrieve Service");
			final StatusService statusService = service
					.getPort(StatusService.class);
			LOGGER.info("Service found");
			LOGGER.info("{}",statusService.status());
		} catch (final Throwable e) {
			e.printStackTrace();
		}

	}

}

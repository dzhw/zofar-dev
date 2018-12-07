package eu.dzhw.zofar.management.generator.preloads;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.dzhw.zofar.management.security.text.TextCipherClient;
import eu.dzhw.zofar.management.utils.files.CSVClient;

public class PreloadClient {

	private static final PreloadClient INSTANCE = new PreloadClient();
	private final static String PRELOAD_PREFIX = "PRELOAD";

	private static final Logger LOGGER = LoggerFactory.getLogger(PreloadClient.class);
	public static enum FIELDS{
		PARTICIPANT,DATA,QML;
	}
	
	private PreloadClient() {
		super();
	}

	public static PreloadClient getInstance() {
		return INSTANCE;
	}

	public Map<FIELDS, String> csv2sql(final File csv) throws IOException {
		final CSVClient cvsClient = CSVClient.getInstance();
		ArrayList<String> headers = cvsClient.getCSVHeaders(csv);
		System.out.println("retrieved Headers : "+headers);
		return csv2sql(csv, headers);
	}

	public Map<FIELDS, String> csv2sql(final File csv, final ArrayList<String> headers) throws IOException {
		TextCipherClient encoder = TextCipherClient.getInstance();
		final CSVClient cvsClient = CSVClient.getInstance();
		final List<Map<String, String>> csvData = cvsClient.loadCSV(csv, headers, true);

		if (csvData != null) {
			final StringBuffer participant = new StringBuffer();
			final StringBuffer data = new StringBuffer();
			final StringBuffer qml = new StringBuffer();
			
			for (final Map<String, String> row : csvData) {
				final String token = row.get("token");
				participant.append("INSERT INTO participant(id, version, password, token) VALUES (nextval('seq_participant_id'),0,'" + encoder.encodeSHA(token) + "','" + token + "');\n");
				qml.append("<zofar:preload password=\"" + token + "\" name=\"" + token + "\">\n");

				final Iterator<String> variableIt = headers.iterator();
				while (variableIt.hasNext()) {
					String variable = variableIt.next();
					if (variable.equals("token"))
						continue;
					if (variable.equals("url"))
						continue;
					if (variable.startsWith(PRELOAD_PREFIX))
						variable = variable.substring(PRELOAD_PREFIX.length());

					final String value = row.get(variable);
					if (value != null){
						data.append("INSERT INTO surveydata(id, version, value, variablename, participant_id) VALUES (nextval('seq_surveydata_id'), 0, '" + value + "', '" + PRELOAD_PREFIX + variable + "', (select DISTINCT id from participant where token = '" + token + "'));\n");
						qml.append("\t<zofar:preloadItem variable=\""+variable+"\" value=\"" + value + "\"></zofar:preloadItem>\n");
					}
				}
				qml.append("</zofar:preload>\n");
			}
			final Map<FIELDS, String> back = new HashMap<FIELDS, String>();
			back.put(FIELDS.PARTICIPANT, participant.toString());
			back.put(FIELDS.DATA, data.toString());
			back.put(FIELDS.QML, qml.toString());
			return back;
		}

		return null;
	}

}

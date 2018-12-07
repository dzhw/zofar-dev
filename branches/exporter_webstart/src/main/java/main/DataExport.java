package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.HeaderEntry;
import model.ValueEntry;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NodeList;

import service.counting.CountingService;
import service.data.DataService;
import service.statistics.StatisticService;
import service.statistics.StatisticService.TYPE;
import de.his.export.xml.export.DataType;
//import de.his.zofar.persistence.surveyengine.entities.ParticipantEntity;
//import de.his.zofar.service.surveyengine.model.SurveyHistory;
import de.his.export.xml.export.ParticipantType;
import eu.dzhw.zofar.management.utils.files.FileClient;
import eu.dzhw.zofar.management.utils.xml.XmlClient;

// TODO: Auto-generated Javadoc
/**
 * The Class DataExport.
 */
@Deprecated
public class DataExport {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataExport.class);

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 * @throws Throwable 
	 * @throws NumberFormatException 
	 */
	public static void main(final String[] args) throws NumberFormatException, Throwable {
		final Configuration conf = new Configuration();
		final String url = "jdbc:postgresql://xxx:5432/xxx";
		final StatisticService service = StatisticService.getInstance();

		conf.setProperty("connection.url", url);
		conf.setProperty("hibernate.connection.url", url);
		conf.setProperty("connection.username", "xxx");
		conf.setProperty("connection.password", "xxx");
		conf.setProperty("hibernate.connection.username", "xxx");
		conf.setProperty("hibernate.connection.password", "xxx");

		final Map<String, String> mapping = new HashMap<String, String>();
		mapping.put("true", "1");
		mapping.put("false", "0");

		mapping.put("NOT ANSWERED", "-1");
		mapping.put("UNSELECTED", "-2");
		mapping.put("EMPTY", "-2");
		mapping.put("UNSET", "-2");
		mapping.put("NOT CALCULATED", "-2");
		mapping.put("NOT SEEN", "-3");

		final File certFile = new File("xxx/openssl/export.pem ");

		final String packetSize = "1250";

		//globals
		final String qmlPath = "xxx/src/main/resources/questionnaire.xml";
		final NodeList usingNodes = XmlClient.getInstance().getByXPath(qmlPath, "//*[@variable]");
		final Map<String, String> missings = DataService.getInstance().getMissings(qmlPath,usingNodes);
		final Map<String, Map<String, String>> values = DataService.getInstance().getValues(qmlPath,usingNodes);
		final Map<String, List<String>> variablePages = DataService.getInstance().retrievePages(qmlPath,usingNodes);
		final Map<String, List<List<String>>> variableVisibles = DataService.getInstance().retrieveVisibles(qmlPath,usingNodes);
		final Map<String, String> types = DataService.getInstance().getVariableTypes(qmlPath,usingNodes);
		final Map<String, List<String>> pageTree = DataService.getInstance().pageTree(qmlPath);

		
		final List<String> pages = service.getPages(qmlPath);

		final List<String> filteredVariables = new ArrayList<String>();
		for (final String page : pages) {
			filteredVariables.add("comment" + page);
		}
		final Map<String, Set<String>> questions = CountingService.getInstance().getQuestions(qmlPath, filteredVariables,usingNodes);
		int packetCount = service.getPacketCount(conf, Integer.parseInt(packetSize));
		System.out.println(packetCount + " Packets found");

		final File exportDir = new File("xxx/export");
		final File dataFile = new File(exportDir, "data.csv");

		// final Map<String, Object> csvPackage = new HashMap<String, Object>();
		// Map<String, Object> exportCsvData = null;
		final Map<HeaderEntry, Map<ValueEntry, Integer>> counting = new LinkedHashMap<HeaderEntry, Map<ValueEntry, Integer>>();
		Map<String, Integer> exitPages = new HashMap<String, Integer>();
		final Map<String, Map<String, ValueEntry>> options = CountingService.getInstance().getOptions(qmlPath, filteredVariables,usingNodes);
		boolean first = true;
		for (int index = 1; index <= packetCount; index++) {
			final Map<TYPE, Object> packetData = service.createPacket(qmlPath, filteredVariables, options,conf, index, Integer.parseInt(packetSize), "-3",false,usingNodes,missings,values,variablePages,variableVisibles,types,pageTree,questions);
			if (packetData != null) {
				// Merge countings
				final Map<HeaderEntry, Map<ValueEntry, Integer>> tmp = (Map<HeaderEntry, Map<ValueEntry, Integer>>) packetData.get(TYPE.counting);

				try {
					format(dataFile, (Set<ParticipantType>) packetData.get(TYPE.data), ',', '\n', mapping, !first);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				first = false;
			}
		}


		System.exit(0);
	}

	public static void format(final File toAppend, final Set<ParticipantType> data, final char fieldSeparator, final char rowSeparator, final Map<String, String> mapping, final boolean ignoreFirst) throws IOException {
		final Iterator<ParticipantType> rowIt = data.iterator();

		final StringBuffer dataBack = new StringBuffer();

		List<String> columns = null;
		while (rowIt.hasNext()) {
			final ParticipantType entry = rowIt.next();
			boolean skip = false;
			// convert data to map
			final Map<String, String> answerMap = new LinkedHashMap<String, String>();
			answerMap.put("id", entry.getId());
			answerMap.put("token", entry.getToken());

			for (final DataType inputDataEntry : entry.getDataset().getDataArray()) {
				String value = inputDataEntry.getStringValue();
				final String type = inputDataEntry.getType();
				if ((type != null) && (type.equals("string"))) {
					value = value.substring(0, Math.min(243, value.length()));
					value = stripBreaks("" + value + "", fieldSeparator, rowSeparator);
				}
				answerMap.put(inputDataEntry.getVariable(), value);
				if (inputDataEntry.getVariable().equals("lastPage") && (value.equals("UNKOWN")))
					skip = true;
			}
			if ((columns == null)) {
				columns = new ArrayList<String>();
				final Iterator<String> colIt = answerMap.keySet().iterator();
				while (colIt.hasNext()) {
					final String col = colIt.next();
					columns.add(col);
					if (!ignoreFirst) {
						dataBack.append("\"" + col + "\"");
						if (colIt.hasNext())
							dataBack.append(fieldSeparator);
					}
				}
				if (!ignoreFirst)
					dataBack.append(rowSeparator);
			}
			if (skip)
				continue;
			final Iterator<String> colIt = columns.iterator();
			while (colIt.hasNext()) {
				final String col = colIt.next();
				Object value = answerMap.get(col);
				if ((mapping != null) && (mapping.containsKey(value + "")))
					value = mapping.get(value + "");
				String tmp = value + "";
				tmp = tmp.replace('\"', '\'');
				tmp = tmp.replace('#', ' ');
				tmp = stripBreaks("" + tmp + "", fieldSeparator, rowSeparator);
				dataBack.append("\"" + tmp + "\"");
				if (colIt.hasNext())dataBack.append(fieldSeparator);
			}
			dataBack.append(rowSeparator);
		}

		if (!dataBack.toString().equals(""))
			FileClient.getInstance().writeToFile(toAppend.getAbsolutePath(), dataBack.toString() + "\n", true);
	}

	private static String stripBreaks(final String text, final char fieldSeparator, final char rowSeparator) {
		String back = text.replaceAll("\n", " ");
		back = back.replaceAll("\r", "");
		back = back.replaceAll("\t", " ");
		back = back.replaceAll(fieldSeparator + "", "#");
		back = back.replaceAll(rowSeparator + "", "#");
		back = back.replaceAll(" {2,}", " ");
		return back;
	}

}

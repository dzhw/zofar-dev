package eu.dzhw.zofar.management.utils.files.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.LineIterator;

import eu.dzhw.zofar.management.utils.files.CSVClient;
import eu.dzhw.zofar.management.utils.files.FileClient;
import eu.dzhw.zofar.management.utils.objects.CollectionClient;
import eu.dzhw.zofar.management.utils.string.ReplaceClient;
import net.sf.uadetector.ReadableUserAgent;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.service.UADetectorServiceFactory;

public class UserAgentAnalysis {

	public static void main(String[] args) throws IOException {
		final File basedir = new File("xxx");
		final File logFile = new File(basedir, "xxx.log");
		final File agentsFile = new File(basedir, "agents.csv");
		final File countingDir = new File(basedir, "countings");

		final FileClient fileClient = FileClient.getInstance();
		final ReplaceClient replaceClient = ReplaceClient.getInstance();
		final UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();

		// 'survey';'token';'agent_cat';'agent_name';'agent_version';'agent_os';'raw'
		List<String> headers = new ArrayList<String>();
		headers.add("survey");
		headers.add("token");
		headers.add("agent_cat");
		headers.add("agent_name");
		headers.add("agent_version");
		headers.add("agent_os");
		headers.add("raw");

		final boolean stage1 = false;
		final boolean stage2 = false;
		final boolean stage3 = true;

		if (stage1) {
			// analysis to file
			final LineIterator it = fileClient.iterateContent(logFile);

			final String startPattern = "(HTTP/[0-9]\\.[0-9] *([0-9]*) *([0-9]*) *(-? *)?)";
			final String stopPattern = "JSESSIONID=([^ ]*)";

			final String surveyPattern = "GET /([^/]*)";
			final String tokenPattern = "zofar_token=([a-zA-Z0-9]*)";

			final Map<String, Map<String, String>> survey_session_token = new LinkedHashMap<String, Map<String, String>>();
			try {
				fileClient.writeToFile(agentsFile.getAbsolutePath(), "'survey';'token';'agent_cat';'agent_name';'agent_version';'agent_os';'raw';\n", false);
			} catch (IOException e) {
				e.printStackTrace();
			}

			while (it.hasNext()) {
				final String line = it.nextLine();
				final List<String> starts = replaceClient.findInString(startPattern, line);
				final List<String> stops = replaceClient.findInString(stopPattern, line, true);
				List<String> tokens = replaceClient.findInString(tokenPattern, line);
				final List<String> cleanTokens = new ArrayList<String>();
				for (final String token : tokens) {
					cleanTokens.add(token.replaceFirst("zofar_token=", ""));
				}
				tokens = cleanTokens;

				List<String> surveys = replaceClient.findInString(surveyPattern, line);
				final List<String> cleanSurvey = new ArrayList<String>();
				for (final String survey : surveys) {
					String tmp = survey.replaceFirst("GET ?/", "");
					final int indexOfSlash = tmp.indexOf('/');
					if (indexOfSlash > -1)
						tmp = tmp.substring(0, indexOfSlash);
					cleanSurvey.add(tmp);
				}
				surveys = cleanSurvey;

				if ((!starts.isEmpty()) && (!stops.isEmpty())) {
					final int start = line.indexOf(starts.get(0));
					final int stop = line.indexOf(stops.get(stops.size() - 1), start);
					String rawAgentStr = "UNKOWN";

					final Map<String, String> agentMap = new HashMap<String, String>();

					if ((start > -1) && (stop > -1) && (start < stop)) {
						rawAgentStr = line.substring(start + starts.get(0).length(), stop);

						final ReadableUserAgent agent = parser.parse(rawAgentStr);
						if (agent != null) {
							agentMap.put("cat", agent.getDeviceCategory().getCategory().getName());
							agentMap.put("name", agent.getName());
							agentMap.put("version", agent.getVersionNumber().getMajor() + "." + agent.getVersionNumber().getMinor());
							agentMap.put("os", agent.getOperatingSystem().getName());
						}
					}

					final String session = (stops.get(0).toUpperCase()).replaceAll("JSESSIONID=", "");

					String currentSurvey = null;
					if ((surveys != null) && (!surveys.isEmpty()))
						currentSurvey = surveys.get(0);

					String currentToken = null;
					if ((tokens != null) && (!tokens.isEmpty()))
						currentToken = tokens.get(0);

					if ((currentSurvey != null) && (currentToken == null) && (session != null)) {
						if (survey_session_token.containsKey(currentSurvey)) {
							if (survey_session_token.get(currentSurvey).containsKey(session)) {
								currentToken = survey_session_token.get(currentSurvey).get(session);
							}
						}
					}

					if ((currentSurvey != null) && (currentToken != null) && (!agentMap.isEmpty())) {
						try {
							// "'survey';'token';'agent_cat';'agent_name';'agent_version';'agent_os';'raw'
							fileClient.writeToFile(agentsFile.getAbsolutePath(), "'" + currentSurvey + "';'" + currentToken + "';'" + agentMap.get("cat") + "';'" + agentMap.get("name") + "';'" + agentMap.get("version") + "';'" + agentMap.get("os") + "';'" + rawAgentStr + "';\n", true);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					if ((currentSurvey != null) && (currentToken != null) && (session != null)) {
						if (!survey_session_token.containsKey(currentSurvey))
							survey_session_token.put(currentSurvey, new LinkedHashMap<String, String>());
						survey_session_token.get(currentSurvey).put(session, currentToken);
					}
				}
			}
		}

		if (stage2) {
			// cleaning
			List<Map<String, String>> data = CSVClient.getInstance().loadCSV(agentsFile, headers, true, ';', '\'');
			final File cleanedAgentsFile = new File(agentsFile.getParentFile(), fileClient.getNameWithoutSuffix(agentsFile) + "_clean." + fileClient.getSuffix(agentsFile));
			try {
				fileClient.writeToFile(cleanedAgentsFile.getAbsolutePath(), "'survey';'token';'agent_cat';'agent_name';'agent_version';'agent_os';'raw';\n", false);
			} catch (IOException e) {
				e.printStackTrace();
			}
			for (final Map<String, String> item : data) {
				final String survey = item.get("survey");
				final String token = item.get("token");
				final String agentCat = item.get("agent_cat");
				final String agentName = item.get("agent_name");
				final String agentVersion = item.get("agent_version");
				final String agentOs = item.get("agent_os");
				final String agentRaw = item.get("raw").trim().replace(';', '#');

				if ((survey == null) || (survey.equals("")))
					continue;
				if ((token == null) || (token.equals("")))
					continue;

				try {
					// "'survey';'token';'agent_cat';'agent_name';'agent_version';'agent_os';'raw'
					String output = "'" + survey + "';'" + token + "';'" + agentCat + "';'" + agentName + "';'" + agentVersion + "';'" + agentOs + "';'" + agentRaw + "';".replaceAll("\n", " ").replaceAll(" {2,}", " ");
					fileClient.writeToFile(cleanedAgentsFile.getAbsolutePath(), output + "\n", true);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			final File oldAgentsFile = new File(agentsFile.getParentFile(), fileClient.getNameWithoutSuffix(agentsFile) + "_old." + fileClient.getSuffix(agentsFile));
			try {
				fileClient.writeToFile(oldAgentsFile.getAbsolutePath(), "", false);
			} catch (IOException e) {
				e.printStackTrace();
			}
			fileClient.copyFile(agentsFile, oldAgentsFile);
			fileClient.copyFile(cleanedAgentsFile, agentsFile);

		}

		if (stage3) {
			// Counting and output
			List<Map<String, String>> data = CSVClient.getInstance().loadCSV(agentsFile, headers, true, ';', '\'');
			final Map<String, Map<String, List<String>>> survey_token_cat = new LinkedHashMap<String, Map<String, List<String>>>();
			final Map<String, Map<String, List<String>>> survey_token_name = new LinkedHashMap<String, Map<String, List<String>>>();
			final Map<String, Map<String, List<String>>> survey_token_version = new LinkedHashMap<String, Map<String, List<String>>>();
			final Map<String, Map<String, List<String>>> survey_token_os = new LinkedHashMap<String, Map<String, List<String>>>();

			final List<String> surveys = new ArrayList<String>();

			for (final Map<String, String> item : data) {
				final String survey = item.get("survey");
				final String token = item.get("token");
				final String agentCat = item.get("agent_cat");
				final String agentName = item.get("agent_name");
				final String agentVersion = item.get("agent_version");
				final String agentOs = item.get("agent_os");

				if (!surveys.contains(survey))
					surveys.add(survey);

				if (!survey_token_cat.containsKey(survey))
					survey_token_cat.put(survey, new LinkedHashMap<String, List<String>>());
				CollectionClient.getInstance().addToMap(survey_token_cat.get(survey), token, agentCat);

				if (!survey_token_name.containsKey(survey))
					survey_token_name.put(survey, new LinkedHashMap<String, List<String>>());
				CollectionClient.getInstance().addToMap(survey_token_name.get(survey), token, agentName);

				if (!survey_token_version.containsKey(survey))
					survey_token_version.put(survey, new LinkedHashMap<String, List<String>>());
				CollectionClient.getInstance().addToMap(survey_token_version.get(survey), token, agentVersion);

				if (!survey_token_os.containsKey(survey))
					survey_token_os.put(survey, new LinkedHashMap<String, List<String>>());
				CollectionClient.getInstance().addToMap(survey_token_os.get(survey), token, agentOs);

			}

			final Map<String, Map<String, Integer>> countingCat = new LinkedHashMap<String, Map<String, Integer>>();

			for (final Map.Entry<String, Map<String, List<String>>> item : survey_token_cat.entrySet()) {
				final String survey = item.getKey();
				final Map<String, List<String>> token_agent = item.getValue();
				for (final Map.Entry<String, List<String>> token_agent_item : token_agent.entrySet()) {
					final List<String> agents = token_agent_item.getValue();
					CollectionClient.getInstance().removeDoubles(agents);
					for (final String agent : agents) {
						if (!countingCat.containsKey(survey))
							countingCat.put(survey, new LinkedHashMap<String, Integer>());
						if (!countingCat.get(survey).containsKey(agent))
							countingCat.get(survey).put(agent, 1);
						else
							countingCat.get(survey).put(agent, countingCat.get(survey).get(agent) + 1);
					}
				}
			}

			final Map<String, Map<String, Integer>> countingName = new LinkedHashMap<String, Map<String, Integer>>();

			for (final Map.Entry<String, Map<String, List<String>>> item : survey_token_name.entrySet()) {
				final String survey = item.getKey();
				final Map<String, List<String>> token_agent = item.getValue();
				for (final Map.Entry<String, List<String>> token_agent_item : token_agent.entrySet()) {
					final List<String> agents = token_agent_item.getValue();
					CollectionClient.getInstance().removeDoubles(agents);
					for (final String agent : agents) {
						if (!countingName.containsKey(survey))
							countingName.put(survey, new LinkedHashMap<String, Integer>());
						if (!countingName.get(survey).containsKey(agent))
							countingName.get(survey).put(agent, 1);
						else
							countingName.get(survey).put(agent, countingName.get(survey).get(agent) + 1);
					}
				}
			}

			final Map<String, Map<String, Integer>> countingVersion = new LinkedHashMap<String, Map<String, Integer>>();

			for (final Map.Entry<String, Map<String, List<String>>> item : survey_token_version.entrySet()) {
				final String survey = item.getKey();
				final Map<String, List<String>> token_agent = item.getValue();
				for (final Map.Entry<String, List<String>> token_agent_item : token_agent.entrySet()) {
					final List<String> agents = token_agent_item.getValue();
					CollectionClient.getInstance().removeDoubles(agents);
					for (final String agent : agents) {
						if (!countingVersion.containsKey(survey))
							countingVersion.put(survey, new LinkedHashMap<String, Integer>());
						if (!countingVersion.get(survey).containsKey(agent))
							countingVersion.get(survey).put(agent, 1);
						else
							countingVersion.get(survey).put(agent, countingVersion.get(survey).get(agent) + 1);
					}
				}
			}

			final Map<String, Map<String, Integer>> countingOS = new LinkedHashMap<String, Map<String, Integer>>();

			for (final Map.Entry<String, Map<String, List<String>>> item : survey_token_os.entrySet()) {
				final String survey = item.getKey();
				final Map<String, List<String>> token_agent = item.getValue();
				for (final Map.Entry<String, List<String>> token_agent_item : token_agent.entrySet()) {
					final List<String> agents = token_agent_item.getValue();
					CollectionClient.getInstance().removeDoubles(agents);
					for (final String agent : agents) {
						if (!countingOS.containsKey(survey))
							countingOS.put(survey, new LinkedHashMap<String, Integer>());
						if (!countingOS.get(survey).containsKey(agent))
							countingOS.get(survey).put(agent, 1);
						else
							countingOS.get(survey).put(agent, countingOS.get(survey).get(agent) + 1);
					}
				}
			}

			final Map<String, Map<String, Map<String, Integer>>> countingMap = new LinkedHashMap<String, Map<String, Map<String, Integer>>>();
			countingMap.put("Category", countingCat);
			countingMap.put("Browser", countingName);
			countingMap.put("Version", countingVersion);
			countingMap.put("OS", countingOS);

			for (final Map.Entry<String, Map<String, Map<String, Integer>>> counting : countingMap.entrySet()) {

				final File countingFile = new File(countingDir, counting.getKey() + ".csv");
				try {
					fileClient.writeToFile(countingFile.getAbsolutePath(), "'survey';'" + counting.getKey() + "';'count';\n", false);
				} catch (IOException e) {
					e.printStackTrace();
				}

				final Map<String, Map<String, Integer>> critcountings = counting.getValue();

				for (final Map.Entry<String, Map<String, Integer>> critcounting : critcountings.entrySet()) {
					final String survey = critcounting.getKey();
					final Map<String, Integer> surveyCountings = critcounting.getValue();
					if (surveyCountings != null) {
						for (final Map.Entry<String, Integer> surveyCounting : surveyCountings.entrySet()) {
							try {
								fileClient.writeToFile(countingFile.getAbsolutePath(),"'"+ survey + "';'"+surveyCounting.getKey()+"','"+surveyCounting.getValue()+"';\n", true);

							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}

	}

}

package eu.dzhw.zofar.management.dev.automation.raffle;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.dzhw.zofar.management.dev.automation.AbstractExecutor;
import eu.dzhw.zofar.management.utils.objects.CollectionClient;

public class Executor extends AbstractExecutor {

	private static final long serialVersionUID = 3033179150546277894L;
	private static final Executor INSTANCE = new Executor();
	private static final Logger LOGGER = LoggerFactory.getLogger(Executor.class);

	public enum Parameter implements ABSTRACTPARAMETER {
		count,dbMainServer, dbMainName, dbMainPort, dbMainUser, dbMainPass,dbAddonServer, dbAddonName, dbAddonPort, dbAddonUser, dbAddonPass, addonCriteria, addonTokenColumn;
	};

	private Executor() {
		super();

	}

	public static Executor getInstance() {
		return INSTANCE;
	}
	
	public ParameterMap<ABSTRACTPARAMETER, Object> getParameterMap(final int count, final String dbMainServer, final String dbMainName, final String dbMainPort, final String dbMainUser, final String dbMainPass, final String dbAddonServer, final String dbAddonName, final String dbAddonPort, final String dbAddonUser, final String dbAddonPass, final String addonCriteria, final String addonTokenColumn) {
		final ParameterMap<ABSTRACTPARAMETER, Object> back = new ParameterMap<ABSTRACTPARAMETER, Object>();
		back.put(Parameter.count, count);
		back.put(Parameter.dbMainServer, dbMainServer);
		back.put(Parameter.dbMainName, dbMainName);
		back.put(Parameter.dbMainPort, dbMainPort);
		back.put(Parameter.dbMainUser, dbMainUser);
		back.put(Parameter.dbMainPass, dbMainPass);
		back.put(Parameter.dbAddonServer, dbAddonServer);
		back.put(Parameter.dbAddonName, dbAddonName);
		back.put(Parameter.dbAddonPort, dbAddonPort);
		back.put(Parameter.dbAddonUser, dbAddonUser);
		back.put(Parameter.dbAddonPass, dbAddonPass);
		back.put(Parameter.addonCriteria, addonCriteria);
		back.put(Parameter.addonTokenColumn, addonTokenColumn);

		return back;
	}

	@Override
	public void process(ParameterMap<ABSTRACTPARAMETER, Object> parameter) throws Exception {
		final int count = (Integer)parameter.get(Parameter.count);
		final String dbMainServer = (String) parameter.get(Parameter.dbMainServer);
		final String dbMainName = (String) parameter.get(Parameter.dbMainName);
		final String dbMainPort = (String) parameter.get(Parameter.dbMainPort);
		final String dbMainUser = (String) parameter.get(Parameter.dbMainUser);
		final String dbMainPass = (String) parameter.get(Parameter.dbMainPass);
		final String dbAddonServer = (String) parameter.get(Parameter.dbAddonServer);
		final String dbAddonName = (String) parameter.get(Parameter.dbAddonName);
		final String dbAddonPort = (String) parameter.get(Parameter.dbAddonPort);
		final String dbAddonUser = (String) parameter.get(Parameter.dbAddonUser);
		final String dbAddonPass = (String) parameter.get(Parameter.dbAddonPass);
		final String addonCriteria = (String) parameter.get(Parameter.addonCriteria);
		final String addonTokenColumn = (String) parameter.get(Parameter.addonTokenColumn);

		final List<String> tokens = new ArrayList<String>();
		Connection dbConn = null;
		try {
			dbConn = postgresClient.getConnection(dbMainServer, dbMainPort, dbMainName, dbMainUser, dbMainPass);
			if (dbConn != null) {
				postgresClient.executeDb(dbConn, "CREATE EXTENSION IF NOT EXISTS dblink;");
				final List<Map<String, String>> dbTokens = postgresClient.queryDb(dbConn, "SELECT token from participant where id in (SELECT distinct participant_id FROM surveydata where value IN (SELECT addon.token FROM dblink('dbname="+dbAddonName+" host="+dbAddonServer+" port="+dbAddonPort+" user="+dbAddonUser+" password="+dbAddonPass+"','Select token from participant where id IN (select distinct participant_id from surveydata where "+addonCriteria.replaceAll("'", "''")+")') AS addon(token varchar(255))) and variablename='"+addonTokenColumn+"');");
				if (dbTokens != null) {
					for (final Map<String, String> item : dbTokens) {
						tokens.add(item.get("token"));
					}
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (dbConn != null)
				postgresClient.close(dbConn);
		}
		
		if(tokens.isEmpty())throw new Exception("No tokens found");
		else if(tokens.size() < count)throw new Exception("less than "+count+" tokens found");
		else{
			List<String> schuffled = CollectionClient.getInstance().shuffledList(tokens);
			List<String> back = schuffled.subList(0, count);
			System.out.println(CollectionClient.getInstance().implode(back.toArray(), "\n"));
		}
		
	}
}

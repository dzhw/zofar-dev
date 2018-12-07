package eu.dzhw.zofar.management.dev.automation.raffle.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.dzhw.zofar.management.dev.automation.AbstractExecutor.ABSTRACTPARAMETER;
import eu.dzhw.zofar.management.dev.automation.AbstractExecutor.ParameterMap;
import eu.dzhw.zofar.management.dev.automation.raffle.Executor;

public class Starter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Starter.class);

	public static void main(String[] args) {
		final String dbMainServer = "xxxxxxxx";
		final String dbMainName = "xxxxxxxx";
		final String dbMainPort = "5432";
		final String dbMainUser = "xxxxxxxx";
		final String dbMainPass = "xxxxxxxx";
		
		final String dbAddonServer = "xxxxxxxx";
		final String dbAddonName = "xxxxxxxx";
		final String dbAddonPort = "5432";
		final String dbAddonUser = "xxxxxxxx";
		final String dbAddonPass = "xxxxxxxx";
		
		final String addonCriteria = "variablename='verlosung' and value='ao1'";
		
		final String addonTokenColumn = "PRELOADpanel";
		
		final int count = 86;

		final Executor executor = Executor.getInstance();

		try {
			ParameterMap<ABSTRACTPARAMETER, Object> parameter = executor.getParameterMap(count, dbMainServer, dbMainName, dbMainPort, dbMainUser, dbMainPass, dbAddonServer, dbAddonName, dbAddonPort, dbAddonUser, dbAddonPass, addonCriteria, addonTokenColumn);
			executor.process(parameter);
			LOGGER.info("done");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

package eu.dzhw.zofar.management.dev.automation;

import java.io.Serializable;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.dzhw.zofar.management.comm.db.postgresql.PostgresClient;
import eu.dzhw.zofar.management.comm.tomcat.ManagerClient;
import eu.dzhw.zofar.management.dev.builder.BuilderClient;
import eu.dzhw.zofar.management.dev.maven.MavenClient;

public abstract class AbstractExecutor implements Serializable {

	private static final long serialVersionUID = -8064992413960370195L;
	protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractExecutor.class);
	
	protected final BuilderClient builder;
	protected final ManagerClient tomcatManager;
	protected final MavenClient mavenClient;
	protected final PostgresClient postgresClient;
	
	public interface ABSTRACTPARAMETER {		
	};
	
	public class ParameterMap<ABSTRACTPARAMETER, Object> extends HashMap<ABSTRACTPARAMETER, Object>{

		private static final long serialVersionUID = 5536503960458459540L;

		public ParameterMap() {
			super();
		}
	};

	protected AbstractExecutor() {
		super();
		builder = BuilderClient.getInstance();
		mavenClient = MavenClient.getInstance();
		tomcatManager = ManagerClient.getInstance();
		postgresClient = PostgresClient.getInstance();
//		fileClient = FileClient.getInstance();
	}
	
	public abstract void process(final ParameterMap<ABSTRACTPARAMETER, Object> parameter) throws Exception;

}

package eu.dzhw.zofar.management.comm.db.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import eu.dzhw.zofar.management.comm.db.DBClient;

public class MySQLClient extends DBClient {
	
	/** The Constant INSTANCE. */
	private static final MySQLClient INSTANCE = new MySQLClient();

	private MySQLClient() {
		super();
		try {

			Class.forName("com.mysql.jdbc.Driver");

		} catch (ClassNotFoundException e) {

			System.out.println("Where is your MySQL JDBC Driver? " + "Include in your library path!");
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Gets the single instance of PostgresClient.
	 *
	 * @return single instance of PostgresClient
	 */
	public static MySQLClient getInstance() {
		return INSTANCE;
	}

	@Override
	public Connection getConnection(String url, String port, String database, String user, String pass) throws SQLException {
		Connection connection = DriverManager.getConnection("jdbc:mysql://" + url + ":" + port + "/" + database+"?useSSL=false", user, pass);
		return connection;
	}

	@Override
	public Connection getMaintenanceConnection(String url, String port, String user, String pass) throws SQLException {
		throw new SQLException("not implemented yet");
	}

	@Override
	public boolean disconnectOtherFromDB(Connection conn, String dbName) throws Exception {
		throw new SQLException("not implemented yet");
	}

	@Override
	public boolean existDB(Connection conn, String dbName) throws Exception {
	    try{
	        ResultSet resultSet = conn.getMetaData().getCatalogs();

	        while (resultSet.next()) {

	          String databaseName = resultSet.getString(1);
	            if(databaseName.equals(dbName)){
	                return true;
	            }
	        }
	        resultSet.close();

	    }
	    catch(Exception e){
	        e.printStackTrace();
	    }
		return false;
	}

}

package de.his.zofar.simulator.main;

import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import eu.dzhw.zofar.management.comm.db.postgresql.PostgresClient;
import eu.dzhw.zofar.management.utils.files.CSVClient;
import eu.dzhw.zofar.management.utils.files.DirectoryClient;

public class DBFullCheck {

	public static void main(String[] args) {
		try {
			checkPersistence("xxx","xxx","5432","xxx","xxx","xxx");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static boolean checkPersistence(final String recordDir,final String db_location,final String db_port,final String db_name,final String db_user,final String db_password) throws Exception{
		final File dir = DirectoryClient.getInstance().createDir(new File(recordDir), "persistence");
		List<File> datasets = DirectoryClient.getInstance().readDir(dir, DirectoryClient.SortMode.UNSORTED);
		if(datasets != null){
//			System.out.println("datasets : "+datasets.size());
			PostgresClient postgres = PostgresClient.getInstance();
			Connection connection = null;
			try{
				connection = postgres.getConnection(db_location, db_port, db_name, db_user, db_password);
			}
			catch(Exception e){
				throw new Exception("cannot establish connection : location="+db_location+" port="+db_port+" dbname="+db_name+" db_user="+db_user+" db_password="+db_password,e);
			}
			CSVClient csvClient = CSVClient.getInstance();
			List<String> headers = new LinkedList<String>();
			headers.add("VARIABLE");
			headers.add("DATA");
			headers.add("VERSION");
			if(connection != null){
				for(final File dataset : datasets){
					final String token = dataset.getName().replaceAll(Pattern.quote(".csv"), "");
//					System.out.print("analyse dataset "+token);
					List<Map<String, String>> fromFile  = csvClient.loadCSV(dataset, headers, true);
					Map<String,String> fromFileData = new HashMap<String,String>();
					Map<String,Integer> fromFileVersion = new HashMap<String,Integer>();
					for(final Map<String,String> line :fromFile){
						final String variable = line.get("VARIABLE");
						final String tmpData =  line.get("DATA");
						final String version =  line.get("VERSION");
						fromFileData.put(variable, tmpData);
						fromFileVersion.put(variable, Integer.parseInt(version));
					}
					
					
					List<Map<String, String>> fromDB = null;
					try{
						fromDB = postgres.queryDb(connection, "select t1.variablename AS variable,t1.value as data,t1.version as version from surveydata AS t1,participant AS t2 where t1.participant_id=t2.id AND t2.token = '"+token+"';");
					}
					catch(Exception e){
						throw new Exception("cannot query db : location="+db_location+" port="+db_port+" dbname="+db_name+" db_user="+db_user+" db_password="+db_password,e);
					}
					
					Map<String,String> fromDBData = new HashMap<String,String>();
					Map<String,Integer> fromDBVersion = new HashMap<String,Integer>();
					
					for(final Map<String,String> line :fromDB){
						final String variable = line.get("variable");
						final String tmpData =  line.get("data");
						final String version =  line.get("version");
						fromDBData.put(variable, tmpData);
						fromDBVersion.put(variable, Integer.parseInt(version));
					}
					
//					System.out.println("fromFile : "+fromFileData);
//					System.out.println("fromDB : "+fromDBData);
					
					Set<String> variables = fromFileData.keySet();
					boolean ok = true;
					for(final String variable: variables){
						if(variable.equals("UNKOWN"))continue;
						final String dataFromFile = (fromFileData.get(variable)+"").trim();
						final String dataFromDB = (fromDBData.get(variable)+"").trim();
						
						if(!dataFromFile.equals(dataFromDB)){
							final Exception e = new Exception("["+token+"] Data not equals for variable "+variable+" ==> "+dataFromFile+" != "+dataFromDB);
//							System.err.println(e);
							System.out.println("["+token+"] Data not equals for variable "+variable+" ==> "+dataFromFile+" != "+dataFromDB);
							ok = false;
							//throw e;
						}
					}
//					System.out.println(" "+ok);
				}
			}
			else throw new Exception("cannot establish connection : location="+db_location+" port="+db_port+" dbname="+db_name+" db_user="+db_user+" db_password="+db_password);
			return true;
		}
		else{
			throw new Exception("no persistence check data found at "+dir.getAbsolutePath());
		}
	}

}

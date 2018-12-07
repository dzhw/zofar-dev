package de.his.zofar.simulator.main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.w3c.dom.Document;

import de.his.zofar.simulator.threading.RefactoredThreadSuite;
import eu.dzhw.zofar.management.utils.xml.XmlClient;

public class UnitTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String path = "";
		if (args.length > 0) {
			if (args.length == 1) {
				path = args[0];
			}
		}
		
		final Map<Object,Object> properties = new HashMap<Object,Object>();
		
		//Init with default values
		properties.put("parameterbase", null);
		properties.put("proxyurl", null);
		properties.put("proxyport", 80);
		properties.put("targetprotocol", "http");
		properties.put("targeturl", "localhost");
		properties.put("targetport", 8080);
		properties.put("recordsdir", null);
		properties.put("packetsize", 10);
		properties.put("repeatcount", 6);
		properties.put("repeattimeout", 2000);
		properties.put("packettimeout", 2000);
		properties.put("threadtimeout", 3000);
		properties.put("minthreadtimeout", 0);
		properties.put("maxthreadtimeout", 0);
		properties.put("actiondelaymin", 10000);
		properties.put("actiondelaymax", 30000);
		properties.put("count", 1);
		properties.put("offset", 0);
		properties.put("runsynchron", 0);
		properties.put("pageCount", 115);
		properties.put("tokenfile", null);
		properties.put("qmlfile", null);
		properties.put("tokenin", null);
		properties.put("pagecount", 0);

		//fields for persistence check
		properties.put("db_check_location", "localhost");
		properties.put("db_check_port", 5432);
		properties.put("db_check_database", "UNKOWN");
		properties.put("db_check_user", "postgres");
		properties.put("db_check_password", "postgres");

		
		Properties props = null;
		FileInputStream in = null;
		try {
			in = new FileInputStream(path);
			props = new Properties();
			props.load(in);
			
			if(props != null){
				final Set<Object> keys = props.keySet();
				if(keys != null){
					final Iterator<Object> it =  keys.iterator();
					while(it.hasNext()){
						final Object key = it.next();
						Object value = props.get(key);
						try{
							final Integer tmp = Integer.parseInt(value+"");
							value = tmp;
						}
						catch(final NumberFormatException e){
							
						}
//						properties.put(((String)key).toLowerCase(), value+"".trim());
						properties.put(((String)key).toLowerCase(), value);
						System.out.println("[LOAD CONFIG] "+key+" = "+value+" ("+value.getClass()+")");
					}
				}
			}
			
		} catch (final FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		finally{
			if(in != null)
				try {
					in.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
		}
		
		
		if(properties != null){
			final Set<Object> keys = properties.keySet();
			if(keys != null){
				final Iterator<Object> it =  keys.iterator();
				while(it.hasNext()){
					final Object key = it.next();
					final Object value = properties.get(key);
					System.out.println("[CONFIG] "+key+" = "+value+"");
				}
			}
		}

		Document qml = null;
		if(properties.get("qmlfile") != null){
			final XmlClient xmlClient = XmlClient.getInstance();
			try {
				qml = xmlClient.getDocument(properties.get("qmlfile")+"");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		List<String> tokens = null;
	
		if ((properties.get("tokenin") != null)
				&& (!properties.get("tokenin").equals(""))) {
			final String[] tmpTokens = ((String)properties.get("tokenin")).split(",");

			if ((tmpTokens != null) && (tmpTokens.length > 0)) {
				tokens = new LinkedList<String>();
				final int count = tmpTokens.length;
				for (int a = (Integer)properties.get("offset"); a < count; a++) {
					String tmp = tmpTokens[a];
					tmp = tmp.replaceAll("\'", "");
					tokens.add(tmp);
					if (tokens.size() >= (Integer)properties.get("count"))
						break;
				}
			}
		}
		else if((properties.get("tokenfile") != null)
				&& (!properties.get("tokenfile").equals(""))){
			try{
			    final BufferedReader br = new BufferedReader(new FileReader((String) properties.get("tokenfile")));
			    try {
			        String line = br.readLine();
			        int index =0;
			        while (line != null) {
			        	index = index + 1;
			        	if(tokens == null)tokens = new LinkedList<String>();
			        	if(index > Integer.parseInt(properties.get("offset")+"")){
				        	tokens.add(line.trim());
							if (tokens.size() >= (Integer)properties.get("count"))
								break;
			        	}
			            line = br.readLine();
			        }
			    } finally {
			        br.close();
			    }
			}
			catch(final FileNotFoundException e){
				
			}
			catch(final IOException e){
				
			}

		}
		
//		System.out.println("tokens : "+tokens);
		if ((tokens != null) && (!tokens.isEmpty())) {
			final LinkedList<String> tokenList = new LinkedList<String>();

			final Iterator<String> tokenIt = tokens.iterator();
			int lft = 0;

			while (tokenIt.hasNext()) {
				tokenList.add(tokenIt.next());

				if (lft >= ((Integer)properties.get("count")) - 1)
					break;
				lft = lft + 1;
			}
			
			final RefactoredThreadSuite suite = new RefactoredThreadSuite((String)properties.get("proxyurl"),
					(Integer)properties.get("proxyport"), (String)properties.get("targetprotocol"), (String)properties.get("targeturl"), (Integer)properties.get("targetport"),
					(String)properties.get("parameterbase"), tokenList, (String)properties.get("recordsdir"), (String)properties.get("recordssuffix"),qml);

			suite.setPacketSize((Integer)properties.get("packetsize"));
			// suite.setRepeatCount(repeatCount);
			// suite.setRepeatTimeOut(repeatTimeOut);
			suite.setPacketTimeOut((Integer)properties.get("packettimeout"));
			suite.setThreadTimeOut((Integer)properties.get("threadtimeout"));
			suite.setMinThreadTimeOut((Integer)properties.get("minthreadtimeout"));
			suite.setMaxThreadTimeOut((Integer)properties.get("maxthreadtimeout"));
			suite.setActionDelayMin((Integer)properties.get("actiondelaymin"));
			suite.setActionDelayMax((Integer)properties.get("actiondelaymax"));
			suite.setRunSynchron((Integer)properties.get("runsynchron"));
			suite.setPageCount((Integer)properties.get("pagecount"));

			try {
				suite.clearPersistence();
				suite.exec();
				System.out.println("check datasets...");
				try{
			
					if(suite.checkPersistence(properties.get("db_check_location")+"",properties.get("db_check_port")+"",properties.get("db_check_database")+"",properties.get("db_check_user")+"",properties.get("db_check_password")+"")){
						System.out.println("DB ok");
					}
					else System.out.println("DB nok");
				}
				catch(Exception e){
					System.out.println("DB nok : ");
					e.printStackTrace();
				}
				
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}

}

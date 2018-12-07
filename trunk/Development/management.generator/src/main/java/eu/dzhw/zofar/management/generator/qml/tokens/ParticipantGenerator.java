package eu.dzhw.zofar.management.generator.qml.tokens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.dzhw.zofar.management.generator.qml.tokens.components.PermutationGenerator;
import eu.dzhw.zofar.management.generator.qml.tokens.components.preloads.PreloadFuntions;
import eu.dzhw.zofar.management.security.text.TextCipherClient;

public class ParticipantGenerator {
	private static final ParticipantGenerator INSTANCE = new ParticipantGenerator();

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ParticipantGenerator.class);

	private ParticipantGenerator() {
		super();
	}

	public static ParticipantGenerator getInstance() {
		return INSTANCE;
	}
	
	public Map<String,String> generate(final String chars,final int maxlength,final int count,final Map<Integer, Map<String, Map<String, String>>> preloadRules) throws Exception{
		return generate(null,null,null,chars,maxlength,maxlength,count,preloadRules);
	}
	
	public Map<String,String> generate(final String prefix,final String postfix,final String url,final String chars,final int maxlength,final int count,final Map<Integer, Map<String, Map<String, String>>> preloadRules) throws Exception{
		return generate(prefix,postfix,url,chars,maxlength,maxlength,count,preloadRules);
	}
	
	public Map<String,String> generate(final String prefix,final String postfix,final String url,final String chars,final int minlength,final int maxlength,final int count,final Map<Integer, Map<String, Map<String, String>>> preloadRules) throws Exception{
		final PermutationGenerator permutationGenerator = PermutationGenerator.getInstance();
		final TextCipherClient encoder = TextCipherClient.getInstance();
		final PreloadFuntions preloadFunctions = PreloadFuntions.getInstance();
		final List<String> permutations = permutationGenerator.calculatePermutations(chars,minlength,maxlength,count,true);
//		List<String> tocsv = new ArrayList<String>();
		
		List<String> csvHeader = new ArrayList<String>();
		csvHeader.add("token");
		csvHeader.add("url");

		List<Map<String, String>> tocsv = new ArrayList<Map<String, String>>();
		
		StringBuffer qmlBuffer = new StringBuffer();
		StringBuffer csvBuffer = new StringBuffer();
		StringBuffer sqlBuffer = new StringBuffer();

//		System.out.println("!!QML Start!!");
		Integer lft = 1;
		for (final String token : permutations) {
			String extendedToken = token;
			if(prefix != null) extendedToken = prefix+extendedToken;
			if(postfix != null) extendedToken = extendedToken+postfix;
			
			Map<String, Map<String, String>> individualPreloadRules = null;
			if (preloadRules.containsKey(lft))
				individualPreloadRules = preloadRules.get(lft);

			final Map<String, String> individualPreloads = new LinkedHashMap<String, String>();
			if (individualPreloadRules != null) {
				for (final Map.Entry<String, Map<String, String>> preloadItem : individualPreloadRules.entrySet()) {
					individualPreloads.put(preloadItem.getKey(), preloadFunctions.execute(lft, preloadItem.getValue().get("function"), preloadItem.getValue().get("value")));
				}
			}

			final StringBuffer buffer = new StringBuffer();
			buffer.append("<zofar:preload password=\"" + extendedToken + "\" name=\"" + extendedToken + "\">\n");
			for (final Map.Entry<String, String> preloadItem : individualPreloads.entrySet()) {
				buffer.append("<zofar:preloadItem variable=\"" + preloadItem.getKey() + "\" value=\"" + preloadItem.getValue() + "\"></zofar:preloadItem>\n");
			}
			buffer.append("</zofar:preload>\n");
//			tocsv.add(extendedToken);
			final Map<String, String> csvRow = new HashMap<String, String>();
			csvRow.put("token", extendedToken);
			csvRow.put("url", url + extendedToken);
			
			for (final Map.Entry<String, String> preloadItem : individualPreloads.entrySet()) {
				if(!csvHeader.contains(preloadItem.getKey()))csvHeader.add(preloadItem.getKey());
				csvRow.put(preloadItem.getKey(), preloadItem.getValue());
			}

			tocsv.add(csvRow);
			
//			System.out.println(buffer.toString());
			qmlBuffer.append(buffer.toString()+"\n");
			
			sqlBuffer.append("INSERT INTO participant(id, version, password, token) VALUES (nextval('seq_participant_id'),0,'"+ encoder.encodeSHA(extendedToken) + "','" + extendedToken + "');\n");
			for (final Map.Entry<String, String> preloadItem : individualPreloads.entrySet()) {
				sqlBuffer.append("INSERT INTO surveydata(id, version, value, variablename, participant_id) VALUES (nextval('seq_surveydata_id'), 0, '" + preloadItem.getValue() + "', 'PRELOAD" + preloadItem.getKey() + "', (select DISTINCT id from participant where token = '"
						+ extendedToken + "'));\n");
			}
			

			lft = lft + 1;
		}
//		System.out.println("!!QML Stop!!");
//		System.out.println("!!CSV Start!!");
//		csvBuffer.append("'token';'url'\n");
//		for (final String token : tocsv) {
////			System.out.println(token+";"+url+token);
//			csvBuffer.append("'"+token+"';'"+url+token+"'\n");
//		}
		boolean first = true;
		for(final String header: csvHeader){
			if(!first)csvBuffer.append(";");
			csvBuffer.append("'"+header+"'");
			first = false;
		}
		if(!first)csvBuffer.append("\n");
		
		for(final Map<String, String> row: tocsv){
			first = true;
			for(final String header: csvHeader){
				if(!first)csvBuffer.append(";");
				String value = "";
				if(row.containsKey(header))value = row.get(header);
				csvBuffer.append("'"+value+"'");
				first = false;
			}
			if(!first)csvBuffer.append("\n");
		}
		
		
//		System.out.println("!!CSV Stop!!");
		
		
		
		
		
		Map<String,String> back = new HashMap<String,String>();
		back.put("qml", qmlBuffer.toString());
		back.put("csv", csvBuffer.toString());
		back.put("sql", sqlBuffer.toString());
		
		return back;
	}
	
	
}

package presentation.data.format.csv;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import presentation.data.format.AbstractFormat;
import de.his.export.xml.export.DataType;
import de.his.export.xml.export.HistoryType;
import de.his.export.xml.export.HistorysetType;
import de.his.export.xml.export.ParticipantType;
import eu.dzhw.zofar.management.utils.files.FileClient;

/**
 * The Class CsvFormat.
 */
public class CsvFormat extends AbstractFormat {

	/** The instance. */
	private static CsvFormat INSTANCE;
	
	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(CsvFormat.class);

	/**
	 * Instantiates a new csv format.
	 */
	private CsvFormat() {
		super();
	}

	/**
	 * Gets the single instance of CsvFormat.
	 * 
	 * @return single instance of CsvFormat
	 */
	public static CsvFormat getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CsvFormat();
		return INSTANCE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see presentation.data.format.AbstractFormat#format(java.util.Set)
	 */
	@Override
	public Object format(final Set<ParticipantType> data) {
		return this.format(data, ';', '\n');
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see presentation.data.format.AbstractFormat#format(java.util.Set, char,
	 * char)
	 */
	@Override
	public Object format(final Set<ParticipantType> data, final char fieldSeparator, final char rowSeparator) {
		return this.format(data, fieldSeparator, rowSeparator, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see presentation.data.format.AbstractFormat#format(java.util.Set, char,
	 * char, java.util.Map)
	 */
	@Override
	public Object format(final Set<ParticipantType> data, final char fieldSeparator, final char rowSeparator, final Map<String, String> mapping) {
		return this.format(data, fieldSeparator, rowSeparator, mapping, false,true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see presentation.data.format.AbstractFormat#format(java.util.Set, char,
	 * char, java.util.Map, boolean)
	 */
	@Override
	public Object format(final Set<ParticipantType> data, final char fieldSeparator, final char rowSeparator, final Map<String, String> mapping, final boolean ignoreFirst, final boolean limitStrings) {
		return this.format(null, data, fieldSeparator, rowSeparator, mapping, ignoreFirst,limitStrings);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see presentation.data.format.AbstractFormat#format(java.lang.Object,
	 * java.util.Set, char, char, java.util.Map, boolean)
	 */
	@Override
	public Object format(Object prebuild, final Set<ParticipantType> data, final char fieldSeparator, final char rowSeparator, final Map<String, String> mapping, final boolean ignoreFirst, final boolean limitStrings) {
			return this.format(prebuild, data, fieldSeparator, rowSeparator, mapping, ignoreFirst, limitStrings, null,null);
	}
	
//	@Override
//	public Object format(Object prebuild, final Set<ParticipantType> data, final char fieldSeparator, final char rowSeparator, final Map<String, String> mapping, final boolean ignoreFirst, final boolean limitStrings,final Map<String,Set<String>> reverseFileMapping) {
//		final Iterator<ParticipantType> rowIt = data.iterator();
//		if (prebuild == null){
//			prebuild = new HashMap<String,Object>();
//			((HashMap)prebuild).put("data", "");
//			((HashMap)prebuild).put("history", new HashMap<String,String>());
//		}
//		Map<String,Object> back = new HashMap<String,Object>();
//		final StringBuffer dataBack = new StringBuffer((String) ((HashMap)prebuild).get("data"));
//		
//		final Map<String,String> historyBack = new HashMap<String,String>();
//		historyBack.putAll((Map<? extends String, ? extends String>) ((HashMap)prebuild).get("history"));
//		List<String> columns = null;
//		while (rowIt.hasNext()) {
//			final ParticipantType entry = rowIt.next();
//			// convert data to map
//			final Map<String, String> answerMap = new LinkedHashMap<String, String>();
//			answerMap.put("id", entry.getId());
//			answerMap.put("token", entry.getToken());
//			for (final DataType inputDataEntry : entry.getDataset().getDataArray()) {
//				String value = inputDataEntry.getStringValue();
//				final String type = inputDataEntry.getType();
//				if ((type == null)||((type != null)&&(type.equals("string")))) {
//					if(limitStrings)value = value.substring(0, Math.min(243, value.length()));
//					value = this.stripBreaks("" + value + "", fieldSeparator, rowSeparator);
//				}
//				answerMap.put(inputDataEntry.getVariable(), value);
//			}
//
//			if ((columns == null)) {
//				columns = new ArrayList<String>();
//				final Iterator<String> colIt = answerMap.keySet().iterator();
//				while (colIt.hasNext()) {
//					final String col = colIt.next();
//					columns.add(col);
//					if (!ignoreFirst) {
//						dataBack.append("\"" + col + "\"");
//						if (colIt.hasNext())
//							dataBack.append(fieldSeparator);
//					}
//				}
//				if (!ignoreFirst)
//					dataBack.append(rowSeparator);
//			}
//
//			final Iterator<String> colIt = columns.iterator();
//			while (colIt.hasNext()) {
//				final String col = colIt.next();
//				Object value = answerMap.get(col);
//				if ((mapping != null) && (mapping.containsKey(value + "")))
//					value = mapping.get(value + "");
//				String tmp = value + "";
//				tmp = tmp.replace('\"', '\'');
//				tmp = tmp.replace('#', ' ');
//				dataBack.append("\"" + tmp + "\"");
//				if (colIt.hasNext())
//					dataBack.append(fieldSeparator);
//			}
//			dataBack.append(rowSeparator);
//			
////			final StringBuffer historyBuffer = new StringBuffer();
////			historyBuffer.append("'Page';'Time'\n");
////			
////			final HistorysetType historySet = entry.getHistoryset();
////			if(historySet != null){
////				HistoryType[] historyArray = historySet.getHistoryArray();
////				if(historyArray != null){
////					for (final HistoryType inputHistoryEntry : historyArray) {
////						historyBuffer.append("'"+inputHistoryEntry.getStringValue()+"';'"+inputHistoryEntry.getStamp()+"'\n");
////					}
////				}
////			}
//			
//			final StringBuffer historyBuffer = new StringBuffer();
////			historyBuffer.append("'Page';'Time'\n");
//			
//			final HistorysetType historySet = entry.getHistoryset();
//			if(historySet != null){
//				HistoryType[] historyArray = historySet.getHistoryArray();
//				if(historyArray != null){
//					for (final HistoryType inputHistoryEntry : historyArray) {
////						historyBuffer.append("'"+inputHistoryEntry.getId()+"';'"+inputHistoryEntry.getStringValue()+"';'"+inputHistoryEntry.getStamp()+"';'"+inputHistoryEntry.getPid()+"';'"+inputHistoryEntry.getToken()+"'\n");
//						historyBuffer.append("\""+inputHistoryEntry.getId()+"\",\""+inputHistoryEntry.getStringValue()+"\",\""+inputHistoryEntry.getStamp()+"\",\""+inputHistoryEntry.getPid()+"\",\""+inputHistoryEntry.getToken()+"\"\n");
//					}
//				}
//			}
//
//			historyBack.put(entry.getToken(), historyBuffer.toString());
//			
//		}
//		back.put("data", dataBack.toString());
//		back.put("history", historyBack);
//		return back;
//	}
	
	@Override
	public Object format(Object prebuild, final Set<ParticipantType> data, final char fieldSeparator, final char rowSeparator, final Map<String, String> mapping, final boolean ignoreFirst, final boolean limitStrings,final Map<File,Set<String>> fileMapping,final Map<String,Set<File>> reverseFileMapping) {
		final Iterator<ParticipantType> rowIt = data.iterator();
		if (prebuild == null){
			prebuild = new HashMap<String,Object>();
			((HashMap)prebuild).put("data", "");
			
			if((fileMapping != null)&&(!fileMapping.isEmpty())){
				Set<File> additionalFiles = fileMapping.keySet();
				for(final File additionalFile:additionalFiles){
					((HashMap)prebuild).put(additionalFile, "");
				}
			}
						
			((HashMap)prebuild).put("history", new HashMap<String,String>());
		}
		Map<Object,Object> back = new HashMap<Object,Object>();
		final StringBuffer dataBack = new StringBuffer((String) ((HashMap)prebuild).get("data"));
		
		final Map<File,StringBuffer> dataBackMap = new HashMap<File,StringBuffer>();
		if((fileMapping != null)&&(!fileMapping.isEmpty())){
			Set<File> additionalFiles = fileMapping.keySet();
			for(final File additionalFile:additionalFiles){
				dataBackMap.put(additionalFile, new StringBuffer((String) ((HashMap)prebuild).get(additionalFile)));
			}
		}
		
		final Map<String,String> historyBack = new HashMap<String,String>();
		historyBack.putAll((Map<? extends String, ? extends String>) ((HashMap)prebuild).get("history"));
		List<String> columns = null;
		while (rowIt.hasNext()) {
			final ParticipantType entry = rowIt.next();
			// convert data to map
			final Map<String, String> answerMap = new LinkedHashMap<String, String>();
			answerMap.put("id", entry.getId());
			answerMap.put("token", entry.getToken());
			
			for (final DataType inputDataEntry : entry.getDataset().getDataArray()) {
				String value = inputDataEntry.getStringValue();
				final String type = inputDataEntry.getType();
				if ((type == null)||((type != null)&&(type.equals("string")))) {
					if(limitStrings)value = value.substring(0, Math.min(243, value.length()));
					value = this.stripBreaks("" + value + "", fieldSeparator, rowSeparator);
				}
				answerMap.put(inputDataEntry.getVariable(), value);
			}
			
			//Header
			if ((columns == null)) {
				columns = new ArrayList<String>();
				
				final Set<StringBuffer> cache = new HashSet<StringBuffer>();
				
				final Iterator<String> colIt = answerMap.keySet().iterator();
				boolean first = true;
				while (colIt.hasNext()) {
					final String col = colIt.next();
					columns.add(col);
					
					if (!ignoreFirst) {
						dataBack.append("\"" + col + "\"");
						if (colIt.hasNext()){
							dataBack.append(fieldSeparator);
						}
						
						if((reverseFileMapping != null)&&(reverseFileMapping.containsKey(col))){
							final Set<File> files = reverseFileMapping.get(col);
							if((files != null)&&(!files.isEmpty())){
								for(final File file : files){
									final StringBuffer fileBuffer = dataBackMap.get(file);
									if(fileBuffer != null){
										if(!first)fileBuffer.append(fieldSeparator);
										fileBuffer.append("\"" + col + "\"");
										cache.add(fileBuffer);
										first = false;
									}
								}
							}
						}
					}
				}
				if (!ignoreFirst){
					dataBack.append(rowSeparator);
					for(final StringBuffer cacheItem : cache){
						cacheItem.append(rowSeparator);
					}
				}
			}
			
			//Data
			final Set<StringBuffer> cache = new HashSet<StringBuffer>();
			final Iterator<String> colIt = columns.iterator();
			boolean first = true;
			while (colIt.hasNext()) {
				final String col = colIt.next();
				Object value = answerMap.get(col);
				if ((mapping != null) && (mapping.containsKey(value + "")))
					value = mapping.get(value + "");
				String tmp = value + "";
				tmp = tmp.replace('\"', '\'');
				tmp = tmp.replace('#', ' ');
				
				dataBack.append("\"" + tmp + "\"");
				if (colIt.hasNext())dataBack.append(fieldSeparator);
				
				if((reverseFileMapping != null)&&(reverseFileMapping.containsKey(col))){
					final Set<File> files = reverseFileMapping.get(col);
					if((files != null)&&(!files.isEmpty())){
						for(final File file : files){
							final StringBuffer fileBuffer = dataBackMap.get(file);
							if(fileBuffer != null){
								if(!first)fileBuffer.append(fieldSeparator);
								fileBuffer.append("\"" + tmp + "\"");							
								cache.add(fileBuffer);
								first = false;
							}
						}
					}
				}
			}
			
			dataBack.append(rowSeparator);
			
			for(final StringBuffer cacheItem : cache){
				cacheItem.append(rowSeparator);
			}
			
			
			
			//History
			final StringBuffer historyBuffer = new StringBuffer();
			
			final HistorysetType historySet = entry.getHistoryset();
			if(historySet != null){
				HistoryType[] historyArray = historySet.getHistoryArray();
				if(historyArray != null){
					for (final HistoryType inputHistoryEntry : historyArray) {
						historyBuffer.append("\""+inputHistoryEntry.getId()+"\",\""+inputHistoryEntry.getStringValue()+"\",\""+inputHistoryEntry.getStamp()+"\",\""+inputHistoryEntry.getPid()+"\",\""+inputHistoryEntry.getToken()+"\"\n");
					}
				}
			}

			historyBack.put(entry.getToken(), historyBuffer.toString());
		}
		back.put("data", dataBack.toString());
		
		if((dataBackMap != null)&&(!dataBackMap.isEmpty())){
			for(final Entry<File, StringBuffer> additionalBack:dataBackMap.entrySet()){
				back.put(additionalBack.getKey(), additionalBack.getValue());
			}
		}
		
		back.put("history", historyBack);
		return back;
	}
	
	
	
	
//	@Override
//	public Object formatAsStream(File historyDirectory,File dataFile, Set<ParticipantType> data, char fieldSeparator, char rowSeparator, Map<String, String> mapping, boolean ignoreFirst, final boolean saveHistory) throws IOException {
//		Map<String, Object> processed = (Map<String, Object>)format(null, data, fieldSeparator, rowSeparator, mapping, ignoreFirst);
//		if(processed != null){
//			FileClient.getInstance().writeToFile(dataFile.getAbsolutePath(), processed.get("data").toString(), true);
//			if(saveHistory){
//				// History Files
//				final Map<String, String> historyBack = (Map<String, String>) processed.get("history");
//				if (historyBack != null) {
//					final int count = historyBack.size();
//					LOGGER.info("write {} history files", count);
//					int lft = 1;
//					for (final Map.Entry<String, String> historyItem : historyBack.entrySet()) {
////						LOGGER.info("write history file {} of {}", lft, count);
////						LOGGER.info("history item {} = {}", lft, historyItem.getValue());
//						final File historyFile = FileClient.getInstance().createOrGetFile(historyItem.getKey(), ".csv", historyDirectory);
//						FileClient.getInstance().writeToFile(historyFile.getAbsolutePath(), historyItem.getValue(), false);
//						lft = lft + 1;
//					}
//				}
//			}
//		}
//		return null;
//	}
	
	@Override
	public Object formatAsStream(File history,File dataFile, Set<ParticipantType> data, char fieldSeparator, char rowSeparator, Map<String, String> mapping, boolean ignoreFirst, final boolean saveHistory, final boolean limitStrings) throws IOException {
		return this.formatAsStream(history, dataFile, data, fieldSeparator, rowSeparator, mapping, ignoreFirst, saveHistory, limitStrings,null);
	}
	
	@Override
	public Object formatAsStream(File history,File dataFile, Set<ParticipantType> data, char fieldSeparator, char rowSeparator, Map<String, String> mapping, boolean ignoreFirst, final boolean saveHistory, final boolean limitStrings,final Map<File,Set<String>> fileMapping) throws IOException {
		
		Map<String,Set<File>> reverseFileMapping = null;
		if((fileMapping != null)&&(!fileMapping.isEmpty())){
			for(Map.Entry<File,Set<String>> entry:fileMapping.entrySet()){
				final File file = entry.getKey();
				final Set<String> variables = entry.getValue();
				for(final String variable : variables){
					if(reverseFileMapping == null)reverseFileMapping = new HashMap<String,Set<File>>();
					Set<File> reverseFiles = null;
					if(reverseFileMapping.containsKey(variable))reverseFiles = reverseFileMapping.get(variable);
					if(reverseFiles == null)reverseFiles = new HashSet<File>();
					reverseFiles.add(file);
					reverseFileMapping.put(variable,reverseFiles);
				}
			}
		}
		
		Map<String, Object> processed = (Map<String, Object>)format(null, data, fieldSeparator, rowSeparator, mapping, ignoreFirst,limitStrings,fileMapping,reverseFileMapping);
		if(processed != null){
			FileClient.getInstance().writeToFile(dataFile.getAbsolutePath(), processed.get("data").toString(), true);
			
			if((fileMapping != null)&&(!fileMapping.isEmpty())){
				Set<File> additionalFiles = fileMapping.keySet();
				for(final File additionalFile:additionalFiles){
					if(processed.containsKey(additionalFile)){
//						LOGGER.info("ADDITIONAL ({}) : {}",additionalFile.getAbsolutePath(),processed.get(additionalFile).toString());
						FileClient.getInstance().writeToFile(additionalFile.getAbsolutePath(), processed.get(additionalFile).toString(), true);
					}
				}
			}
			
			if(saveHistory){
				// History Files
				final Map<String, String> historyBack = (Map<String, String>) processed.get("history");
				if (historyBack != null) {
					if(!ignoreFirst){
						FileClient.getInstance().writeToFile(history.getAbsolutePath(), "\"id\",\"page\",\"timestamp\",\"participant_id\",\"token\"\n", true);
					}
					
					final int count = historyBack.size();
					LOGGER.info("write {} history files", count);
					for (final Map.Entry<String, String> historyItem : historyBack.entrySet()) {
						FileClient.getInstance().writeToFile(history.getAbsolutePath(), historyItem.getValue(), true);
					}
				}
			}
		}
		return null;
	}

	/**
	 * Strip breaks.
	 * 
	 * @param text
	 *            the text
	 * @param fieldSeparator
	 *            the field separator
	 * @param rowSeparator
	 *            the row separator
	 * @return the string
	 */
	private String stripBreaks(final String text, final char fieldSeparator, final char rowSeparator) {
		String back = text.replaceAll("\n", " ");
		back = back.replaceAll("\r", "");
		back = back.replaceAll("\\x13", "");
		back = back.replaceAll("\\x10", "");
		back = back.replaceAll("\t", " ");
		back = back.replaceAll(fieldSeparator + "", "#");
		back = back.replaceAll(rowSeparator + "", "#");
		back = back.replaceAll(" {2,}", " ");
		return back;
	}
}

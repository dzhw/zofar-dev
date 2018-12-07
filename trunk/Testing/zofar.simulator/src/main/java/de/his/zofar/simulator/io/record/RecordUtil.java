package de.his.zofar.simulator.io.record;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class RecordUtil {

	private final static String KEY = "page";
	public final static String RECORDDIR = System.getProperty("java.io.tmpdir");
	public final static String RECORDSUFFIX = "rec";
	public final static String RECORDSPLITTER = "::";
	public final static String RECORDPAIRSPLITTER = "=";

//	private static synchronized String mapToXML(Map<String, Object> map) {
//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//		XMLEncoder xmlEncoder = new XMLEncoder(bos);
//		xmlEncoder.writeObject(map);
//		xmlEncoder.flush();
//		xmlEncoder.close();
//		String serializedMap = bos.toString();
//		return flat(serializedMap);
//	}

//	private static synchronized Map<String, Object> xmlToMap(String input) {
//		XMLDecoder xmlDecoder = new XMLDecoder(new ByteArrayInputStream(deflat(
//				input).getBytes()));
//		@SuppressWarnings("unchecked")
//		Map<String, Object> parsedMap = (Map<String, Object>) xmlDecoder
//				.readObject();
//		xmlDecoder.close();
//		return parsedMap;
//
//	}
	
	public static synchronized Map<String, Object> csvToMap(String input,final String splitter) {
	final Map<String, Object> parsedMap = new HashMap<String,Object>();
	final BufferedReader bufRdr = new BufferedReader(new StringReader(deflat(input,splitter)));
	String line = null;
	try {
		while ((line = bufRdr.readLine()) != null) {
			if (line != null) {
				String[] pair = line.split(RecordUtil.RECORDPAIRSPLITTER);
				if(pair.length >=2)parsedMap.put(pair[0], pair[1]);
			}
		}
	} catch (IOException e) {
		e.printStackTrace();
	}
	return parsedMap;

}

//	private static synchronized String flat(final String input) {
//		if (input == null)
//			return null;
//		String output = input;
//		output = output.replace('\n', '#');
//		output = output.replaceAll(" {2,}", " ");
//		return output;
//	}

	private static synchronized String deflat(final String input,final String splitter) {
		if (input == null)
			return null;
		String output = input;
		output = output.replaceAll(splitter, "\n");
		return output;
	}

	public synchronized static Stack<Map<String, Object>> loadRecords(
			final String token) {
		return loadRecords(RecordUtil.RECORDDIR, token, RecordUtil.RECORDSUFFIX,RecordUtil.RECORDSPLITTER);
	}

	public synchronized static Stack<Map<String, Object>> loadRecords(
			final String directory, final String token, final String ending,final String splitter) {
		final Stack<Map<String, Object>> back = new Stack<Map<String, Object>>();
		String path = token + "." + ending;
		if ((directory != null) && (!directory.equals(""))) {
			path = directory + File.separator + path;
		}
		final File recordFile = new File(path);

		if (recordFile.exists() && recordFile.canRead()) {
			BufferedReader bufRdr = null;
			try {
				bufRdr = new BufferedReader(new FileReader(recordFile));
				String line = null;
				while ((line = bufRdr.readLine()) != null) {
					if (line != null) {
						final Map<String, Object> record = csvToMap(line.trim(),splitter);
						if (record != null) {
//							final String identifier = (String) record.get(KEY);
//							record.remove(KEY);
//							back.put(identifier, record);
//							Map<String, Object> set = null;
//							if(back.containsKey(identifier))set = back.get(identifier);
//							if(set == null)set = new Stack<Map<String,Object>>();
//							set.add(record);
//							back.put(identifier,set);
							back.add(record);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (bufRdr != null) {
					try {
						bufRdr.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return back;
	}

//	public synchronized static void saveRecord(
//			final String token, final String identifier, Map<String, Object> map) {
//		saveRecord(RecordUtil.RECORDDIR, token, RecordUtil.RECORDSUFFIX,identifier,map);
//	}

//	public synchronized static void saveRecord(final String directory,
//			final String token, final String ending, final String identifier,
//			Map<String, Object> map) {
//		String path = token + "." + ending;
//		if ((directory != null) && (!directory.equals(""))) {
//			path = directory + File.separator + path;
//		}
//		final File recordFile = new File(path);
//		if (!recordFile.exists())
//			try {
//				recordFile.createNewFile();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//
//		if (recordFile.exists() && recordFile.canWrite()) {
//			try {
//				PrintWriter out = new PrintWriter(new BufferedWriter(
//						new FileWriter(recordFile, true)));
//				map.put(KEY, identifier);
//				out.println(mapToXML(map));
//				out.close();
//			} catch (IOException e) {
//				// oh noes!
//			}
//		}
//	}
}

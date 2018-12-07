package de.his.zofar.simulator.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.his.zofar.simulator.io.record.RecordUtil;

public class RecordExtruder {
	
	private final static String TAG = "[RECORD]";
	private final static String ITEMSPLIT = "::";
	private final static String VALUESPLIT = "=";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final String dir = "" + File.separator + "home" + File.separator
				+ "hisuser" + File.separator + "Temp" + File.separator
				+ "xxx";
		Map<String,List<String>> records = readFile(dir+ File.separator + "xxx.log"); 
		System.out.println("records : "+records.size());
		writeFiles(dir,records);
	}
	
	private static void writeFiles(final String directory,Map<String,List<String>> records){
		if(records == null)return;
		Iterator<String> tokens = records.keySet().iterator();
		Writer tokenOut = null;
		try{
			tokenOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(directory+File.separator+"simulator_tokens.txt"), "UTF8"));
			while(tokens.hasNext()){
				final String token = tokens.next();
				tokenOut.write(token+"\n");
				final List<String> recordList = records.get(token);
				final String recordFilePath = directory+File.separator+token+".rec";
				Writer out = null;
				try{
					out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(recordFilePath), "UTF8"));
					Iterator<String> record = recordList.iterator();
					while(record.hasNext()){
						final String recordLine = record.next();
//						System.out.println("record Line : "+recordLine);
						out.write(recordLine+"\n");
					}
				}
				catch(UnsupportedEncodingException e){
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				finally{
					if(out != null)
						try {
							out.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
				
			}
		}
		catch(UnsupportedEncodingException e){
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			if(tokenOut != null)
				try {
					tokenOut.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

		
	}

	private static Map<String,List<String>> readFile(final String path) {
		Map<String,List<String>> back = new HashMap<String,List<String>>();
		BufferedReader bufRdr = null;
		FileInputStream in = null;

		final File recordFile = new File(path);
		try {
			if (recordFile.exists() && recordFile.canRead()) {
				bufRdr = new BufferedReader(new FileReader(recordFile));
				String line = null;
				while ((line = bufRdr.readLine()) != null) {
					if (line != null) {
						final int index = line.indexOf(TAG);
						if(index >= 0){
							final String cutted = (line.substring(index+TAG.length())).trim();
							final Map<String, Object> record = RecordUtil.csvToMap(cutted,"::");
							if(record != null){
								final String token = (String) record.get("token");
								List<String> recordList = null;
								if(back.containsKey(token))recordList = back.get(token);
								if(recordList == null)recordList = new ArrayList<String>();
								recordList.add(cutted);
								back.put(token, recordList);
							}
						}
					}
				}
			}
		} catch (final FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			if(bufRdr != null)
				try {
					bufRdr.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return back;

	}

}

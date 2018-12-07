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
import java.io.Writer;
import java.text.SimpleDateFormat;

public class Generator {

	private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	public static void main(String[] args) {
		String path="part2.rec";
		String repl="=part2:";
		int fromPart=100001;
		int toPart=102001;		
		generate(path,repl,fromPart,toPart);
	}
	
	public static void generate(String path,String repl, int fromPart, int toPart){
		String directory = "" + File.separator+ "xxx" + File.separator+ "xxx" + File.separator+ "xxx"+ File.separator+ "tmp"+ File.separator+ "simulator"+ File.separator+ "xxx"+ File.separator+ "records"+File.separator;// + File.separator+ "part.rec";
		BufferedReader bufRdr = null;
		FileInputStream in = null;
		if ((directory != null) && (!directory.equals(""))) {
			path = directory + File.separator + path;
		}
		final File recordFile = new File(path);
		try {
			if (recordFile.exists() && recordFile.canRead()) {
				Writer tokens = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(""+directory+"tokens.txt"), "UTF8"));
				for (int i=fromPart; i<toPart;i++){
					bufRdr = new BufferedReader(new FileReader(recordFile));
					String line = null;
					Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(""+directory+"part"+i+".rec"), "UTF8"));
					while ((line = bufRdr.readLine()) != null) {
						if (line != null) {
							line=line+"\n";
							out.write(line.replaceAll(repl, "=part"+(i)+":"));
						}
					}
					out.flush();
					out.close();
					tokens.write("part"+(i)+"\n");
				}
				tokens.flush();
				tokens.close();
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
	}

}

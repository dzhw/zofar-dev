package eu.dzhw.zofar.management.generator.preloads.main;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import eu.dzhw.zofar.management.generator.preloads.PreloadClient;
import eu.dzhw.zofar.management.utils.files.FileClient;

public class PreloadExtractor {

	public static void main(String[] args) {
		final String csvPath = "xxxxx.csv";
		final File csvFile = new File(csvPath);
		final File sqlParticipantFile = new File(csvFile.getParentFile(),"participants.sql");
		final File sqlDataFile = new File(csvFile.getParentFile(),"surveydata.sql");
		
		final PreloadClient preloader = PreloadClient.getInstance();
		try {
			final Map<PreloadClient.FIELDS,String> parsed = preloader.csv2sql(csvFile);
			if(parsed != null){
				FileClient.getInstance().writeToFile(sqlParticipantFile.getAbsolutePath(), parsed.get(PreloadClient.FIELDS.PARTICIPANT), false);
				FileClient.getInstance().writeToFile(sqlDataFile.getAbsolutePath(), parsed.get(PreloadClient.FIELDS.DATA), false);
//				System.out.println("participants:\n"+parsed.get(PreloadClient.FIELDS.PARTICIPANT));
//				System.out.println("data:\n"+parsed.get(PreloadClient.FIELDS.DATA));
//				System.out.println("qml:\n"+parsed.get(PreloadClient.FIELDS.QML));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

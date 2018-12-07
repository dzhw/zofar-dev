package eu.dzhw.zofar.management.generator.survey.translation.main;

import java.io.File;
import java.io.IOException;

import de.his.zofar.translation.Importer;

public class TranslationImporter {

	public static void main(String[] args) {
		final String dirname = ""+File.separator+"xxxx"+File.separator+"xxxx"+File.separator+"xxxx"+File.separator+"xxxx"+File.separator+"xxxx"+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"de"+File.separator+"his"+File.separator+"zofar"+File.separator+"messages";
		final String filenname = "translation.csv";
		final File dir = new File(dirname);
		final File csv = new File(dir,filenname);
		
		Importer translater = new Importer(csv);
		try {
			translater.exportTo(dir);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

package eu.dzhw.zofar.management.utils.files.main;

import java.io.File;
import java.io.IOException;

import eu.dzhw.zofar.management.utils.files.FileClient;

public class WriteToFile {

	public static void main(String[] args) {
		File file = FileClient.getInstance().createTempFile("bla", ".txt");
		try {
			FileClient.getInstance().writeToFile(file.getAbsolutePath(), "test", false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(FileClient.getInstance().readAsString(file));

	}

}

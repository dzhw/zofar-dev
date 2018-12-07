package main;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.json.JsonObject;

import eu.dzhw.zofar.management.utils.files.DirectoryClient;
import eu.dzhw.zofar.management.utils.files.FileClient;
import eu.dzhw.zofar.management.utils.files.PackagerClient;
import eu.dzhw.zofar.management.utils.system.ConfigurationUtils;
import presentation.mdm.format.FormatFactory;
import service.mdm.MDMService;

public class JSON {

	public static void main(String[] args) throws Exception {
		final String dataAcquestionProject="DATAACQUESTIONPROJECT";
		final String surveyId="SURVEYID";
		final String instrumentId="INSTRUMENTID";
		final String datasetId="DATASETID";
		
		final File baseDirectory = new File(DirectoryClient.getInstance().getHome(),"Projekte");

		final File projectDirectory = new File(baseDirectory,"xxxt");
		final File mdmDirectory = new File(projectDirectory,"FDZ/MDM/");
		
		final File jsonDirectory = DirectoryClient.getInstance().createDir(mdmDirectory, "json");
		DirectoryClient.getInstance().cleanDirectory(jsonDirectory);
		
		final File variableDirectory = DirectoryClient.getInstance().createDir(jsonDirectory, "variables");
		DirectoryClient.getInstance().cleanDirectory(variableDirectory);
		
		final File questionDirectory = DirectoryClient.getInstance().createDir(jsonDirectory, "questions");
		DirectoryClient.getInstance().cleanDirectory(questionDirectory);
		
		final File imagesDirectory = DirectoryClient.getInstance().createDir(questionDirectory, "images");
		DirectoryClient.getInstance().cleanDirectory(imagesDirectory);
		
		final FileClient fileClient = FileClient.getInstance();	
		
		MDMService service = MDMService.getInstance();
		final File qmlFile = new File(mdmDirectory,"questionnaire.xml");
		final File translationFile = new File(mdmDirectory,"text_de.properties");
		final File screenshotFile = new File(mdmDirectory,"screenshots.zip");
//		String transPath = null;
//		transPath = translationFile.getAbsolutePath();
		
		Map<String,Properties> transFiles = new HashMap<String,Properties>();
		if(translationFile != null) {
			transFiles.put("de", ConfigurationUtils.getInstance().getConfigurationFromFileSystem(translationFile.getAbsolutePath()));
		}
		
		final Map<String, Object> data =  service.buildUp(qmlFile.getAbsolutePath(),transFiles,imagesDirectory,screenshotFile, dataAcquestionProject, surveyId, instrumentId,
			datasetId);
		if (data != null) {
			@SuppressWarnings("unchecked")
			Map<String,Map<String,Object>> variables = (Map<String,Map<String,Object>>) data.get("variable");
			String variablesOutput = (String) FormatFactory.getInstance().getFormat(FormatFactory.FORMAT.csv).formatCSV(variables);
			if(variablesOutput != null){
//				System.out.println("Variable Output : "+variablesOutput);
				final File variable_csv_File = fileClient.createTextFile("Variables.csv", variablesOutput, variableDirectory);

			}
			@SuppressWarnings("unchecked")
			Map<String, JsonObject> questions = (Map<String, JsonObject>) data.get("question");
			@SuppressWarnings("unchecked")
			Map<String, String> questionsOutput = (Map<String, String>) FormatFactory.getInstance().getFormat(FormatFactory.FORMAT.json).formatJSON(questions);
			for (Map.Entry<String, String> item : questionsOutput.entrySet()) {
//				System.out.println("Question Output : "+item.getKey() + " => " + item.getValue());
				final File question_json_File = fileClient.createTextFile(item.getKey()+".json", item.getValue(), questionDirectory);
			}

			@SuppressWarnings("unchecked")
			Map<String, File> images = (Map<String, File>) data.get("images");
			if(images != null){
				for (Map.Entry<String, File> item : images.entrySet()) {
					if(item.getValue() != null){
						final File dest = fileClient.createOrGetFile(item.getKey(), "."+fileClient.getSuffix(item.getValue()), imagesDirectory);
						fileClient.copyFile(item.getValue(), dest);
					}
				}
			}
			final File packageFile= fileClient.createOrGetFile("mdm_"+projectDirectory.getName(), ".zip", mdmDirectory);
			Map<String, Object> packageObj = new HashMap<String, Object>();
			packageObj.put(variableDirectory.getName(), variableDirectory);
			packageObj.put(questionDirectory.getName(), questionDirectory);
			PackagerClient.getInstance().packageZip(packageFile, packageObj);
		}
	}
}

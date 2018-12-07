package de.his.zofar.generator.maven.plugin.mojo;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.json.JsonObject;
import javax.json.JsonValue;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.xmlbeans.XmlObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import de.his.zofar.generator.maven.plugin.generator.mdm.MDMGenerator;
import de.his.zofar.generator.maven.plugin.generator.mdm.MDMGenerator.CSV_TYPE;
import de.his.zofar.generator.maven.plugin.generator.mdm.MDMGenerator.JSON_TYPE;
import de.his.zofar.xml.questionnaire.QuestionnaireDocument;
import eu.dzhw.zofar.management.comm.json.JSONClient;
import eu.dzhw.zofar.management.utils.files.DirectoryClient;
import eu.dzhw.zofar.management.utils.files.FileClient;

@Deprecated
public class MDMGeneratorMojo {

	private static final Logger LOGGER = LoggerFactory.getLogger(MDMGeneratorMojo.class);

	private final QuestionnaireDocument questionnaire;

	private final MDMGenerator generator = new MDMGenerator();

	private final File outputDirectory;

	public MDMGeneratorMojo(final QuestionnaireDocument questionnaire, final File outputDirectory) {
		super();
		this.questionnaire = questionnaire;
		this.outputDirectory = DirectoryClient.getInstance().createDir(outputDirectory, "mdm");
		DirectoryClient.getInstance().cleanDirectory(this.outputDirectory);
	}

	public final void execute() throws MojoExecutionException, MojoFailureException {
		final String dataAcquestionProject="DATAACQUESTIONPROJECT";
		final String surveyId="SURVEYID";
		final String instrumentId="INSTRUMENTID";
		final String datasetId="DATASETID";
		
		final File jsonDirectory = DirectoryClient.getInstance().createDir(this.outputDirectory, "json");
		DirectoryClient.getInstance().cleanDirectory(jsonDirectory);
		
		final File jsonVariableDirectory = DirectoryClient.getInstance().createDir(jsonDirectory, "variables");
		DirectoryClient.getInstance().cleanDirectory(jsonVariableDirectory);
		
		final File jsonQuestionDirectory = DirectoryClient.getInstance().createDir(jsonDirectory, "questions");
		DirectoryClient.getInstance().cleanDirectory(jsonQuestionDirectory);
		
		final FileClient fileClient = FileClient.getInstance();

		final File questionCsvFile = fileClient.createOrGetFile("questions", ".csv", this.outputDirectory);
		try {
			fileClient.writeToFile(questionCsvFile.getAbsolutePath(), "", false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		final File variableCsvFile = fileClient.createOrGetFile("variables", ".csv", this.outputDirectory);
		try {
			fileClient.writeToFile(variableCsvFile.getAbsolutePath(), "", false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		final Map<String,Set<XmlObject>> questionMap = new LinkedHashMap<String,Set<XmlObject>>();
		
		final XmlObject[] questions = questionnaire.getQuestionnaire().selectPath("//*[@variable]");
		
		if (questions != null) {
			for (final XmlObject question : questions) {
				//get variable
				final XmlObject[] variableArray = question.selectPath("@variable");
				for(final XmlObject varObj:variableArray){
					final String variable = varObj.newCursor().getTextValue();
					Set<XmlObject> questionObjs = null;
					if(questionMap.containsKey(variable))questionObjs = questionMap.get(variable);
					if(questionObjs == null)questionObjs = new LinkedHashSet<XmlObject>();
					questionObjs.add(question);
					questionMap.put(variable, questionObjs);
				}			
			}
		}
		
		int index = 1;
		for(Map.Entry<String,Set<XmlObject>> questionPair: questionMap.entrySet()){
			final String variable = questionPair.getKey();
			final Set<XmlObject> questionArray = questionPair.getValue();
			for(final XmlObject questionObj:questionArray){

				// Generate JSONs
				Map<JSON_TYPE,JsonValue> jsons = generator.writeJSON(questionnaire.getQuestionnaire(),questionObj,index,dataAcquestionProject,surveyId,instrumentId,datasetId);
				final JsonValue variable_json = jsons.get(JSON_TYPE.VARIABLE);
				
				String name = variable;
				if(index > 1)name = name+"_"+index;
				
				final File variable_json_File = fileClient.createOrGetFile(name, ".json", jsonVariableDirectory);
				try {
					fileClient.writeToFile(variable_json_File.getAbsolutePath(), JSONClient.getInstance().writeToString((JsonObject)variable_json), false);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				final JsonValue question_json = jsons.get(JSON_TYPE.QUESTION);
				
				final File question_json_File = fileClient.createOrGetFile(name, ".json", jsonQuestionDirectory);
				try {
					fileClient.writeToFile(question_json_File.getAbsolutePath(), JSONClient.getInstance().writeToString((JsonObject)question_json), false);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// CSV Generation
				Map<CSV_TYPE,String> csvs = generator.writeCSV(questionnaire.getQuestionnaire(),questionObj,index,dataAcquestionProject,surveyId,instrumentId,datasetId);
				
				index = index + 1;
			}
			
		}
		
//		VariableType[] variables = null;
//		if(questionnaire != null){
//			if (questionnaire.getQuestionnaire().getVariables() != null) {
//				variables = questionnaire.getQuestionnaire().getVariables().getVariableArray();
//			}
//		}
		
		
//		if (variables != null) {
//			for (final VariableType variable : variables) {
//				final String vname = variable.getName();
//				final Enum vtype = variable.getType();
//				final XmlObject[] result = questionnaire.getQuestionnaire()
//						.selectPath("//*[@variable='" + vname + "']");
//
//				if (result != null) {
//					XmlObject questionObj = null;
//					final int count = result.length;
//					if (count == 1) {
//						questionObj = result[0];
//					} else if (count > 1) {
//						// is there a zofar:researchdata version
//
//						for (int a = 0; a < count; a++) {
//							if (this.hasParent(result[a].getDomNode(), "zofar:researchdata")) {
//								questionObj = result[a];
//								break;
//							}
//						}
//					}
//
//					if (questionObj != null) {
////						LOGGER.info("node : {}",questionObj);
//						// Generate JSONs
//						generator.writeJSON(questionnaire.getQuestionnaire(),questionObj,0,surveyId,instrumentId,datasetId);
//						
//						// CSV Generation
//					} else {
//						if (count == 0)
//							LOGGER.error("No Question Object found for Variable {} ", vname);
//						if (count > 1)
//							LOGGER.error(
//									"Multiple Question Objects, but no zofar:researchdata version found for Variable {} ",
//									vname);
//					}
//				}
//			}
//		}
	}
}

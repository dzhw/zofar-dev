package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import model.ParticipantEntity;
import model.SurveyHistory;

import org.hibernate.cfg.Configuration;

//import de.his.zofar.persistence.surveyengine.entities.ParticipantEntity;
//import de.his.zofar.service.surveyengine.model.SurveyHistory;
import presentation.data.format.FormatFactory;
import service.data.DataService;
import utils.ConfigurationUtils;
import de.his.export.xml.export.ParticipantType;

public class DataExport {

	public static void main(String[] args) {
		final Properties system = ConfigurationUtils.getInstance()
				.getConfiguration("System.properties");
		final DataService dataService = DataService.getInstance();
//		final CodebookService codebookService = CodebookService.getInstance();
//		final NavigationGraphService navigationService = NavigationGraphService.getInstance();
//		final CountingService countingService = CountingService.getInstance();
		final Configuration conf = dataService.getConfiguration();
		final List<ParticipantEntity> participants = dataService.getParticipants(conf);
		final Map<String,List<SurveyHistory>> historyMap = dataService.getHistory(participants,conf) ;
		
		final List<String> variables = new ArrayList<String>();
		variables.add("merkmal1");
		variables.add("merkmal2");
		variables.add("merkmal3");
		variables.add("merkmal4");
		variables.add("merkmal5");


		try {
			Set<ParticipantType> export = dataService.getExport(
					system.getProperty("qml.location"), variables,participants,historyMap, "-96","-97",false,conf);
			System.out.println("export : "+export.size());
			Object formatted = FormatFactory.getInstance()
					.getFormat(FormatFactory.FORMAT.csv)
					.format(export, ';', '\n');
			System.out.println(formatted);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
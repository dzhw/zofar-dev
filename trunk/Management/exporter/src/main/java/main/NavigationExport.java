package main;

import java.io.File;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import model.TransitionEntry;
import service.navigation.NavigationGraphService;
import utils.ConfigurationUtils;
import eu.dzhw.zofar.management.utils.files.FileClient;

public class NavigationExport {

	public static void main(String[] args) {
		final Properties system = ConfigurationUtils.getInstance()
				.getConfiguration("System.properties");
		NavigationGraphService service = NavigationGraphService.getInstance();
		final Map<String, Set<TransitionEntry>> transitions = service.buildUpTransitionMatrix(system	.getProperty("qml.location"));
//		Object txtTransitions =  presentation.navigation.format.FormatFactory.getInstance().getFormat(presentation.navigation.format.FormatFactory.FORMAT.txt).format(transitions);
		Object xmlTransitions =  presentation.navigation.format.FormatFactory.getInstance().getFormat(presentation.navigation.format.FormatFactory.FORMAT.xml).format(transitions);
		System.out.println(FileClient.getInstance().showContent((File)xmlTransitions));
	}

}

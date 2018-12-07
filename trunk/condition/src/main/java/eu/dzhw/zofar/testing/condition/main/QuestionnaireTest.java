package eu.dzhw.zofar.testing.condition.main;

import java.io.File;
import java.util.Map;

import eu.dzhw.zofar.management.utils.files.FileClient;
import eu.dzhw.zofar.testing.condition.ConditionAnalyzerClient;

public class QuestionnaireTest {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		final File workDir = new File(
				"xxxx/src/main/resources/");
		final File qmlFile = new File(workDir, "questionnaire.xml");
		final File treeFile = new File(workDir, "TransitionTree.csv");
		final File matrixFile = new File(workDir, "TransitionMatrix.csv");

		final ConditionAnalyzerClient analyzer = ConditionAnalyzerClient.getInstance();

		try {
			Map<String, String> map = analyzer.vectorMap(qmlFile);
			if (map != null) {
				if (map.containsKey("tree")) {
					System.out.println("Tree:");
					System.out.println(map.get("tree"));
					FileClient.getInstance().writeToFile(treeFile.getAbsolutePath(), map.get("tree"), false);
				}
				if (map.containsKey("matrix")) {
					System.out.println("matrix:");
					System.out.println(map.get("matrix"));
					FileClient.getInstance().writeToFile(matrixFile.getAbsolutePath(), map.get("matrix"), false);
				}
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}

//		try {
//			analyzer.checkAccessibility(qmlFile);
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//
//		System.exit(1);
	}
}

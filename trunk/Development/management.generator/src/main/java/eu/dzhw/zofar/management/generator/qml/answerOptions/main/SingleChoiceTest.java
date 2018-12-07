package eu.dzhw.zofar.management.generator.qml.answerOptions.main;

import java.io.File;
import java.util.List;

import eu.dzhw.zofar.management.generator.qml.answerOptions.SingleChoiceGenerator;

public class SingleChoiceTest {

	public static void main(String[] args) {
		final SingleChoiceGenerator scGenerator = SingleChoiceGenerator.getInstance();
		final File dir = new File(File.separator+"home"+File.separator+"hisuser"+File.separator+"Dokumente"+File.separator+"xxxxxxxx");
		final File csv = new File(dir,"xxxxxxxx.csv");
		List<String> generated = scGenerator.generate(csv);
		for(String ao : generated){
			System.out.println(ao);
		}

	}

}

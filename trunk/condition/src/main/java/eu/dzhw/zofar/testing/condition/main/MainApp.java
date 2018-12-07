package eu.dzhw.zofar.testing.condition.main;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import eu.dzhw.zofar.testing.condition.term.TermAnalyzerClient;
import eu.dzhw.zofar.testing.condition.term.elements.Element;
import eu.dzhw.zofar.testing.condition.term.elements.ElementVector;

@Deprecated
public class MainApp {

	public static void main(String[] args) throws Exception {


		final Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("group", new Element("3"));
		properties.put("flag1", new Element("10"));
		
		final ElementVector initial = new ElementVector();
		
		Set<Element> groupVector = new LinkedHashSet<Element>();
		groupVector.add(new Element("'1'"));
		groupVector.add(new Element("'2'"));
		groupVector.add(new Element("'3'"));
		groupVector.add(new Element("'4'"));
		groupVector.add(new Element("'5'"));
		initial.getTupels().put("group.value", groupVector);

		Set<Element> flagVector = new LinkedHashSet<Element>();
		flagVector.add(new Element("'6'"));
		flagVector.add(new Element("'7'"));
		flagVector.add(new Element("'8'"));
		flagVector.add(new Element("'9'"));
		flagVector.add(new Element("'10'"));
		initial.getTupels().put("flag1.value", flagVector);

		final String condition = "(group.value!='1' or (NOT (group.value == '3'))) and flag1.value!='1'";

		final TermAnalyzerClient analyzer = TermAnalyzerClient.getInstance();
		Map<String, ElementVector> result = analyzer.analyze(condition, properties, initial,true);
		if (result != null) {
			for (Entry<String, ElementVector> possibles : result.entrySet()) {
				final String termId = possibles.getKey();
				System.out.println("TermId : " + termId);
				final ElementVector vector = possibles.getValue();
				for (Map.Entry<String, Set<Element>> tupel : vector.getTupels().entrySet()) {
					final String variable = tupel.getKey();
					final Set<Element> values = tupel.getValue();
					System.out.println("\t " + variable + " : " + values);
				}

			}
		}
	}

}

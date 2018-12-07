package main;

import java.io.File;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Node;

import presentation.codebook.format.AbstractFormat;
import presentation.codebook.format.odf.OdfFormat.FORMAT;
import service.codebook.CodebookService;

/**
 * The Class Codebook.
 */
public class Codebook {

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 * @throws Throwable 
	 */
	public static void main(final String[] args) throws Throwable {
		final String path = "xxx/src/main/resources/questionnaire.xml";
		final CodebookService service = CodebookService.getInstance();
		final AbstractFormat renderer = presentation.codebook.format.odf.OdfFormat.getInstance();
		
		final Map<String, Set<Node>> structuredData = service.buildUpDataMatrixNew(path);
		Map<FORMAT,File> rendered = (Map<FORMAT,File>)renderer.formatNew(structuredData);
		
		System.out.println(rendered.get(FORMAT.odf).getAbsolutePath());
		
//		final Properties system = ConfigurationUtils.getInstance().getConfiguration("System.properties");
//
//		final List<String> variables = new ArrayList<String>();
//
//		// variables.clear();
//		variables.add("merkmal1");
//		variables.add("merkmal2");
//		variables.add("merkmal3");
//		variables.add("merkmal4");
//		variables.add("merkmal5");
//		
//		final NodeList usingNodes = XmlClient.getInstance().getByXPath(system.getProperty("qml.location"), "//*[@variable]");
//		final Map<String, Set<String>> questions = CountingService.getInstance().getQuestions(system.getProperty("qml.location"), variables,usingNodes);
//
//		final DataService dataService = DataService.getInstance();
//		final CountingService service = CountingService.getInstance();
//		final Map<String, Map<String, ValueEntry>> options = CountingService.getInstance().getOptions(system.getProperty("qml.location"), variables,usingNodes);
//		final Map<HeaderEntry, Map<ValueEntry, Integer>> codebookMatrix = service.buildUpDataMatrix(system.getProperty("qml.location"), null,options,
//				dataService.getParticipants(dataService.getConfiguration(),false),usingNodes,questions);
//		System.out.println("codebookmatrix : " + codebookMatrix);
//		if (codebookMatrix != null) {
//			final Set<HeaderEntry> varnames = codebookMatrix.keySet();
//			final Iterator<HeaderEntry> varnameIt = varnames.iterator();
//			while (varnameIt.hasNext()) {
//				final HeaderEntry varname = varnameIt.next();
//				final Set<ValueEntry> valueCounts = codebookMatrix.get(varname).keySet();
//				final Iterator<ValueEntry> valueIt = valueCounts.iterator();
//				System.out.println("Variable : " + varname.getVariable() + " Text : " + varname.getText());
//				while (valueIt.hasNext()) {
//					final ValueEntry valueItem = valueIt.next();
//					final String value = valueItem.getValue();
//					String label = valueItem.getLabel().replaceAll("\n", " ");
//					label = label.replaceAll("\r", "");
//					label = label.replaceAll("\t", " ");
//					label = label.replaceAll(" {2,}", " ");
//					final Integer count = codebookMatrix.get(varname).get(valueItem);
//					System.out.println("\tValue : (" + value + ") " + label + " Count : " + count);
//				}
//				System.out.println();
//			}
//		}

	}

}

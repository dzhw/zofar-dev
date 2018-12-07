package eu.dzhw.zofar.management.generator.qml.slc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.TransformerException;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;

import de.his.zofar.xml.questionnaire.QuestionnaireDocument;
import eu.dzhw.zofar.management.utils.xml.XmlClient;

public class LoopGenerator extends GenericGenerator {

	private static LoopGenerator INSTANCE;

	private LoopGenerator() {
		super();
	}

	public static LoopGenerator getInstance() {
		if (INSTANCE == null)
			INSTANCE = new LoopGenerator();
		return INSTANCE;
	}

	@Override
	public String process(String template, List<Map<String, String>> replacements) {
		

		//Configuration of XML-Output
		XmlOptions opts = new XmlOptions();
		opts.setCharacterEncoding("utf8");
		opts.setSavePrettyPrint();
		opts.setSavePrettyPrintIndent(4);
		opts.setSaveOuter();
		opts.setSaveAggressiveNamespaces();

		HashMap<String, String> nsMap = new HashMap<String, String>();
		nsMap.put("http://www.his.de/zofar/xml/questionnaire", "zofar");
		opts.setSaveSuggestedPrefixes(nsMap);
		
		//Add XML-Header and Footer to be able to parse 
		final QuestionnaireDocument doc = getDocument(GenericGenerator.HEADER + template + GenericGenerator.FOOTER);

		try {
			//Convert QML to String
			String generatedStr = XmlClient.getInstance().convert2String(docToXmlObject(doc), opts).replaceAll("<zofar:questionnaire [^>]*>", "").replaceAll("</zofar:questionnaire>", "");
			//Replace Placeholder	
			return super.replacePlaceholder(generatedStr, replacements);
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (XmlException e) {
			e.printStackTrace();
		}
		return null;
	}
}

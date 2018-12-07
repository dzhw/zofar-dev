package main;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.NodeList;

import eu.dzhw.zofar.management.utils.xml.XmlClient;
import model.HeaderEntry;
import service.dokudat.DokudatService;

public class Dokudat {

	public static void main(String[] args) throws Exception {
		DokudatService service = DokudatService.getInstance();
		final String qmlPath = "xxx/src/main/resources/questionnaire.xml";
		
		System.out.print("usingNodes...");
		final NodeList usingNodes = XmlClient.getInstance().getByXPath(qmlPath, "//*[@variable]");
		System.out.println("done");
		
		Map<String,String> mapping = new HashMap<String,String>();
		final Map<HeaderEntry, Object> data =  service.buildUpDataMatrix(qmlPath, usingNodes);
		if (data != null) {
			final Map<String,Object> exportDokudat = (Map<String, Object>) presentation.dokudat.format.FormatFactory.getInstance().getFormat(presentation.dokudat.format.FormatFactory.FORMAT.txt).format((Map<HeaderEntry, Object>) data,mapping);
//			System.out.println(exportDokudat.get("questions"));
			System.out.println(exportDokudat.get("answers"));
		}

	}

}

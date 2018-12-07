package eu.dzhw.zofar.management.dev.qml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.xml.questionnaire.IdentificationalType;
import de.his.zofar.xml.questionnaire.QuestionnaireDocument;
import eu.dzhw.zofar.management.utils.xml.XmlClient;

public class QMLClient {

	private static final QMLClient INSTANCE = new QMLClient();

	private static final Logger LOGGER = LoggerFactory.getLogger(QMLClient.class);

	private QMLClient() {
		super();
	}

	public static QMLClient getInstance() {
		return INSTANCE;
	}

	public QuestionnaireDocument getDocument(final File xml) {
		try {
			if (!xml.isFile()) {
				throw new IllegalArgumentException("cannot find file: " + xml.getAbsolutePath());
			}
			return QuestionnaireDocument.Factory.parse(xml);
		} catch (XmlException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getIdPath(final IdentificationalType child) {
		if (child == null)
			return "";
		final XmlClient client = XmlClient.getInstance();
		
		final List<XmlObject> questionPath = new ArrayList<XmlObject>();
		questionPath.add(child);
		XmlObject parent = client.getParent(child);
		while (parent != null) {
			if (!client.hasParent(parent.getDomNode(), "zofar:page"))break;
			questionPath.add(parent);
			parent = client.getParent(parent);
		}
		
		final StringBuffer back = new StringBuffer();
		Collections.reverse(questionPath);
		
		boolean first = true;
		for(final XmlObject item : questionPath){
			if ((IdentificationalType.class).isAssignableFrom(item.getClass())) {
				IdentificationalType tmp = (IdentificationalType)item;
				if(!first)back.append("_");
				back.append(tmp.getUid());
				first = false;
			}
		}
		return back.toString();
	}
}

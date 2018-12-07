package service.navigation;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import model.TransitionEntry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import utils.XmlUtils;

public class NavigationGraphService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(NavigationGraphService.class);
	
	private static NavigationGraphService INSTANCE;

	private NavigationGraphService() {
		super();
	}

	public static NavigationGraphService getInstance() {
		if (INSTANCE == null)
			INSTANCE = new NavigationGraphService();
		return INSTANCE;
	}
	
	public Map<String,Set<TransitionEntry>> buildUpTransitionMatrix(
			final String qmlPath) {
		final NodeList usingNodes = XmlUtils.getInstance().getByXPath(qmlPath,
				"page");
		final int count = usingNodes.getLength();
		Map<String,Set<TransitionEntry>> back = new LinkedHashMap<String,Set<TransitionEntry>>();
		for (int i = 0; i < count; ++i) {
			Node e = (Node) usingNodes.item(i);
			final NamedNodeMap nodeAttributes = e.getAttributes();
			final Node sourceAttribute = nodeAttributes.getNamedItem("uid");
			final String source = sourceAttribute.getNodeValue();			
			final NodeList transitionList = XmlUtils.getInstance().getByXPath(e, "transitions/*");
			Set<TransitionEntry> transitions = null;
			if(back.containsKey(source))transitions = back.get(source);
			if(transitions == null)transitions = new LinkedHashSet<TransitionEntry>();
			for (int j = 0; j < transitionList.getLength(); ++j) {
				final Node transition = transitionList.item(j);
				final NamedNodeMap transitionAttributes = transition
						.getAttributes();
				
				final Node targetAttribute = transitionAttributes.getNamedItem("target");
				final Node conditionAttribute = transitionAttributes.getNamedItem("condition");
				
				final String target = targetAttribute.getNodeValue();
				String condition = null;
				if(conditionAttribute != null)condition = conditionAttribute.getNodeValue();
				transitions.add(new TransitionEntry(source, condition, target));
			}
			back.put(source, transitions);
		}
		return back;
	}
	
	public Map<String,String> buildUpVisibleMatrix(
			final String qmlPath) {
		return null;
	}

}

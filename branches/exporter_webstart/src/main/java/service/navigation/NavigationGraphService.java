package service.navigation;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eu.dzhw.zofar.management.utils.xml.XmlClient;
import model.TransitionEntry;

// TODO: Auto-generated Javadoc
/**
 * The Class NavigationGraphService.
 */
public class NavigationGraphService {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(NavigationGraphService.class);

	/** The instance. */
	private static NavigationGraphService INSTANCE;

	/**
	 * Instantiates a new navigation graph service.
	 */
	private NavigationGraphService() {
		super();
	}

	/**
	 * Gets the single instance of NavigationGraphService.
	 * 
	 * @return single instance of NavigationGraphService
	 */
	public static NavigationGraphService getInstance() {
		if (INSTANCE == null)
			INSTANCE = new NavigationGraphService();
		return INSTANCE;
	}

	/**
	 * Builds the up transition matrix.
	 * 
	 * @param qmlPath
	 *            the qml path
	 * @return the map
	 * @throws Exception 
	 */
	public Map<String, Set<TransitionEntry>> buildUpTransitionMatrix(final String qmlPath) throws Exception {
		final NodeList usingNodes = XmlClient.getInstance().getByXPath(qmlPath, "page");
		final int count = usingNodes.getLength();
		final Map<String, Set<TransitionEntry>> back = new LinkedHashMap<String, Set<TransitionEntry>>();
		for (int i = 0; i < count; ++i) {
			final Node e = usingNodes.item(i);
			if(XmlClient.getInstance().hasParent(e, "zofar:researchdata"))continue;
			final NamedNodeMap nodeAttributes = e.getAttributes();
			final Node sourceAttribute = nodeAttributes.getNamedItem("uid");
			final String source = sourceAttribute.getNodeValue();
			final NodeList transitionList = XmlClient.getInstance().getByXPath(e, "transitions/*");
			Set<TransitionEntry> transitions = null;
			if (back.containsKey(source))
				transitions = back.get(source);
			if (transitions == null)
				transitions = new LinkedHashSet<TransitionEntry>();
			for (int j = 0; j < transitionList.getLength(); ++j) {
				final Node transition = transitionList.item(j);
				final NamedNodeMap transitionAttributes = transition.getAttributes();

				final Node targetAttribute = transitionAttributes.getNamedItem("target");
				final Node conditionAttribute = transitionAttributes.getNamedItem("condition");

				final String target = targetAttribute.getNodeValue();
				String condition = null;
				if (conditionAttribute != null)
					condition = conditionAttribute.getNodeValue();
				transitions.add(new TransitionEntry(source, condition, target));
			}
			back.put(source, transitions);
		}
		return back;
	}

	/**
	 * Builds the up visible matrix.
	 * 
	 * @param qmlPath
	 *            the qml path
	 * @return the map
	 */
	public Map<String, String> buildUpVisibleMatrix(final String qmlPath) {
		return null;
	}

}

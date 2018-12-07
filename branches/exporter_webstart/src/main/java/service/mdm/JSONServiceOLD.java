package service.mdm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.json.JsonObject;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.dzhw.zofar.management.comm.json.JSONClient;
import eu.dzhw.zofar.management.utils.objects.CollectionClient;
import eu.dzhw.zofar.management.utils.string.ReplaceClient;
import eu.dzhw.zofar.management.utils.xml.XmlClient;

public class JSONServiceOLD extends Observable {

	/** The instance. */
	private static JSONServiceOLD INSTANCE;

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(MDMService.class);

	private final JSONClient JSON;

	private final XmlOptions opts;

	/**
	 * Instantiates a new JSON service.
	 */
	private JSONServiceOLD() {
		super();
		JSON = JSONClient.getInstance();
		opts = new XmlOptions();
		opts.setCharacterEncoding("utf8");
	}

	/**
	 * Gets the single instance of JSONService.
	 * 
	 * @return single instance of JSONService
	 */
	public static JSONServiceOLD getInstance() {
		if (INSTANCE == null)
			INSTANCE = new JSONServiceOLD();
		return INSTANCE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observable#addObserver(java.util.Observer)
	 */
	@Override
	public synchronized void addObserver(final Observer o) {
		super.addObserver(o);
		this.setChanged();
		this.notifyObservers("phases=1");
	}

	public Map<String, Object> buildUp(final String qmlPath, final String dataAcquestionProject,
			final String surveyId, final String instrumentId, final String datasetId) throws Exception {
		final Map<String, Object> back = new HashMap<String, Object>();

		final Map<String, Set<XmlObject>> questionMap = new LinkedHashMap<String, Set<XmlObject>>();
		XmlObject doc = XmlClient.getInstance().docToXmlObject(XmlClient.getInstance().getDocument(qmlPath));
		final XmlObject[] questions = doc.selectPath("//*[@variable]");

		if (questions != null) {
			for (final XmlObject question : questions) {
				// get variable
				final XmlObject[] variableArray = question.selectPath("@variable");
				for (final XmlObject varObj : variableArray) {
					final String variable = varObj.newCursor().getTextValue();
					Set<XmlObject> questionObjs = null;
					if (questionMap.containsKey(variable))
						questionObjs = questionMap.get(variable);
					if (questionObjs == null)
						questionObjs = new LinkedHashSet<XmlObject>();
					questionObjs.add(question);
					questionMap.put(variable, questionObjs);
				}
			}
		}

		int index = 1;
		
//		Map<String, JsonObject> variablesBack = new HashMap<String, JsonObject>();
		Map<String, JsonObject> questionsBack = new HashMap<String, JsonObject>();
		
		
		for (Map.Entry<String, Set<XmlObject>> questionPair : questionMap.entrySet()) {
			final String variable = questionPair.getKey();
			final Set<XmlObject> questionArray = questionPair.getValue();
			for (final XmlObject questionObj : questionArray) {
				
				final Map<String, Map<String, Object>> structuredData = getDataTable(doc, questionObj, index, dataAcquestionProject,
						surveyId, instrumentId, datasetId);

				// Generate JSONs
//				final JsonValue variable_json = writeVariableJSON(doc, questionObj, index, dataAcquestionProject,
//						surveyId, instrumentId, datasetId);
				
//				final JsonObject variable_json = JSON.createObject(structuredData.get("variable"));
				final JsonObject question_json = JSON.createObject(structuredData.get("question"));		


				String name = variable;
				if (index > 1)
					name = name + "_" + index;
				
//				variablesBack.put(name, variable_json);
				questionsBack.put(name, question_json);

//				LOGGER.info("json : {}", question_json);
				index = index + 1;
			}
		}
		
//		back.put("variable", variablesBack);
		back.put("question", questionsBack);
		
		
		return back;
	}

	private Map<String, Object> getObjAsTable(final Object node, final int serial) {
		if (node == null)
			return null;
		final Map<String, Object> structuredData = new LinkedHashMap<String, Object>();
		if ((XmlObject.class).isAssignableFrom(node.getClass())) {
			structuredData.put("xmlObj", (XmlObject) node);
			final Method[] methods = node.getClass().getMethods();
			if (methods != null) {
				final int methodCount = methods.length;
				for (int a = 0; a < methodCount; a++) {
					final Method method = methods[a];

					final String modifier = Modifier.toString(method.getModifiers());
					final Class<?> declaringClass = method.getDeclaringClass();
					final Class<?>[] parameterTypes = method.getParameterTypes();

					if ((declaringClass.getName().contains("zofar")) && (method.getName().startsWith("get"))
							&& (parameterTypes.length == 0)) {
						final String name = method.getName().replaceFirst("get", "").toLowerCase();
						final Class<?> returnType = method.getReturnType();
						Object value = null;
						try {
							value = method.invoke(node, new Object[0]);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							LOGGER.error("[{}] {}",
									node.getClass().getSimpleName() + "," + declaringClass.getSimpleName(),
									modifier + " " + returnType.getSimpleName() + " " + name + " ==> " + value + " ## "
											+ e.getMessage());
						}

						if (value != null) {
							if (returnType.getName().contains("zofar"))
								structuredData.put(name, getObjAsTable(value, serial));
							else
								structuredData.put(name, value);
						}
					}
				}
			}
		} else if ((Object[].class).isAssignableFrom(node.getClass())) {
			Object[] tmp = (Object[]) node;
			final int arrayCount = tmp.length;
			Object[] rebuildTmp = new Object[arrayCount];
			if (arrayCount > 0) {
				for (int a = 0; a < arrayCount; a++)
					rebuildTmp[a] = getObjAsTable(tmp[a], serial);
				structuredData.put("ARRAY", rebuildTmp);
			} else
				structuredData.put("ARRAY", "NULL");
		} else {
			structuredData.put("VALUE", node);
		}

		return structuredData;
	}

	private Map<String, Map<String, Object>> getDataTable(final XmlObject doc, final XmlObject node, final int serial,
			final String dataAcquestionProject, final String surveyId, final String instrumentId,
			final String datasetId) {
		if (node == null)
			return null;
		final Map<String, Object> structuredData = getObjAsTable(node, serial);

		final XmlClient client = XmlClient.getInstance();

		if (structuredData == null)
			return null;
		if (structuredData.isEmpty())
			return null;

		// Question JSON

		// id
		// number
		// surveyId
		// instrumentId
		// questionText
		// instruction
		// introduction
		// type
		// additionalQuestionText
		// technicalRepresentation
		// variableIds
		// successor

		// Variable JSON

		// name
		// dataSetIds
		// surveyIds

		// dataType
		// scaleLevel
		// label?

		// distribution.missings[value,label]
		// distribution.validResponses[value,label]

		final Map<String, Object> jsonQuestion = new LinkedHashMap<String, Object>();
//		final Map<String, Object> jsonVariable = new LinkedHashMap<String, Object>();

		jsonQuestion.put("id", getQuestionId(instrumentId, serial));

		jsonQuestion.put("number", serial);

		if (structuredData.containsKey("variable")) {
//			jsonVariable.put("name", (String) structuredData.get("variable"));
//			jsonVariable.put("id", getVariableId((String) structuredData.get("variable"), dataAcquestionProject));

			String[] variableIds = new String[] { (String) structuredData.get("variable") };
			jsonQuestion.put("variableIds", variableIds);

		}

		jsonQuestion.put("surveyId", surveyId);
		jsonQuestion.put("instrumentId", instrumentId);

//		String[] surveyIds = new String[] { surveyId };
//		jsonVariable.put("surveyIds", surveyIds);

//		String[] datasetIds = new String[] { datasetId };
//		jsonVariable.put("datasetIds", datasetIds);

		Set<Map<String, String>> aos = null;

		Set<String> questionTexts = null;
		Set<String> instructions = null;
		Set<String> introductions = null;
		Set<String> labels = null;

		String type = "UNKOWN";
		if (structuredData.containsKey("xmlObj")) {
			XmlObject tmp = (XmlObject) structuredData.get("xmlObj");

			questionTexts = getQuestionHeader(tmp);
			instructions = getInstructionHeader(tmp);
			introductions = getIntroductionHeader(tmp);
			labels = getLabelHeader(tmp);

			aos = new LinkedHashSet<Map<String, String>>();

			jsonQuestion.put("additionalQuestionText", getAdditional(tmp));
			jsonQuestion.put("technicalRepresentation", tmp.xmlText(opts));

			type = tmp.getDomNode().getNodeName();

			List<XmlObject> aoObjs = new ArrayList<XmlObject>();
			if (type.equals("zofar:answerOption")) {
				aoObjs.add(tmp);
			} else {
				final Collection<? extends XmlObject> subaos = this.getAnswerOptions(tmp);
				aoObjs.addAll(subaos);
			}

			if (aoObjs != null) {
				for (final XmlObject aoObj : aoObjs) {
					aos.add(this.getAnswerOptionAttributes(aoObj));
				}
			}

			final List<XmlObject> nextList = this.getNext(tmp, doc);
			if (nextList != null) {
				Object[] rebuildTmp = new Object[nextList.size()];
				if ((nextList != null) && (!nextList.isEmpty())) {
					int index = 0;
					for (XmlObject next : nextList) {
						final String nextType = next.getDomNode().getNodeName();
						if (nextType.equals("variable"))
							next = client.getParent(next);

						rebuildTmp[index] =
						 getQuestionId(instrumentId,serial);

						index = index + 1;
					}
					jsonQuestion.put("successor", rebuildTmp);
				}
			}
		}
		String questionTypeDE = "UNBEKANNT";
		String questionTypeENG = "UNKOWN";

		String questionDE = "";
		String questionENG = "";

		String instructionDE = "";
		String instructionENG = "";

		String introductionDE = "";
		String introductionENG = "";

		String labelDE = "";
		String labelENG = "";

		if (questionTexts != null) {
			questionDE = CollectionClient.getInstance().implode(questionTexts.toArray(), " ");
		}

		if (instructions != null) {
			instructionDE = CollectionClient.getInstance().implode(instructions.toArray(), " ");
		}

		if (introductions != null) {
			introductionDE = CollectionClient.getInstance().implode(introductions.toArray(), " ");
		}

		if (labels != null) {
			labelDE = CollectionClient.getInstance().implode(labels.toArray(), " ");
		}

		if (type.equals("zofar:responseDomain")) {
			// single choice
			questionTypeDE = "Einfachnennung";
			questionTypeENG = "Single Choice";

			if (!aos.isEmpty()) {

				Set<Map<String, Object>> missings = new LinkedHashSet<Map<String, Object>>();
				Set<Map<String, Object>> validResponses = new LinkedHashSet<Map<String, Object>>();

				for (final Map<String, String> ao : aos) {
					final Map<String, Object> aoMap = new LinkedHashMap<String, Object>();
					final boolean missing = ((ao.containsKey("missing")) && (ao.get("missing").equals("true")));

					if (ao.containsKey("value"))
						aoMap.put("code", ao.get("value"));
					if (ao.containsKey("label")) {
						final Map<String, String> labelMap = new LinkedHashMap<String, String>();
						labelMap.put("de", ao.get("label"));
						labelMap.put("en", "");
						aoMap.put("label", labelMap);
					}

					if (missing) {
						missings.add(aoMap);
					} else {
						validResponses.add(aoMap);
					}
				}

				final Map<String, Object> distribution = new LinkedHashMap<String, Object>();
				distribution.put("missings", missings);
				distribution.put("validResponses", validResponses);
//				jsonVariable.put("distribution", distribution);
			}

		} 
		else if (type.equals("zofar:left")) {
			// single choice
			questionTypeDE = "Einfachnennung";
			questionTypeENG = "Single Choice";

			if (!aos.isEmpty()) {

				Set<Map<String, Object>> missings = new LinkedHashSet<Map<String, Object>>();
				Set<Map<String, Object>> validResponses = new LinkedHashSet<Map<String, Object>>();

				for (final Map<String, String> ao : aos) {
					final Map<String, Object> aoMap = new LinkedHashMap<String, Object>();
					final boolean missing = ((ao.containsKey("missing")) && (ao.get("missing").equals("true")));

					if (ao.containsKey("value"))
						aoMap.put("code", ao.get("value"));
					if (ao.containsKey("label")) {
						final Map<String, String> labelMap = new LinkedHashMap<String, String>();
						labelMap.put("de", ao.get("label"));
						labelMap.put("en", "");
						aoMap.put("label", labelMap);
					}

					if (missing) {
						missings.add(aoMap);
					} else {
						validResponses.add(aoMap);
					}
				}

				final Map<String, Object> distribution = new LinkedHashMap<String, Object>();
				distribution.put("missings", missings);
				distribution.put("validResponses", validResponses);
//				jsonVariable.put("distribution", distribution);
			}

		}
		else if (type.equals("zofar:right")) {
			// single choice
			questionTypeDE = "Einfachnennung";
			questionTypeENG = "Single Choice";

			if (!aos.isEmpty()) {

				Set<Map<String, Object>> missings = new LinkedHashSet<Map<String, Object>>();
				Set<Map<String, Object>> validResponses = new LinkedHashSet<Map<String, Object>>();

				for (final Map<String, String> ao : aos) {
					final Map<String, Object> aoMap = new LinkedHashMap<String, Object>();
					final boolean missing = ((ao.containsKey("missing")) && (ao.get("missing").equals("true")));

					if (ao.containsKey("value"))
						aoMap.put("code", ao.get("value"));
					if (ao.containsKey("label")) {
						final Map<String, String> labelMap = new LinkedHashMap<String, String>();
						labelMap.put("de", ao.get("label"));
						labelMap.put("en", "");
						aoMap.put("label", labelMap);
					}

					if (missing) {
						missings.add(aoMap);
					} else {
						validResponses.add(aoMap);
					}
				}

				final Map<String, Object> distribution = new LinkedHashMap<String, Object>();
				distribution.put("missings", missings);
				distribution.put("validResponses", validResponses);
//				jsonVariable.put("distribution", distribution);
			}

		}
		else if (type.equals("zofar:answerOption")) {
			// multiple choice
			questionTypeDE = "Mehrfachnennung";
			questionTypeENG = "Multiple Choice";

			if (!aos.isEmpty()) {

				Set<Map<String, Object>> missings = new LinkedHashSet<Map<String, Object>>();
				Set<Map<String, Object>> validResponses = new LinkedHashSet<Map<String, Object>>();

				for (final Map<String, String> ao : aos) {
					final Map<String, Object> aoMap = new LinkedHashMap<String, Object>();
					final boolean missing = ((ao.containsKey("missing")) && (ao.get("missing").equals("true")));

					if (ao.containsKey("value"))
						aoMap.put("code", ao.get("value"));
					if (ao.containsKey("label")) {
						final Map<String, String> labelMap = new LinkedHashMap<String, String>();
						labelMap.put("de", ao.get("label"));
						labelMap.put("en", "");
						aoMap.put("label", labelMap);
					}

					if (missing) {
						missings.add(aoMap);
					} else {
						validResponses.add(aoMap);
					}
				}

				final Map<String, Object> distribution = new LinkedHashMap<String, Object>();
				distribution.put("missings", missings);
				distribution.put("validResponses", validResponses);
//				jsonVariable.put("distribution", distribution);
			}

		} else if (type.equals("zofar:questionOpen")) {
			// open
			questionTypeDE = "Offene Angabe";
			questionTypeENG = "Open";

			labelDE = "";
			labelENG = "";

		} else if (type.equals("zofar:attachedOpen")) {
			// open
			questionTypeDE = "Offene Angabe";
			questionTypeENG = "Open";

			labelDE = "";
			labelENG = "";

		} else if (type.equals("zofar:question")) {
			// open
			questionTypeDE = "Offene Angabe";
			questionTypeENG = "Open";

			labelDE = "";
			labelENG = "";

		} else if (type.equals("zofar:variable")) {
			// open
			questionTypeDE = "Abgeleitete Variable";
			questionTypeENG = "Derived Variable";

			labelDE = "";
			labelENG = "";

		}

		else {
			LOGGER.info("unkown type : {}", type);
		}

		final Map<String, String> typeMap = new LinkedHashMap<String, String>();
		typeMap.put("de", questionTypeDE);
		typeMap.put("en", questionTypeENG);
		jsonQuestion.put("type", typeMap);

		final Map<String, String> questionMap = new LinkedHashMap<String, String>();
		questionMap.put("de", questionDE);
		questionMap.put("en", questionENG);
		jsonQuestion.put("questionText", questionMap);

		final Map<String, String> instructionMap = new LinkedHashMap<String, String>();
		instructionMap.put("de", instructionDE);
		instructionMap.put("en", instructionENG);
		jsonQuestion.put("instruction", instructionMap);

		final Map<String, String> introductionMap = new LinkedHashMap<String, String>();
		introductionMap.put("de", introductionDE);
		introductionMap.put("en", introductionENG);
		jsonQuestion.put("introduction", introductionMap);

		final Map<String, String> labelMap = new LinkedHashMap<String, String>();
		labelMap.put("de", labelDE);
		labelMap.put("en", labelENG);
		jsonQuestion.put("label", labelMap);

		Map<String, Map<String, Object>> back = new HashMap<String, Map<String, Object>>();
		back.put("question", jsonQuestion);
//		back.put("variable", jsonVariable);
		return back;
	}

	private String getAdditional(XmlObject node) {
		if (node == null)
			return null;
		return CollectionClient.getInstance().implode(getQuestionHeader(node).toArray(), " ");
	}

//	private String getVariableId(final String variable, final String dataAcquestionProject) {
//		String id = dataAcquestionProject + "-" + variable;
//		return id;
//	}

	private String getQuestionId(final String instrumentId, final int index) {
		String id = instrumentId + "-" + index;
		return id;
	}

	private Set<String> getLabelHeader(final XmlObject question) {
		Set<String> fields = new HashSet<String>();
		fields.add("label");
		return retrieveHeaderField(question, fields);
	}

	private Set<String> getQuestionHeader(final XmlObject question) {
		Set<String> fields = new HashSet<String>();
		fields.add("question");
		fields.add("label");
		fields.add("title");
		return getHeaderHelper(question, fields);
	}

	private Set<String> getInstructionHeader(final XmlObject question) {
		Set<String> fields = new HashSet<String>();
		fields.add("instruction");
		return getHeaderHelper(question, fields);
	}

	private Set<String> getIntroductionHeader(final XmlObject question) {
		Set<String> fields = new HashSet<String>();
		fields.add("introduction");
		return getHeaderHelper(question, fields);
	}

	/**
	 * Gets the questions helper.
	 * 
	 * @param node
	 *            the node
	 * @return the questions helper
	 */
	private Set<String> getHeaderHelper(final XmlObject node, final Set<String> fields) {
		final Set<String> back = new LinkedHashSet<String>();
		if (node == null)
			return back;
		final XmlClient client = XmlClient.getInstance();
		back.addAll(getHeaderHelper(client.getParent(node), fields));
		back.addAll(retrieveHeaderField(node, fields));
		return back;
	}

	/**
	 * Retrieve header.
	 * 
	 * @param parentNode
	 *            the parent node
	 * @return the sets the
	 */
	private Set<String> retrieveHeaderField(final XmlObject parentNode, final Set<String> headerFields) {
		final Set<String> back = new LinkedHashSet<String>();
		final XmlObject[] childs = XmlClient.getInstance().getByXPath(parentNode, "./*");
		if (childs != null) {
			for (XmlObject child : childs) {
				if (!child.getDomNode().getNodeName().equals("zofar:header"))
					continue;
				final XmlObject[] headerItems = child.selectPath("./*");
				for (XmlObject headerItem : headerItems) {
					final String headerItemType = headerItem.getDomNode().getNodeName().replace("zofar:", "");
					if (!headerFields.contains(headerItemType))
						continue;
					back.add(ReplaceClient.getInstance().cleanedString(headerItem.newCursor().getTextValue()));
				}
			}
		}

		final XmlObject[] attributes = XmlClient.getInstance().getByXPath(parentNode, "./@*");
		if (attributes != null) {
			for (XmlObject attribute : attributes) {
				final String attributeType = attribute.getDomNode().getNodeName();
				if (!headerFields.contains(attributeType))
					continue;
				back.add(ReplaceClient.getInstance().cleanedString(attribute.newCursor().getTextValue()));
			}
		}
		return back;
	}

//	private String getUIDPath(XmlObject node) {
//		List<XmlObject> path = getPath(node);
//		if (path == null)
//			return null;
//		final StringBuffer back = new StringBuffer();
//		final Iterator<XmlObject> it = path.iterator();
//		boolean first = true;
//		while (it.hasNext()) {
//			XmlObject tmp = it.next();
//			XmlObject uidObj = getUIDObj(tmp);
//			if (!first)
//				back.append(":");
//			if (uidObj != null) {
//
//				back.append(uidObj.newCursor().getTextValue());
//
//			} else {
//				back.append("XX");
//			}
//			first = false;
//		}
//		return back.toString();
//	}

//	private XmlObject getUIDObj(XmlObject obj) {
//		if (obj == null)
//			return null;
//		final XmlClient client = XmlClient.getInstance();
//		XmlObject[] resultSet = client.getByXPath(obj, "@uid");
//		if ((resultSet != null) && (resultSet.length > 0)) {
//			return resultSet[0];
//		}
//		return null;
//	}
//
//	private List<XmlObject> getPath(XmlObject node) {
//		if (node == null)
//			return null;
//		final XmlClient client = XmlClient.getInstance();
//		final String type = node.getDomNode().getNodeName();
//		// LOGGER.info("path type : {}",type);
//		if (!type.startsWith("zofar:"))
//			return null;
//		List<XmlObject> back = new ArrayList<XmlObject>();
//		List<XmlObject> parentPath = getPath(client.getParent(node));
//		if (parentPath != null)
//			back.addAll(parentPath);
//		back.add(node);
//		return back;
//	}

	// private Map<String, String> getAnswerOptionAttributes(XmlObject node) {
	// if (node == null)
	// return null;
	// final String type = node.getDomNode().getNodeName();
	// if (!type.equals("zofar:answerOption"))
	// return null;
	//
	// return getAttributes(node);
	// }

	private Map<String, String> getAnswerOptionAttributes(XmlObject node) {
		if (node == null)
			return null;
		final String type = node.getDomNode().getNodeName();
		if (!type.equals("zofar:answerOption"))
			return null;
		final XmlClient client = XmlClient.getInstance();

		Map<String, String> attributes = getAttributes(node);

		if (client.hasParent(node.getDomNode(), "zofar:item") && (!attributes.containsKey("label"))) {
			String position = "DEFAULT";
			if (client.hasParent(node.getDomNode(), "zofar:left"))
				position = "LEFT";
			if (client.hasParent(node.getDomNode(), "zofar:right"))
				position = "RIGHT";

			XmlObject parent = node;
			if (position.equals("DEFAULT")) {
				while ((parent != null) && (!parent.getDomNode().getNodeName().equals("zofar:responseDomain")))
					parent = client.getParent(parent);
			} else if (position.equals("LEFT")) {
				while ((parent != null) && (!parent.getDomNode().getNodeName().equals("zofar:left")))
					parent = client.getParent(parent);
			} else if (position.equals("RIGHT")) {
				while ((parent != null) && (!parent.getDomNode().getNodeName().equals("zofar:right")))
					parent = client.getParent(parent);
			}
			List<XmlObject> aos = getAnswerOptions(parent);
			int index = -1;
			if (aos != null) {
				index = aos.indexOf(node);
			}

			if (index != -1) {
				parent = client.getParent(parent);
				while ((parent != null) && (!parent.getDomNode().getNodeName().equals("zofar:responseDomain")))
					parent = client.getParent(parent);
				final XmlObject[] matrixRdcChilds = XmlClient.getInstance().getByXPath(parent, "./*");
				if (matrixRdcChilds != null) {
					final List<String> labels = new ArrayList<String>();

					for (XmlObject child : matrixRdcChilds) {
						if (position.equals("DEFAULT")) {
							if (child.getDomNode().getNodeName().equals("zofar:header")) {
								final XmlObject[] headerChilds = XmlClient.getInstance().getByXPath(child, "./*");
								if (headerChilds != null) {
									for (XmlObject headerChild : headerChilds) {
										labels.add(ReplaceClient.getInstance()
												.cleanedString(headerChild.newCursor().getTextValue()));
									}
								}
							} else if (child.getDomNode().getNodeName().equals("zofar:missingHeader")) {
								final XmlObject[] headerChilds = XmlClient.getInstance().getByXPath(child, "./*");
								if (headerChilds != null) {
									for (XmlObject headerChild : headerChilds) {
										labels.add(ReplaceClient.getInstance()
												.cleanedString(headerChild.newCursor().getTextValue()));
									}
								}
							} else {

							}
						} else if (position.equals("LEFT")) {
							if (child.getDomNode().getNodeName().equals("zofar:leftHeader")) {
								final XmlObject[] headerChilds = XmlClient.getInstance().getByXPath(child, "./*");
								if (headerChilds != null) {
									for (XmlObject headerChild : headerChilds) {
										labels.add(ReplaceClient.getInstance()
												.cleanedString(headerChild.newCursor().getTextValue()));
									}
								}
							}
						} else if (position.equals("RIGHT")) {
							if (child.getDomNode().getNodeName().equals("zofar:rightHeader")) {
								final XmlObject[] headerChilds = XmlClient.getInstance().getByXPath(child, "./*");
								if (headerChilds != null) {
									for (XmlObject headerChild : headerChilds) {
										labels.add(ReplaceClient.getInstance()
												.cleanedString(headerChild.newCursor().getTextValue()));
									}
								}
							}
						}
					}
					attributes.put("label", labels.get(index));
				}
			}
		}
		return attributes;
	}

	// private Map<String, String> getAnswerOptionAttributes(XmlObject node) {
	// if (node == null)
	// return null;
	// final String type = node.getDomNode().getNodeName();
	// if (!type.equals("zofar:answerOption"))
	// return null;
	// final XmlClient client = XmlClient.getInstance();
	//
	// Map<String, String> attributes = getAttributes(node);
	// System.out.println("Attributes : " + attributes);
	//
	// // if(client.hasParent(node.getDomNode(), "zofar:matrixDouble")){
	// //
	// // }
	//
	// if (!attributes.containsKey("label")) {
	// final List<String> labels = new ArrayList<String>();
	//
	// XmlObject current = node;
	// while (current != null) {
	// final String currentType = current.getDomNode().getNodeName();
	// if(currentType.equals("zofar:body"))break;
	// if(currentType.equals("zofar:section"))break;
	//
	//
	//
	//
	// current = client.getParent(current);
	// }
	// }
	//
	// if (client.hasParent(node.getDomNode(), "zofar:item") &&
	// (!attributes.containsKey("label"))) {
	// // XmlObject parent = node;
	// // while ((parent != null) &&
	// // (!parent.getDomNode().getNodeName().equals("zofar:responseDomain")))
	// // parent = client.getParent(parent);
	// //// System.out.println("Parent1 Type :
	// // "+parent.getDomNode().getNodeName());
	// // List<XmlObject> aos = getAnswerOptions(parent);
	// // int index = -1;
	// // if (aos != null) {
	// // index = aos.indexOf(node);
	// // }
	// //
	// // if (index != -1) {
	// // if(parent.getDomNode().getNodeName().equals("zofar:matrixDouble")){
	// //
	// //
	// // }
	// // else{
	// // parent = client.getParent(parent);
	// //
	// // while ((parent != null) &&
	// // (!parent.getDomNode().getNodeName().equals("zofar:responseDomain"))){
	// // parent = client.getParent(parent);
	// // }
	// //
	// // final XmlObject[] matrixRdcChilds =
	// // XmlClient.getInstance().getByXPath(parent, "./*");
	// // if (matrixRdcChilds != null) {
	// // final List<String> labels = new ArrayList<String>();
	// // for (XmlObject child : matrixRdcChilds) {
	// // if (child.getDomNode().getNodeName().equals("zofar:header")) {
	// // final XmlObject[] headerChilds =
	// // XmlClient.getInstance().getByXPath(child, "./*");
	// // if (headerChilds != null) {
	// // for (XmlObject headerChild : headerChilds) {
	// // labels.add(ReplaceClient.getInstance()
	// // .cleanedString(headerChild.newCursor().getTextValue()));
	// // }
	// // }
	// // } else if
	// // (child.getDomNode().getNodeName().equals("zofar:missingHeader"))
	// // {
	// // final XmlObject[] headerChilds =
	// // XmlClient.getInstance().getByXPath(child, "./*");
	// // if (headerChilds != null) {
	// // for (XmlObject headerChild : headerChilds) {
	// // labels.add(ReplaceClient.getInstance()
	// // .cleanedString(headerChild.newCursor().getTextValue()));
	// // }
	// // }
	// // }
	// // else if
	// // (child.getDomNode().getNodeName().equals("zofar:leftHeader")) {
	// // final XmlObject[] headerChilds =
	// // XmlClient.getInstance().getByXPath(child, "./*");
	// // if (headerChilds != null) {
	// // for (XmlObject headerChild : headerChilds) {
	// // labels.add(ReplaceClient.getInstance()
	// // .cleanedString(headerChild.newCursor().getTextValue()));
	// // }
	// // }
	// // }
	// // else if
	// // (child.getDomNode().getNodeName().equals("zofar:rightHeader")) {
	// // final XmlObject[] headerChilds =
	// // XmlClient.getInstance().getByXPath(child, "./*");
	// // if (headerChilds != null) {
	// // for (XmlObject headerChild : headerChilds) {
	// // labels.add(ReplaceClient.getInstance()
	// // .cleanedString(headerChild.newCursor().getTextValue()));
	// // }
	// // }
	// // }
	// // else {
	// // System.out.println("Else Child :
	// // "+child.getDomNode().getNodeName());
	// // }
	// // }
	// // attributes.put("label", labels.get(index));
	// // }
	// // }
	// //
	// // }
	// }
	// return attributes;
	// }

	private Map<String, String> getAttributes(XmlObject node) {
		if (node == null)
			return null;
		final String type = node.getDomNode().getNodeName();
		if (!type.startsWith("zofar:"))
			return null;
		final Map<String, String> back = new HashMap<String, String>();

		final XmlObject[] attributes = XmlClient.getInstance().getByXPath(node, "./@*");
		if (attributes != null) {
			for (XmlObject attribute : attributes) {
				final String attributeType = attribute.getDomNode().getNodeName();
				back.put(attributeType,
						ReplaceClient.getInstance().cleanedString(attribute.newCursor().getTextValue()));
			}
		}
		return back;
	}

	private List<XmlObject> getAnswerOptions(XmlObject node) {
		if (node == null)
			return null;
		final XmlObject[] childs = XmlClient.getInstance().getByXPath(node, "descendant::*");
		if (childs != null) {
			final List<XmlObject> back = new ArrayList<XmlObject>();
			for (XmlObject child : childs) {
				if (!child.getDomNode().getNodeName().equals("zofar:answerOption"))
					continue;
				back.add(child);
			}
			return back;
		}
		return null;
	}

	private List<XmlObject> getNext(final XmlObject current, XmlObject xmlDoc) {
		if (current == null)
			return null;
		final XmlClient client = XmlClient.getInstance();
		List<XmlObject> back = new ArrayList<XmlObject>();
		XmlObject item = client.getNextSibling(current);
		if (item != null) {
			XmlObject varObj = getVariableObjOnPage(item, false);
			if (varObj != null) {
				// back.add(item);
				// parent of varObj
				XmlObject parObj = client.getParent(varObj);
				while (parObj != null) {
					final String parType = parObj.getDomNode().getNodeName();
					if (parType.equals("zofar:responseDomain")) {
						parObj = client.getParent(parObj);
						break;
					} else if (parType.equals("zofar:answerOption"))
						break;
					else if (parType.equals("zofar:questionOpen"))
						break;
					else if (parType.equals("zofar:attachedOpen"))
						break;
					else if (parType.equals("zofar:question"))
						break;
					else if (parType.equals("zofar:variable"))
						break;
					parObj = client.getParent(parObj);
				}
				back.add(parObj);

			} else
				back.addAll(getNext(item, xmlDoc));
		} else {
			XmlObject parentObj = client.getParent(current);
			if (parentObj != null) {
				final String parentType = parentObj.getDomNode().getNodeName();
				if (parentType.equals("zofar:page")) {
					// page switch
					// follow all transitions on this page
					List<XmlObject> trans = getTransitionsFrom(parentObj, xmlDoc);
					if (trans != null) {
						for (final XmlObject nextPage : trans) {
							XmlObject varObj = getVariableObjOnPage(nextPage, false);
							if (varObj != null)
								back.add(varObj);
						}
					}
				} else {
					List<XmlObject> varObj = getNext(parentObj, xmlDoc);
					if ((varObj != null) && (!varObj.isEmpty()))
						back.addAll(varObj);
				}
			}
		}
		return back;
	}

	private XmlObject getVariableObjOnPage(XmlObject obj, final boolean last) {
		if (obj == null)
			return null;
		final XmlClient client = XmlClient.getInstance();
		XmlObject[] resultSet = client.getByXPath(obj, "descendant::*/@variable");
		if ((resultSet != null) && (resultSet.length > 0)) {
			final int count = resultSet.length;
			if (last)
				return resultSet[count - 1];
			else
				return resultSet[0];
		}
		return null;
	}

	private List<XmlObject> getTransitionsFrom(XmlObject obj, XmlObject xmlDoc) {
		if (obj == null)
			return null;
		if (!obj.getDomNode().getNodeName().equals("zofar:page"))
			return null;

		final XmlClient client = XmlClient.getInstance();
		XmlObject[] resultSet = client.getByXPath(obj, "./zofar:transitions/*/@target");
		final List<XmlObject> back = new ArrayList<XmlObject>();
		if ((resultSet != null) && (resultSet.length > 0)) {
			for (final XmlObject found : resultSet) {
				final String targetUID = found.newCursor().getTextValue();
				XmlObject[] pages = client.getByXPath(xmlDoc, "//*[@uid='" + targetUID + "']");
				if (pages != null) {
					for (XmlObject page : pages) {
						if (page.getDomNode().getNodeName().equals("zofar:page"))
							back.add(page);
					}
				}
			}
		}
		return back;
	}
}

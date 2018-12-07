package de.his.zofar.generator.maven.plugin.generator.mdm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.json.JsonValue;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.schema.beans.BeansDocument;

import de.his.zofar.generator.maven.plugin.generator.AbstractGenerator;
import eu.dzhw.zofar.management.comm.json.JSONClient;
import eu.dzhw.zofar.management.utils.objects.CollectionClient;
import eu.dzhw.zofar.management.utils.string.ReplaceClient;
import eu.dzhw.zofar.management.utils.xml.XmlClient;

public class MDMGenerator extends AbstractGenerator<BeansDocument> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MDMGenerator.class);
	private final JSONClient JSON;
	
	public enum JSON_TYPE {VARIABLE,QUESTION};
	public enum CSV_TYPE {VARIABLE,QUESTION};

	private final XmlOptions opts;

	public MDMGenerator() {
		super(BeansDocument.Factory.newInstance());
		JSON = JSONClient.getInstance();
		opts = new XmlOptions();
		opts.setCharacterEncoding("utf8");
		// opts.setSaveUseOpenFrag();
		// opts.setSavePrettyPrint();
		// opts.setSavePrettyPrintIndent(0);
	}
	
	public Map<JSON_TYPE, JsonValue> writeJSON(final XmlObject doc, final XmlObject node, final int serial,
			final String dataAcquestionProject, final String surveyId, final String instrumentId,
			final String datasetId) {
		if (node == null)
			return null;
		// Extract data from question
		// create structured Map
		final Map<String, Map<String, Object>> structuredData = getDataTable(doc, node, serial, dataAcquestionProject,
				surveyId, instrumentId, datasetId);

		final Map<JSON_TYPE,JsonValue> back = new HashMap<JSON_TYPE,JsonValue>();
		
		// create JSON Objects
		final JsonValue jsonVariableObj = JSON.createObject(structuredData.get("variable"));

		if (jsonVariableObj != null) back.put(JSON_TYPE.VARIABLE, jsonVariableObj);
		// LOGGER.info("JSON variable: {}", jsonVariableObj);

		final JsonValue jsonQuestionObj = JSON.createObject(structuredData.get("question"));
		//
		if (jsonQuestionObj != null) back.put(JSON_TYPE.QUESTION, jsonQuestionObj);
		// LOGGER.info("JSON question: {}", jsonQuestionObj);

		// convert JSON Object to String and write to single File in Directory
		// pointed by path
		return back;
	}

	public Map<CSV_TYPE, String> writeCSV(final XmlObject doc, final XmlObject node, final int serial,
			final String dataAcquestionProject, final String surveyId, final String instrumentId,
			final String datasetId) {
		if (node == null)
			return null;
		// Extract data from question
		// create structured Map
		final Map<String, Map<String, Object>> structuredData = getDataTable(doc, node, serial, dataAcquestionProject,
				surveyId, instrumentId, datasetId);

		final Map<CSV_TYPE,String> back = new HashMap<CSV_TYPE,String>();
		return back;
	}

	private Map<String, Object> getObjAsTable(final Object node, final String dataAcquestionProject,
			final String surveyId, final String instrumentId, final String datasetId,final int serial) {
		if (node == null)
			return null;
		final Map<String, Object> structuredData = new LinkedHashMap<String, Object>();
		if ((XmlObject.class).isAssignableFrom(node.getClass())) {
			structuredData.put("xmlObj", (XmlObject) node);
			structuredData.put("vid", getVariableId((XmlObject) node, dataAcquestionProject, surveyId, instrumentId, datasetId));
			structuredData.put("qid", getQuestionId((XmlObject) node, dataAcquestionProject, surveyId, instrumentId, datasetId,serial));

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
								structuredData.put(name,
										getObjAsTable(value, dataAcquestionProject, surveyId, instrumentId, datasetId,serial));
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
					rebuildTmp[a] = getObjAsTable(tmp[a], dataAcquestionProject, surveyId, instrumentId, datasetId,serial);
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
		final Map<String, Object> structuredData = getObjAsTable(node, dataAcquestionProject, surveyId, instrumentId,
				datasetId,serial);
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
		final Map<String, Object> jsonVariable = new LinkedHashMap<String, Object>();

		if (structuredData.containsKey("qid")) {
			jsonQuestion.put("id", (String) structuredData.get("qid"));
		}
		
		if (structuredData.containsKey("vid")) {
			jsonVariable.put("id", (String) structuredData.get("vid"));
		}

		jsonQuestion.put("number", serial);

		if (structuredData.containsKey("variable")) {
			jsonVariable.put("name", (String) structuredData.get("variable"));

			String[] variableIds = new String[] { (String) structuredData.get("variable") };
			jsonQuestion.put("variableIds", variableIds);

		}

		jsonQuestion.put("surveyId", surveyId);
		jsonQuestion.put("instrumentId", instrumentId);

		String[] surveyIds = new String[] { surveyId };
		jsonVariable.put("surveyIds", surveyIds);

		String[] datasetIds = new String[] { datasetId };
		jsonVariable.put("datasetIds", datasetIds);

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
				aoObjs.addAll(this.getAnswerOptions(tmp));
			}

			if (aoObjs != null) {
				for (final XmlObject aoObj : aoObjs) {
					aos.add(this.getAnswerOptionAttributes(aoObj));
				}
			}

			// final List<XmlObject> previousList = this.getPrevious(tmp,doc);
			// if (previousList != null){
			// Object[] rebuildTmp = new Object[previousList.size()];
			// if ((previousList != null)&&(!previousList.isEmpty())) {
			// int index = 0;
			// for (XmlObject previous :previousList){
			// final String previousType = previous.getDomNode().getNodeName();
			// if(previousType.equals("variable"))previous =
			// client.getParent(previous);
			// rebuildTmp[index] = getId(previous);
			// index = index + 1;
			// }
			// jsonQuestion.put("predecessor", rebuildTmp);
			// }
			// }
			final List<XmlObject> nextList = this.getNext(tmp, doc);
			if (nextList != null) {
				Object[] rebuildTmp = new Object[nextList.size()];
				if ((nextList != null) && (!nextList.isEmpty())) {
					int index = 0;
					for (XmlObject next : nextList) {
						final String nextType = next.getDomNode().getNodeName();
						if (nextType.equals("variable"))
							next = client.getParent(next);
						rebuildTmp[index] = getQuestionId(next, dataAcquestionProject, surveyId, instrumentId, datasetId,serial);
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
				jsonVariable.put("distribution", distribution);
			}

		} else if (type.equals("zofar:answerOption")) {
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
				jsonVariable.put("distribution", distribution);
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
		back.put("variable", jsonVariable);
		return back;
	}

	private String getAdditional(XmlObject node) {
		if (node == null)
			return null;
		return CollectionClient.getInstance().implode(getQuestionHeader(node).toArray(), " ");
	}

	private String getVariableId(XmlObject node, final String dataAcquestionProject, final String surveyId,
			final String instrumentId, final String datasetId) {
		if (node == null)
			return null;
		final String type = node.getDomNode().getNodeName();
		
		if(type.equals("zofar:responseDomain")){}
		else if(type.equals("zofar:answerOption")){}
		else if(type.equals("zofar:question")){}
		else if(type.equals("zofar:questionOpen")){}
		else if(type.equals("zofar:attachedOpen")){}
		else{
			System.out.println("ID Variable Type : " + type);
		}
		return getUIDPath(node);
	}
	
	private String getQuestionId(XmlObject node, final String dataAcquestionProject, final String surveyId,
			final String instrumentId, final String datasetId,final int index) {
		if (node == null)
			return null;
		final String type = node.getDomNode().getNodeName();
		
		if(type.equals("zofar:responseDomain")){}
		else if(type.equals("zofar:answerOption")){}
		else if(type.equals("zofar:question")){}
		else if(type.equals("zofar:questionOpen")){}
		else if(type.equals("zofar:attachedOpen")){}
		else{
			System.out.println("ID Question Type : " + type);
		}
		return getUIDPath(node);
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

	private String getUIDPath(XmlObject node) {
		List<XmlObject> path = getPath(node);
		if (path == null)
			return null;
		final StringBuffer back = new StringBuffer();
		final Iterator<XmlObject> it = path.iterator();
		boolean first = true;
		while (it.hasNext()) {
			XmlObject tmp = it.next();
			XmlObject uidObj = getUIDObj(tmp);
			if (!first)
				back.append(":");
			if (uidObj != null) {

				back.append(uidObj.newCursor().getTextValue());

			} else {
				back.append("XX");
			}
			first = false;
		}
		return back.toString();
	}

	private XmlObject getUIDObj(XmlObject obj) {
		if (obj == null)
			return null;
		final XmlClient client = XmlClient.getInstance();
		XmlObject[] resultSet = client.getByXPath(obj, "@uid");
		if ((resultSet != null) && (resultSet.length > 0)) {
			return resultSet[0];
		}
		return null;
	}

	private List<XmlObject> getPath(XmlObject node) {
		if (node == null)
			return null;
		final XmlClient client = XmlClient.getInstance();
		final String type = node.getDomNode().getNodeName();
		// LOGGER.info("path type : {}",type);
		if (!type.startsWith("zofar:"))
			return null;
		List<XmlObject> back = new ArrayList<XmlObject>();
		List<XmlObject> parentPath = getPath(client.getParent(node));
		if (parentPath != null)
			back.addAll(parentPath);
		back.add(node);
		return back;
	}

	// private Map<String, String> getAnswerOptionAttributes(XmlObject node) {
	// if (node == null)
	// return null;
	// final String type = node.getDomNode().getNodeName();
	// if (!type.equals("zofar:answerOption"))
	// return null;
	//
	// return getAttributes(node);
	// }

	private Map<String, String> getOpenAttributes(XmlObject node) {
		if (node == null)
			return null;

		Map<String, String> attributes = getAttributes(node);

		final XmlObject[] childs = XmlClient.getInstance().getByXPath(node, "./*");
		if (childs != null) {
			final List<String> prefixes = new ArrayList<String>();
			final List<String> postfixes = new ArrayList<String>();

			for (XmlObject child : childs) {

				if (child.getDomNode().getNodeName().equals("zofar:prefix")) {
					final XmlObject[] headerChilds = XmlClient.getInstance().getByXPath(child, "./*");
					if (headerChilds != null) {
						for (XmlObject headerChild : headerChilds) {
							prefixes.add(
									ReplaceClient.getInstance().cleanedString(headerChild.newCursor().getTextValue()));
						}
					}
				} else if (child.getDomNode().getNodeName().equals("zofar:postfix")) {
					final XmlObject[] headerChilds = XmlClient.getInstance().getByXPath(child, "./*");
					if (headerChilds != null) {
						for (XmlObject headerChild : headerChilds) {
							postfixes.add(
									ReplaceClient.getInstance().cleanedString(headerChild.newCursor().getTextValue()));
						}
					}
				} else {

				}
			}
			attributes.put("label", prefixes + " ___ " + postfixes);
		}
		return attributes;
	}

	private Map<String, String> getAnswerOptionAttributes(XmlObject node) {
		if (node == null)
			return null;
		final String type = node.getDomNode().getNodeName();
		if (!type.equals("zofar:answerOption"))
			return null;
		final XmlClient client = XmlClient.getInstance();

		Map<String, String> attributes = getAttributes(node);

		if (client.hasParent(node.getDomNode(), "zofar:item") && (!attributes.containsKey("label"))) {
			XmlObject parent = node;
			while ((parent != null) && (!parent.getDomNode().getNodeName().equals("zofar:responseDomain")))
				parent = client.getParent(parent);

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
						if (!child.getDomNode().getNodeName().equals("zofar:header")) {
							final XmlObject[] headerChilds = XmlClient.getInstance().getByXPath(child, "./*");
							if (headerChilds != null) {
								for (XmlObject headerChild : headerChilds) {
									labels.add(ReplaceClient.getInstance()
											.cleanedString(headerChild.newCursor().getTextValue()));
								}
							}
						} else if (!child.getDomNode().getNodeName().equals("zofar:missingHeader")) {
							final XmlObject[] headerChilds = XmlClient.getInstance().getByXPath(child, "./*");
							if (headerChilds != null) {
								for (XmlObject headerChild : headerChilds) {
									labels.add(ReplaceClient.getInstance()
											.cleanedString(headerChild.newCursor().getTextValue()));
								}
							}
						} else {

						}
					}
					attributes.put("label", labels.get(index));
				}
			}
		}
		return attributes;
	}

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
		final XmlObject[] childs = XmlClient.getInstance().getByXPath(node, "./*");
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

	// private List<XmlObject> getPrevious(final XmlObject current, XmlObject
	// xmlDoc) {
	// if (current == null)
	// return null;
	// final XmlClient client = XmlClient.getInstance();
	// List<XmlObject> back = new ArrayList<XmlObject>();
	// XmlObject item = client.getPreviousSibling(current);
	// if (item != null) {
	// XmlObject varObj = getVariableObjOnPage(item, true);
	// if (varObj != null)
	// back.add(item);
	// else
	// back.addAll(getPrevious(item, xmlDoc));
	// } else {
	//
	// XmlObject parentObj = client.getParent(current);
	// if (parentObj != null) {
	// final String parentType = parentObj.getDomNode().getNodeName();
	// if (parentType.equals("zofar:page")) {
	// // page switch
	// // retrieve all transitions with target thispage.uid
	// List<XmlObject> trans = getTransitionsTo(parentObj, xmlDoc);
	// if (trans != null) {
	// for (final XmlObject prevPage : trans) {
	// XmlObject varObj = getVariableObjOnPage(prevPage, true);
	// if (varObj != null)
	// back.add(varObj);
	// }
	// }
	// } else {
	// List<XmlObject> varObj = getPrevious(parentObj, xmlDoc);
	// if ((varObj != null) && (!varObj.isEmpty()))
	// back.addAll(varObj);
	// }
	// }
	// }
	// return back;
	// }

	private List<XmlObject> getNext(final XmlObject current, XmlObject xmlDoc) {
		if (current == null)
			return null;
		final XmlClient client = XmlClient.getInstance();
		List<XmlObject> back = new ArrayList<XmlObject>();
		XmlObject item = client.getNextSibling(current);
		if (item != null) {
			XmlObject varObj = getVariableObjOnPage(item, false);
			if (varObj != null){
//				back.add(item);
				//parent of varObj
				XmlObject parObj = client.getParent(varObj);
				while(parObj != null){
					final String parType = parObj.getDomNode().getNodeName();
					if(parType.equals("zofar:responseDomain")){
						parObj = client.getParent(parObj);
						break;
					}
					else if(parType.equals("zofar:answerOption"))break;
					else if(parType.equals("zofar:questionOpen"))break;
					else if(parType.equals("zofar:attachedOpen"))break;
					else if(parType.equals("zofar:question"))break;
					else if(parType.equals("zofar:variable"))break;
					parObj = client.getParent(parObj);
				}
				back.add(parObj);
			}
			else
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

	// private List<XmlObject> getTransitionsTo(XmlObject obj, XmlObject xmlDoc)
	// {
	// if (obj == null)
	// return null;
	// if (!obj.getDomNode().getNodeName().equals("zofar:page"))
	// return null;
	//
	// final XmlClient client = XmlClient.getInstance();
	//
	// // retieve page uid
	// final List<XmlObject> back = new ArrayList<XmlObject>();
	// XmlObject[] resultSet = client.getByXPath(obj, "attribute::uid");
	// if ((resultSet != null) && (resultSet.length == 1)) {
	// final String pageUID = resultSet[0].newCursor().getTextValue();
	// XmlObject[] transitions = client.getByXPath(xmlDoc, "//*[@target='" +
	// pageUID + "']");
	// if (transitions != null) {
	// for (XmlObject transition : transitions) {
	// if (transition.getDomNode().getNodeName().equals("zofar:transition")) {
	// final XmlObject sourcePage =
	// client.getParent(client.getParent(transition));
	// if ((sourcePage != null) &&
	// (sourcePage.getDomNode().getNodeName().equals("zofar:page"))) {
	// back.add(sourcePage);
	// }
	// }
	// }
	// }
	// }
	// return back;
	// }

	// /**
	// * saves the document.
	// *
	// * @param path
	// * the path in which the document will be saved.
	// * @throws IOException
	// */
	// public final void saveDocument(final String path) throws IOException {
	// // setting some options to customize the file
	// final XmlOptions options = new XmlOptions();
	// options.setSavePrettyPrint();
	// options.setUseDefaultNamespace();
	//
	// // if (!this.validate()) {
	// // throw new IllegalStateException("the document ist not a valid XML
	// // document. File: " + FILE_NAME);
	// // }
	// //
	// // this.saveDocument(path, FILE_NAME, options);
	// }

}

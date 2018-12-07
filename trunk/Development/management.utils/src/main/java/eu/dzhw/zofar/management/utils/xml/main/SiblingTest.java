package eu.dzhw.zofar.management.utils.xml.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.w3c.dom.Document;

import eu.dzhw.zofar.management.utils.string.ReplaceClient;
import eu.dzhw.zofar.management.utils.xml.XmlClient;

public class SiblingTest {

	public static void main(String[] args) throws Exception {
		final String path = "xxx/questionnaire.xml";

		final XmlClient client = XmlClient.getInstance();
		final Document doc = client.getDocument(path);
		XmlObject xmlDoc = client.docToXmlObject(doc);
		if (xmlDoc != null) {
			final Map<String, List<XmlObject>> indexMap = client.index(xmlDoc);
			if (indexMap != null) {
				for (Map.Entry<String, List<XmlObject>> index : indexMap.entrySet()) {
					final List<XmlObject> objects = index.getValue();
					for (XmlObject obj : objects) {

						final String type = obj.getDomNode().getNodeName();
						if (!type.startsWith("zofar:"))
							continue;
						final String variable = (String) client.getAttribute(obj, "variable");
						boolean skip = true;
						// if ((variable != null) &&
						// (variable.equals("soft_rem_1")))skip = false;
						if ((variable != null) && (variable.equals("xxx")))
							skip = false;

						if (skip)
							continue;

						XmlObject parent = client.getParent(obj);
						// List<XmlObject> previousList = getPrev(obj,xmlDoc);
						List<XmlObject> nextList = getNext(obj, xmlDoc);
						System.out.println("-----------------------------------------------");
						// System.out.println("variable : "+variable);
						System.out.println("obj : " + obj);
						if (nextList != null) {
							for (final XmlObject next : nextList) {
								System.out.println("next : ("+getUIDPath(next)+") " + next);
							}
						}
						
//						final List<XmlObject> aos = getAnswerOptions(obj);
//						if(aos != null){
//							for(final XmlObject ao:aos){
//								System.out.println("ao : " + getAnswerOptionAttributes(ao));
//							}
//						}
						
						

					}
				}
			}
		}
	}
	
	private static Map<String,String> getOpenAttributes(XmlObject node) {
		if (node == null)
			return null;
		
		final XmlClient client = XmlClient.getInstance(); 
		Map<String,String> attributes = getAttributes(node);

		final XmlObject[] childs = XmlClient.getInstance().getByXPath(node, "./*");
		if (childs != null) {
			final List<String> prefixes = new ArrayList<String>();
			final List<String> postfixes = new ArrayList<String>();

			for (XmlObject child : childs) {

				if(child.getDomNode().getNodeName().equals("zofar:prefix")){
					final XmlObject[] headerChilds = XmlClient.getInstance().getByXPath(child, "./*");
					if (headerChilds != null) {
						for (XmlObject headerChild : headerChilds) {
							prefixes.add(ReplaceClient.getInstance().cleanedString(headerChild.newCursor().getTextValue()));
						}
					}
				}
				else if(child.getDomNode().getNodeName().equals("zofar:postfix")){
					final XmlObject[] headerChilds = XmlClient.getInstance().getByXPath(child, "./*");
					if (headerChilds != null) {
						for (XmlObject headerChild : headerChilds) {
							postfixes.add(ReplaceClient.getInstance().cleanedString(headerChild.newCursor().getTextValue()));
						}
					}
				}
				else{
					
				}
			}
			attributes.put("label", prefixes+" ___ "+postfixes);
		}
		return attributes;
	}
	
	private static Map<String,String> getAnswerOptionAttributes(XmlObject node) {
		if (node == null)
			return null;
		final String type = node.getDomNode().getNodeName();
		if(!type.equals("zofar:answerOption"))return null;
		final XmlClient client = XmlClient.getInstance(); 
		
		Map<String,String> attributes = getAttributes(node);
		
		if(client.hasParent(node.getDomNode(), "zofar:item")){
			XmlObject parent = node;
			while((parent != null)&&(!parent.getDomNode().getNodeName().equals("zofar:responseDomain")))parent = client.getParent(parent);
			
			List<XmlObject> aos = getAnswerOptions(parent);
			int index = -1; 
			if(aos != null){
				index = aos.indexOf(node);
			}
		
			if(index != -1){
				parent = client.getParent(parent);
				while((parent != null)&&(!parent.getDomNode().getNodeName().equals("zofar:responseDomain")))parent = client.getParent(parent);
				final XmlObject[] matrixRdcChilds = XmlClient.getInstance().getByXPath(parent, "./*");
				if (matrixRdcChilds != null) {
					final List<String> labels = new ArrayList<String>();
					for (XmlObject child : matrixRdcChilds) {
						if(!child.getDomNode().getNodeName().equals("zofar:header")){
							final XmlObject[] headerChilds = XmlClient.getInstance().getByXPath(child, "./*");
							if (headerChilds != null) {
								for (XmlObject headerChild : headerChilds) {
									labels.add(ReplaceClient.getInstance().cleanedString(headerChild.newCursor().getTextValue()));
								}
							}
						}
						else if(!child.getDomNode().getNodeName().equals("zofar:missingHeader")){
							final XmlObject[] headerChilds = XmlClient.getInstance().getByXPath(child, "./*");
							if (headerChilds != null) {
								for (XmlObject headerChild : headerChilds) {
									labels.add(ReplaceClient.getInstance().cleanedString(headerChild.newCursor().getTextValue()));
								}
							}
						}
						else{
							
						}
					}
					attributes.put("label", labels.get(index));
				}
			}
		}
		return attributes;
	}
	
	private static Map<String,String> getAttributes(XmlObject node) {
		if (node == null)
			return null;
		final String type = node.getDomNode().getNodeName();
		if(!type.startsWith("zofar:"))return null;
		final Map<String,String> back = new HashMap<String,String>();
		
		final XmlObject[] attributes = XmlClient.getInstance().getByXPath(node, "./@*");
		if (attributes != null) {
			for (XmlObject attribute : attributes) {
				final String attributeType = attribute.getDomNode().getNodeName();
				back.put(attributeType,ReplaceClient.getInstance().cleanedString(attribute.newCursor().getTextValue()));
			}
		}
		return back;
	}
	
	private static List<XmlObject> getAnswerOptions(XmlObject node) {
		if (node == null)
			return null;
		final XmlObject[] childs = XmlClient.getInstance().getByXPath(node, "./*");
		if (childs != null) {
			final List<XmlObject> back = new ArrayList<XmlObject>();
			for (XmlObject child : childs) {
				if(!child.getDomNode().getNodeName().equals("zofar:answerOption"))continue;
				back.add(child);
			}
			return back;
		}
		return null;
	}

	/**
	 * Gets the questions.
	 * 
	 * @param qmlPath
	 *            the qml path
	 * @return the questions
	 * @throws Exception
	 */
	private static Set<String> getQuestionHeader(final XmlObject question) throws Exception {
		Set<String> fields = new HashSet<String>();
		fields.add("question");
		fields.add("label");
		fields.add("title");
		return getHeaderHelper(question,fields);
	}

	/**
	 * Gets the questions helper.
	 * 
	 * @param node
	 *            the node
	 * @return the questions helper
	 */
	private static Set<String> getHeaderHelper(final XmlObject node,final Set<String> fields) {
		final Set<String> back = new LinkedHashSet<String>();
		if (node == null)
			return back;
		final XmlClient client = XmlClient.getInstance();
		back.addAll(getHeaderHelper(client.getParent(node),fields));
		back.addAll(retrieveHeaderField(node,fields));
		return back;
	}

	/**
	 * Retrieve header.
	 * 
	 * @param parentNode
	 *            the parent node
	 * @return the sets the
	 */
	private static Set<String> retrieveHeaderField(final XmlObject parentNode,final Set<String> headerFields) {
		final Set<String> back = new LinkedHashSet<String>();
		final XmlObject[] childs = XmlClient.getInstance().getByXPath(parentNode, "./*");
		if (childs != null) {
			for (XmlObject child : childs) {
				if(!child.getDomNode().getNodeName().equals("zofar:header"))continue;
				final XmlObject[] headerItems = child.selectPath( "./*");
				for (XmlObject headerItem : headerItems) {
					final String headerItemType = headerItem.getDomNode().getNodeName().replace("zofar:", "");
					if(!headerFields.contains(headerItemType))continue;
					back.add(ReplaceClient.getInstance().cleanedString(headerItem.newCursor().getTextValue()));
				}
			}
		}
		
		final XmlObject[] attributes = XmlClient.getInstance().getByXPath(parentNode, "./@*");
		if (attributes != null) {
			for (XmlObject attribute : attributes) {
				final String attributeType = attribute.getDomNode().getNodeName();
				if(!headerFields.contains(attributeType))continue;
				back.add(ReplaceClient.getInstance().cleanedString(attribute.newCursor().getTextValue()));
			}
		}
		return back;
	}

	private static List<XmlObject> getPath(XmlObject node) {
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

	private static String getUIDPath(XmlObject node) {
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

	private static List<XmlObject> getPrev(final XmlObject current, XmlObject xmlDoc) {
		if (current == null)
			return null;
		final XmlClient client = XmlClient.getInstance();
		List<XmlObject> back = new ArrayList<XmlObject>();
		XmlObject item = client.getPreviousSibling(current);
		if (item != null) {
			// XmlObject varObj = getVariableObj(item, true);
			XmlObject varObj = getVariableObjOnPage(item, true);
			if (varObj != null)
				back.add(item);
			else
				back.addAll(getPrev(item, xmlDoc));
		} else {
			XmlObject parentObj = client.getParent(current);
			if (parentObj != null) {
				final String parentType = parentObj.getDomNode().getNodeName();
				if (parentType.equals("zofar:page")) {
					// page switch
					// retrieve all transitions with target thispage.uid
					List<XmlObject> trans = getTransitionsTo(parentObj, xmlDoc);
					if (trans != null) {
						for (final XmlObject prevPage : trans) {
							XmlObject varObj = getVariableObjOnPage(prevPage, true);
							if (varObj != null)
								back.add(varObj);
						}
					}
				} else {
					// ??
					List<XmlObject> varObj = getPrev(parentObj, xmlDoc);
					if ((varObj != null) && (!varObj.isEmpty()))
						back.addAll(varObj);
				}
			}
		}
		return back;
	}

	// private static List<XmlObject> getPrev(final XmlObject current, XmlObject
	// xmlDoc) {
	// if (current == null)
	// return null;
	// final XmlClient client = XmlClient.getInstance();
	// List<XmlObject> back = new ArrayList<XmlObject>();
	// XmlObject item = client.getPreviousSibling(current);
	// if (item != null) {
	// XmlObject varObj = getVariableObj(item, true);
	// if (varObj != null) back.add(item);
	// else back.addAll(getPrev(item,xmlDoc));
	// } else {
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
	// if (varObj != null) back.add(varObj);
	// }
	// }
	// } else {
	// List<XmlObject> varObj = getPrev(parentObj,xmlDoc);
	// if ((varObj != null) && (!varObj.isEmpty()))
	// back.addAll(varObj);
	// }
	// }
	// }
	// return back;
	// }

	private static List<XmlObject> getNext(final XmlObject current, XmlObject xmlDoc) {
		if (current == null)
			return null;
		final XmlClient client = XmlClient.getInstance();
		List<XmlObject> back = new ArrayList<XmlObject>();
		XmlObject item = client.getNextSibling(current);
		if (item != null) {
			XmlObject varObj = getVariableObjOnPage(item, false);
			if (varObj != null){
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

	// private static XmlObject getVariableObj(XmlObject obj, final boolean
	// last) {
	// if (obj == null)
	// return null;
	// final XmlClient client = XmlClient.getInstance();
	// XmlObject[] resultSet = client.getByXPath(obj, "@variable");
	// if ((resultSet != null) && (resultSet.length > 0)) {
	// final int count = resultSet.length;
	// if (last)
	// return resultSet[count - 1];
	// else
	// return resultSet[0];
	// }
	// return null;
	// }

	private static XmlObject getUIDObj(XmlObject obj) {
		if (obj == null)
			return null;
		final XmlClient client = XmlClient.getInstance();
		XmlObject[] resultSet = client.getByXPath(obj, "@uid");
		if ((resultSet != null) && (resultSet.length > 0)) {
			return resultSet[0];
		}
		return null;
	}

	private static XmlObject getVariableObjOnPage(XmlObject obj, final boolean last) {
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

	private static List<XmlObject> getTransitionsFrom(XmlObject obj, XmlObject xmlDoc) {
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

	private static List<XmlObject> getTransitionsTo(XmlObject obj, XmlObject xmlDoc) {
		if (obj == null)
			return null;
		if (!obj.getDomNode().getNodeName().equals("zofar:page"))
			return null;

		final XmlClient client = XmlClient.getInstance();

		// retieve page uid
		final List<XmlObject> back = new ArrayList<XmlObject>();
		XmlObject[] resultSet = client.getByXPath(obj, "attribute::uid");
		if ((resultSet != null) && (resultSet.length == 1)) {
			final String pageUID = resultSet[0].newCursor().getTextValue();
			XmlObject[] transitions = client.getByXPath(xmlDoc, "//*[@target='" + pageUID + "']");
			if (transitions != null) {
				for (XmlObject transition : transitions) {
					if (transition.getDomNode().getNodeName().equals("zofar:transition")) {
						final XmlObject sourcePage = client.getParent(client.getParent(transition));
						if ((sourcePage != null) && (sourcePage.getDomNode().getNodeName().equals("zofar:page"))) {
							back.add(sourcePage);
						}
					}
				}
			}
		}
		return back;
	}
}

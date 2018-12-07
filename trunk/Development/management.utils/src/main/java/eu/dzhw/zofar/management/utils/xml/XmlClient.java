/*
 * Utility class to work with XML-Documents
 */
package eu.dzhw.zofar.management.utils.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlCursor.TokenType;
import org.apache.xmlbeans.XmlDocumentProperties;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xpath.CachedXPathAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * The Class XmlClient.
 */
public class XmlClient {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(XmlClient.class);

	/** The instance. */
	private static XmlClient INSTANCE;

	/**
	 * Instantiates a new xml client.
	 */
	private XmlClient() {
		super();
	}

	/**
	 * Gets the single instance of XmlClient.
	 * 
	 * @return single instance of XmlClient
	 */
	public static XmlClient getInstance() {
		if (INSTANCE == null)
			INSTANCE = new XmlClient();
		return INSTANCE;
	}

	/**
	 * Load an Xml-Document from path.
	 * 
	 * @param path
	 *            the path
	 * @return the document
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public Document getDocument(final String path) throws Exception {

		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringComments(true);
		factory.setIgnoringElementContentWhitespace(true);
		factory.setValidating(false);

		final DocumentBuilder builder = factory.newDocumentBuilder();
		File file = new File(path);
		InputStream inputStream = new FileInputStream(file);
		Reader reader = new InputStreamReader(inputStream, "UTF-8");
		InputSource is = new InputSource(reader);
		is.setEncoding("UTF-8");

		// LOGGER.info("parsing Doc from {}",path);
		final Document doc = builder.parse(is);
		// LOGGER.info("Doc from {} : {}",path,doc);
		return doc;
	}

	public Document getDocumentFromString(final String content) throws Exception {

		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringComments(true);
		factory.setIgnoringElementContentWhitespace(true);
		factory.setValidating(false);
		
		final DocumentBuilder builder = factory.newDocumentBuilder();
		InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
		Reader reader = new InputStreamReader(inputStream, "UTF-8");
		InputSource is = new InputSource(reader);
		is.setEncoding("UTF-8");

		final Document doc = builder.parse(is);
		return doc;
	}

	public XmlObject docToXmlObject(final Document doc) throws Exception {
		DOMSource domSource = new DOMSource(doc);
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.transform(domSource, result);
		final XmlObject back = XmlObject.Factory.parse(writer.toString());
		DocumentType doctype = doc.getDoctype();
		if (doctype != null) {
//			System.out.println("Doctype:");
//			System.out.println("\tName: "+doctype.getName());
//			System.out.println("\tPublic Id: "+doctype.getPublicId());
//			System.out.println("\tSystem Id: "+doctype.getSystemId());
//			System.out.println("\tInternal Subset: "+doctype.getInternalSubset());
			
			XmlDocumentProperties props = back.documentProperties();
			props.setDoctypeName(doctype.getName());
			props.setDoctypePublicId(doctype.getPublicId());
			props.setDoctypeSystemId(doctype.getSystemId());
		}
		return back;
	}

	public void saveDocument(final Document doc, final File xmlFile) {
		try {
			DOMSource source = new DOMSource(doc);
			StreamResult file = new StreamResult(xmlFile);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, file);
		} catch (Exception e) {
		}
	}

	/**
	 * Search in Document or one of its elements by XPath-Query.
	 * 
	 * @param root
	 *            the root
	 * @param query
	 *            the query
	 * @return the by x path
	 */
	public NodeList getByXPathOrig(final Node root, final String query) {
		if (root == null)
			return null;
		try {
			final XPath xPath = XPathFactory.newInstance().newXPath();
			return (NodeList) xPath.evaluate(query, root, XPathConstants.NODESET);
		} catch (final XPathExpressionException e) {
		}
		return null;
	}
	
	public NodeList getByXPath(final Node root, final String query) {
		if (root == null)
			return null;
		return getByCachedXPath(root,query);
//		return getByXPathOrig(root,query);
	}
	
	public NodeList getByCachedXPath(final String path, final String query) throws Exception {
		if (path == null) {
			LOGGER.error("getByXPath: path is null");
			return null;
		}
		final Document doc = this.getDocument(path);
		if (doc == null) {
			LOGGER.error("getByXPath: document for path {} is null", path);
			return null;
		}
		return this.getByCachedXPath(doc.getDocumentElement(), query);
	}
	
	
	CachedXPathAPI cachedXPath = null;
	public NodeList getByCachedXPath(final Node root, final String query) {
		if (root == null)
			return null;
		if(cachedXPath == null)cachedXPath = new CachedXPathAPI();
		
		try {
			return (NodeList) cachedXPath.selectNodeList(root, query);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Search in Document by XPath-Query.
	 * 
	 * @param path
	 *            the path
	 * @param query
	 *            the query
	 * @return the by x path
	 * @throws Exception
	 */
	public NodeList getByXPath(final String path, final String query) throws Exception {
		if (path == null) {
			LOGGER.error("getByXPath: path is null");
			return null;
		}
		final Document doc = this.getDocument(path);
		if (doc == null) {
			LOGGER.error("getByXPath: document for path {} is null", path);
			return null;
		}
		return this.getByXPath(doc.getDocumentElement(), query);
	}

	public XmlObject[] getByXPath(final XmlObject root, final String query) {
		if (root == null)
			return null;

		// get Namespaces
		final XmlCursor cursor = root.newCursor();
		final Map<String, String> retrievedNamespaces = new HashMap<String, String>();
		cursor.getAllNamespaces(retrievedNamespaces);
		final StringBuffer namespaces = new StringBuffer();
		if ((retrievedNamespaces != null) && (!retrievedNamespaces.isEmpty())) {
			for (final Map.Entry<String, String> namespace : retrievedNamespaces.entrySet()) {
				namespaces.append("declare namespace " + namespace.getKey() + "='" + namespace.getValue() + "';");
//				System.out.println("NS : "+ namespace.getKey() + "='" + namespace.getValue());
			}
		}

		final XmlObject[] back = root.selectPath(namespaces.toString() + query);
		return back;
	}

	// public XmlObject getParent(final XmlObject child) {
	// if (child == null)
	// return null;
	// XmlCursor c = child.newCursor();
	// XmlObject parent = null;
	// if (c.toParent())
	// parent = c.getObject();
	// return parent;
	// }

	public Map<String, List<XmlObject>> index(XmlObject root) {
		if (root == null)
			return null;

		final Map<String, List<XmlObject>> indexMap = new LinkedHashMap<String, List<XmlObject>>();
		XmlCursor documentCursor = root.newCursor();
		// System.out.println("rootObj : "+documentCursor.getObject());
		while (!documentCursor.toNextToken().isNone()) {
			switch (documentCursor.currentTokenType().intValue()) {
			case TokenType.INT_START:
				final XmlObject currentObj = documentCursor.getObject();
				final String rawXml = currentObj.xmlText();
				// System.out.println(currentObj);
				List<XmlObject> indexItemSet = null;
				if (indexMap.containsKey(rawXml))
					indexItemSet = indexMap.get(rawXml);
				if (indexItemSet == null)
					indexItemSet = new ArrayList<XmlObject>();
				indexItemSet.add(currentObj);
				indexMap.put(rawXml, indexItemSet);
				break;
			}
		}
		documentCursor.dispose();
		return indexMap;
	}

	public XmlObject getParent(XmlObject node) {
		if (node == null)
			return null;
		XmlCursor xc = node.newCursor();
		try {
			if (!xc.toParent()) {
				// LOGGER.error("failed to select parent Node");
				return null;
			}
			final XmlObject parent = xc.getObject();
			return parent;
		} finally {
			xc.dispose();
		}
	}

	public XmlObject getParent(XmlObject node,final String parentName) {
		if (node == null)
			return null;
		XmlCursor xc = node.newCursor();
		try {
			if (!xc.toParent()) {
				return null;
			}
			
			final XmlObject parent = xc.getObject();
			final String nodeName = parent.getDomNode().getNodeName();
			if(nodeName.equals(parentName))return parent;
			else return getParent(parent,parentName);
		} finally {
			xc.dispose();
		}
	}


	public XmlObject getFirst(XmlObject node) {
		if (node == null)
			return null;
		XmlCursor xc = node.newCursor();
		try {
			if (!xc.toFirstChild()) {
				// LOGGER.error("failed to select first Node");
				return null;
			}
			return xc.getObject();
		} finally {
			xc.dispose();
		}
	}

	public XmlObject getLast(XmlObject node) {
		if (node == null)
			return null;
		XmlCursor xc = node.newCursor();
		try {
			if (!xc.toLastChild()) {
				// LOGGER.error("failed to select last Node");
				return null;
			}
			return xc.getObject();
		} finally {
			xc.dispose();
		}
	}

	public XmlObject getNextSibling(XmlObject node) {
		if (node == null)
			return null;
		XmlCursor xc = node.newCursor();
		try {
			if (!xc.toNextSibling()) {
				// LOGGER.error("failed to select next Node");
				return null;
			}
			final XmlObject sibling = xc.getObject();
			return sibling;
		} finally {
			xc.dispose();
		}
	}

	public XmlObject getPreviousSibling(XmlObject node) {
		if (node == null)
			return null;
		XmlCursor xc = node.newCursor();
		try {
			if (!xc.toPrevSibling()) {
				// LOGGER.error("failed to select previous Node for : {}",node);
				return null;
			}
			return xc.getObject();
		} finally {
			xc.dispose();
		}
	}

	public String getAttribute(XmlObject obj, String attribute) {
		if (obj == null)
			return null;
		XmlObject[] resultSet = this.getByXPath(obj, "@" + attribute);
		if (resultSet != null) {
			final int count = resultSet.length;
			if (count == 1)
				return resultSet[0].newCursor().getTextValue();
		}
		return null;
	}

	public Map<String, String> getAttributes(XmlObject node) {
		if (node == null)
			return null;

		final Map<String, String> back = new HashMap<String, String>();

		final XmlObject[] attributes = XmlClient.getInstance().getByXPath(node, "./@*");
		if (attributes != null) {
			for (XmlObject attribute : attributes) {
				final String attributeType = attribute.getDomNode().getNodeName();
				back.put(attributeType, attribute.newCursor().getTextValue());
			}
		}
		return back;
	}

	public QName getQName(final XmlObject xmlObj) {
		if (xmlObj == null)
			return null;
		final Node node = xmlObj.getDomNode();
		QName qname = new QName(node.getNamespaceURI(), node.getLocalName());
		return qname;
	}

	public boolean hasParent(final Node node, final String parentName) {
		if (node == null)
			return false;
		if (node.getNodeName().equals(parentName))
			return true;
		return hasParent(node.getParentNode(), parentName);
	}

	public String show(Document doc) {
		try {
			final Transformer tf = TransformerFactory.newInstance().newTransformer();
			tf.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			tf.setOutputProperty(OutputKeys.INDENT, "yes");
			tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

			final StringWriter writer = new StringWriter();
			final StreamResult result = new StreamResult(writer);
			tf.transform(new DOMSource(doc), result);
			return writer.toString();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	public String convert2String(XmlObject xml) {
		if (xml == null)
			return null;
		
		XmlOptions opts = new XmlOptions();
		opts.setCharacterEncoding("utf8");
		opts.setSavePrettyPrint();
		opts.setSavePrettyPrintIndent(4);
		opts.setSaveOuter();
		opts.setSaveAggressiveNamespaces();
		

		return convert2String(xml,opts);
	}

	public String convert2String(XmlObject xml,final XmlOptions opts) {
		if (xml == null)
			return null;
		if(opts != null)return xml.xmlText(opts);
		else return xml.xmlText();
	}


}

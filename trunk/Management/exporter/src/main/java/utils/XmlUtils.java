package utils;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


public class XmlUtils {
	
	private static XmlUtils INSTANCE;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(XmlUtils.class);
	
	private XmlUtils(){
		super();
	}
	
	public static XmlUtils getInstance(){
		if(INSTANCE == null)INSTANCE = new XmlUtils();
		return INSTANCE;
	}
	
	public Document getDocument(final String path) {
//		LOGGER.info("path {}",path);
		try {
			final DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setIgnoringComments(true);
			factory.setIgnoringElementContentWhitespace(true);
			factory.setValidating(false);

			final DocumentBuilder builder = factory.newDocumentBuilder();
			return builder.parse(new InputSource(path));
		} catch (Exception e) {
		}
		return null;
	}
	
	public Document getDocument(final File file) {
		return getDocument(file.getAbsolutePath());
	}
	
	public NodeList getByXPath(final Object root,final String query){
	    
	    try {
	    	final XPath xPath = XPathFactory.newInstance().newXPath();
			return (NodeList)xPath.evaluate(query,
					root, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
		}
		return null;
	}
	
	public NodeList getByXPath(final String path,final String query){
		return getByXPath(getDocument(path).getDocumentElement(),query);
	}
	
	public NodeList getByXPath(final File path,final String query){
		return getByXPath(getDocument(path).getDocumentElement(),query);
	}

}

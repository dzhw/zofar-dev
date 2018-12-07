package de.dzhw.manager.ui.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlHtmlConverter implements Converter {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(XmlHtmlConverter.class);
	
	private static final XmlHtmlConverter instance = new XmlHtmlConverter();

	private XmlHtmlConverter() {
		super();
		LOGGER.info("XmlHtmlConverter init");
	}

	public static XmlHtmlConverter getInstance() {
		return instance;
	}

	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {

		arg2 = arg2.replaceAll("<p>","");
		arg2 = arg2.replaceAll("</p>","");
		arg2 = arg2.replaceAll("&gt;",">");
		arg2 = arg2.replaceAll("&lt;","<");
		arg2 = arg2.replaceAll("&quot;","\"");		
		arg2 = arg2.replaceAll("&nbsp;","");
		arg2 = arg2.replaceAll("<br />","");
//		LOGGER.info("getAsObject : {} ==> {}",arg1,arg2);
		return arg2;
	}

	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		LOGGER.info("getAsString : {} <== {}",arg1,arg2);
		return arg2.toString();
	}

}

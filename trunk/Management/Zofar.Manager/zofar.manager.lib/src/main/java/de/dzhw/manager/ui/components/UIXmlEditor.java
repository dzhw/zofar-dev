package de.dzhw.manager.ui.components;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.apache.xmlbeans.SchemaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dzhw.manager.common.utils.XmlObjectUtils;
import de.dzhw.manager.ui.converter.XmlHtmlConverter;
import de.dzhw.manager.ui.validator.ImportValidator;
/**
 * @author meisner
 *
 */
@FacesComponent("UIXmlEditor")
public class UIXmlEditor extends UINamingContainer implements Serializable {

	private static final long serialVersionUID = -3742148606182698324L;
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(UIXmlEditor.class);
	
	private final FacesMessage notValidMessage = new FacesMessage("Kein valider XML-Code");
	
	private XmlHtmlConverter converter;
	private Validator validator;

	public UIXmlEditor() {
		super();
		converter = XmlHtmlConverter.getInstance();
		validator = ImportValidator.getInstance();
		LOGGER.info("init");
	}

	public XmlHtmlConverter getConverter() {
		return converter;
	}

	public void setConverter(XmlHtmlConverter converter) {
//		this.converter = converter;
	}

	public Validator getValidator() {
		return validator;
	}

	public void setValidator(Validator validator) {
//		this.validator = validator;
	}
	
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		
		if(value != null){
			boolean valid = false;
			final String variableTypeClassName = (String) component.getAttributes().get("variableType");
			
			try {
				Class variableClass = component.getClass().getClassLoader().loadClass(variableTypeClassName);
				if(variableClass != null){
					SchemaType schemaType = XmlObjectUtils.getInstance().retrieveSchemaType(variableClass);
					valid = XmlObjectUtils.getInstance().validate(schemaType,value.toString());
					LOGGER.info("Validate {}",valid);

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
			if(!valid){
				throw new ValidatorException(notValidMessage);
			}
		}

	}

}

package de.dzhw.manager.ui.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dzhw.manager.common.utils.XmlObjectUtils;

public class ImportValidator implements Validator {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ImportValidator.class);
	
	private static final ImportValidator instance = new ImportValidator();
	
	private final FacesMessage notValidMessage = new FacesMessage("Kein valider XML-Code");

	private ImportValidator() {
		super();
	}
	
	public static ImportValidator getInstance() {
		return instance;
	}

	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
//		if(value != null){
//			final boolean valid = XmlObjectUtils.getInstance().validate(value.toString());
//			LOGGER.info("Validate {} = {}",value,valid);
//			if(!valid){
//				throw new ValidatorException(notValidMessage);
//			}
//		}

	}

}

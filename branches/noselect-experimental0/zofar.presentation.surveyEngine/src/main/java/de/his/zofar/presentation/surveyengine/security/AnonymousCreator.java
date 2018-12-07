package de.his.zofar.presentation.surveyengine.security;

import java.io.Serializable;
import java.util.UUID;

import javax.faces.validator.FacesValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import de.his.zofar.presentation.surveyengine.facades.SurveyEngineServiceFacade;
import de.his.zofar.service.surveyengine.model.Participant;

/**
 * @author meisner
 * 
 */
@Controller("anonymousCreator")
@Scope("application")
@FacesValidator("de.his.zofar.security.captcha")
public class AnonymousCreator implements Serializable {

	private static final long serialVersionUID = 3186592828969020596L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AnonymousCreator.class);

	// private Map<String, Participant> anonymousMap;

	public AnonymousCreator() {
		super();
	}

	public String createAnonymousAccount() {
		Participant anonymous = null;
		if (anonymous == null) {
			final SurveyEngineServiceFacade surveyEngineService = new SurveyEngineServiceFacade();
			anonymous = surveyEngineService.createAnonymousParticipant();
		}
		return anonymous.getToken();
	}

	public boolean checkCaptcha(final String captchaId, final String captchaText) {
		return true;
	}
	
	public String generateCaptchaId() {
		return UUID.randomUUID().toString();
	}
}

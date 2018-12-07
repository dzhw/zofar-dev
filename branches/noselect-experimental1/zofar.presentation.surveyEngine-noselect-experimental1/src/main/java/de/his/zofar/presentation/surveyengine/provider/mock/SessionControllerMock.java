package de.his.zofar.presentation.surveyengine.provider.mock;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;

import de.his.zofar.presentation.surveyengine.controller.SessionController;
import de.his.zofar.service.surveyengine.model.Participant;

public class SessionControllerMock extends SessionController {

	private static final long serialVersionUID = -6500362184815458551L;
	private Locale locale;
	private Participant participant;
	private Map<String,String> valueMap;

	public SessionControllerMock() {
		super(false);
		locale = new Locale("de");
		participant = new Participant();
		valueMap = new HashMap<String,String>();
	}

	@Override
	public Locale getCurrentLocale() {
		return this.locale;
	}

	@Override
	public Participant getParticipant() {
		return this.participant;
	}

	@Override
	public String getValueForVariablename(String variableName) {
		return valueMap.get(variableName);
	}

	@Override
	public void processAction(ActionEvent actionEvent)
			throws AbortProcessingException {
//		super.processAction(actionEvent);
	}

	@Override
	public void setValueForVariablename(String variableName, String value) {
		valueMap.put(variableName,value);
	}

	@Override
	public String switchCurrentLocale(String locale) {
		return "";
	}
	
	

}

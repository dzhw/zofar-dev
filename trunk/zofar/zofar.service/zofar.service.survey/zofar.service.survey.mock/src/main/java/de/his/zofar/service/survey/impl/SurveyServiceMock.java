/**
 *
 */
package de.his.zofar.service.survey.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import de.his.zofar.service.common.exceptions.NotYetImplementedException;
import de.his.zofar.service.common.model.PageDTO;
import de.his.zofar.service.questionnaire.model.Questionnaire;
import de.his.zofar.service.survey.model.Survey;
import de.his.zofar.service.survey.model.SurveyQueryDTO;
import de.his.zofar.service.survey.service.SurveyService;

/**
 * @author le
 *
 */
@Service
public class SurveyServiceMock implements SurveyService {

    public Survey create(final Survey survey) {
        throw new NotYetImplementedException();
    }

    public void delete(final Survey survey) {
        throw new NotYetImplementedException();
    }

    public List<Survey> searchAll() {
        throw new NotYetImplementedException();
    }

    public PageDTO<Survey> searchAll(final SurveyQueryDTO surveyQuery) {
        throw new NotYetImplementedException();
    }

    public Survey search(final SurveyQueryDTO surveyQuery) {
    	final Survey survey = new Survey();
    	survey.setName(surveyQuery.getName());
    	if((surveyQuery.getStates() != null)&&(!surveyQuery.getStates().isEmpty()))survey.setState(surveyQuery.getStates().get(0));
    	final Questionnaire questionnaire = null;


    	survey.setQuestionnaire(questionnaire);


    	return survey;
    }
}

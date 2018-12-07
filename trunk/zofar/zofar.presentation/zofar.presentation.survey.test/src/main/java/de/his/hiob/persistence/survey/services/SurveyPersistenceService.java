/**
 *
 */
package de.his.hiob.persistence.survey.services;

import java.util.List;

import org.springframework.stereotype.Service;

import de.his.hiob.model.common.exceptions.NotYetImplementedException;
import de.his.hiob.model.common.models.PageDTO;
import de.his.hiob.model.questionnaire.models.Questionnaire;
import de.his.hiob.model.survey.models.Survey;
import de.his.hiob.model.survey.models.SurveyQueryDTO;
import de.his.hiob.persistence.common.services.PersistenceService;

/**
 * @author le
 *
 */
@Service
public class SurveyPersistenceService extends PersistenceService {
	
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
    	throw new NotYetImplementedException();
//    	Survey survey = new Survey();
//    	survey.setName(surveyQuery.getName());
//    	if((surveyQuery.getStates() != null)&&(!surveyQuery.getStates().isEmpty()))survey.setState(surveyQuery.getStates().get(0));
//    	Questionnaire questionnaire = null;
//    	
//    	
//    	survey.setQuestionnaire(questionnaire);
//    	
//    	
//    	return survey;
    }
}

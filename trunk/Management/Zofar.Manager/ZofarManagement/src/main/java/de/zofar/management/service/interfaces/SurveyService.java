package de.zofar.management.service.interfaces;

import java.util.List;

import de.zofar.management.service.model.project.Survey;
import de.zofar.management.service.model.search.SurveySearch;


/**
 * @author dick
 * 
 */
public interface SurveyService {

    Survey save(Survey survey);

    void saveAndForget(Survey survey);

    void delete(Survey survey);

    Survey findOne(Long id);

    List<Survey> findBySearchParameter(SurveySearch parameter);

    List<Survey> findAll();

}

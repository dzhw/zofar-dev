/**
 * 
 */
package de.his.zofar.service.survey.service;

import javax.validation.constraints.NotNull;

import org.springframework.transaction.annotation.Transactional;

import de.his.zofar.service.common.model.PageDTO;
import de.his.zofar.service.survey.model.Survey;
import de.his.zofar.service.survey.model.SurveyQueryDTO;

/**
 * @author le
 *
 */
public interface SurveyService {

    @Transactional
    public abstract Survey create(Survey surveyDTO);

    @Transactional
    public abstract void delete(@NotNull Survey surveyDTO);

    @Transactional(readOnly = true)
    public abstract PageDTO<Survey> searchAll(SurveyQueryDTO surveyQuery);

    @Transactional(readOnly = true)
    public abstract Survey search(SurveyQueryDTO surveyQuery);

}
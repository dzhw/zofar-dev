/**
 *
 */
package de.his.zofar.service.survey.internal;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import de.his.zofar.persistence.survey.daos.SurveyDao;
import de.his.zofar.service.common.exceptions.NotYetImplementedException;
import de.his.zofar.service.common.internal.InternalServiceInterface;
import de.his.zofar.service.common.model.PageDTO;
import de.his.zofar.service.survey.model.Survey;
import de.his.zofar.service.survey.model.SurveyQueryDTO;

/**
 * @author le
 *
 */
@Service
public class SurveyInternalService implements InternalServiceInterface {

    @Resource
    private SurveyDao surveyDao;

    public Survey create(final Survey survey) {
        // final SurveyEntity entityToSave = this.mapper.map(survey,
        // SurveyEntity.class);
        // final SurveyEntity savedEntity = this.surveyDao.save(entityToSave);
        // return this.mapper.map(savedEntity, Survey.class);
        throw new NotYetImplementedException();
    }

    public void delete(final Survey survey) {
        // final SurveyEntity entityToDelete = this.mapper.map(survey,
        // SurveyEntity.class);
        // this.surveyDao.delete(entityToDelete);
        throw new NotYetImplementedException();
    }

    // TODO dick remove!!
    public List<Survey> searchAll() {
        // final List<SurveyEntity> entities = this.surveyDao.findAll();
        // final List<Survey> result = new ArrayList<Survey>();
        // for (final SurveyEntity entity : entities) {
        // result.add(mapper.map(entity, Survey.class));
        // }
        // return result;
        throw new NotYetImplementedException();
    }

    public PageDTO<Survey> searchAll(final SurveyQueryDTO surveyQuery) {
        // final BooleanExpression expression = SurveyExpression
        // .surveyNameMatches(surveyQuery.getName())
        // .and(SurveyExpression.surveyStateIn(surveyQuery.getStates()));
        //
        // final PageRequestEntity pageRequest = this.mapper.map(
        // surveyQuery.getPageRequestDTO(), PageRequestEntity.class);
        //
        // final Pageable pageable = new
        // org.springframework.data.domain.PageRequest(
        // pageRequest.getPageNumber(), pageRequest.getPageSize());
        // @SuppressWarnings("unused")
        // final Page<SurveyEntity> result = this.surveyDao.findAll(expression,
        // pageable);

        // TODO le: this is not working see unit test
        // return this.mapper.map(result, PageDTO.class);
        throw new NotYetImplementedException();
    }

    /**
     * @param surveyQuery
     *            the query to search the survey for
     * @return the found survey
     */
    public Survey search(final SurveyQueryDTO surveyQuery) {
        throw new NotYetImplementedException();
    }

}

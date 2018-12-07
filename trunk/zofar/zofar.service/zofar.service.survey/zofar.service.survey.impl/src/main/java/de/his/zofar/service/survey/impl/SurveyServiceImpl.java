package de.his.zofar.service.survey.impl;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.his.zofar.persistence.survey.daos.SurveyDao;
import de.his.zofar.persistence.survey.entities.SurveyEntity;
import de.his.zofar.service.common.AbstractService;
import de.his.zofar.service.common.model.PageDTO;
import de.his.zofar.service.survey.internal.SurveyInternalService;
import de.his.zofar.service.survey.model.Survey;
import de.his.zofar.service.survey.model.SurveyQueryDTO;
import de.his.zofar.service.survey.service.SurveyService;

/**
 * @author Meisner
 *
 */
@Service
public class SurveyServiceImpl extends AbstractService implements SurveyService {

    @SuppressWarnings("unused")
    private static Logger LOGGER = LoggerFactory
            .getLogger(SurveyServiceImpl.class);

    @Resource
    private SurveyDao surveyDao;

    @Inject
    private SurveyInternalService surveyInternalService;

    @Inject
    public SurveyServiceImpl(final Mapper mapper) {
        super(mapper);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.his.zofar.service.survey.impl.SurveyService#create(de.his.zofar.service
     * .survey.model.Survey)
     */
    @Override
    @Transactional
    public Survey create(final Survey surveyDTO) {
        final SurveyEntity entityToSave = this.mapper.map(surveyDTO,
                SurveyEntity.class);
        final SurveyEntity savedEntity = this.surveyDao.save(entityToSave);
        return this.mapper.map(savedEntity, Survey.class);
        // return this.surveyInternalService.create(surveyDTO);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.his.zofar.service.survey.impl.SurveyService#delete(de.his.zofar.service
     * .survey.model.Survey)
     */
    @Override
    @Transactional
    public void delete(final @NotNull Survey surveyDTO) {
        final SurveyEntity entityToDelete = this.mapper.map(surveyDTO,
                SurveyEntity.class);
        this.surveyDao.delete(entityToDelete);
        // this.surveyInternalService.delete(surveyDTO);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.his.zofar.service.survey.impl.SurveyService#search(de.his.zofar.service
     * .survey.model.SurveyQueryDTO)
     */
    @Override
    @Transactional(readOnly = true)
    public Survey search(final SurveyQueryDTO surveyQuery) {
        return this.surveyInternalService.search(surveyQuery);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.his.zofar.service.survey.impl.SurveyService#searchAll(de.his.zofar
     * .service.survey.model.SurveyQueryDTO)
     */
    @Override
    @Transactional(readOnly = true)
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
        //
        // // TODO le: this is not working see unit test
        // return this.mapper.map(result, PageDTO.class);
        return this.surveyInternalService.searchAll(surveyQuery);
    }
}

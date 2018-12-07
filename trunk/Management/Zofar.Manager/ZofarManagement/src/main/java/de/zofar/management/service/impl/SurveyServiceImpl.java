package de.zofar.management.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.zofar.management.persistence.daos.survey.SurveyDao;
import de.zofar.management.persistence.entities.survey.SurveyEntity;
import de.zofar.management.service.AbstractService;
import de.zofar.management.service.interfaces.SurveyService;
import de.zofar.management.service.model.project.Survey;
import de.zofar.management.service.model.search.SurveySearch;

/**
 * @author dick
 * 
 */
@Service("surveyService")
public class SurveyServiceImpl extends AbstractService implements SurveyService {

    private SurveyDao surveyDao;

    /**
     * @param surveyDao
     *            the surveyDao to set
     */
    @Resource
    public void setSurveyDao(final SurveyDao surveyDao) {
        this.surveyDao = surveyDao;
    }

    @Override
    @Transactional
    public Survey save(final Survey survey) {
//        final SurveyEntity mapped = getMapper().map(survey, SurveyEntity.class);
//        final SurveyEntity saved = surveyDao.save(mapped);
//        participantDao.save(saved.getParticipants());
//        return getMapper().map(saved, Survey.class);
        return null;
    }

    @Override
    @Transactional
    public void delete(final Survey survey) {
//        final SurveyEntity mapped = getMapper().map(survey, SurveyEntity.class);
//        surveyDao.delete(mapped);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Survey> findAll() {
        final List<SurveyEntity> entities = surveyDao.findAll();
        final List<Survey> result = new ArrayList<Survey>();
//        for (final SurveyEntity entity : entities) {
//            result.add(getMapper().map(entity, Survey.class));
//        }
        return result;
    }

    @Override
    @Transactional
    public Survey findOne(final Long id) {
        final SurveyEntity entity = surveyDao.findOne(id);
//        return getMapper().map(entity, Survey.class);
        return null;
    }

    @Override
    @Transactional
    public void saveAndForget(final Survey survey) {
//        final SurveyEntity mapped = getMapper().map(survey, SurveyEntity.class);
//        final SurveyEntity saved = surveyDao.saveAndFlush(mapped);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Survey> findBySearchParameter(final SurveySearch parameter) {
        List<SurveyEntity> entities;
        final String textValue = parameter.getName() == null
                || parameter.getName().isEmpty() ? "%" : parameter.getName();
        entities = surveyDao.findByNameLikeOrderByIdAsc(textValue);
//        return mapPage(entities, Survey.class, pageable);
        return null;
    }
}


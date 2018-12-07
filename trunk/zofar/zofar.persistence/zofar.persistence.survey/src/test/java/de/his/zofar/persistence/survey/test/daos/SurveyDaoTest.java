package de.his.zofar.persistence.survey.test.daos;

import static de.his.zofar.persistence.survey.daos.SurveyDao.qSurvey;
import static de.his.zofar.persistence.survey.expressions.SurveyExpression.surveyCanceled;
import static de.his.zofar.persistence.survey.expressions.SurveyExpression.surveyReleased;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.types.expr.BooleanExpression;

import de.his.zofar.persistence.survey.daos.SurveyDao;
import de.his.zofar.persistence.survey.entities.SurveyEntity;
import de.his.zofar.persistence.survey.entities.SurveyState;
import de.his.zofar.persistence.test.daos.AbstractDaoTest;

public class SurveyDaoTest extends AbstractDaoTest {

    @Resource
    private SurveyDao surveyDao;

    private final Pageable pageable = new PageRequest(0, 2);

    private void createSurveys() {
        final List<SurveyEntity> surveys = new ArrayList<SurveyEntity>();

        final SurveyEntity firstSurvey = new SurveyEntity("NEPS1");
        firstSurvey.setState(SurveyState.CREATED);
        surveys.add(firstSurvey);

        final SurveyEntity secondSurvey = new SurveyEntity("NEPS2");
        secondSurvey.setState(SurveyState.RELEASED);
        surveys.add(secondSurvey);

        final SurveyEntity thirdSurvey = new SurveyEntity("NEPS3");
        thirdSurvey.setState(SurveyState.CANCELED);
        surveys.add(thirdSurvey);

        this.surveyDao.save(surveys);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void loadSurveyByComplexQuery() {
        createSurveys();

        final BooleanExpression expression = surveyCanceled().or(
                surveyReleased()).and(qSurvey.name.containsIgnoreCase("neps"));
        this.logger.info(expression.toString());

        final List<SurveyEntity> surveys = this.surveyDao.findAll(expression);

        Assert.assertEquals(2, surveys.size());

        // use QueryDSL and Paging
        final Page<SurveyEntity> surveyPage = this.surveyDao.findAll(qSurvey.name
                .like("NEPS%").and(qSurvey.state.in(SurveyState.values())),
                this.pageable);
        Assert.assertEquals(2, surveyPage.getNumberOfElements());
    }
}

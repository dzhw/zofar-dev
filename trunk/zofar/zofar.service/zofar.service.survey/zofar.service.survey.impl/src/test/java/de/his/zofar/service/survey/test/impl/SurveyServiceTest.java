/**
 *
 */
package de.his.zofar.service.survey.test.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import de.his.zofar.persistence.survey.entities.SurveyState;
import de.his.zofar.service.common.exceptions.NotYetImplementedException;
import de.his.zofar.service.common.model.PageDTO;
import de.his.zofar.service.common.model.PageRequestDTO;
import de.his.zofar.service.survey.model.Survey;
import de.his.zofar.service.survey.model.SurveyQueryDTO;
import de.his.zofar.service.survey.service.SurveyService;
import de.his.zofar.service.test.AbstractServiceTest;

/**
 * @author meisner
 *
 */
public class SurveyServiceTest extends AbstractServiceTest {

    @Resource
    private SurveyService surveyService;

    @Test
    public void createSurvey() {
        Survey surveyDTO = new Survey();
        surveyDTO.setName("Testsurvey");
        surveyDTO.setState(SurveyState.RELEASED);
        surveyDTO = this.surveyService.create(surveyDTO);
        Assert.assertEquals("Testsurvey", surveyDTO.getName());
        Assert.assertEquals(SurveyState.RELEASED, surveyDTO.getState());
    }

    @Ignore
    @Test(expected = NotYetImplementedException.class)
    // TODO le: make mapping working
    public void loadSurveyByComplexQuery() {
        Survey surveyDTO = new Survey();
        surveyDTO.setName("NEPS");
        surveyDTO.setState(SurveyState.RELEASED);
        surveyDTO = this.surveyService.create(surveyDTO);

        final SurveyQueryDTO surveyQueryDTO = new SurveyQueryDTO();
        surveyQueryDTO.setName("NEPS");
        final List<SurveyState> states = new ArrayList<SurveyState>();
        states.add(SurveyState.RELEASED);
        surveyQueryDTO.setStates(states);
        surveyQueryDTO.setPageRequestDTO(new PageRequestDTO(0, 10));
        final PageDTO<Survey> result =
                this.surveyService.searchAll(surveyQueryDTO);
        Assert.assertEquals(1, result.getNumberOfElements());
    }

}

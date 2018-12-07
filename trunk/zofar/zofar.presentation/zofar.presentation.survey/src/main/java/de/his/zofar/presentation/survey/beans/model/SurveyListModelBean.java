/**
 *
 */
package de.his.zofar.presentation.survey.beans.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.persistence.survey.entities.SurveyState;
import de.his.zofar.service.common.model.PageDTO;
import de.his.zofar.service.common.model.PageRequestDTO;
import de.his.zofar.service.survey.model.Survey;
import de.his.zofar.service.survey.model.SurveyQueryDTO;

/**
 * this class holds all models and their values for the survey list
 *
 * @author le
 *
 */
@ManagedBean
@ViewScoped
public class SurveyListModelBean implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -4040718757679202720L;
    private static final Logger LOGGER = LoggerFactory
            .getLogger(SurveyListModelBean.class);
    private transient SurveyQueryDTO surveyQuery;
    private transient PageDTO<Survey> surveysPage;

    /**
     * @return the surveyQuery
     */
    public SurveyQueryDTO getSurveyQuery() {
        return surveyQuery;
    }

    /**
     * @param surveyQuery
     *            the surveyQuery to set
     */
    public void setSurveyQuery(final SurveyQueryDTO surveyQuery) {
        this.surveyQuery = surveyQuery;
    }

    /**
     * creating the survey state list for the view (checkbox).
     * set all survey states for the survey query as default.
     */
    @PostConstruct
    private void initSurveyQuery() {
        surveyQuery = new SurveyQueryDTO();
        final List<SurveyState> states = Arrays.asList(SurveyState.values());
        LOGGER.debug("states: {}", states);
        surveyQuery.setPageRequestDTO(new PageRequestDTO(1, 10));
        surveyQuery.setStates(states);
    }

    /**
     * @return the surveyStates
     */
    public SurveyState[] getSurveyStates() {
        return SurveyState.values();
    }

    /**
     * @return the surveysPage
     */
    public PageDTO<Survey> getSurveysPage() {
        return surveysPage;
    }

    /**
     * @param surveysPage
     *            the surveysPage to set
     */
    public void setSurveysPage(final PageDTO<Survey> surveys) {
        this.surveysPage = surveys;
    }
}

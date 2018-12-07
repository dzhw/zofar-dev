/**
 * 
 */
package de.his.zofar.presentation.survey.beans.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.survey.beans.model.SurveyListModelBean;
import de.his.zofar.service.common.model.PageDTO;
import de.his.zofar.service.survey.model.Survey;
import de.his.zofar.service.survey.service.SurveyService;

/**
 * this class holds all actions and listeners for survey list view.
 * 
 * @author le
 * 
 */
@ManagedBean
@ViewScoped
public class SurveyListControllerBean implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 4385558214756777224L;
    private static final Logger LOGGER = LoggerFactory
            .getLogger(SurveyListControllerBean.class);
    @ManagedProperty(value = "#{surveyExternalService}")
    private SurveyService surveyService;
    @ManagedProperty(value = "#{surveyListModelBean}")
    private SurveyListModelBean modelBean;

    /**
     * @param surveyService
     *            the surveyService to set
     */
    public void setSurveyService(final SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    /**
     * @param modelBean
     *            the modelBean to set
     */
    public void setModelBean(final SurveyListModelBean modelBean) {
        this.modelBean = modelBean;
    }

    /**
     * loads surveys depending on the SurveyQueryDTO instance and fills the
     * survey list
     * 
     * @param event
     */
    public void search(final ActionEvent event) {
        LOGGER.debug("clicked on search button");
        LOGGER.debug("query: {}, states: {}", modelBean.getSurveyQuery()
                .getName(), modelBean.getSurveyQuery().getStates());

        final PageDTO<Survey> foundSurveys = surveyService.searchAll(modelBean
                .getSurveyQuery());

        // sets the survey list of the model bean
        modelBean.setSurveysPage(foundSurveys);
    }

    /**
     * delete on survey from the survey list
     * 
     * @param survey
     * @return
     */
    public String delete(final Survey survey) {
        LOGGER.debug("deleting survey with name: {}", survey.getName());

        // deleting the entity
        surveyService.delete(survey);

        // removing dto from PageDTO
        final boolean success = modelBean.getSurveysPage().getContent().remove(survey);
        LOGGER.debug("deleting successful? {}", success);

        return null;
    }

}

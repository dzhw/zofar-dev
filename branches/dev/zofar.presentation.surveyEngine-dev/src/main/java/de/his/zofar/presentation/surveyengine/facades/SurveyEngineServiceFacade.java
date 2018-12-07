package de.his.zofar.presentation.surveyengine.facades;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import de.his.zofar.presentation.common.util.BeanHelper;
import de.his.zofar.service.surveyengine.model.Participant;
import de.his.zofar.service.surveyengine.model.SurveyHistory;
import de.his.zofar.service.surveyengine.service.SurveyEngineService;

/**
 * The SurveyEngineServiceFacades delegates all calls to the SurveyServiceEngine
 * and ensures that there is a valid instance of the service. It is serializable
 * in order to be used in session scoped beans.
 *
 * @author Reitmann
 */
public class SurveyEngineServiceFacade implements Serializable {

    private static final long serialVersionUID = 8057880979588063911L;

    private transient SurveyEngineService surveyEngineService = null;

    /**
     * Retrieve the singleton service from the FacesContext.
     *
     * @return A reference to the service bean.
     */
    private SurveyEngineService getSurveyEngineService() {
        if (this.surveyEngineService == null) {
            this.surveyEngineService = BeanHelper
                    .findBean(SurveyEngineService.class);
        }
        return this.surveyEngineService;
    }

    public List<SurveyHistory> loadHistory(final Participant participant) {
        return this.getSurveyEngineService().loadHistory(participant);
    }

    public Participant loadParticipant(final String token) {
        return this.getSurveyEngineService().loadParticipant(token);
    }

    /**
     * @return The ViewId which was picked randomly previously.
     */
    public String loadRandomViewTransition(final Participant participant,
            final String fromViewId) {
        return this.getSurveyEngineService().loadRandomViewTransition(
                participant, fromViewId);
    }

    public Map<String, List<String>> loadSortings(final Participant participant) {
        return this.getSurveyEngineService().loadSortings(participant);
    }

    public void saveHistory(final SurveyHistory history) {
        this.getSurveyEngineService().saveHistory(history);
    }

    public Participant saveParticipant(final Participant participant) {
        return this.getSurveyEngineService().saveSurveyParticipant(participant);
    }
    
    public Participant createAnonymousParticipant() {
        return this.getSurveyEngineService().createAnonymousParticipant();
    }

    public void saveRandomViewTransition(final Participant participant,
            final String fromViewId, final String toViewId) {
        this.getSurveyEngineService().saveRandomViewTransition(participant,
                fromViewId, toViewId);
    }

    public void saveSorting(final String parentUID,
            final List<String> childrenUIDs, final Participant participant) {
        this.getSurveyEngineService().saveSorting(parentUID, childrenUIDs,
                participant);
    }

    public Map<String, String> loadLabelsAndConditions(final String variable,
            final String answerOptionUid) {
        return this.getSurveyEngineService().loadLabelsAndConditions(variable,
                answerOptionUid);
    }

}

/**
 *
 */
package de.his.zofar.service.surveyengine.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import de.his.zofar.service.surveyengine.model.Participant;
import de.his.zofar.persistence.surveyengine.entities.ParticipantEntity;
import de.his.zofar.service.surveyengine.model.SurveyHistory;

/**
 * @author le
 *
 */
public interface SurveyEngineService {
    @Transactional(readOnly = true)
    public List<SurveyHistory> loadHistory(Participant participant);

    @Transactional(readOnly = true)
    public String loadRandomViewTransition(Participant participant,
            String fromViewId);

    @Transactional(readOnly = true)
    public Participant loadParticipant(String token);

    @Transactional(readOnly = true)
    public Map<String, List<String>> loadSortings(Participant participant);

    @Transactional
    public void saveHistory(SurveyHistory history);

    @Transactional
    public Participant saveParticipant(Participant participant);
    
    @Transactional
    public Participant saveSurveyParticipant(Participant participant, List<String> dirty);
    
    @Transactional
    public Participant createAnonymousParticipant();
    
    @Transactional
    public Participant createParticipant(final String token,final String password);
    
    @Transactional
    public Participant createParticipant(final String token,final String password,final Map<String,String> preloads);

    @Transactional
    public void saveRandomViewTransition(Participant participant,
            String fromViewId, String toViewId);

    @Transactional
    public void saveSorting(String parentUID, List<String> childrenUIDs,
            Participant participant);

    @Transactional(readOnly = true)
    public Map<String, String> loadLabelsAndConditions(String variable,
            String answerOptionUid);
    
    @Transactional(readOnly = true)
    public List<ParticipantEntity> exportParticipants();
}

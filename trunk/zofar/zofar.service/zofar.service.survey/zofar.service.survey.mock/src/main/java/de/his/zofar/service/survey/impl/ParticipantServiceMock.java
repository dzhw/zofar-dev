/**
 *
 */
package de.his.zofar.service.survey.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;

import de.his.zofar.persistence.survey.entities.SurveyState;
import de.his.zofar.service.survey.model.Participant;
import de.his.zofar.service.survey.model.Survey;
import de.his.zofar.service.survey.service.ParticpantService;

/**
 * the fake of the real ParticipantPersistenceService.
 *
 * @author le
 *
 */
@Service
public class ParticipantServiceMock implements ParticpantService {

    /**
     * @param token
     * @return
     */
    @Override
    public Participant findByToken(final String token) {
        return participants.get(token);
    }

    // fake

    @Inject
    private PasswordEncoder passwordEncoder;

    private final Map<String, Participant> participants = new HashMap<String, Participant>();

    @PostConstruct
    private void init() {
        final Survey survey = new Survey("Test");
        survey.setState(SurveyState.RELEASED);
        participants.put("participant1",
                createParticipant("participant1", survey));
        participants.put("participant2",
                createParticipant("participant2", survey));
        participants.put("participant3",
                createParticipant("participant3", survey));
    }

    /**
     * @param token
     * @param survey
     * @return
     */
    private Participant createParticipant(final String token, final Survey survey) {
        return new Participant(token, passwordEncoder.encodePassword(token,
                null), survey);
    }

}

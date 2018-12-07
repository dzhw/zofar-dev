/**
 *
 */
package de.his.zofar.service.survey.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.his.zofar.service.survey.internal.ParticipantInternalService;
import de.his.zofar.service.survey.model.Participant;
import de.his.zofar.service.survey.service.ParticpantService;

/**
 * @author le
 *
 */
@Service
public class ParticipantServiceImpl implements ParticpantService {

    /**
     *
     */
    @Inject
    private ParticipantInternalService participantInternalService;

    /* (non-Javadoc)
     * @see de.his.zofar.service.survey.impl.ParticpantService#findByToken(java.lang.String)
     */
    @Override
    @Transactional(readOnly = true)
    public Participant findByToken(final String token) {
        return participantInternalService.findByToken(token);
    }

}

/**
 *
 */
package de.his.zofar.service.survey.internal;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import de.his.zofar.persistence.survey.daos.ParticipantDao;
import de.his.zofar.service.common.exceptions.NotYetImplementedException;
import de.his.zofar.service.common.internal.InternalServiceInterface;
import de.his.zofar.service.survey.model.Participant;

/**
 * @author le
 *
 */
@Service
public class ParticipantInternalService implements InternalServiceInterface {

    /**
     *
     */
    @Resource
    private ParticipantDao participantDao;

    /**
     * find a participant by its token.
     *
     * @param token
     *            the identifier of the participant
     * @return the found participant
     */
    public Participant findByToken(final String token) {
        throw new NotYetImplementedException();
    }

}

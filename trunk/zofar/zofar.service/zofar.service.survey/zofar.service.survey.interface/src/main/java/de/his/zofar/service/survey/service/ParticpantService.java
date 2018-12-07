/**
 * 
 */
package de.his.zofar.service.survey.service;

import org.springframework.transaction.annotation.Transactional;

import de.his.zofar.service.survey.model.Participant;

/**
 * @author le
 *
 */
public interface ParticpantService {

    /**
     * @param token
     * @return
     */
    @Transactional(readOnly = true)
    public abstract Participant findByToken(String token);

}
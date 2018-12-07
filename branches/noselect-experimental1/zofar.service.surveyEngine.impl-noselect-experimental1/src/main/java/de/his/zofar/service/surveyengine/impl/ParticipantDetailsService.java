/**
 *
 */
package de.his.zofar.service.surveyengine.impl;

import javax.inject.Inject;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import de.his.zofar.persistence.surveyengine.daos.ParticipantDao;
import de.his.zofar.persistence.surveyengine.entities.ParticipantEntity;
import de.his.zofar.service.common.AbstractService;
import de.his.zofar.service.surveyengine.model.ParticipantPrincipal;

/**
 * @author le
 *
 */
public class ParticipantDetailsService extends AbstractService implements
        UserDetailsService {

    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ParticipantDetailsService.class);

    /**
     *
     */
    private static final String PARTICIPANT_NOT_FOUND_MESSAGE = "No Participant with token '%s' in DB";

    /**
     *
     */
    private final ParticipantDao participantDao;

    /**
     * constructor injection to provide instance wide access to daos.
     *
     * @param participantDao
     */
    @Inject
    public ParticipantDetailsService(final Mapper mapper,
            final ParticipantDao participantDao) {
        super(mapper);
        this.participantDao = participantDao;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.security.core.userdetails.UserDetailsService#
     * loadUserByUsername(java.lang.String)
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String username)
            throws UsernameNotFoundException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(
                    "loading Participant for token '{}' for authentication.",
                    username);
        }
        final ParticipantEntity foundEntity = this.participantDao
                .findByToken(username);
        if (foundEntity == null) {
            throw new UsernameNotFoundException(String.format(
                    PARTICIPANT_NOT_FOUND_MESSAGE, username));
        }
        return new ParticipantPrincipal(foundEntity.getToken(),
                foundEntity.getPassword());
    }

}

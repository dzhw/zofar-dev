/**
 *
 */
package de.zofar.management.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import de.zofar.management.persistence.daos.user.UserDao;
import de.zofar.management.persistence.entities.user.UserEntity;



public class ParticipantDetailsService implements UserDetailsService {

    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ParticipantDetailsService.class);

    /**
     *
     */
    private static final String PARTICIPANT_NOT_FOUND_MESSAGE = "No Participant has been found";

    /**
     *
     */
    @Autowired
    private UserDao userDao;

    /**
     * constructor injection to provide instance wide access to daos.
     * 
     * @param participantDao
     */

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
        LOGGER.debug("loading Participant for token '{}' for authentication.",
                username);
        final UserEntity foundEntity = userDao.findByName(username);
        if (foundEntity == null) {
            throw new UsernameNotFoundException(String.format(
                    PARTICIPANT_NOT_FOUND_MESSAGE, username));
        }
        // TODO le: prevent dozer from triggering lazy loading on persistent bag
        // when mapping the participant entity to model, because we don't need
        // the survey data on the participant.
        return foundEntity;
    }

}
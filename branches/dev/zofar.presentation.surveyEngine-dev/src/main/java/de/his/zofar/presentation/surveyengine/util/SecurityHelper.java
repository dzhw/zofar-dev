/**
 *
 */
package de.his.zofar.presentation.surveyengine.util;

import org.springframework.security.core.context.SecurityContextHolder;

import de.his.zofar.service.surveyengine.model.ParticipantPrincipal;

/**
 * @author le
 *
 */
public class SecurityHelper {

    /**
     * block this class from being instantiated.
     */
    private SecurityHelper() {
        super();
    }

    /**
     * Retrieving the participant from Spring Security.
     *
     * @return the participant that is logged in this session.
     */
    public static ParticipantPrincipal retrieveParticipantFromSecurityContext() {
        final Object principal = SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        if (ParticipantPrincipal.class.isAssignableFrom(principal.getClass())) {
            return (ParticipantPrincipal) principal;
        } else {
            throw new IllegalStateException(
                    "The principal is from unexpected type "
                            + principal.getClass().getSimpleName());
        }
    }
}

/**
 *
 */
package de.his.zofar.presentation.surveyengine.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

/**
 * On successful authentication the user will be redirected to the URL the user
 * had previously entered.
 *
 * Deprecated: This class causes following exception when trying to log in after
 * session time out on the login page. "Your login attempt was not successful,
 * try again. Reason: Authentication method not supported: GET"
 *
 * @author le
 *
 */
@Deprecated
public class BypassAuthenticationSuccessHandler extends
        SavedRequestAwareAuthenticationSuccessHandler {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(BypassAuthenticationSuccessHandler.class);

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.security.web.authentication.
     * AbstractAuthenticationTargetUrlRequestHandler
     * #determineTargetUrl(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected String determineTargetUrl(final HttpServletRequest request,
            final HttpServletResponse response) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("user will be redirected to: {}",
                    request.getServletPath());
        }
        return request.getServletPath();
    }

}

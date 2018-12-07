/**
 *
 */
package de.his.zofar.presentation.surveyengine.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.Assert;

/**
 * Login to the survey by entering token in the URL AKA direct token.
 *
 * @author le
 *
 */
public class DirectTokenAuthenticationFilter extends
        AbstractAuthenticationProcessingFilter {

    /**
     *
     */
    private static final String DEFAULT_TOKEN_REQUEST_PARAMETER = "zofar_token";

    /**
     *
     */
    private String tokenRequestParameter = DEFAULT_TOKEN_REQUEST_PARAMETER;

    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(DirectTokenAuthenticationFilter.class);

    /**
     *
     */
    public DirectTokenAuthenticationFilter() {
        super("/index.html");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.security.web.authentication.
     * AbstractAuthenticationProcessingFilter
     * #attemptAuthentication(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    @Override
    public Authentication attemptAuthentication(
            final HttpServletRequest request, final HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        String token = obtainTokenFromRequest(request);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("token from request: {}", token);
        }

        if (token == null) {
            token = "";
        }

        token = token.trim();

        final UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                token, token);

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("return Authentication : {}", this
                    .getAuthenticationManager().authenticate(authRequest));
        }
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * Provided so that subclasses may configure what is put into the
     * authentication request's details property.
     *
     * @param request
     *            that an authentication request is being created for
     * @param authRequest
     *            the authentication request object that should have its details
     *            set
     */
    protected void setDetails(final HttpServletRequest request,
            final UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource
                .buildDetails(request));
    }

    /**
     * @param request
     *            the request to obtain the token from
     * @return the token found in the request
     */
    protected final String obtainTokenFromRequest(
            final HttpServletRequest request) {
        return request.getParameter(tokenRequestParameter);
    }

    /**
     * @param tokenRequestParameter
     *            the tokenRequestParameter to set
     */
    public void setTokenRequestParameter(final String tokenRequestParameter) {
        Assert.hasText(tokenRequestParameter, "Token parameter cannot be empty");
        this.tokenRequestParameter = tokenRequestParameter;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.security.web.authentication.
     * AbstractAuthenticationProcessingFilter
     * #requiresAuthentication(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected boolean requiresAuthentication(final HttpServletRequest request,
            final HttpServletResponse response) {
        final String tokenFromRequest = obtainTokenFromRequest(request);
        if (tokenFromRequest == null || tokenFromRequest.isEmpty()) {
            return false;
        } else {
            return super.requiresAuthentication(request, response);
        }
    }

}

/**
 *
 */
package de.his.zofar.service.survey.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import de.his.zofar.persistence.survey.entities.SurveyState;
import de.his.zofar.service.common.model.BaseDTO;

/**
 * represents a participant of a survey. implements UserDetails for
 * authentication reasons.
 *
 * @author le
 *
 */
public class Participant extends BaseDTO implements UserDetails {

    /**
     *
     */
    private static final long serialVersionUID = 3763667739515392165L;

    /**
     * every participant has the ROLE_PARTICPANT role and nothing else.
     */
    private static final Collection<? extends GrantedAuthority> AUTHORITIES = new HashSet<GrantedAuthority>(
            Arrays.asList(new GrantedAuthority[] { new SimpleGrantedAuthority(
                    "ROLE_PARTICIPANT") }));

    /**
     * the token. e.g. the email. if no additional password is provided then
     * this is the password.
     */
    @NotNull
    @Length(min = 8, max = 25)
    private final String token;

    /**
     *
     */
    @Length(min = 8, max = 25)
    private String password;

    /**
     * the survey the participant is participating.
     */
    @NotNull
    private final Survey survey;

    /**
     *
     */
    private Map<SurveyVariable, ParticipantValue> variableValues;

    /**
     * @param token
     *            the token
     * @param survey
     *            the survey
     */
    public Participant(final String token, final Survey survey) {
        super();
        this.token = token;
        this.survey = survey;
    }

    /**
     * @param token
     *            the token
     * @param password
     *            additional password if token is not used as password
     * @param survey
     *            the survey
     */
    public Participant(final String token, final String password,
            final Survey survey) {
        super();
        this.token = token;
        this.password = password;
        this.survey = survey;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @return the survey
     */
    public Survey getSurvey() {
        return survey;
    }

    /**
     * @return the variableValues
     */
    public Map<SurveyVariable, ParticipantValue> getVariableValues() {
        return variableValues;
    }

    /**
     * @param variableValues
     *            the variableValues to set
     */
    public void setVariableValues(
            final Map<SurveyVariable, ParticipantValue> variableValues) {
        this.variableValues = variableValues;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.springframework.security.core.userdetails.UserDetails#getAuthorities
     * ()
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AUTHORITIES;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.springframework.security.core.userdetails.UserDetails#getPassword()
     */
    @Override
    public String getPassword() {
        if (password == null || password.isEmpty()) {
            return token;
        } else {
            return password;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.springframework.security.core.userdetails.UserDetails#getUsername()
     */
    @Override
    public String getUsername() {
        return token;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.springframework.security.core.userdetails.UserDetails#isAccountNonExpired
     * ()
     */
    @Override
    public boolean isAccountNonExpired() {
        return isEnabled();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.springframework.security.core.userdetails.UserDetails#isAccountNonLocked
     * ()
     */
    @Override
    public boolean isAccountNonLocked() {
        return isEnabled();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.security.core.userdetails.UserDetails#
     * isCredentialsNonExpired()
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return isEnabled();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.springframework.security.core.userdetails.UserDetails#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        if (survey.getState().equals(SurveyState.RELEASED)) {
            return true;
        } else {
            return false;
        }
    }

}

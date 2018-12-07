package de.his.zofar.persistence.survey.expressions;

import static de.his.zofar.persistence.survey.daos.SurveyDao.qSurvey;

import java.util.List;

import com.mysema.query.types.expr.BooleanExpression;

import de.his.zofar.persistence.survey.entities.SurveyState;

public class SurveyExpression {

    public static BooleanExpression surveyCanceled() {
        return qSurvey.state.eq(SurveyState.CANCELED);
    }

    public static BooleanExpression surveyNameMatches(final String name) {
        return qSurvey.name.equalsIgnoreCase(name);
    }

    public static BooleanExpression surveyReleased() {
        return qSurvey.state.eq(SurveyState.RELEASED);
    }

    public static BooleanExpression surveyStateIn(
            final List<SurveyState> surveyStates) {
        return qSurvey.state.in(surveyStates);
    }

}

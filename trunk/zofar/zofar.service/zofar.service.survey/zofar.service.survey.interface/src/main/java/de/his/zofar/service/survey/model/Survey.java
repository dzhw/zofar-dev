package de.his.zofar.service.survey.model;

import de.his.zofar.persistence.survey.entities.SurveyEntity;
import de.his.zofar.persistence.survey.entities.SurveyState;
import de.his.zofar.service.common.mapper.annotations.DTOEntityMapping;
import de.his.zofar.service.common.model.BaseDTO;
import de.his.zofar.service.questionnaire.model.Questionnaire;

/**
 * @author Meisner
 *
 */
@DTOEntityMapping(entity = SurveyEntity.class)
public class Survey extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = -8389249842614672796L;
    private String name;
    private SurveyState state;
    private Questionnaire questionnaire;

    public Survey() {
        super();
    }

    public Survey(final String name) {
        super();
        this.name = name;
        this.state = SurveyState.CREATED;
    }


    public String getName() {
        return this.name;
    }

    public SurveyState getState() {
        return this.state;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setState(final SurveyState state) {
        if (this.state != null) {
            this.state.isValidSuccessor(state);
        }
        this.state = state;
    }

	public Questionnaire getQuestionnaire() {
		return questionnaire;
	}

	public void setQuestionnaire(final Questionnaire questionnaire) {
		this.questionnaire = questionnaire;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((questionnaire == null) ? 0 : questionnaire.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Survey other = (Survey) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (questionnaire == null) {
			if (other.questionnaire != null)
				return false;
		} else if (!questionnaire.equals(other.questionnaire))
			return false;
		if (state != other.state)
			return false;
		return true;
	}

}

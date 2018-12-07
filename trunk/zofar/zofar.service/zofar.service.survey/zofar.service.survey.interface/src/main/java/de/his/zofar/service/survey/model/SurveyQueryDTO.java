package de.his.zofar.service.survey.model;

import java.util.List;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import de.his.zofar.persistence.survey.entities.SurveyState;
import de.his.zofar.service.common.model.BaseQueryDTO;

public class SurveyQueryDTO extends BaseQueryDTO {

    @Size(min = 5)
    @NotEmpty
    private String name;

    private List<SurveyState> states;

    public String getName() {
        return this.name;
    }

    public List<SurveyState> getStates() {
        return this.states;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setStates(final List<SurveyState> states) {
        this.states = states;
    }

}

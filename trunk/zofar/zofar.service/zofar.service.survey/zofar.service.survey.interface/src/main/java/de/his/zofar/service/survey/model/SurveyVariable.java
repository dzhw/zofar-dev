package de.his.zofar.service.survey.model;

import de.his.zofar.service.valuetype.model.Variable;

/**
 * @author meisner
 *
 */
public class SurveyVariable extends Variable {

    /**
     *
     */
    private static final long serialVersionUID = 4533924567489106546L;

    /**
     *
     */
    public SurveyVariable() {
        super();
    }

    public SurveyVariable(final Variable var) {
        this();
        this.setUuid(var.getUuid());
        this.setName(var.getName());
        this.setValueType(var.getValueType());
    }
}

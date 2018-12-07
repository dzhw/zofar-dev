/**
 * 
 */
package de.his.zofar.service.questionnaire.model;

import java.io.Serializable;
import java.util.UUID;

import de.his.zofar.service.common.model.BaseDTO;


/**
 * @author le
 * 
 */
public class Transition extends BaseDTO implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 5548605077167306773L;
    private String condition;
    private UUID nextPageUuid;

    /**
     * 
     */
    public Transition() {
        super();
    }

    /**
     * @return the condition
     */
    public String getCondition() {
        return condition;
    }

    /**
     * @param condition
     *            the condition to set
     */
    public void setCondition(final String conditions) {
        this.condition = conditions;
    }

    /**
     * @return the nextPageUuid
     */
    public UUID getNextPageUuid() {
        return nextPageUuid;
    }

    /**
     * @param nextPageUuid
     *            the nextPage to set
     */
    public void setNextPageUuid(final UUID nextPage) {
        this.nextPageUuid = nextPage;
    }

}

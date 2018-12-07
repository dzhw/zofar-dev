/**
 * 
 */
package de.his.zofar.service.question.model.questionvalues;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.service.question.model.QuestionVariable;

/**
 * @author le
 * 
 */
public class QuestionBooleanValue extends QuestionValue implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -8866553040443593646L;

	/**
     * 
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(QuestionBooleanValue.class);

    /**
     * 
     */
    private Boolean value;

    /**
     * 
     */
    public QuestionBooleanValue() {
        super();
    }

    /**
     * @param value
     */
    public QuestionBooleanValue(final Boolean value) {
        super();
        this.value = value;
    }

    /**
     * @param variable
     */
    public QuestionBooleanValue(final QuestionVariable variable,
            final Boolean value) {
        super(variable);
        this.value = value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.his.hiob.service.question.external.dtos.questionvalues.QuestionValueDTO
     * #getValue()
     */
    @Override
    public Object getValue() {
        return value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.his.hiob.service.question.external.dtos.questionvalues.QuestionValueDTO
     * #setValue(java.lang.Object)
     */
    @Override
    public void setValue(final Object value) {
        LOGGER.debug("value class {}", value.getClass());
        if (!Boolean.class.isAssignableFrom(value.getClass())) {
            throw new IllegalArgumentException(
                    "value of BooleanValue must be a Boolean");
        }
        this.value = (Boolean) value;
    }

}

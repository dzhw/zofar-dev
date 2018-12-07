/**
 * 
 */
package de.his.zofar.service.question.model.questionvalues;

import java.io.Serializable;

import de.his.zofar.service.question.model.QuestionVariable;

/**
 * @author le
 * 
 */
// @DTOEntityMapping(entity = QuestionNumberValueEntity.class)
public class QuestionNumberValue extends QuestionValue implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 6001108423500258120L;
	private Long value;

    /**
     * 
     */
    public QuestionNumberValue() {
        super();
    }

    /**
     * @param value
     */
    public QuestionNumberValue(final Long value) {
        super();
        this.value = value;
    }

    /**
     * @param variable
     */
    public QuestionNumberValue(final QuestionVariable variable,
            final Long value) {
        super(variable);
        this.value = value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.his.hiob.service.question.external.dtos.questionvalues.QuestionValueDTO
     * #getValue ()
     */
    @Override
    public Object getValue() {
        return this.value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final QuestionNumberValue other = (QuestionNumberValue) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

    /**
     * @param value
     *            the value to set
     */
    @Override
    public void setValue(final Object value) {
        if (value != null) {
            if (String.class.isAssignableFrom(value.getClass())) {
                this.value = Long.parseLong((String) value);
            } else if (Long.class.isAssignableFrom(value.getClass())) {
                this.value = (Long) value;
            } else {
                throw new NumberFormatException();
            }
        } else {
            this.value = null;
        }
    }

    @Override
    public String toString() {
        return "QuestionNumberValue [getValue()=" + getValue()
                + ", getVariable()=" + getVariable() + "]";
    }

}

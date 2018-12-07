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
// @DTOEntityMapping(entity = QuestionStringValueEntity.class)
public class QuestionStringValue extends QuestionValue implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -5759180533020139813L;
	private String value;

    /**
     * 
     */
    public QuestionStringValue() {
        super();
    }

    /**
     * @param value
     */
    public QuestionStringValue(final String value) {
        super();
        this.value = value;
    }

    /**
     * @param variable
     */
    public QuestionStringValue(final QuestionVariable variable,
            final String value) {
        super(variable);
        this.value = value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final QuestionStringValue other = (QuestionStringValue) obj;
        if (this.value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!this.value.equals(other.value)) {
            return false;
        }
        return true;
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
        int result = super.hashCode();
        result = prime * result
                + ((this.value == null) ? 0 : this.value.hashCode());
        return result;
    }

    /**
     * @param value
     *            the value to set
     */
    @Override
    public void setValue(final Object value) {
        this.value = (String) value;
    }

    @Override
    public String toString() {
        return "QuestionStringValueDTO [getValue()=" + getValue()
                + ", getVariable()=" + getVariable() + "]";
    }

}

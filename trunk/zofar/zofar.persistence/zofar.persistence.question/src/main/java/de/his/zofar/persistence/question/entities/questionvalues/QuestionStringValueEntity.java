/**
 * 
 */
package de.his.zofar.persistence.question.entities.questionvalues;

import javax.persistence.Entity;

import de.his.zofar.persistence.question.entities.QuestionVariableEntity;

/**
 * @author le
 * 
 */
@Entity
public class QuestionStringValueEntity extends QuestionValueEntity {
    private String value;

    /**
     * 
     */
    public QuestionStringValueEntity() {
        super();
    }

    /**
     * @param variable
     */
    public QuestionStringValueEntity(QuestionVariableEntity variable, String value) {
        super(variable);
        this.value = value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.his.hiob.model.question.entities.questionvalues.QuestionValue#getValue
     * ()
     */
    @Override
    public Object getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    @Override
    public void setValue(Object value) {
        this.value = (String) value;
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
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        QuestionStringValueEntity other = (QuestionStringValueEntity) obj;
        if (value == null) {
            if (other.value != null) return false;
        } else if (!value.equals(other.value)) return false;
        return true;
    }

}

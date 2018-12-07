/**
 * 
 */
package de.his.zofar.persistence.question.entities.questionvalues;

import static javax.persistence.CascadeType.PERSIST;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import de.his.zofar.persistence.common.entities.BaseEntity;
import de.his.zofar.persistence.question.entities.QuestionVariableEntity;

/**
 * @author le
 * 
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@SequenceGenerator(initialValue = 1, name = "primary_key_generator",
        sequenceName = "SEQ_QUESTION_VALUE_ID")
public abstract class QuestionValueEntity extends BaseEntity {
    @ManyToOne(optional = false, cascade = PERSIST)
    private QuestionVariableEntity variable;

    /**
     * 
     */
    public QuestionValueEntity() {
        super();
    }

    /**
     * @param variable
     */
    public QuestionValueEntity(QuestionVariableEntity variable) {
        super();
        this.variable = variable;
    }

    /**
     * @return the variable
     */
    public QuestionVariableEntity getVariable() {
        return variable;
    }

    /**
     * @param variable
     *            the variable to set
     */
    public void setVariable(QuestionVariableEntity variable) {
        this.variable = variable;
    }

    public abstract Object getValue();

    public abstract void setValue(Object value);
}

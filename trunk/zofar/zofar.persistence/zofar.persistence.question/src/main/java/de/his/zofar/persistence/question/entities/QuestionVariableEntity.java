/**
 * 
 */
package de.his.zofar.persistence.question.entities;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;

import de.his.zofar.persistence.valuetype.entities.VariableEntity;

/**
 * @author le
 *
 */
@Entity
@SequenceGenerator(initialValue = 1, name = "primary_key_generator",
        sequenceName = "SEQ_QUESTION_VARIABLE_ID")
public class QuestionVariableEntity extends VariableEntity {

    /**
     * 
     */
    public QuestionVariableEntity() {
        super();
    }

}

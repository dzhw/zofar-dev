package de.his.zofar.persistence.question.entities.concrete;

import javax.persistence.Entity;

import de.his.zofar.persistence.question.entities.AnswerableQuestionEntity;

@Entity
public class OpenQuestionEntity extends AnswerableQuestionEntity {

    public OpenQuestionEntity() {
        super();
    }
}

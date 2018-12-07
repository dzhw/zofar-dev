package de.his.zofar.persistence.question.entities;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;

import de.his.zofar.persistence.common.entities.BaseEntity;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@SequenceGenerator(initialValue = 1, name = "primary_key_generator",
        sequenceName = "SEQ_QUESTION_ID")
// @DTOEntityMapping(model = Question.class)
public abstract class QuestionEntity extends BaseEntity {
    private String introduction;

    private String questionText;

    private String instruction;

    /**
     *
     */
    public QuestionEntity() {
        super();
    }

    /**
     * @param questionText
     */
    public QuestionEntity(final String questionText) {
        super();
        this.questionText = questionText;
    }

    /**
     * @param introduction
     * @param questionText
     * @param instruction
     */
    public QuestionEntity(final String introduction, final String questionText, final String instruction) {
        super();
        this.introduction = introduction;
        this.questionText = questionText;
        this.instruction = instruction;
    }

    /**
     * @return the introduction
     */
    public String getIntroduction() {
        return introduction;
    }

    /**
     * @param introduction
     *            the introduction to set
     */
    public void setIntroduction(final String introduction) {
        this.introduction = introduction;
    }

    public String getQuestionText() {
        return this.questionText;
    }

    public void setQuestionText(final String questionText) {
        this.questionText = questionText;
    }

    /**
     * @return the instruction
     */
    public String getInstruction() {
        return instruction;
    }

    /**
     * @param instruction
     *            the instruction to set
     */
    public void setInstruction(final String instruction) {
        this.instruction = instruction;
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
                + ((instruction == null) ? 0 : instruction.hashCode());
        result = prime * result
                + ((introduction == null) ? 0 : introduction.hashCode());
        result = prime * result
                + ((questionText == null) ? 0 : questionText.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        final QuestionEntity other = (QuestionEntity) obj;
        if (instruction == null) {
            if (other.instruction != null) return false;
        } else if (!instruction.equals(other.instruction)) return false;
        if (introduction == null) {
            if (other.introduction != null) return false;
        } else if (!introduction.equals(other.introduction)) return false;
        if (questionText == null) {
            if (other.questionText != null) return false;
        } else if (!questionText.equals(other.questionText)) return false;
        return true;
    }

}

/**
 *
 */
package de.his.zofar.service.question.model.structure;

/**
 * @author le
 *
 */
public class QuestionText extends Text {

    /**
     *
     */
    private static final long serialVersionUID = 6053831259249838752L;

    /**
     *
     */
    public QuestionText() {
        super("DEFAULT QUESTION TEXT");
    }

    /**
     * @param text
     */
    public QuestionText(final String text) {
        super(text);
    }

}

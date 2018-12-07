/**
 *
 */
package de.his.zofar.service.question.model.structure;

/**
 * @author le
 *
 */
public class IntroductionText extends Text {

    /**
     *
     */
    private static final long serialVersionUID = 6693845081159118497L;

    /**
     *
     */
    public IntroductionText() {
        super("DEFAULT INTRODUCTION TEXT");
    }

    /**
     * @param text
     */
    public IntroductionText(final String text) {
        super(text);
    }

}

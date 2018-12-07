/**
 *
 */
package de.his.zofar.service.question.model.structure;

/**
 * @author le
 *
 */
public class InstructionText extends Text {

    /**
     *
     */
    private static final long serialVersionUID = -4947870864882828048L;

    /**
     *
     */
    public InstructionText() {
        super("DEFAULT INSTRUCTION TEXT");
    }

    /**
     * @param text
     */
    public InstructionText(final String text) {
        super(text);
    }

}

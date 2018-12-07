/**
 *
 */
package de.his.zofar.service.question.model.structure;

import java.io.Serializable;

/**
 * @author le
 *
 */
public class Text implements StructuredElement, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3962086848989705038L;

    /**
     *
     */
    private String visibilityCondition;

    /**
     *
     */
    private final String text;

    /**
     *
     */
    public Text() {
        this("DEFAULT TEXT");
    }

    /**
     * @param text
     */
    public Text(final String text) {
        super();
        this.text = text;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.his.hiob.service.question.external.dtos.structure.StructuredElementDTO
     * #getContent()
     */
    @Override
    public Object getContent() {
        return text;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.his.hiob.service.common.external.dtos.conditionable.impl.Visibility
     * #getVisibilityCondition()
     */
    @Override
    public final String getVisibilityCondition() {
        if (visibilityCondition == null) {
            visibilityCondition = "";
        }
        return visibilityCondition;
    }

    /**
     * @param visibilityCondition
     *            the visibilityCondition to set
     */
    public final void setVisibilityCondition(final String visibilityCondition) {
        this.visibilityCondition = visibilityCondition;
    }

}

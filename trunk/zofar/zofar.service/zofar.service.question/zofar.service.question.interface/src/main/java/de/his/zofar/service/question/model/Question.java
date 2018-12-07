package de.his.zofar.service.question.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.his.zofar.service.common.model.BaseDTO;
import de.his.zofar.service.common.model.conditionable.impl.Visibility;
import de.his.zofar.service.question.model.structure.StructuredElement;

public abstract class Question extends BaseDTO implements Visibility,
        Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 7504274977862694597L;

    /**
     *
     */
    private String visibilityCondition;

    /**
     *
     */
    private List<StructuredElement> header;

    /**
     *
     */
    public Question() {
        super();
    }

    /**
     * @return the header
     */
    public List<StructuredElement> getHeader() {
        if (header == null) {
            header = new ArrayList<StructuredElement>();
        }
        return header;
    }

    /**
     * @param element
     *            the element to add
     */
    public final void addHeaderElement(final StructuredElement element) {
        getHeader().add(element);
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

/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.question;

import javax.faces.component.UINamingContainer;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.common.Visible;
import de.his.zofar.presentation.surveyengine.ui.renderer.ZofarQuestionRenderer;

/**
 * base component for all questions in zofar. this also includes matrices and
 * other composites.
 *
 * @author le
 *
 */
public abstract class UIQuestion extends UINamingContainer implements Identificational,Visible {

	private static final String STYLE_CLASS = "zo-question";

    public static final String HEADER_STYLE_CLASS = "zo-question-header";

    public static final String COMPONENT_FAMILY = "org.zofar.Question";

    private enum PropertyKeys {
        styleClass
    }

    public UIQuestion() {
        super();
    }
    
	@Override
	@Deprecated
	public String getUID() {
		return this.getId();
	}

	@Override
	@Deprecated
	public Boolean visibleCondition() {
		return this.isRendered();
	}

    /**
     * returns the specific CSS style class of the question.
     *
     * @return
     */
    protected abstract String getSpecificStyleClass();

    /**
     * appends the style class set by the user of the component to the one set
     * in the constructor.
     *
     * @param styleClass
     */
    public void setStyleClass(final String styleClass) {
        this.getStateHelper().put(PropertyKeys.styleClass, styleClass);
    }

    /**
     * returns the style class of this component.
     *
     * @return
     */
    public String getStyleClass() {
        String styleClass = STYLE_CLASS + " " + this.getSpecificStyleClass();

        final String additionalStyleClass = (String) this.getStateHelper().get(
                PropertyKeys.styleClass);
        if (additionalStyleClass != null && !additionalStyleClass.isEmpty()) {
            styleClass += " " + additionalStyleClass;
        }

        return styleClass;
    }

    /**
     * the layout of all questions is always "block".
     *
     * @return
     */
    public String getLayout() {
        return "block";
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.component.UIComponentBase#getRendererType()
     */
    @Override
    public String getRendererType() {
        return ZofarQuestionRenderer.RENDERER_TYPE;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.component.UINamingContainer#getFamily()
     */
    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.component.UIComponentBase#isTransient()
     */
    @Override
    public boolean isTransient() {
        return true;
    }

}

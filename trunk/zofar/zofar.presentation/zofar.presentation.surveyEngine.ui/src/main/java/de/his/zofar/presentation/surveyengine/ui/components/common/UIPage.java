/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.common;

import javax.faces.component.FacesComponent;
import javax.faces.component.html.HtmlForm;

/**
 * @author le
 *
 */
@FacesComponent("org.zofar.Page")
public class UIPage extends HtmlForm {

    private static final String CSS_CLASS = "zo-page";

    private enum PropertyKeys {
        styleClass
    }

    @Override
    public String getRendererType() {
        return "javax.faces.Form";
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.component.html.HtmlForm#getStyleClass()
     */
    @Override
    public String getStyleClass() {
        final String styleClass = (String) getStateHelper().eval(
                PropertyKeys.styleClass);
        if (styleClass == null) {
            return CSS_CLASS;
        } else {
            return CSS_CLASS + " " + styleClass;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.component.html.HtmlForm#setStyleClass(java.lang.String)
     */
    @Override
    public void setStyleClass(final String styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, styleClass);
    }

}

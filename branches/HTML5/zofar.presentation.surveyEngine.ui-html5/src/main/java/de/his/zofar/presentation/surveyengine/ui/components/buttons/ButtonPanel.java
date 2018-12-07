/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.buttons;

import java.util.ResourceBundle;

import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;

/**
 * @author meisner
 *
 */
@FacesComponent(value = "org.zofar.ButtonPanel")
public class ButtonPanel extends UINamingContainer {

	private ResourceBundle bundle;

	public ButtonPanel() {
		super();
		this.bundle = ResourceBundle.getBundle("de.his.zofar.messages.buttonPanel", FacesContext.getCurrentInstance().getViewRoot().getLocale());
	}

	public ResourceBundle getBundle() {
		if ((this.bundle != null) && (!this.bundle.getLocale().equals(FacesContext.getCurrentInstance().getViewRoot().getLocale()))) {
			this.bundle = ResourceBundle.getBundle("de.his.zofar.messages.buttonPanel", FacesContext.getCurrentInstance().getViewRoot().getLocale());
		}
		return this.bundle;
	}

}

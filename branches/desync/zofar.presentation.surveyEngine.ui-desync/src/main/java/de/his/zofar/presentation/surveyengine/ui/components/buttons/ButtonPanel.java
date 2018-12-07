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
        bundle = ResourceBundle.getBundle("de.his.zofar.messages.buttonPanel", 
                FacesContext.getCurrentInstance().getViewRoot().getLocale());
    }
    
    public ResourceBundle getBundle() {
    	if((bundle != null)&&(!bundle.getLocale().equals(FacesContext.getCurrentInstance().getViewRoot().getLocale()))){
            bundle = ResourceBundle.getBundle("de.his.zofar.messages.buttonPanel", 
                    FacesContext.getCurrentInstance().getViewRoot().getLocale());
    	}
        return bundle;
    }

}

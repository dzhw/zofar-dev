package de.his.zofar.presentation.surveyengine.ui.components.common;

/**
 * marker interface which indicates zofar components.
 *
 * @author meisner
 *
 */
public interface Visible {
	
    /**
     * @deprecated using isRendered() from UIComponent instead.
     * @return
     */
    @Deprecated
    public Boolean visibleCondition();
	
	public boolean isRendered();
	public void setRendered(boolean condition);

}

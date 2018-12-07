package de.his.zofar.presentation.surveyengine.ui.components.common;
/**
 * @author meisner
 *
 */
public interface Alignable {
	
    /**
     * @deprecated using getId() from UIComponent instead.
     * @return
     */
    @Deprecated
    public Boolean isHorizontalAligned();
    
    public String getDirection();
    public void setDirection(String id);
}

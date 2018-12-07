/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.common;


/**
 * marker interface which indicates zofar components.
 *
 * @author le
 *
 */
public interface Identificational {

    /**
     * @deprecated using getId() from UIComponent instead.
     * @return
     */
    @Deprecated
    public String getUID();

    public String getId();
    public void setId(String id);

    public String getClientId();

}
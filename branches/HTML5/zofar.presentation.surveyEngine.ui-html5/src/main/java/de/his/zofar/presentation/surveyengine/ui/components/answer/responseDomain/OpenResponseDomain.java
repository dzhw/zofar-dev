/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.answer.responseDomain;

import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.interfaces.IResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.open.responsedomain.ZofarOpenResponseDomainRenderer;

/**
 * @author meisner
 *
 */
@FacesComponent(value = "org.zofar.OpenResponseDomain")
public class OpenResponseDomain extends UINamingContainer implements Identificational,IResponseDomain {
	
	public static final String COMPONENT_FAMILY = "org.zofar.OpenResponseDomain";
	
	public OpenResponseDomain() {
		super();
	}
	
	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public String getRendererType() {
		String rendererType = "UNKOWN";
		rendererType = ZofarOpenResponseDomainRenderer.RENDERER_TYPE;
//		System.out.println("render type : "+rendererType);
		return rendererType;
	}
	
	@Override
	public boolean isTransient() {
		return true;
	}

}

package de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.singlechoice.responsedomain;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import de.his.zofar.presentation.surveyengine.ui.interfaces.IResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.singechoice.responsedomain.ZofarDropDownResponseDomainRenderer;

@FacesRenderer(componentFamily = IResponseDomain.COMPONENT_FAMILY, rendererType = ZofarDropDownMatrixResponseDomainRenderer.RENDERER_TYPE)
public class ZofarDropDownMatrixResponseDomainRenderer extends ZofarDropDownResponseDomainRenderer {
	
	 public static final String RENDERER_TYPE = "org.zofar.DropDownMatrixResponseDomain";
	 
	 @Override
	public synchronized void encodeBegin(FacesContext context, UIComponent component)
			throws IOException {
		 if (!component.isRendered()) {
	            return;
	        }
		ResponseWriter writer=context.getResponseWriter();
		writer.startElement("td", component);
		super.encodeBegin(context, component);
		
	}

	@Override
	public synchronized void encodeEnd(FacesContext context, UIComponent component)
			throws IOException {
		ResponseWriter writer=context.getResponseWriter();
		
		super.encodeEnd(context, component);
		writer.endElement("td");
	}

}

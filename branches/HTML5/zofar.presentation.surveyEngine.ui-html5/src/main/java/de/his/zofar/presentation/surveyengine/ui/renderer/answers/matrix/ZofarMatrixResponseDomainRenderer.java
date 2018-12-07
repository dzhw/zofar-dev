package de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.answer.responseDomain.UIDropDownMissingResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.components.question.matrix.UIMatrixItem;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.ZofarResponseDomainRenderer;

/**
 * @author meisner
 *
 */
public abstract class ZofarMatrixResponseDomainRenderer extends ZofarResponseDomainRenderer {
	private static final Logger LOGGER = LoggerFactory.getLogger(ZofarMatrixResponseDomainRenderer.class);
	

	public ZofarMatrixResponseDomainRenderer() {
		super();
	}

	@Override
	public boolean getRendersChildren() {
		return true;
	}

	public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
//		LOGGER.info("encodeBegin");
		if (!component.isRendered()) {
			return;
		}
		
		boolean dropdown=false;
		
		for (final UIComponent child : component.getChildren()) {
			for (final UIComponent childD : child.getChildren()) {
				if((UIDropDownMissingResponseDomain.class).isAssignableFrom(childD.getClass())){
					dropdown=true;
				}
			}
		}
		
		final ResponseWriter writer = context.getResponseWriter();
		
		writer.startElement("div", component);
		writer.writeAttribute("id", component.getClientId(context), null);
		writer.writeAttribute("class", "form-orientation form-responsive", null);
		
		writer.startElement("div", component);
		writer.writeAttribute("class", "form-scroller", null);
		
		writer.startElement("div", component);
		writer.writeAttribute("class", "question-sub", null);
		writer.startElement("div", component);
		writer.writeAttribute("class", "container", null);
		
		writer.startElement("p", component);
		writer.writeAttribute("class", "text-head hidden-md-up", null);
		writer.writeAttribute("data-carousel-head-id", component.getClientId(context).replace(':', '_')+"_carousel", null);
		writer.endElement("p");
		
		writer.endElement("div");
		writer.endElement("div");
		
		writer.startElement("div", component);
		writer.writeAttribute("class", "form-inner form-2-col pt-2 pt-sm-4", null);
		
		writer.startElement("div", component);
		writer.writeAttribute("class", "container", null);
		
		
		if (!dropdown) {
			writer.startElement("div", component);
			writer.writeAttribute("id", component.getClientId(context).replace(':', '_')+"_carousel", null);
			writer.writeAttribute("class", "carousel slide", null);
		
			writer.writeAttribute("data-interval","false", null);
				
			writer.startElement("ol", component);
			writer.writeAttribute("class", "carousel-indicators carousel-indicators-numbers hidden-md-up", null);
			int lft = 0;
			for (final UIComponent child : component.getChildren()) {
				if((UIMatrixItem.class).isAssignableFrom(child.getClass())) {
					if(!child.isRendered())continue;
					
					writer.startElement("li", component);
					
					writer.writeAttribute("data-target", "#"+component.getClientId(context).replace(':', '_')+"_carousel", null);
					writer.writeAttribute("data-slide-to", lft, null);
					if(lft == 0)writer.writeAttribute("class", "active", null);
					
					writer.endElement("li");
					lft = lft + 1;
					
				}
			}
			writer.endElement("ol");
	
			writer.startElement("div", component);
			writer.writeAttribute("class", "carousel-inner", null);
			writer.writeAttribute("role", "listbox", null);
		}
	}
	
	@Override
	public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
//		LOGGER.info("encodeChildren");
		if (!component.isRendered()) {
			return;
		}
		super.encodeChildren(context, component);
	}

	@Override
	public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
//		LOGGER.info("encodeEnd");
		if (!component.isRendered()) {
			return;
		}
		final ResponseWriter writer = context.getResponseWriter();
		
		boolean dropdown=false;
		
		for (final UIComponent child : component.getChildren()) {
			for (final UIComponent childD : child.getChildren()) {
				if((UIDropDownMissingResponseDomain.class).isAssignableFrom(childD.getClass())){
					dropdown=true;
					break;
				}
			}
		}		
		if (!dropdown) {
			writer.endElement("div");
			
			writer.startElement("div", component);
			writer.writeAttribute("class", "carousel-control-outer hidden-md-up", null);
			
			writer.startElement("a", component);
			writer.writeAttribute("class", "btn btn-secondary carousel-control-prev", null);
			writer.writeAttribute("hRef", "#"+component.getClientId(context).replace(':', '_')+"_carousel", null);
			writer.writeAttribute("role", "button", null);
			writer.writeAttribute("data-slide", "prev", null);
			
			writer.startElement("i", component);
			writer.writeAttribute("class", "fa fa-angle-left", null);
			writer.writeAttribute("aria-hidden", "true", null);
			writer.endElement("i");
			
			writer.startElement("span", component);
			writer.writeAttribute("class", "sr-only", null);
			writer.write("Previous");
			writer.endElement("span");
			
			writer.endElement("a");
			
			writer.startElement("a", component);
			writer.writeAttribute("class", "btn btn-secondary carousel-control-next", null);
			writer.writeAttribute("hRef", "#"+component.getClientId(context).replace(':', '_')+"_carousel", null);
			writer.writeAttribute("role", "button", null);
			writer.writeAttribute("data-slide", "next", null);
			
			writer.startElement("i", component);
			writer.writeAttribute("class", "fa fa-angle-right", null);
			writer.writeAttribute("aria-hidden", "true", null);
			writer.endElement("i");
			
			writer.startElement("span", component);
			writer.writeAttribute("class", "sr-only", null);
			writer.write("Next");
			writer.endElement("span");
			
			writer.endElement("a");
			
			writer.endElement("div");
		}
		
		writer.endElement("div");
		writer.endElement("div");
		writer.endElement("div");
		writer.endElement("div");
//		writer.endElement("div");

	}



}

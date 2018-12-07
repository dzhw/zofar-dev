package de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.answer.options.UIAttachedOpenQuestion;
import de.his.zofar.presentation.surveyengine.ui.components.answer.responseDomain.UIDropDownMissingResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.interfaces.ISequence;
import de.his.zofar.presentation.surveyengine.ui.util.JsfUtility;

/**
 * @author meisner
 *
 */
public abstract class ZofarMatrixItemRenderer extends Renderer {

	private static final Logger LOGGER = LoggerFactory.getLogger(ZofarMatrixItemRenderer.class);

	public ZofarMatrixItemRenderer() {
		super();
	}

	@Override
	public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}
		
		boolean dropdown=false;
		
		for (final UIComponent child : component.getChildren()) {
			if((UIDropDownMissingResponseDomain.class).isAssignableFrom(child.getClass())){
				dropdown=true;
				break;
			}
		}

		final ResponseWriter writer = context.getResponseWriter();
		writer.startElement("div", component);
		String classes = "row highlight ";
		
		if(((ISequence.class).isAssignableFrom(component.getClass()))){
			final Object sequenceId = ((ISequence)component).getSequenceId();
			if(sequenceId != null){
				final int index = (Integer)sequenceId;
				if(index % 2 == 0)classes += "highlight-even ";
				else classes += "highlight-odd ";
				//TODO VD
//				classes += "carousel-item item";
				if(!dropdown) {
					classes += "carousel-item item";
				} else {
					classes += "item";
				}
				if(index == 0)classes += " active";
//				writer.writeAttribute("data-matrix-sequence", sequenceId, null);
			}
		}
		
//		classes += "carousel-item active";
		writer.writeAttribute("class", classes, null);
		writer.writeAttribute("data-matrix", "item", null);

		
		writer.startElement("div", component);
		if(!dropdown) { 
			writer.writeAttribute("class", "col-md-4 hidden-sm-down", null);
		} else {
			writer.writeAttribute("class", "col-md-6 dropDownsmall", null);
		}
		final UIComponent header = component.getFacet("header");
		if (header != null) {
			writer.startElement("p", header);
			writer.writeAttribute("class", "text-sub", null);
			writer.writeAttribute("id", header.getClientId(context)+"_header", null);

			writer.write(JsfUtility.getInstance().getTextComponentAsString(context, header));
			
			for (final UIComponent child : component.getChildren()) {
				if((UIAttachedOpenQuestion.class).isAssignableFrom(child.getClass())){
					child.encodeAll(context);
				}
			}
			writer.endElement("p");
			
		}
		writer.endElement("div");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * javax.faces.component.UIComponentBase#encodeChildren(javax.faces.context
	 * .FacesContext)
	 */
	@Override
	public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}
		for (final UIComponent child : component.getChildren()) {
			if((UIAttachedOpenQuestion.class).isAssignableFrom(child.getClass()))continue;
			child.encodeAll(context);
		}
	}
	
	@Override
	public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}
//		super.encodeEnd(context, component);
		final ResponseWriter writer = context.getResponseWriter();
		writer.endElement("div");
	}

	@Override
	public boolean getRendersChildren() {
		return true;
	}

}

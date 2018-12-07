package de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.singlechoice.responsedomain;

import java.io.IOException;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.answer.options.SingleOption;
import de.his.zofar.presentation.surveyengine.ui.components.answer.responseDomain.UIDropDownMissingResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.components.question.composite.doublematrix.UIDoubleMatrixResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.components.question.matrix.UIMatrixResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.components.question.matrix.singlechoice.UISingleChoiceMatrixItemResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.interfaces.ISequence;
import de.his.zofar.presentation.surveyengine.ui.util.JsfUtility;

/**
 * @author meisner
 *
 */
@FacesRenderer(componentFamily = UISingleChoiceMatrixItemResponseDomain.COMPONENT_FAMILY, rendererType = ZofarSingleChoiceMatrixItemResponseDomainRenderer.RENDERER_TYPE)
public class ZofarSingleChoiceMatrixItemResponseDomainRenderer extends ZofarGeneralRadioMatrixItemResponseDomainRenderer {

	public static final String RENDERER_TYPE = "org.zofar.SCMatrixItemResponseDomain";

	private static final Logger LOGGER = LoggerFactory.getLogger(ZofarSingleChoiceMatrixItemResponseDomainRenderer.class);

	public ZofarSingleChoiceMatrixItemResponseDomainRenderer() {
		super();
	}

	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}
		// super.encodeBegin(context, component);
		if (!(UIDropDownMissingResponseDomain.class).isAssignableFrom(JsfUtility.getInstance().getParentResponseDomain(component.getParent()).getClass())) {
			final ResponseWriter writer = context.getResponseWriter();
			writer.startElement("div", component);
			writer.writeAttribute("class", "col-12 col-md-8", null);
	
			writer.startElement("div", component);
			writer.writeAttribute("id", component.getClientId(), null);
			writer.writeAttribute("class", "custom-form custom-form-radio add-pipe", null);
	
			writer.startElement("div", component);
			writer.writeAttribute("class", "flex-wrapper", null);
		}

	}

	@Override
	public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}

		boolean isFirst = true;
		
		UIComponent currentMatrixItem = component.getParent();
		if((currentMatrixItem != null)&&((ISequence.class).isAssignableFrom(currentMatrixItem.getClass()))){
			final Object sequenceId = ((ISequence)currentMatrixItem).getSequenceId();
			if((sequenceId != null)&&(((Integer)sequenceId)>0))isFirst = false;
		}
		
		UIMatrixResponseDomain matrixRDC=null;
		if ((UIDropDownMissingResponseDomain.class).isAssignableFrom(JsfUtility.getInstance().getParentResponseDomain(currentMatrixItem).getClass())) {
			matrixRDC = (UIMatrixResponseDomain) JsfUtility.getInstance().getParentResponseDomain(currentMatrixItem).getParent().getParent();
		} else {
			matrixRDC = (UIMatrixResponseDomain) JsfUtility.getInstance().getParentResponseDomain(currentMatrixItem);
		}
		if(matrixRDC != null){
			component.getAttributes().put("showValues",matrixRDC.isShowValues());
		}
		
//		UIMatrixResponseDomain matrixRDC = (UIMatrixResponseDomain) JsfUtility.getInstance().getParentResponseDomain(currentMatrixItem);
//		if(matrixRDC != null){
//			component.getAttributes().put("showValues",matrixRDC.isShowValues());
//		}
		
		

		List<UIComponent> singleOptions = JsfUtility.getInstance().getVisibleChildrensOfType(component, SingleOption.class);	
		if ((singleOptions != null) && (!isFirst)) {
			for (final UIComponent child : singleOptions) {
				if (((SingleOption.class).isAssignableFrom(child.getClass()))) {
					((SingleOption) child).setShowLabelFlag(false);
				}
			}
		}

		super.encodeChildren(context, component);
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}
		// super.encodeEnd(context, component);
		if (!(UIDropDownMissingResponseDomain.class).isAssignableFrom(JsfUtility.getInstance().getParentResponseDomain(component.getParent()).getClass())) {
			final ResponseWriter writer = context.getResponseWriter();
			writer.endElement("div");
			writer.endElement("div");
			writer.endElement("div");
		}
			
		
	}

}

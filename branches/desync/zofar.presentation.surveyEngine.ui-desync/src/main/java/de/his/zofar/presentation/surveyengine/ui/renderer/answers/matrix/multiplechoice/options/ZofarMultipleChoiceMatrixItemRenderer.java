package de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.multiplechoice.options;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.faces.facelets.compiler.UIInstructions;

import de.his.zofar.presentation.surveyengine.ui.components.answer.options.UIAttachedOpenQuestion;
import de.his.zofar.presentation.surveyengine.ui.components.composite.multiplechoice.UIMultipleChoiceCompositeItem;
import de.his.zofar.presentation.surveyengine.ui.components.tablebase.AbstractTableItem;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.ZofarMatrixItemRenderer;
import de.his.zofar.presentation.surveyengine.ui.util.JsfUtility;
/**
 * @author meisner
 * 
 */
@FacesRenderer(componentFamily = AbstractTableItem.COMPONENT_FAMILY, rendererType = ZofarMultipleChoiceMatrixItemRenderer.RENDERER_TYPE)
public class ZofarMultipleChoiceMatrixItemRenderer extends
		ZofarMatrixItemRenderer {
	
	public static final String RENDERER_TYPE = "org.zofar.MultipleChoiceCompositeItem";
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ZofarMultipleChoiceMatrixItemRenderer.class);


	public ZofarMultipleChoiceMatrixItemRenderer() {
		super();
	}

	@Override
	public void encodeBegin(FacesContext context, UIComponent component)
			throws IOException {
		final ResponseWriter writer = context.getResponseWriter();

		// rendering header facet
		final UIComponent header = component.getFacet(HEADER_FACET);
//		LOGGER.info("header {} ({})",header,this.isHasQuestionColumn(context,component));
		if (header != null && this.isHasQuestionColumn(context, component)) {
			writer.startElement(TABLE_CELL, component);
			writer.writeAttribute("class",
					"zo-matrix-item-header zo-mc-matrix-item-header", null);
			if (header.getChildren().isEmpty()) {
				header.encodeAll(context);
			} else {
				for (final UIComponent child : header.getChildren()) {
					if (UIInstructions.class.isAssignableFrom(child.getClass())) {
						writer.write(JsfUtility.getInstance()
								.evaluateValueExpression(context,
										String.valueOf(child), String.class));
					} else {
						child.encodeAll(context);
					}
				}
			}
			if((UIMultipleChoiceCompositeItem.class).isAssignableFrom(component.getClass())){
				for (final UIComponent child : component.getChildren()) {
					if((UIAttachedOpenQuestion.class).isAssignableFrom(child.getClass())){
						child.encodeAll(context);
					}
				}
			}
			writer.endElement(TABLE_CELL);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.faces.component.UIComponentBase#encodeChildren(javax.faces.context
	 * .FacesContext)
	 */
	@Override
	public void encodeChildren(FacesContext context, UIComponent component)
			throws IOException {
		final ResponseWriter writer = context.getResponseWriter();
		int counter=0;
		for (final UIComponent child : component.getChildren()) {
			if((UIAttachedOpenQuestion.class).isAssignableFrom(child.getClass())){
				continue;
			}
			writer.startElement(TABLE_CELL, component);
			writer.writeAttribute("class", "zo-mc-matrix-item-response-domain-check-"+(counter++), null);       
			writer.writeAttribute("align", "center", null);       
			child.encodeAll(context);
			writer.endElement(TABLE_CELL);
		}
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component)
			throws IOException {
		// LOGGER.info("encodeEnd");
		super.encodeEnd(context, component);
	}

}

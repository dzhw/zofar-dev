package de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.singlechoice.options;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.faces.facelets.compiler.UIInstructions;

import de.his.zofar.presentation.surveyengine.ui.components.answer.options.UIAttachedOpenQuestion;
import de.his.zofar.presentation.surveyengine.ui.components.matrix.singlechoice.UISingleChoiceMatrixItem;
import de.his.zofar.presentation.surveyengine.ui.components.tablebase.AbstractTableItem;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.ZofarMatrixItemRenderer;
import de.his.zofar.presentation.surveyengine.ui.util.JsfUtility;

/**
 * @author meisner
 * 
 */
@FacesRenderer(componentFamily = AbstractTableItem.COMPONENT_FAMILY, rendererType = ZofarSingleChoiceMatrixItemRenderer.RENDERER_TYPE)
public class ZofarSingleChoiceMatrixItemRenderer extends
		ZofarMatrixItemRenderer {

	public static final String RENDERER_TYPE = "org.zofar.SingleChoiceMatrixItem";

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ZofarSingleChoiceMatrixItemRenderer.class);

	public ZofarSingleChoiceMatrixItemRenderer() {
		super();
	}

	@Override
	public synchronized void encodeBegin(FacesContext context, UIComponent component)
			throws IOException {
		if (!component.isRendered()) {
			return;
		}
//		LOGGER.debug("encodeBegin");
		final ResponseWriter writer = context.getResponseWriter();

		// rendering header facet
		final UIComponent header = component.getFacet(HEADER_FACET);
		if (header != null && this.isHasQuestionColumn(context, component)) {
			writer.startElement(TABLE_CELL, component);
			writer.writeAttribute("class",
					"zo-matrix-item-header zo-sc-matrix-item-header", null);
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
			if((UISingleChoiceMatrixItem.class).isAssignableFrom(component.getClass())){
				for (final UIComponent child : component.getChildren()) {
					if((UIAttachedOpenQuestion.class).isAssignableFrom(child.getClass())){
						child.encodeAll(context);
					}
				}
			}
			writer.endElement(TABLE_CELL);
		}
	}

	@Override
	public synchronized void encodeEnd(FacesContext context, UIComponent component)
			throws IOException {
//		LOGGER.debug("encodeEnd");
		super.encodeEnd(context, component);
	}

}

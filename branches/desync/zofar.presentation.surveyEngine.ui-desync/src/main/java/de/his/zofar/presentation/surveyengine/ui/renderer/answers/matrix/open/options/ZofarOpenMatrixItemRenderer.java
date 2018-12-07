/*
 * 
 */
package de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.open.options;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.faces.facelets.compiler.UIInstructions;

import de.his.zofar.presentation.surveyengine.ui.components.answer.options.UIAttachedOpenQuestion;
import de.his.zofar.presentation.surveyengine.ui.components.matrix.open.UIOpenMatrixItem;
import de.his.zofar.presentation.surveyengine.ui.components.tablebase.AbstractTableItem;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.ZofarMatrixItemRenderer;
import de.his.zofar.presentation.surveyengine.ui.util.JsfUtility;

/**
 * @author meisner
 * 
 */
@FacesRenderer(componentFamily = AbstractTableItem.COMPONENT_FAMILY, rendererType = ZofarOpenMatrixItemRenderer.RENDERER_TYPE)
public class ZofarOpenMatrixItemRenderer extends ZofarMatrixItemRenderer {

	public static final String RENDERER_TYPE = "org.zofar.OpenMatrixItem";

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ZofarOpenMatrixItemRenderer.class);

	public ZofarOpenMatrixItemRenderer() {
		super();
	}

	@Override
	public void encodeBegin(final FacesContext context, final UIComponent component)
			throws IOException {
		final ResponseWriter writer = context.getResponseWriter();

		// rendering header facet
		final UIComponent header = component.getFacet(ZofarMatrixItemRenderer.HEADER_FACET);
		// LOGGER.info("header {} ({})",header,this.isHasQuestionColumn(context,component));
		if (header != null && this.isHasQuestionColumn(context, component)) {
			writer.startElement(ZofarMatrixItemRenderer.TABLE_CELL, component);
			writer.writeAttribute("class",
					"zo-matrix-item-header zo-open-matrix-item-header", null);
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
			if((UIOpenMatrixItem.class).isAssignableFrom(component.getClass())){
				for (final UIComponent child : component.getChildren()) {
					if((UIAttachedOpenQuestion.class).isAssignableFrom(child.getClass())){
						child.encodeAll(context);
					}
				}
			}
			writer.endElement(ZofarMatrixItemRenderer.TABLE_CELL);
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
	public void encodeChildren(final FacesContext context, final UIComponent component)
			throws IOException {
		final ResponseWriter writer = context.getResponseWriter();

		for (final UIComponent child : component.getChildren()) {
			if((UIAttachedOpenQuestion.class).isAssignableFrom(child.getClass())){
				continue;
			}
			writer.startElement(ZofarMatrixItemRenderer.TABLE_CELL, component);
			child.encodeAll(context);
			writer.endElement(ZofarMatrixItemRenderer.TABLE_CELL);
		}
	}

	@Override
	public void encodeEnd(final FacesContext context, final UIComponent component)
			throws IOException {
		// LOGGER.info("encodeEnd");
		super.encodeEnd(context, component);
	}

}

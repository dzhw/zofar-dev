/*
 * 
 */
package de.his.zofar.presentation.surveyengine.ui.renderer.mixed;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.faces.facelets.compiler.UIInstructions;

import de.his.zofar.presentation.surveyengine.ui.components.composite.mixed.UIMixedMatrix;
import de.his.zofar.presentation.surveyengine.ui.components.composite.mixed.UIMixedMatrixItem;
import de.his.zofar.presentation.surveyengine.ui.components.tablebase.AbstractTableResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.ZofarMatrixItemRenderer;
import de.his.zofar.presentation.surveyengine.ui.util.JsfUtility;

/**
 * @author meisner
 * 
 */
@FacesRenderer(componentFamily = UIMixedMatrixItem.COMPONENT_FAMILY, rendererType = ZofarMixedMatrixItemRenderer.RENDERER_TYPE)
public class ZofarMixedMatrixItemRenderer extends ZofarMatrixItemRenderer {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ZofarMixedMatrixItemRenderer.class);

	public static final String RENDERER_TYPE = "rg.zofar.MixedMatrixItem";

	public ZofarMixedMatrixItemRenderer() {
		super();
	}

	@Override
	public void encodeBegin(final FacesContext context,
			final UIComponent component) throws IOException {
		final ResponseWriter writer = context.getResponseWriter();
		writer.startElement("tr", component);
		// rendering header facet
		final UIComponent header = component.getFacet(ZofarMatrixItemRenderer.HEADER_FACET);
		if (header != null && this.isHasQuestionColumn(context, component)) {
			writer.startElement(ZofarMatrixItemRenderer.TABLE_CELL, component);
			writer.writeAttribute("class",
					"zo-matrix-item-header zo-mixed-matrix-item-header", null);
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
			writer.endElement(ZofarMatrixItemRenderer.TABLE_CELL);
		}
	}

	@Override
	protected Boolean isHasQuestionColumn(
			final FacesContext context, final UIComponent component) {
		Boolean hasQuestionColumn = true;
		UIComponent parent = component;
		while (!((AbstractTableResponseDomain.class).isAssignableFrom(parent
				.getClass())))
			parent = parent.getParent();

		if (parent.getAttributes().get(ZofarMatrixItemRenderer.QUESTION_COLUMN) != null) {
			hasQuestionColumn = Boolean.valueOf(parent.getAttributes().get(
					ZofarMatrixItemRenderer.QUESTION_COLUMN)
					+ "");
		}

		if (hasQuestionColumn) {
			Boolean tmpFlag = false;

			if ((UIMixedMatrixItem.class)
					.isAssignableFrom(component.getClass())) {
				final UIMixedMatrixItem tmp = (UIMixedMatrixItem) component;
				final UIComponent header = tmp
						.getFacet(de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.ZofarMatrixItemRenderer.HEADER_FACET);
				if ((header != null) && (header.getChildren() != null)
						&& (!header.getChildren().isEmpty()))
					tmpFlag = true;
			}

			if (!tmpFlag)
				hasQuestionColumn = false;
		}
		return hasQuestionColumn;
	}

	// /*
	// * (non-Javadoc)
	// *
	// * @see
	// *
	// javax.faces.component.UIComponentBase#encodeChildren(javax.faces.context
	// * .FacesContext)
	// */
	@Override
	public void encodeChildren(final FacesContext context,
			final UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}
		int counter=1;
		int counteMMX=100;
		int i=0;
		final ResponseWriter writer = context.getResponseWriter();
		for (final UIComponent child : component.getChildren()) {
			
			if (UIMixedMatrix.class.isAssignableFrom(child.getClass())){
				i=(counteMMX++);
			}
			else{
				i=(counter++);
			}
			writer.startElement(ZofarMatrixItemRenderer.TABLE_CELL, component);
			writer.writeAttribute("class",
					"zo-matrix-item-header zo-mixed-matrix-item-cell zo-mixed-matrix-item-cell-"+(i)+"", null);
			child.encodeAll(context);
			writer.endElement(ZofarMatrixItemRenderer.TABLE_CELL);
			i++;
		}
	}

	@Override
	public void encodeEnd(final FacesContext context,
			final UIComponent component) throws IOException {
		final ResponseWriter writer = context.getResponseWriter();
		writer.endElement("tr");
	}

}

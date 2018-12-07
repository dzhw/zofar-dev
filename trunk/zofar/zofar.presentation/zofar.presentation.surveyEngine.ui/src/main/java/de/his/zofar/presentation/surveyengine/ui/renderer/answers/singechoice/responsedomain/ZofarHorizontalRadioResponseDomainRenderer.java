package de.his.zofar.presentation.surveyengine.ui.renderer.answers.singechoice.responsedomain;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.answer.responseDomain.RadioButtonSingleChoiceResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.components.common.UISort;
import de.his.zofar.presentation.surveyengine.ui.components.composite.mixed.UIMixedMatrixItem;
import de.his.zofar.presentation.surveyengine.ui.components.container.Section;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.ZofarResponseDomainRenderer;

/**
 * @author meisner
 * 
 */
@FacesRenderer(componentFamily = RadioButtonSingleChoiceResponseDomain.COMPONENT_FAMILY, rendererType = ZofarHorizontalRadioResponseDomainRenderer.RENDERER_TYPE)
public class ZofarHorizontalRadioResponseDomainRenderer extends
		ZofarSingleChoiceResponseDomainRenderer {

	public static final String RENDERER_TYPE = "org.zofar.HorizontalRadioResponseDomain";
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ZofarHorizontalRadioResponseDomainRenderer.class);

	// private String[] rowClasses;
	//
	// private boolean hasRowClasses;
	//
	private int row;
	private int missingCount;
	//
	// private String labelPosition;

//	private static final Logger LOGGER = LoggerFactory
//			.getLogger(ZofarHorizontalRadioResponseDomainRenderer.class);

	public ZofarHorizontalRadioResponseDomainRenderer() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.faces.render.Renderer#encodeBegin(javax.faces.context.FacesContext,
	 * javax.faces.component.UIComponent)
	 */
	@Override
	public synchronized void encodeBegin(final FacesContext context,
			final UIComponent component) throws IOException {

		if (!component.isRendered()) {
			return;
		}
//		LOGGER.info("encodeBegin {} ({})",component.getClientId(),component.getRendererType());

//		final String[] rowClasses = itemClassesToArray((String) component
//				.getAttributes().get("rowClasses"));

//		final boolean hasRowClasses = rowClasses.length > 0;

		row = 0;
		missingCount=0;

//		labelPosition = (String) component.getAttributes().get("labelPosition");

		final ResponseWriter writer = context.getResponseWriter();

		String additonalClasses = null;
		final boolean separateMissings = (Boolean) component.getAttributes()
				.get("missingSeparated");
		if (separateMissings)
			additonalClasses = "zo-missingSeparated zo-missingSeparated-horizontal";
		startTable(context.getResponseWriter(), component, additonalClasses);

		// startTable(writer, component);
		writer.startElement("tr", component);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.faces.render.Renderer#encodeChildren(javax.faces.context.FacesContext
	 * , javax.faces.component.UIComponent)
	 */
	@Override
	public synchronized void encodeChildren(final FacesContext context,
			final UIComponent component) throws IOException {

		if (!component.isRendered()) {
			return;
		}

		for (final UIComponent child : component.getChildren()) {
			if (!child.isRendered()) {
				continue;
			}

			// LOGGER.info("child {} {} ", child.getClass(),
			// child.getRendererType());
			encodeChildrenHelper(context, component, child);

		}
	}

	// /*
	// * (non-Javadoc)
	// *
	// * @see
	// *
	// javax.faces.render.Renderer#encodeChildren(javax.faces.context.FacesContext
	// * , javax.faces.component.UIComponent)
	// */
	// @Override
	// public void encodeChildren(final FacesContext context,
	// final UIComponent component) throws IOException {
	//
	// if (!component.isRendered()) {
	// return;
	// }
	//
	// final ResponseWriter writer = context.getResponseWriter();
	//
	// final String[] rowClasses = UIResponseDomain
	// .rowClassesToArray((String) component.getAttributes().get(
	// "rowClasses"));
	//
	// final boolean hasRowClasses = rowClasses.length > 0;
	//
	// int row = 0;
	// for (final UIComponent child : component.getChildren()) {
	// if (!child.isRendered()) {
	// continue;
	// }
	// writer.startElement("td", component);
	// if (hasRowClasses) {
	// writer.writeAttribute("class", rowClasses[row++
	// % rowClasses.length], null);
	// }
	//
	// child.encodeAll(context);
	//
	// writer.endElement("td");
	// }
	// }

	public synchronized void encodeChildrenHelper(final FacesContext context,
			final UIComponent component, final UIComponent child)
			throws IOException {

		if (!child.isRendered()) {
			return;
		}

		final ResponseWriter writer = context.getResponseWriter();
		
		if ((Section.class).isAssignableFrom(child.getClass())) {
			writer.startElement("td", component);
			final String labelPosition = (String) component.getAttributes().get("labelPosition");
			if (labelPosition != null) {
				if (labelPosition
						.equals(ZofarResponseDomainRenderer.LABEL_POSITION_BOTTOM)) {
					writer.writeAttribute("valign", "top", null);
				} else {
					writer.writeAttribute("valign", "bottom", null);
				}
			}
			
			
			writer.startElement("table", component);
			writer.writeAttribute("cellspacing", 0, null);
			writer.writeAttribute("cellpadding", 0, null);
			writer.writeAttribute("border", 0, null);
			
			writer.startElement("tr", component);
			writer.startElement("td", component);
			writer.writeAttribute("colspan", child.getChildCount(), null);
			writer.writeAttribute("align", "center", null);
			child.encodeAll(context);
			writer.endElement("td");
			writer.endElement("tr");
			writer.startElement("tr", component);
			for (final UIComponent sectionChild : child.getChildren()) {
				encodeChildrenHelper(context, component, sectionChild);
			}
			writer.endElement("tr");
			writer.endElement("table");
			writer.endElement("td");
		} else if ((UISort.class).isAssignableFrom(child.getClass())) {
			for (final UIComponent sortChild : ((UISort) child).sortChildren()) {
				encodeChildrenHelper(context, component, sortChild);
			}
		} else {
			final String labelPosition = (String) component.getAttributes().get("labelPosition");
			final String[] rowClasses = itemClassesToArray((String) component
					.getAttributes().get("itemClasses"));

			final boolean hasRowClasses = rowClasses.length > 0;
			
			
			Boolean isMissing = (Boolean) child.getAttributes().get("missing");
			if (isMissing == null)
				isMissing = false;
			writer.startElement("td", component);
			if (labelPosition != null) {
				if (labelPosition
						.equals(ZofarResponseDomainRenderer.LABEL_POSITION_BOTTOM)) {
					writer.writeAttribute("valign", "top", null);
				} else {
					writer.writeAttribute("valign", "bottom", null);
				}
			}
			if (isMissing) {
				writer.writeAttribute("class", "zo-ao-missing zo-ao-missing-horizontal-"+missingCount, null);
				missingCount++;
			}
			
			writer.startElement("table", component);
//			writer.writeAttribute("border", 1, null);
//			
			if (component.getChildCount()>0){
				if ((UIMixedMatrixItem.class).isAssignableFrom(component.getParent().getParent().getClass())) {
					writer.writeAttribute("width", ""+((700/component.getParent().getParent().getChildCount())/component.getChildCount())+"px", null);
				}
				else{
					writer.writeAttribute("width", ""+(700/component.getChildCount())+"px", null);
				}
			}
			// if (hasRowClasses) {
			// writer.writeAttribute("class", rowClasses[row++
			// % rowClasses.length], null);
			// }
			String classes = "";
			if (hasRowClasses) {
				// writer.writeAttribute("class", rowClasses[row++
				// % rowClasses.length], null);
				classes += rowClasses[row++ % rowClasses.length] + " ";
			}
			if (!classes.equals(""))
				writer.writeAttribute("class", classes.trim(), null);
			child.encodeAll(context);
			writer.endElement("table");
			writer.endElement("td");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.faces.render.Renderer#encodeEnd(javax.faces.context.FacesContext,
	 * javax.faces.component.UIComponent)
	 */
	@Override
	public synchronized void encodeEnd(final FacesContext context,
			final UIComponent component) throws IOException {

		if (!component.isRendered()) {
			return;
		}

		final ResponseWriter writer = context.getResponseWriter();

		writer.endElement("tr");

		endTable(writer);

	}
}

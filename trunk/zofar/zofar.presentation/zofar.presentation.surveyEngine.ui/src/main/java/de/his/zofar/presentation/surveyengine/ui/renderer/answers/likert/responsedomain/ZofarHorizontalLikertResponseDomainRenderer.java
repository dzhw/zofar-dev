package de.his.zofar.presentation.surveyengine.ui.renderer.answers.likert.responsedomain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.answer.responseDomain.LikertResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.components.common.UISort;
import de.his.zofar.presentation.surveyengine.ui.components.container.Section;

/**
 * @author meisner
 * 
 */
@FacesRenderer(componentFamily = LikertResponseDomain.COMPONENT_FAMILY, rendererType = ZofarHorizontalLikertResponseDomainRenderer.RENDERER_TYPE)
public class ZofarHorizontalLikertResponseDomainRenderer extends
		ZofarLikertResponseDomainRenderer {

	public static final String RENDERER_TYPE = "org.zofar.HorizontalLikertResponseDomain";

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ZofarHorizontalLikertResponseDomainRenderer.class);
	private int row;

	@Override
	public boolean getRendersChildren() {
		return true;
	}

	@Override
	public synchronized void encodeBegin(FacesContext context, UIComponent component)
			throws IOException {
		if (!component.isRendered()) {
			return;
		}

		row = 0;

		String additonalClasses = null;
		final boolean separateMissings = (Boolean) component.getAttributes()
				.get("missingSeparated");
		if (separateMissings)
			additonalClasses = "zo-likert zo-likert-horizontal zo-missingSeparated zo-missingSeparated-horizontal";
		startTable(context.getResponseWriter(), component, additonalClasses);
		context.getResponseWriter().startElement("tr", component);
		context.getResponseWriter().writeAttribute("style", "valign='bottom'",null);
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

		// retrieve labels
		List<UIComponent> labels = new ArrayList<UIComponent>();

		for (final UIComponent child : component.getChildren()) {
			labels.addAll(retrieveLabels(context, component, child));
		}
		
		final boolean showValues = (Boolean) component.getAttributes().get(
				"showValues");

		final ResponseWriter writer = context.getResponseWriter();

		if (!labels.isEmpty()) {

			writer.startElement("td", component);
			writer.writeAttribute("class", "zo-likert-label-horizontal-left",null);
			writer.writeAttribute("valign", "bottom",null);
//			writer.writeAttribute("style", "padding-bottom:5px",null);
			writer.startElement("table", component);
			writer.startElement("tr", component);
			writer.startElement("td", component);
			writer.writeAttribute("class", "zo-likert-horizontal-empty",null);
			writer.endElement("td");
			writer.endElement("tr");
			writer.startElement("tr", component);
			writer.startElement("td", component);
			labels.get(0).encodeAll(context);
			writer.endElement("td");
			writer.endElement("tr");
			writer.startElement("tr", component);
			writer.startElement("td", component);
			writer.writeAttribute("class", "zo-likert-horizontal-empty",null);
			if(showValues)writer.write("&nbsp;");
			writer.endElement("td");
			writer.endElement("tr");

			writer.endElement("table");
			writer.endElement("td");

			labels.remove(0);

		}

		final List<UIComponent> missingChilds = new ArrayList<UIComponent>();

		for (final UIComponent child : component.getChildren()) {
			if (!child.isRendered()) {
				continue;
			}
			Boolean isMissing = (Boolean) child.getAttributes().get("missing");
			if (isMissing == null)
				isMissing = false;

			if (isMissing) {
				missingChilds.add(child);
			} else {
				// LOGGER.info("child {} {} ",child.getClass(),child.getRendererType());
				encodeChildrenHelper(context, component, child);
			}
		}

		if (!labels.isEmpty()) {

			writer.startElement("td", component);
			writer.writeAttribute("class", "zo-likert-label-horizontal-right",null);
			writer.writeAttribute("valign", "bottom",null);
//			writer.writeAttribute("style", "padding-bottom:5px",null);
			writer.startElement("table", component);
			writer.startElement("tr", component);
			writer.startElement("td", component);
			writer.writeAttribute("class", "zo-likert-horizontal-empty",null);
			writer.endElement("td");
			writer.endElement("tr");
			writer.startElement("tr", component);
			writer.startElement("td", component);
			labels.get(0).encodeAll(context);
			writer.endElement("td");
			writer.endElement("tr");
			writer.startElement("tr", component);
			writer.startElement("td", component);
			writer.writeAttribute("class", "zo-likert-horizontal-empty",null);
			if(showValues)writer.write("&nbsp;");
			writer.endElement("td");
			writer.endElement("tr");

			writer.endElement("table");
			writer.endElement("td");

			labels.remove(0);
		}

		for (final UIComponent child : missingChilds) {
			if (!child.isRendered()) {
				continue;
			}
			// LOGGER.info("child {} {} ",child.getClass(),child.getRendererType());
			encodeChildrenHelper(context, component, child);
		}
	}

	public synchronized void encodeChildrenHelper(final FacesContext context,
			final UIComponent component, final UIComponent child)
			throws IOException {

		if (!child.isRendered()) {
			return;
		}
		final String[] rowClasses = itemClassesToArray((String) component
				.getAttributes().get("itemClasses"));
		final boolean hasRowClasses = rowClasses.length > 0;

		final ResponseWriter writer = context.getResponseWriter();

		if ((Section.class).isAssignableFrom(child.getClass())) {

			writer.startElement("td", component);
			// writer.writeAttribute("colspan", "4", null);
			writer.writeAttribute("class",
					"zo-unit zo-sc-unit zo-vertical-sc-unit", null);
			writer.writeAttribute("style",
					"padding-top:10px;padding-bottom:10px;font-style:italic;",
					null);
			child.encodeAll(context);
			writer.endElement("td");

			for (final UIComponent sectionChild : ((Section) child)
					.getChildren()) {
				if (!sectionChild.isRendered()) {
					continue;
				}
				encodeChildrenHelper(context, component, sectionChild);
			}
		} else if ((UISort.class).isAssignableFrom(child.getClass())) {
			for (final UIComponent sortChild : ((UISort) child).sortChildren()) {
				if (!sortChild.isRendered()) {
					continue;
				}
				encodeChildrenHelper(context, component, sortChild);
			}
		} else {
			Boolean isMissing = (Boolean) child.getAttributes().get("missing");
			if (isMissing == null)
				isMissing = false;

			writer.startElement("td", component);
			final StringBuffer classes = new StringBuffer();
			classes.append("zo-likert-item-horizontal");
			if (hasRowClasses) {
				// writer.writeAttribute("class", rowClasses[row++
				// % rowClasses.length], null);
				classes.append(rowClasses[row++ % rowClasses.length] + " ");
			}
			if (isMissing) {
				classes.append("zo-ao-missing zo-ao-missing-likert-horizontal ");
			}
			if (!classes.toString().trim().equals(""))writer.writeAttribute("class", classes.toString().trim(), null);
			writer.writeAttribute("valign", "bottom",null);

			writer.startElement("table", component);
			writer.startElement("tr", component);
			writer.startElement("td", component);
			if (isMissing){
				writer.writeAttribute("class", "zo-ao-missing-likert-horizontal-label",null);
				this.encodeLabel(context, child);
			}					
			else {
				writer.writeAttribute("class", "zo-likert-horizontal-empty",null);
			}
			writer.endElement("td");
			writer.endElement("tr");
			writer.startElement("tr", component);
			writer.startElement("td", component);
			writer.writeAttribute("class", "zo-likert-item-horizontal-radio", null);
			this.encodeInput(context, child);
			writer.endElement("td");
			writer.endElement("tr");
			writer.startElement("tr", component);
			writer.startElement("td", component);
			final boolean showValues = (Boolean) component.getAttributes().get(
					"showValues");
			if (showValues){
				writer.writeAttribute("class", "zo-likert-item-horizontal-value",null);
				this.encodeValueLabel(context, child);
			}				
			else{
				writer.writeAttribute("class", "zo-likert-horizontal-value-empty",null);
			}
//			if (showValues && isMissing) {
//				writer.startElement("tr", component);
//				writer.startElement("td", component);
//				this.encodeValueLabel(context, child);
//				writer.endElement("td");
//				writer.endElement("tr");
//				writer.startElement("tr", component);
//				writer.startElement("td", component);
//			}

			writer.endElement("td");
			writer.endElement("tr");

			writer.endElement("table");
			writer.endElement("td");

		}
	}

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

package de.his.zofar.presentation.surveyengine.ui.renderer.answers.likert.responsedomain;

import java.awt.Point;
import java.awt.geom.Point2D;
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
@FacesRenderer(componentFamily = LikertResponseDomain.COMPONENT_FAMILY, rendererType = ZofarVerticalLikertResponseDomainRenderer.RENDERER_TYPE)
public class ZofarVerticalLikertResponseDomainRenderer extends
		ZofarLikertResponseDomainRenderer {

	public static final String RENDERER_TYPE = "org.zofar.VerticalLikertResponseDomain";
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ZofarVerticalLikertResponseDomainRenderer.class);
	
//	private int row;
//	private int rowMissing;
	
	

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

//		row = 0;
//		rowMissing=0;

		String additonalClasses = null;
		final boolean separateMissings = (Boolean) component.getAttributes().get("missingSeparated");
		if(separateMissings)additonalClasses = "zo-likert zo-likert-vertical zo-missingSeparated zo-missingSeparated-vertical";
        startTable(context.getResponseWriter(), component,additonalClasses);
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
		Point2D rowPair = new Point(1,0);
		//retrieve labels
		List<UIComponent> labels = new ArrayList<UIComponent>();
		
		for (final UIComponent child : component.getChildren()) {
			labels.addAll(retrieveLabels(context,component,child));
		}
		
		final ResponseWriter writer = context.getResponseWriter();

		if(!labels.isEmpty()){
			writer.startElement("tr", component);
			writer.writeAttribute("class", "zo-likert-top-label", null);
			writer.startElement("td", component);
			writer.writeAttribute("colspan", "2", null);
			labels.get(0).encodeAll(context);
			writer.endElement("td");
			writer.endElement("tr");
			labels.remove(0);
			
		}
		final List<UIComponent> missingChilds = new ArrayList<UIComponent>();

		for (final UIComponent child : component.getChildren()) {
			if (!child.isRendered()) {
				continue;
			}
	        Boolean isMissing = (Boolean) child.getAttributes().get("missing");
			if (isMissing == null)isMissing = false;
			
			if(isMissing){
				missingChilds.add(child);
			}
			else{
//				LOGGER.info("child {} {} ",child.getClass(),child.getRendererType());
				rowPair = encodeChildrenHelper(context,component,child,rowPair);
			}
		}
		
		if(!labels.isEmpty()){
			writer.startElement("tr", component);
			writer.writeAttribute("class", "zo-likert-bottom-label", null);
			writer.startElement("td", component);
			writer.writeAttribute("colspan", "2", null);
			labels.get(0).encodeAll(context);
			writer.endElement("td");
			writer.endElement("tr");
			labels.remove(0);
		}
		
		for (final UIComponent child : missingChilds) {
			if (!child.isRendered()) {
				continue;
			}
//				LOGGER.info("child {} {} ",child.getClass(),child.getRendererType());
			rowPair = encodeChildrenHelper(context,component,child,rowPair);
		}
	}

	public synchronized Point2D encodeChildrenHelper(final FacesContext context,final UIComponent component,
			final UIComponent child, Point2D rowPair) throws IOException {

		if (!child.isRendered()) {
			return rowPair;
		}
		final String[] rowClasses = itemClassesToArray((String) component.getAttributes().get(
				"itemClasses"));
		
		final boolean hasRowClasses = rowClasses.length > 0;

		final ResponseWriter writer = context.getResponseWriter();

		if((Section.class).isAssignableFrom(child.getClass())){
			writer.startElement("tr", component);
			writer.startElement("td", component);
			writer.writeAttribute("colspan", "2", null);
			writer.writeAttribute("class","zo-unit zo-likert-unit zo-vertical-likert-unit",null);
			writer.writeAttribute("style","padding-top:10px;padding-bottom:10px;font-style:italic;",null);
			child.encodeAll(context);
			writer.endElement("td");
			writer.endElement("tr");

			for (final UIComponent sectionChild : ((Section)child).getChildren()) {
				if (!sectionChild.isRendered()) {
					continue;
				}
				rowPair =encodeChildrenHelper(context,component,sectionChild,rowPair);
			}
		}
		else if((UISort.class).isAssignableFrom(child.getClass())){
			for (final UIComponent sortChild : ((UISort)child).sortChildren()) {
				if (!sortChild.isRendered()) {
					continue;
				}
				rowPair =encodeChildrenHelper(context,component,sortChild,rowPair);
			}
		}
		else{
	        Boolean isMissing = (Boolean) child.getAttributes().get("missing");
			if (isMissing == null)isMissing = false;

			writer.startElement("tr", component);
			
			String classes = "";
			if (hasRowClasses) {
				int index = (int)rowPair.getX();
				index++;
				rowPair.setLocation(index,rowPair.getY());
				classes += rowClasses[index % rowClasses.length]+" ";
			}
			if (isMissing){
				int index = (int)rowPair.getY();
				
				if (index==0){
					writer.endElement("tr");
					writer.endElement("table");
					writer.endElement("div");
					writer.startElement("div",component);
					writer.writeAttribute("class", "zo-template-content", null);
					
					writer.startElement("table", component);
					writer.writeAttribute("class", "zo-responsedomain zo-likert-vertical-missing", null);
					writer.writeAttribute("style", "width:30%;align:center", null);
					writer.startElement("tr", component);
				}
				index++;
				rowPair.setLocation(rowPair.getX(), index);
				classes += "zo-ao-missing zo-ao-likert-missing-vertical-"+(index);
			}
			if(!classes.equals(""))writer.writeAttribute("class", classes.trim(), null);
			
			writer.startElement("td", component);
			writer.writeAttribute("class", "zo-likert-ao-input-cell", null);
			this.encodeInput(context, child);
			final boolean showValues = (Boolean)component.getAttributes().get("showValues");
			writer.endElement("td");
			writer.startElement("td", component);
			writer.writeAttribute("class", "zo-likert-ao-label-cell", null);
			if(showValues && !isMissing)this.encodeValueLabel(context, child);
			if(isMissing)this.encodeLabel(context, child);
			writer.endElement("td");
			writer.endElement("tr");
		}
		return rowPair;
	}
	


	
	
	@Override
	public synchronized void encodeEnd(final FacesContext context,
			final UIComponent component) throws IOException {

		if (!component.isRendered()) {
			return;
		}

		final ResponseWriter writer = context.getResponseWriter();
		endTable(writer);

	}
	
	

}

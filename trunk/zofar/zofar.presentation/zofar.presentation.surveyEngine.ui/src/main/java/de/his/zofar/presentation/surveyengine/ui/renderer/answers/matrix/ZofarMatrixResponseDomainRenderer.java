package de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.answer.options.SingleOption;
import de.his.zofar.presentation.surveyengine.ui.components.common.UISort;
import de.his.zofar.presentation.surveyengine.ui.components.matrix.singlechoice.UISingleChoiceMatrixItem;
import de.his.zofar.presentation.surveyengine.ui.components.matrix.singlechoice.UISingleChoiceMatrixItemResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.components.tablebase.AbstractTableItem;
import de.his.zofar.presentation.surveyengine.ui.components.tablebase.AbstractTableResponseDomainUnit;
import de.his.zofar.presentation.surveyengine.ui.components.text.UIText;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.ZofarResponseDomainRenderer;

/**
 * @author meisner
 * 
 */
public abstract class ZofarMatrixResponseDomainRenderer extends
		ZofarResponseDomainRenderer {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ZofarMatrixResponseDomainRenderer.class);
	/**
    *
    */
	protected static final String ROW_CLASSES = "itemClasses";
	protected static final String SHOW_VALUES = "isShowValues";
	protected static final String DIFFERENTIAL = "isDifferential";
	protected static final String QUESTION_COLUMN = "questionColumn";
	protected static final String TABLE = "table";
	protected static final String COLSPAN = "colspan";
	protected static final String SCALE_HEADER = "scaleHeader";
	protected static final String TABLE_HEADER = "th";
	protected static final String TABLE_ROW = "tr";
	protected static final String TABLE_CELL = "td";
	protected static final String HEADER = "header";
	protected static final String MISSING_HEADER = "missingHeader";
	protected static final String MISSING_HEADER_CSS_CLASS = "zofar-missing";
	protected static final String DEFAULT_TABLE_CLASS_NAME = "zofar-table-responsedomain";

//	protected int row;

	// private static final Logger LOGGER = LoggerFactory
	// .getLogger(ZofarMatrixResponseDomainRenderer.class);

	public ZofarMatrixResponseDomainRenderer() {
		super();
	}

	@Override
	public boolean getRendersChildren() {
		return true;
	}

	public synchronized void encodeBegin(final FacesContext context,
			final UIComponent component, final String additionalClasses)
			throws IOException {
//		LOGGER.info("encodeBegin {} ({})",component,component.getRendererType());
		final ResponseWriter writer = context.getResponseWriter();
		final String clientId = component.getClientId(context);
		


		// start encoding single choice matrix items surrounding table
		writer.startElement(TABLE, component);
		writer.writeAttribute("id", clientId, null);
		writer.writeAttribute("width", "100%", null);
//		writer.writeAttribute("border", "1", null);
		writer.writeAttribute("class", additionalClasses, null);
		
		this.renderScaleHeader(context, component);
		this.renderHeader(context, component);
		

//		row = 0;
	}

	@Override
	public synchronized void encodeEnd(FacesContext context, UIComponent component)
			throws IOException {
//		LOGGER.info("encodeEnd {} ({})",component,component.getRendererType());
		final ResponseWriter writer = context.getResponseWriter();

		// end encoding table
		writer.endElement(TABLE);

	}

	protected synchronized Integer getNumberOfResponseOptions(final FacesContext context,
			final UIComponent component) {
		Integer value = (Integer) component.getAttributes().get(
				"noResponseOptions");
		if (value == null) {
			value = 0;
		}
		return value;
	}

	/**
	 * does the response domain have a question column.
	 * 
	 * @return
	 */
	protected synchronized Boolean isHasQuestionColumn(final FacesContext context,
			final UIComponent component) {
		Boolean hasQuestionColumn = false;
		if (component.getAttributes().get(QUESTION_COLUMN) != null) {
//			hasQuestionColumn = (Boolean) component.getAttributes().get(
//					QUESTION_COLUMN);
			hasQuestionColumn = Boolean.valueOf(component.getAttributes().get(
					QUESTION_COLUMN)+"");
		}
		return hasQuestionColumn;
	}

	/**
	 * does the response domain have a question column.
	 * 
	 * @return
	 */
	protected synchronized Boolean isShowValues(final FacesContext context,
			final UIComponent component) {
		Boolean showValues = false;
		if (component.getAttributes().get(SHOW_VALUES) != null) {
			showValues = (Boolean) component.getAttributes().get(SHOW_VALUES);
		}
		return showValues;
	}
	/**
	 * does the response domain have a question column.
	 * 
	 * @return
	 */
	protected synchronized Boolean isDifferential(final FacesContext context,
			final UIComponent component) {
		Boolean isDifferential = false;
		if (component.getAttributes().get(DIFFERENTIAL) != null) {
			isDifferential = (Boolean) component.getAttributes().get(DIFFERENTIAL);
		}
		return isDifferential;
	}

	/**
	 * determine row classes.
	 * 
	 * @return array with the row classes
	 */
	private synchronized String[] getRowClassesArray(final FacesContext context,
			final UIComponent component) {
		String[] rowClasses = null;
		final String rowClassesRaw = (String) component.getAttributes().get(
				ROW_CLASSES);
		rowClasses = itemClassesToArray(rowClassesRaw);

		return rowClasses;
	}

	protected synchronized Integer getNumberOfMissings(final FacesContext context,
			final UIComponent component) {
		final UIComponent missingHeader = component.getFacet(MISSING_HEADER);
		if (missingHeader == null) {
			return 0;
		}

		if (missingHeader.getChildren().isEmpty()) {
			return 1;
		} else {
			return missingHeader.getChildren().size();
		}
	}

	/**
	 * @return
	 */
	private synchronized String getHeaderMinWidth(final FacesContext context,
			final UIComponent component) {
		final int count = (this.getNumberOfResponseOptions(context, component) + this
				.getNumberOfMissings(context, component));
		int width = 0;
		String measure ="";
		if (count > 0){
			if (isDifferential(context, component.getParent())){
				width = 700 / count;
				measure="px;";
			}
			else{
				width = 60 / count;
				measure="%;";
			}

		}
		return "width: " + width + ""+measure+"";
	}
	/**
	 * @return
	 */

	protected void renderHeader(final FacesContext context,
			final UIComponent component) throws IOException {
		final ResponseWriter writer = context.getResponseWriter();
		// the header must not be null
		final UIComponent header = component.getFacet(HEADER);
		int headerCounter=1;
//		LOGGER.info("header : {}",header);
		if (header == null) {
			return;
		}

		// encoding column headers AKA <f:facet name="header/>
		writer.startElement(TABLE_ROW, component);

		if (this.isHasQuestionColumn(context, component)) {
			writer.startElement(TABLE_HEADER, component);
			writer.writeAttribute("class", "zofar-matrix-table-header zofar-matrix-table-header_0",null);
			writer.endElement(TABLE_HEADER);
		}

		if (UIText.class.isAssignableFrom(header.getClass())) {
			writer.startElement(TABLE_HEADER, component);
			writer.writeAttribute("class", "zofar-matrix-table-header", null);
			// if the facet has only one child the child is the facet and must
			// be rendered
			header.encodeAll(context);
			writer.endElement(TABLE_HEADER);
		} else {
			for (final UIComponent columnHeader : header.getChildren()) {
				writer.startElement(TABLE_HEADER, component);
				writer.writeAttribute("class", "zofar-matrix-table-header zofar-matrix-table-header"+(headerCounter++)+"",
						null);
				writer.writeAttribute("style",
						this.getHeaderMinWidth(context, component), null);
				columnHeader.encodeAll(context);
				writer.endElement(TABLE_HEADER);
			}
		}

		this.renderMissingHeader(context, component);

		writer.endElement(TABLE_ROW);
//		LOGGER.info("showValues {}",isShowValues(context, component));
		if (isShowValues(context, component)) {
			final List<String> values = new ArrayList<String>();
			final List<String> missingValues = new ArrayList<String>();

			UISingleChoiceMatrixItem firstItem = null;
			for (final UIComponent child : component.getChildren()) {
				if ((UISingleChoiceMatrixItem.class).isAssignableFrom(child
						.getClass())) {
					firstItem = (UISingleChoiceMatrixItem) child;
					break;
				}
			}
			if (firstItem != null) {
				UISingleChoiceMatrixItemResponseDomain firstRDC = null;
				for (final UIComponent child : firstItem.getChildren()) {
					if ((UISingleChoiceMatrixItemResponseDomain.class)
							.isAssignableFrom(child.getClass())) {
						firstRDC = (UISingleChoiceMatrixItemResponseDomain) child;
						break;
					}
				}
				if (firstRDC != null) {
					for (final UIComponent child : firstRDC.getChildren()) {
						if ((SingleOption.class).isAssignableFrom(child
								.getClass())) {
							final SingleOption tmp = (SingleOption) child;
							if (tmp.isMissing()) {
								missingValues.add(tmp.getValue());
							} else {
								values.add(tmp.getValue());
							}

						}
					}
				}
			}

			if (!values.isEmpty()) {
				final Iterator<String> valueIt = values.iterator();

				writer.startElement(TABLE_ROW, component);
				writer.startElement("td", component);
				writer.endElement("td");
				while (valueIt.hasNext()) {
					writer.startElement("td", component);
					writer.writeAttribute("class",
							"zo-matrix-values zo-sc-matrix-values", null);
					writer.write(valueIt.next());
					writer.endElement("td");
				}
				if (!missingValues.isEmpty()) {
					int counter = 0;
					final Iterator<String> valueMissingIt = missingValues
							.iterator();
					while (valueMissingIt.hasNext()) {
						writer.startElement("td", component);
						writer.writeAttribute("class",
								"zo-matrix-values zo-sc-matrix-values-missing-"
										+ (counter++), null);
						writer.write(valueMissingIt.next());
						writer.endElement("td");
					}
				}

				writer.endElement(TABLE_ROW);
			}
		}

	}

	protected synchronized void renderScaleHeader(final FacesContext context,
			final UIComponent component) throws IOException {
		final ResponseWriter writer = context.getResponseWriter();
		// encoding scale header AKA <f:facet name="scaleHeader" />
		final UIComponent scaleHeader = component.getFacet(SCALE_HEADER);
//		LOGGER.info("scaleHeader : {}",scaleHeader);
		if (scaleHeader != null && scaleHeader.isRendered()) {
			writer.startElement(TABLE_ROW, component);
			if (this.isHasQuestionColumn(context, component)) {
				writer.startElement(TABLE_HEADER, component);
				writer.endElement(TABLE_HEADER);
			}
			// determine how many columns to span.
			final int colSpan = this.getNumberOfResponseOptions(context,
					component);
			writer.startElement(TABLE_HEADER, component);
			writer.writeAttribute(COLSPAN, colSpan, null);
			writer.writeAttribute("class", SCALE_HEADER, null);
			scaleHeader.encodeAll(context);
			writer.endElement(TABLE_HEADER);
			writer.endElement(TABLE_ROW);
		}
	}

	protected synchronized void renderMissingHeader(final FacesContext context,
			final UIComponent component) throws IOException {
		final ResponseWriter writer = context.getResponseWriter();
		int counter = 0;

		final UIComponent missingHeader = component.getFacet(MISSING_HEADER);
//		LOGGER.info("missingHeader : {}",missingHeader);
		if (missingHeader == null) {
			return;
		}

		if (UIText.class.isAssignableFrom(missingHeader.getClass())) {
			writer.startElement(TABLE_HEADER, component);
			writer.writeAttribute("class", MISSING_HEADER_CSS_CLASS + "-"
					+ counter, null);
			// if the facet has only one child the child is the facet and must
			// be rendered
			if(!isDifferential(context, component)){
				writer.writeAttribute("style",this.getHeaderMinWidth(context, component), null);
			}
			missingHeader.encodeAll(context);
			writer.endElement(TABLE_HEADER);
		} else {
			for (final UIComponent child : missingHeader.getChildren()) {
				writer.startElement(TABLE_HEADER, component);
				writer.writeAttribute("class", MISSING_HEADER_CSS_CLASS + "-"
						+ counter, null);
				counter++;
				writer.writeAttribute("style",
						this.getHeaderMinWidth(context, component), null);
				child.encodeAll(context);
				writer.endElement(TABLE_HEADER);
			}
		}
	}

	private synchronized void renderUnitHeader(final FacesContext context,
			final UIComponent component) throws IOException {
		final ResponseWriter writer = context.getResponseWriter();

		final UIComponent header = component.getFacet("header");
		if (header == null) {
			return;
		}

		writer.startElement(TABLE_HEADER, component);
		writer.writeAttribute("class", "zo-matrix-rd-unitheader", null);
		if (component.getChildCount() > 0)
			writer.writeAttribute("style",
					this.getHeaderMinWidth(context, component), null);
		header.encodeAll(context);
		writer.endElement(TABLE_HEADER);
	}

	protected synchronized void renderTableItem(final FacesContext context,
			final UIComponent component, final AbstractTableItem item,
			final int rowIndex) throws IOException {
//		LOGGER.debug("item {} ({})", item, item.getRendererType());
		final ResponseWriter writer = context.getResponseWriter();
		final String[] rowClasses = this.getRowClassesArray(context, component);
		writer.startElement(TABLE_ROW, component);
		if (rowClasses.length > 0) {
			writer.writeAttribute("class", rowClasses[rowIndex
					% rowClasses.length]+" rowindex"+rowIndex, null);
		} else {
			writer.writeAttribute("class","rowindex"+rowIndex, null);
		}
		
		item.encodeAll(context);
		writer.endElement(TABLE_ROW);
	}

	@Override
	public synchronized void encodeChildren(final FacesContext context,
			final UIComponent component) throws IOException {
//		LOGGER.info("encodeChildren {} ({})",component,component.getRendererType());
		this.encodeChildrenHelper(context, component, component.getChildren());
	}

	private synchronized void encodeChildrenHelper(final FacesContext context,
			final UIComponent component, final List<UIComponent> children)
			throws IOException {
		int row = 0;
		for (final UIComponent child : children) {
			if (!child.isRendered()) {
				continue;
			}
			if (AbstractTableItem.class.isAssignableFrom(child.getClass())) {
				final AbstractTableItem item = (AbstractTableItem) child;
				if (item.isRendered()) {
					this.renderTableItem(context, component, item, row++);
				}
			} else if (AbstractTableResponseDomainUnit.class
					.isAssignableFrom(child.getClass())) {
				renderUnitHeader(context, child);
				this.encodeChildrenHelper(context, component,
						child.getChildren());

			} else if (UISort.class.isAssignableFrom(child.getClass())) {
				// sort children first then render
				this.encodeChildrenHelper(context, component,
						((UISort) child).sortChildren());
			} else {
				throw new IllegalStateException(
						"no other children than items, units or sort container are allowed.");
			}
		}
	}
}

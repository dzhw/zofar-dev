package de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.singlechoice.responsedomain;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.answer.options.SingleOption;
import de.his.zofar.presentation.surveyengine.ui.util.JsfUtility;

public abstract class ZofarRadioMatrixItemResponseDomainRenderer extends
		Renderer {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ZofarRadioMatrixItemResponseDomainRenderer.class);
	
	public ZofarRadioMatrixItemResponseDomainRenderer() {
		super();
	}
	
	/**
	 * this class renders its children.
	 * 
	 * @see javax.faces.component.UIComponentBase#getRendersChildren()
	 */
	@Override
	public boolean getRendersChildren() {
		return true;
	}
	
	@Override
	public synchronized void decode(FacesContext context, UIComponent component) {
		final Map<String, String> paramMap = context.getExternalContext()
				.getRequestParameterMap();
		final String clientId = component.getClientId(context);
//		LOGGER.info("decode {} ({})",clientId,paramMap.get(clientId));
		if (paramMap.containsKey(clientId)) {
			// request parameter found, set submitted value
			((UIInput)component).setValue(paramMap.get(clientId));
		} else {
			// see reason for this action at decodeUISelectMany
			((UIInput)component).setValue("");
		}
	}
	public abstract void encodeChildren(final FacesContext context, final UIComponent component) throws IOException;
	
	protected synchronized void renderLabel(final SingleOption option,
			final FacesContext context, final UIComponent component, final String additionalClasses)
			throws IOException {
		final ResponseWriter writer = context.getResponseWriter();

		writer.startElement("td", component);
		String classes = "zo-radio-matrix-item-response-domain-label";
		if(additionalClasses != null)classes += " "+additionalClasses;
		writer.writeAttribute("class", classes, null);
		writer.write(JsfUtility.getInstance().evaluateValueExpression(context,
				option.getLabel(), String.class));
		writer.endElement("td");
	}

	/**
	 * this actually renders the <input type="radio" />.
	 * 
	 * @param context
	 * @param component
	 * @param parentClientId
	 * @param itemNum
	 */
	protected synchronized void renderRadio(final FacesContext context,
			final UIComponent component, final String parentClientId,
			final int itemNum, final Object currentValue,final String additionalClasses) throws IOException {
		if (context == null) {
			throw new NullPointerException("context");
		}

		// generate the clientId of this item according to the naming
		// conventions of JSF.
		final String itemId = parentClientId
				+ UINamingContainer.getSeparatorChar(context) + itemNum;

		final ResponseWriter writer = context.getResponseWriter();

		writer.startElement("td", component);
		writer.writeAttribute("align", "center", null);
		String classes = "zo-radio-matrix-item-response-domain-radio";
		if(additionalClasses != null)classes += " "+additionalClasses;
		writer.writeAttribute("class", classes, null);
//		writer.writeAttribute("style",
//				this.getHeaderMinWidth(itemCount), null);

		if (component.isRendered()) {
			// the value of a single choice answer option is by convention
			// always the uid of the component.
			final Object value = component.getAttributes().get("id");
			
			writer.startElement("input", component);
			writer.writeAttribute("type", "radio", null);
			writer.writeAttribute("id", itemId, null);
			writer.writeAttribute("name", parentClientId, null);
			if (component.getChildren().size()>0){
				writer.writeAttribute("class", "zo-matrix-ao-input-radio", null);
			}
//			writer.writeAttribute("onchange", "javascript:zofar_singleChoice_triggerRadio('"+parentClientId+"','"+itemId+"');",null);
			writer.writeAttribute("onchange", "javascript:zofar_matrix_singleChoice_triggerRadio('"+parentClientId+"','"+component.getClientId()+"','"+itemId+"');",null);

			if (value != null && currentValue != null
					&& !((String) currentValue).isEmpty()
					&& ((String) value).equals((String) currentValue)) {
				writer.writeAttribute("checked", "checked", null);
			}

			// TODO: handle disabled attributes

			// TODO: handle behaviours

			writer.writeAttribute("value", value, null);

			writer.endElement("input");
			
			//Attached Open ?
			encodeOpenQuestion(context,component,parentClientId,component.getClientId(),itemId);
		}

		writer.endElement("td");
	}
	
	protected synchronized boolean encodeOpenQuestion(final FacesContext context, final UIComponent component,final String pId,final String qId,final String rId)
			throws IOException {
		if (!component.isRendered()) {
			return false;
		}

		boolean flag = false;
		for (final UIComponent child : component.getChildren()) {
			child.encodeAll(context);
//			insertJavaScript(context,component,parentId,itemId);
			insertJavaScript(context,component,pId,qId,rId,child.getClientId()+":"+child.getAttributes().get("inputId"));
			flag = true;
		}
		return flag;
	}
	
	private synchronized void insertJavaScript(final FacesContext context,final UIComponent component,final String pId,final String qId,final String rId,final String oId) throws IOException {
		final ResponseWriter writer = context.getResponseWriter();
		writer.startElement("script", component);
		writer.writeAttribute("type", "text/javascript", null);
		String script = "zofar_matrix_singleChoice_register_open('"+ pId + "','"+ qId + "', '" + rId+"', '" + oId+"');\n";
		writer.write(script);
		writer.endElement("script");
	}
	
	/**
	 * @return
	 */
	protected synchronized String getHeaderMinWidth(final int itemCount) {
		final int count = itemCount;
		int width = 0;
		if (count > 0)
			width = 700 / count;
		return "width: " + width + "px;";
	}
}

package de.his.zofar.presentation.surveyengine.ui.renderer.answers.open.options;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

import com.sun.faces.facelets.compiler.UIInstructions;

import de.his.zofar.presentation.surveyengine.ui.components.answer.options.OpenOption;
import de.his.zofar.presentation.surveyengine.ui.util.JsfUtility;

@FacesRenderer(componentFamily = OpenOption.COMPONENT_FAMILY, rendererType = ZofarTextOpenOptionRenderer.RENDERER_TYPE)
public class ZofarTextOpenOptionRenderer extends Renderer {
	
	public static final String RENDERER_TYPE = "org.zofar.TextOpenOption";

	public ZofarTextOpenOptionRenderer() {
		super();
	}
	
	@Override
	public boolean getRendersChildren() {
		return true;
	}

	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}
		
		final ResponseWriter writer = context.getResponseWriter();
		writer.startElement("label", component);
		writer.writeAttribute("id", component.getClientId(context), null);
		
		String forId= null;
		final UIComponent tmp = JsfUtility.getInstance().getComposite(component);
		if (tmp != null) {
			for (final UIComponent child : tmp.getChildren()) {
				if ((javax.faces.component.html.HtmlInputText.class).isAssignableFrom(child.getClass())) {
					forId = child.getClientId(context);
					break;
				}
			}
		}
		if(forId != null)writer.writeAttribute("for", forId, null);

		final UIComponent prefix = component.getFacet("prefix");
		if (prefix != null) {
			writer.startElement("span", component);
			writer.writeAttribute("class", "prefix", null);
			if (UIInstructions.class.isAssignableFrom(prefix.getClass())) {
				writer.write(" " + JsfUtility.getInstance().evaluateValueExpression(context, String.valueOf(prefix), String.class) + " ");
			} else {
				prefix.encodeAll(context);
			}
			writer.endElement("span");
		}
	}

	@Override
	public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}
		final UIComponent prefix = component.getFacet("prefix");
		
		String placeholder = null;
		
		if (prefix != null) {
			if (UIInstructions.class.isAssignableFrom(prefix.getClass())) {
				placeholder = " " + JsfUtility.getInstance().evaluateValueExpression(context, String.valueOf(prefix), String.class) + " ";
			} 
			else if(de.his.zofar.presentation.surveyengine.ui.components.text.UIResponseOptionText.class.isAssignableFrom(prefix.getClass())){
				placeholder = " " + JsfUtility.getInstance().getTextComponentAsString(context, prefix)+" ";

			}
//			else System.out.println("other class "+prefix.getClass());
		}

		final UIComponent tmp = JsfUtility.getInstance().getComposite(component);
		if (tmp != null) {
			for (final UIComponent child : tmp.getChildren()) {
				
				if ((javax.faces.component.html.HtmlInputText.class).isAssignableFrom(child.getClass())) {
					if(placeholder != null){
						child.getPassThroughAttributes(true);
						Map<String, Object> passThrough = ((javax.faces.component.html.HtmlInputText)child).getPassThroughAttributes();
						passThrough.put("placeholder", placeholder);
						passThrough.put("title", placeholder);
					}
					child.encodeAll(context);
					
				} else if((javax.faces.component.html.HtmlInputTextarea.class).isAssignableFrom(child.getClass())){
					if(placeholder != null){
						child.getPassThroughAttributes(true);
						Map<String, Object> passThrough = ((javax.faces.component.html.HtmlInputTextarea)child).getPassThroughAttributes();
						passThrough.put("placeholder", placeholder);
						passThrough.put("title", placeholder);
						
					}
					child.encodeAll(context);
				}
			}
		}
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}

		final ResponseWriter writer = context.getResponseWriter();
		final UIComponent postfix = component.getFacet("postfix");
		if (postfix != null) {
			writer.startElement("span", component);
			writer.writeAttribute("class", "postfix", null);
			if (UIInstructions.class.isAssignableFrom(postfix.getClass())) {
				writer.write(" " + JsfUtility.getInstance().evaluateValueExpression(context, String.valueOf(postfix), String.class));
			} else {
				postfix.encodeAll(context);
			}
			writer.endElement("span");
		}

		final UIComponent tmp = JsfUtility.getInstance().getComposite(component);
		if (tmp != null) {
			for (final UIComponent child : tmp.getChildren()) {
				if ((javax.faces.component.html.HtmlMessage.class).isAssignableFrom(child.getClass())) {
					child.encodeAll(context);
				}
			}
		}

		writer.endElement("label");
	}

}

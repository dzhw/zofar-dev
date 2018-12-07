package de.his.zofar.presentation.surveyengine.ui.renderer.buttons;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.buttons.Jumper;
import de.his.zofar.presentation.surveyengine.ui.components.buttons.JumperContainer;

/**
 * @author meisner
 *
 */
@FacesRenderer(componentFamily = JumperContainer.COMPONENT_FAMILY, rendererType = SideNavRenderer.RENDERER_TYPE)
public class SideNavRenderer extends Renderer{

	public static final String RENDERER_TYPE = "org.zofar.SideNav";
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SideNavRenderer.class);

	public SideNavRenderer() {
		super();
	}

	@Override
	public void encodeChildren(final FacesContext context,
			final UIComponent component) throws IOException {
		
		if (!component.isRendered())
			return;
		List<Object> tree = new ArrayList<Object>();
		Stack<List<Object>> stack = new Stack<List<Object>>();
		stack.add(tree);
		final Iterator<UIComponent> it = component.getChildren().iterator();
		while(it.hasNext()){
			final UIComponent child = it.next();
			if (Jumper.class.isAssignableFrom(child.getClass())) {
				final int level = Integer.parseInt((String)child.getAttributes().get("level"));	
				if(level == (stack.size() - 1)){
					
				}
				else{
//					LOGGER.info("level : {} stack {}",level,(stack.size() - 1));
					while(level < (stack.size() - 1))stack.pop();
					while(level > (stack.size() - 1)){
						final List<Object> tmp = new ArrayList<Object>();
						stack.peek().add(tmp);
						stack.add(tmp);
					}
				}
				stack.peek().add(child);
			} 
		}
		encodeChildrenHelper(context,component,tree);
	}

	private void encodeChildrenHelper(final FacesContext context,
			final UIComponent component, final List<Object> children) throws IOException {
		if (!component.isRendered())
			return;
		final ResponseWriter writer = context.getResponseWriter();
		writer.startElement("ul", component);
		final Iterator<Object> it = children.iterator();
		while(it.hasNext()){
			final Object child = it.next();
			if (Jumper.class.isAssignableFrom(child.getClass())) {
				writer.startElement("li", component);
				((UIComponent)child).encodeAll(context);
				writer.endElement("li");
			} 
			else if((List.class).isAssignableFrom(child.getClass())){
				writer.startElement("li", component);
				encodeChildrenHelper(context,component,(List)child);
				writer.endElement("li");
			}
		}
		writer.endElement("ul");
	}

	@Override
	public boolean getRendersChildren() {
		return true;
	}
	
	
}

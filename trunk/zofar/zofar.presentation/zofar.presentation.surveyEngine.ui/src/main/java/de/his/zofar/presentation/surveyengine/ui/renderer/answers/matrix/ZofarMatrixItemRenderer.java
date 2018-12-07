package de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.answer.options.UIAttachedOpenQuestion;
import de.his.zofar.presentation.surveyengine.ui.components.tablebase.AbstractTableResponseDomain;
public abstract class ZofarMatrixItemRenderer extends Renderer {
	
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ZofarMatrixItemRenderer.class);
    protected static final String TABLE_CELL = "td";
    public static final String HEADER_FACET = "header";
    protected static final String QUESTION_COLUMN = "questionColumn";
	
//	private static final Logger LOGGER = LoggerFactory
//			.getLogger(ZofarMatrixItemRenderer.class);

	public ZofarMatrixItemRenderer() {
		super();
	}

	/**
	 * does the response domain have a question column.
	 * 
	 * @return
	 */
	protected synchronized Boolean isHasQuestionColumn(final FacesContext context,
			final UIComponent component) {
		Boolean hasQuestionColumn = false;
		UIComponent parent = component;
		while(!((AbstractTableResponseDomain.class).isAssignableFrom(parent.getClass())))parent = parent.getParent();
		
		if (parent.getAttributes().get(QUESTION_COLUMN) != null) {
//			hasQuestionColumn = (Boolean) parent
//					.getAttributes().get(QUESTION_COLUMN);
			hasQuestionColumn = Boolean.valueOf(parent.getAttributes().get(
					QUESTION_COLUMN)+"");
		}
		
		return hasQuestionColumn;
	}
	
	public abstract void encodeBegin(FacesContext context, UIComponent component)
			throws IOException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.faces.component.UIComponentBase#encodeChildren(javax.faces.context
	 * .FacesContext)
	 */
	@Override
	public synchronized void encodeChildren(FacesContext context, UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}
		
		for (final UIComponent child : component.getChildren()) {
			if((UIAttachedOpenQuestion.class).isAssignableFrom(child.getClass())){
				continue;
			}
//			LOGGER.info("child {} ({})",child,child.getRendererType());
			child.encodeAll(context);
		}
	}

	@Override
	public boolean getRendersChildren() {
		return true;
	}
	
	

}

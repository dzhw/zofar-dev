package de.his.zofar.presentation.surveyengine.ui.renderer.matrix.doubleMatrix;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.composite.doublematrix.UIDoubleMatrixItem;
import de.his.zofar.presentation.surveyengine.ui.components.matrix.singlechoice.UISingleChoiceMatrixItemResponseDomain;

/**
 * @author meisner
 *
 */
@FacesRenderer(componentFamily = UIDoubleMatrixItem.COMPONENT_FAMILY, rendererType = ZofarDoubleMatrixItemRenderer.RENDERER_TYPE)
public class ZofarDoubleMatrixItemRenderer extends ZofarAbstractDoubleMatrixRenderer {
	
	public static final String RENDERER_TYPE = "org.zofar.DoubleMatrixItem";
	
    private static final Logger LOGGER = LoggerFactory
			.getLogger(ZofarDoubleMatrixItemRenderer.class);

	public ZofarDoubleMatrixItemRenderer() {
		super();
	}

    @Override
    public synchronized void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
        this.renderAnswerOptions(context,component, Side.LEFT);
        this.renderQuestionColumn(context,component);
        this.renderAnswerOptions(context,component, Side.RIGHT);
    }

    /**
     * @param context
     * @param right
     * @throws IOException
     */
    private synchronized void renderAnswerOptions(final FacesContext context, final UIComponent component, final Side side)
            throws IOException {
    	
//    	LOGGER.info("renderAnswerOptions {} ({})",component,side);
        String facetName = null;

        switch (side) {
        case LEFT:
            facetName = LEFT_QUESTION;
            break;
        case RIGHT:
            facetName = RIGHT_QUESTION;
            break;
        default:
            throw new IllegalStateException();
        }

        // the facet must not be null
        final UIComponent question = component.getFacet(facetName);

        if (!UISingleChoiceMatrixItemResponseDomain.class
                .isAssignableFrom(question.getClass())) {
            throw new IllegalStateException(
                    "The left and right side of an double matrix item must be of type UISingleChoiceMatrixItemResponseDomain. But it is: "
                            + question.getClass());
        }
        question.encodeAll(context);
    }

    /**
     * @param context
     */
    private synchronized void renderQuestionColumn(final FacesContext context, final UIComponent component)
            throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        final UIComponent questionFacet = component.getFacet(QUESTION);

        writer.startElement("td", component);
        writer.writeAttribute("class", "zo-db-item-question", null);
        // question facet must not be null!
        questionFacet.encodeAll(context);
        writer.endElement("td");
    }

	@Override
	public synchronized boolean getRendersChildren() {
		return true;
	}
}

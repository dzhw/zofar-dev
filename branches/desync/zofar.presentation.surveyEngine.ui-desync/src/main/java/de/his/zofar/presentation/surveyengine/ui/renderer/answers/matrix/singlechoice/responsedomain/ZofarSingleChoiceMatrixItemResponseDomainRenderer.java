package de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.singlechoice.responsedomain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;

import de.his.zofar.presentation.surveyengine.ui.components.answer.options.SingleOption;
import de.his.zofar.presentation.surveyengine.ui.components.matrix.singlechoice.UISingleChoiceMatrixItemResponseDomain;
/**
 * @author meisner
 *
 */
@FacesRenderer(componentFamily = UISingleChoiceMatrixItemResponseDomain.COMPONENT_FAMILY, rendererType = ZofarSingleChoiceMatrixItemResponseDomainRenderer.RENDERER_TYPE)
public class ZofarSingleChoiceMatrixItemResponseDomainRenderer extends ZofarRadioMatrixItemResponseDomainRenderer {

	public static final String RENDERER_TYPE = "org.zofar.SCMatrixItemResponseDomain";
	
	public ZofarSingleChoiceMatrixItemResponseDomainRenderer() {
		super();
	}

	@Override
	public void encodeChildren(final FacesContext context, final UIComponent component)
			throws IOException {
		// get the clientId of this container which is the parent of the radio
		// input fields.
		final String clientId = component.getClientId(context);

		final Object currentValue = component.getAttributes().get("value");

		final List<SingleOption> missings = new ArrayList<SingleOption>();

		final List<SingleOption> answerOptions = new ArrayList<SingleOption>();

		for (final UIComponent child : component.getChildren()) {
			if (SingleOption.class.isAssignableFrom(child.getClass())) {
				final SingleOption answerOption = (SingleOption) child;
				if (answerOption.isMissing()) {
					missings.add((SingleOption) child);
				} else {
					answerOptions.add(answerOption);
				}
			}
		}

		int itemNum = 0;
		int countMissing=0;
		// render normal answer options as well as scale label for differentials
		
		for (final SingleOption answerOption : answerOptions) {
			renderRadio(context, answerOption, clientId, itemNum++,currentValue,"zo-sc-matrix-item-response-domain-radio");
		}

		for (final SingleOption missing : missings) {
			renderRadio(context, missing, clientId, itemNum++, currentValue,"zo-sc-matrix-item-response-domain-radio-missing-"+(countMissing++));
		}
	}
}

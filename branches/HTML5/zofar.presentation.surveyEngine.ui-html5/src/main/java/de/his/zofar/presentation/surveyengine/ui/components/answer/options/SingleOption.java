/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.answer.options;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.answer.responseDomain.UIDropDownResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.common.Visible;
import de.his.zofar.presentation.surveyengine.ui.components.question.composite.comparison.UIComparisonItem;
import de.his.zofar.presentation.surveyengine.ui.components.question.matrix.singlechoice.UISingleChoiceMatrixItemResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.components.text.UIText;
import de.his.zofar.presentation.surveyengine.ui.interfaces.IAnswerOption;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.singechoice.options.ZofarComparisonRadioSingleChoiceOptionRenderer;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.singechoice.options.ZofarDropDownSingleOptionRenderer;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.singechoice.options.ZofarHorizontalRadioSingleOptionRenderer;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.singechoice.options.ZofarVerticalRadioSingleOptionRenderer;
import de.his.zofar.presentation.surveyengine.ui.util.JsfUtility;

/**
 * @author meisner
 *
 */
@FacesComponent(value = "org.zofar.SingleOption")
public class SingleOption extends UINamingContainer implements Identificational, Visible, IAnswerOption {

	private enum PropertyKeys {
		value, label, missing
	}

	private String label = "";
	private UIComponent labels;

	private boolean showLabelFlag = true;
	private Object sequenceId;

	public static final String COMPONENT_FAMILY = "org.zofar.SingleOption";

	private static final Logger LOGGER = LoggerFactory.getLogger(SingleOption.class);

	public SingleOption() {
		super();
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public String getRendererType() {
		final UIComponent parentRdc = JsfUtility.getInstance().getParentResponseDomain(this.getParent());
		String rendererType = null;

		if (parentRdc != null) {
			final UIComponent context = parentRdc.getParent().getParent();
			final boolean comparsionFlag = ((context != null) && ((UIComparisonItem.class).isAssignableFrom(context.getClass())));

			if (comparsionFlag) {
				rendererType = ZofarComparisonRadioSingleChoiceOptionRenderer.RENDERER_TYPE;
			} else if (parentRdc instanceof UIDropDownResponseDomain) {
				rendererType = ZofarDropDownSingleOptionRenderer.RENDERER_TYPE;
			} else if (parentRdc instanceof UISingleChoiceMatrixItemResponseDomain) {
				rendererType = ZofarHorizontalRadioSingleOptionRenderer.RENDERER_TYPE;
			} else {
				final String direction = (String) parentRdc.getAttributes().get("direction");
				Boolean isHorizontal = false;
				if (direction != null) {
					isHorizontal = direction.equals("horizontal");
				}
				if (isHorizontal) {
					rendererType = ZofarHorizontalRadioSingleOptionRenderer.RENDERER_TYPE;
				} else {
					rendererType = ZofarVerticalRadioSingleOptionRenderer.RENDERER_TYPE;
				}
			}
		}
		return rendererType;
	}

	// @Override
	// public String getRendererType() {
	// final UIComponent parent =
	// JsfUtility.getInstance().getParentResponseDomain(this.getParent());
	// String rendererType = null;
	//
	// if (parent != null) {
	// final UIComponent context = parent.getParent().getParent();
	// final boolean comparsionFlag = ((context != null) &&
	// ((UIComparisonItem.class).isAssignableFrom(context.getClass())));
	//
	// if (comparsionFlag) {
	// rendererType =
	// ZofarComparisonRadioSingleChoiceOptionRenderer.RENDERER_TYPE;
	// } else if (parent instanceof UIDropDownResponseDomain) {
	// rendererType = ZofarDropDownSingleOptionRenderer.RENDERER_TYPE;
	// }
	// else {
	// final String direction = (String)
	// parent.getAttributes().get("direction");
	// Boolean isHorizontal = false;
	// if (direction != null) {
	// isHorizontal = direction.equals("horizontal");
	// }
	// if (isHorizontal) {
	// rendererType = ZofarHorizontalRadioSingleOptionRenderer.RENDERER_TYPE;
	// } else {
	// rendererType = ZofarVerticalRadioSingleOptionRenderer.RENDERER_TYPE;
	// }
	// }
	// }
	// return rendererType;
	// }

	public void setItemValue(final String value) {
		this.getStateHelper().put(PropertyKeys.value, value);
	}

	public String getItemValue() {
		// value = (String) getAttributes().get("itemValue");
		// return value;
		return (String) this.getStateHelper().eval(PropertyKeys.value);
	}

	public String getValue() {
		return this.getItemValue();
	}

	public String getLabel() {
		if ((this.label == null) || (this.label.equals(""))) {
			final Object itemLabel = this.getAttributes().get("itemLabel");
			if ((itemLabel != null) && !((String) itemLabel).isEmpty()) {
				this.label = (String) itemLabel + " ";
			}
			// append label facet to the label string
			final String labels = this.getLabelStringFromFacet();
			if (!labels.isEmpty()) {
				this.label += labels + " ";
			}
		}
		return this.label;
	}

	private UIComponent getLabels() {
		this.labels = this.getFacet("labels");
		return this.labels;
	}

	private String getLabelStringFromFacet() {
		final String delimiter = " ";
		final StringBuilder labels = new StringBuilder();

		final UIComponent facet = this.getLabels();
		if (facet != null) {
			if (facet.getChildren().isEmpty()) {
				if (UIText.class.isAssignableFrom(facet.getClass())) {
					if (!((UIText) facet).getContent().isEmpty()) {
						labels.append(((UIText) facet).getContent()).append(delimiter);
					}
				}
			} else {
				if (UIText.class.isAssignableFrom(facet.getClass())) {
					for (final UIComponent child : facet.getChildren()) {
						labels.append(child).append(delimiter);
					}
				}
			}
		}

		return labels.toString();
	}

	public void setMissing(final boolean missing) {
		this.getStateHelper().put(PropertyKeys.missing, missing);
	}

	public boolean isMissing() {
		return Boolean.valueOf(this.getStateHelper().eval(PropertyKeys.missing, false).toString());
	}

	public boolean isShowLabelFlag() {
		return showLabelFlag;
	}

	public void setShowLabelFlag(boolean showLabelFlag) {
		this.showLabelFlag = showLabelFlag;
	}

	public Boolean hasAttachedOpenQuestion() {
		for (final UIComponent child : this.getChildren()) {
			if (UIAttachedOpenQuestion.class.isAssignableFrom(child.getClass())) {
				return true;
			}
		}
		return false;
	}

	public UIAttachedOpenQuestion getAttachedOpenQuestion() {
		for (final UIComponent child : this.getChildren()) {
			if (UIAttachedOpenQuestion.class.isAssignableFrom(child.getClass())) {
				return (UIAttachedOpenQuestion) child;
			}
		}
		return null;
	}
	
	@Override
	public void setSequenceId(Object id) {
		this.sequenceId = id;
	}

	@Override
	public Object getSequenceId() {
		return sequenceId;
	}

}

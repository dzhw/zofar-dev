package de.his.zofar.presentation.surveyengine;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.controller.SessionController;

/**
 * @author meisner
 * 
 */
public class SingleChoiceAnswerOptionTypeBean extends AbstractLabeledAnswerBean {

	private static final long serialVersionUID = -5588268597517365309L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SingleChoiceAnswerOptionTypeBean.class);

	private Map<String, Object> optionValues = new LinkedHashMap<String, Object>();
	private Map<String, Object> missingValues = new LinkedHashMap<String, Object>();
	private Map<String, Object> labels = new LinkedHashMap<String, Object>();

	private String valueId;

	@Inject
	public SingleChoiceAnswerOptionTypeBean(final SessionController sessionController, final String variableName) {
		super(sessionController, variableName);
	}

	public Map<String, Object> getOptionValues() {
		return this.optionValues;
	}

	public Map<String, Object> getMissingValues() {
		return missingValues;
	}

	public void setMissingValues(Map<String, Object> missingValues) {
		this.missingValues = missingValues;
	}

	public Map<String, Object> getLabels() {
		return labels;
	}

	public void setLabels(Map<String, Object> labels) {
		this.labels = labels;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.his.zofar.presentation.surveyengine.AbstractAnswerBean#getStringValue
	 * ()
	 */
	@Override
	public String getStringValue() {
		return this.valueId;
	}

	public String getValue() {
		String back = "";
		if (this.optionValues.containsKey(this.getValueId())) {
			back = (String) this.optionValues.get(this.getValueId());
		} else if (this.missingValues.containsKey(this.getValueId())) {
			back = (String) this.missingValues.get(this.getValueId());
		}
		return back;
	}

	/**
	 * @return the value
	 */
	public String getValueId() {
		return this.valueId;
	}

	public void setOptionValues(final Map<String, Object> optionValues) {
		this.optionValues = optionValues;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.his.zofar.presentation.surveyengine.AbstractAnswerBean#setStringValue
	 * (java.lang.String)
	 */
	@Override
	public void setStringValue(final String stringValue) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("set String Value ({}) : {}", this.getVariableName(), stringValue);
		}
		this.setValueId(stringValue);
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValueId(final String value) {
		// strip the parent part of the UID to store only the UID of the answer
		// option in the database.
		this.valueId = value.substring(value.lastIndexOf(":") + 1, value.length());
		this.saveValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.his.zofar.surveyengine.AbstractValueTypeBean#toPlaceholder()
	 */
	@Override
	public Object toPlaceholder() {
		final String label = this.getLabel();
		if ((label == null) || (label.isEmpty())) {
			if (this.getValue() == null) {
				return this.getAlternative();
			} else if ("-97".equals(this.getValue())) {
				return this.getAlternative();
			} else if (this.getMissingValues().containsValue(this.getValue())) {
				return this.getAlternative();
			}
			return this.getValue();
		} else {
			return label;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.his.zofar.presentation.surveyengine.AbstractLabeledAnswerBean#loadLabels
	 * ()
	 */
	@Override
	protected Map<String, String> loadLabels() {
		Map<String, String> back = this.getSessionController().loadLabelMap(this.getVariableName(), this.valueId);
		return back;
	}
}

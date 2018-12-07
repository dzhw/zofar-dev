/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.interfaces;

/**
 * @author le
 *
 */
public interface IAnswerBean {

	/**
	 * converts the value to to a string as values are stored as strings.
	 * 
	 * @return
	 */
	public abstract String getStringValue();

	/**
	 * @return the variableName
	 */
	public abstract String getVariableName();

	/**
	 * @param stringValue
	 */
	public abstract void setStringValue(String stringValue);

}

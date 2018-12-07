/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.interfaces;

import java.io.Serializable;
import java.util.Map;

/**
 * @author meisner
 *
 */
public interface Task extends Serializable {

	/**
	 * Method to initialize Referneces
	 */
	public Map<String, Object> dump();

	/**
	 * Method to execute queued Task
	 */
	public void executeTask(Map<String, Object> dump);

}

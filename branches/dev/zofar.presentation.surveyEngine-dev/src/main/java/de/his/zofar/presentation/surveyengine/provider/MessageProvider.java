package de.his.zofar.presentation.surveyengine.provider;

import java.io.Serializable;
import java.util.HashMap;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.util.JsfUtility;
/**
 * Bean to provide EL - Support in ResourceBundle
 *
 * @author meisner
 *
 */
@ManagedBean(name = "msgs")
@SessionScoped
public class MessageProvider extends HashMap<Object, Object> implements
		Serializable {

	private static final long serialVersionUID = -1077163456237674734L;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(MessageProvider.class);

	public MessageProvider() {
		super();
        LOGGER.debug("created");
	}

	@Override
	public Object get(final Object key) {
		String msg = JsfUtility.getInstance().evaluateValueExpression(
				FacesContext.getCurrentInstance(),
				"#{msgbundle['" + key + "']}", String.class);
		if ((msg != null) && ((String.class).isAssignableFrom(msg.getClass()))) {
			msg = JsfUtility.getInstance().evaluateValueExpression(
					FacesContext.getCurrentInstance(), msg + "", String.class);
		}
//		LOGGER.info("get Message for {} ==> {}", key, msg);
		return msg;
	}
}

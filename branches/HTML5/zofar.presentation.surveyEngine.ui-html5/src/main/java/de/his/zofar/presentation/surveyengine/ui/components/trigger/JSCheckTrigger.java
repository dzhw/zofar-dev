package de.his.zofar.presentation.surveyengine.ui.components.trigger;

import java.io.Serializable;

import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.interfaces.IAnswerBean;
import de.his.zofar.presentation.surveyengine.ui.util.JsfUtility;

/**
 * @author meisner
 *
 */
@FacesComponent(value = "org.zofar.JSCheckTrigger")
public class JSCheckTrigger extends UINamingContainer implements Serializable {

	private static final long serialVersionUID = -9208937728141596288L;
	private static final Logger LOGGER = LoggerFactory.getLogger(JSCheckTrigger.class);

	public JSCheckTrigger() {
		super();
	}

	public IAnswerBean variable() {
		final IAnswerBean var = (IAnswerBean) this.getAttributes().get("var");
		return var;
	}

	public IAnswerBean xvariable() {
		final IAnswerBean var = (IAnswerBean) this.getAttributes().get("xvar");
		return var;
	}

	public IAnswerBean yvariable() {
		final IAnswerBean var = (IAnswerBean) this.getAttributes().get("yvar");
		return var;
	}

	public boolean isProxy() {
		return false;
	}

	public String getProxyx() {
		return "-1";
	}

	public void setProxyx(final String proxyx) {
		JsfUtility.getInstance().setExpressionValue(this.getFacesContext(), "#{" + this.xvariable().getVariableName() + ".value}", proxyx);
	}

	public String getProxyy() {
		return "-1";
	}

	public void setProxyy(final String proxyy) {
		JsfUtility.getInstance().setExpressionValue(this.getFacesContext(), "#{" + this.yvariable().getVariableName() + ".value}", proxyy);
	}

	public void setProxy(final boolean proxy) {
		JsfUtility.getInstance().setExpressionValue(this.getFacesContext(), "#{" + this.variable().getVariableName() + ".value}", proxy);
	}

}

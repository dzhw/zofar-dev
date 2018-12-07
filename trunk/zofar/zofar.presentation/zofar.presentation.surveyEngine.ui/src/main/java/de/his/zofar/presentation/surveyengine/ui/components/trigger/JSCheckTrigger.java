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
public class JSCheckTrigger extends UINamingContainer implements Serializable{

	private static final long serialVersionUID = -9208937728141596288L;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(JSCheckTrigger.class);
	
	private boolean proxy;
	private String proxyx;
	private String proxyy;

	public JSCheckTrigger() {
		super();
	}
	
    public IAnswerBean variable() {
        final IAnswerBean var = (IAnswerBean) getAttributes().get("var");
        return var;
    }
    
    public IAnswerBean xvariable() {
        final IAnswerBean var = (IAnswerBean) getAttributes().get("xvar");
        return var;
    }
    
    public IAnswerBean yvariable() {
        final IAnswerBean var = (IAnswerBean) getAttributes().get("yvar");
        return var;
    }

	public boolean isProxy() {
		return false;
	}

	public String getProxyx() {
		return "-1";
	}

	public void setProxyx(String proxyx) {
		JsfUtility.getInstance().setExpressionValue(this.getFacesContext(), "#{"+this.xvariable().getVariableName()+".value}",proxyx);
//		LOGGER.info("set xValue {} to {}",proxyx,this.xvariable());
	}

	public String getProxyy() {
		return "-1";
	}

	public void setProxyy(String proxyy) {
		JsfUtility.getInstance().setExpressionValue(this.getFacesContext(), "#{"+this.yvariable().getVariableName()+".value}",proxyy);
//		LOGGER.info("set yValue {} to {}",proxyy,this.yvariable());
	}

	public void setProxy(boolean proxy) {
		JsfUtility.getInstance().setExpressionValue(this.getFacesContext(), "#{"+this.variable().getVariableName()+".value}",proxy);
//		LOGGER.info("set Value {} to {}",proxy,this.variable());
	}


	
}

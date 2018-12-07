package de.his.zofar.presentation.surveyengine.ui.components.display;

import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.common.Visible;
import de.his.zofar.presentation.surveyengine.ui.renderer.display.TableRenderer2;

/**
 * @author meisner
 *
 */
@FacesComponent(value = "org.zofar.display.table")
public class UIDisplayTable extends UINamingContainer implements Identificational, Visible {

	public static final String COMPONENT_FAMILY = "org.zofar.display.table";

	private static final Logger LOGGER = LoggerFactory.getLogger(UIDisplayTable.class);

	public UIDisplayTable() {
		super();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.faces.component.UIComponentBase#getRendererType()
	 */
	@Override
	public String getRendererType() {
		return TableRenderer2.RENDERER_TYPE;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.faces.component.UIComponent#getFamily()
	 */
	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.faces.component.UIComponentBase#isTransient()
	 */
	@Override
	public boolean isTransient() {
		return true;
	}

}

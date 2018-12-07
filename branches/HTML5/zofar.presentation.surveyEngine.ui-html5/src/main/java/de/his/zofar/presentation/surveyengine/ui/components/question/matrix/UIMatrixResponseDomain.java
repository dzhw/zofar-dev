package de.his.zofar.presentation.surveyengine.ui.components.question.matrix;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.interfaces.IResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.interfaces.ISequence;
import de.his.zofar.presentation.surveyengine.ui.util.JsfUtility;

public abstract class UIMatrixResponseDomain extends UINamingContainer implements Identificational, IResponseDomain {

	public static final String COMPONENT_FAMILY = "org.zofar.MatrixResponseDomain";

	/**
	 * LOGGER
	 */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(UIMatrixResponseDomain.class);

	/**
	 *
	 */
	public UIMatrixResponseDomain() {
		super();
	}

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
	
	public Boolean isShowValues() {
		Boolean showValues = (Boolean) this.getAttributes().get("showValues");
		if(showValues == null)showValues = false;
		return showValues;
	}
	
	public void sequenceMatrixItems(){
		List<UIComponent> visibleMatrixItems = JsfUtility.getInstance().getVisibleChildrensOfType(this, UIMatrixItem.class);
		if(visibleMatrixItems != null){
			int index = 0;
			for(final UIComponent visibleMatrixItem : visibleMatrixItems){
				if((ISequence.class).isAssignableFrom(visibleMatrixItem.getClass())){
					((ISequence)visibleMatrixItem).setSequenceId(index);
					index = index + 1;
				}
			}
		}
	}
	

}

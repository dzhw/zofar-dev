/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.interfaces.ISortBean;

/**
 * self rendered class with sort its children first before rendering them.
 * actual sorting algorithm must be implemented by a class which implements the
 * ISortBean interface.
 * 
 * @author meisner
 * 
 */
@FacesComponent(value = "org.zofar.Sort")
public class UISort extends UINamingContainer {
	private static final Logger LOGGER = LoggerFactory.getLogger(UISort.class);

	private enum PropertyKeys {
		bean, mode, sorted
	}

	public UISort() {
		super();
		this.setRendererType(null);
	}

	@Override
	public boolean getRendersChildren() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.faces.component.UIComponentBase#encodeBegin(javax.faces.context
	 * .FacesContext)
	 */
//	@Override
//	public void encodeBegin(final FacesContext context) throws IOException {
//		if (this.isSorted()) {
//			this.sortChildren();
//		}
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.faces.component.UIComponentBase#encodeChildren(javax.faces.context
	 * .FacesContext)
	 */

	@Override
	public void encodeChildren(final FacesContext context) throws IOException {
		for (final UIComponent child : sortChildren()) {
			child.encodeAll(context);
		}
	}

	/**
	 * delegates the actual sorting to a implementation of the ISortBean
	 * interface.
	 * 
	 * @return
	 */
	public List<UIComponent> sortChildren() {
		// sort children before rendering
//		LOGGER.info("children {} ({})", this.getChildren(),
//				this.getClientId(this.getFacesContext()));

		final List<UIComponent> childrenToSort = new ArrayList<UIComponent>(
				this.getChildren());
//		LOGGER.info("childrenToSort {} ({})", childrenToSort,
//				this.getClientId(this.getFacesContext()));

		final List<UIComponent> fromBean = this.getBean().sort(childrenToSort,
				this.getClientId(this.getFacesContext()), this.getMode());

//		LOGGER.info("fromBean ({})", this.getMode());
		
//		for (final UIComponent child : fromBean) {
//			LOGGER.info("fromBean  child {}", child.getClientId());
//		}
		
		return fromBean;
	}

	public void setBean(final String bean) {
		this.getStateHelper().put(PropertyKeys.bean, bean);
	}

	public ISortBean getBean() {
		return (ISortBean) this.getStateHelper().eval(PropertyKeys.bean,
				new SortBean());
	}

	public void setMode(final String mode) {
		this.getStateHelper().put(PropertyKeys.mode, mode);
	}

	public String getMode() {
		return (String) this.getStateHelper().eval(PropertyKeys.mode, "random");
	}

	public void setSorted(final boolean sorted) {
		this.getStateHelper().put(PropertyKeys.sorted, sorted);
	}

	public boolean isSorted() {
		return Boolean.valueOf(this.getStateHelper()
				.eval(PropertyKeys.sorted, Boolean.TRUE).toString());
	}

	/**
	 * default / fallback implementation of the ISortBean interface. this is a
	 * very dump implementation and always produces a new sort order on every
	 * request.
	 * 
	 * TODO this is unnecessary if we make the bean attribute required. BUT this
	 * is not working right now. which means even if we declare the bean
	 * attribute as required it is possible to use the component without passing
	 * a sort bean instance to the component.
	 * 
	 * @author le
	 * 
	 */
	private class SortBean implements ISortBean {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * de.his.zofar.presentation.surveyengine.ui.interfaces.ISortBean#sort
		 * (java.util.List, java.lang.String, java.lang.String)
		 */
		@Override
		public List<UIComponent> sort(final List<UIComponent> toSort,
				final String parentId, final String mode) {
			Collections.shuffle(toSort);
			return toSort;
		}

	}

}

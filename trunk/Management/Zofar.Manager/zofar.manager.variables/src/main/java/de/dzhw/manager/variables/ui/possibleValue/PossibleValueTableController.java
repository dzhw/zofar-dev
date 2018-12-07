package de.dzhw.manager.variables.ui.possibleValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.dzhw.manager.common.utils.StringUtils;
import de.dzhw.manager.variables.managedBeans.ValueType;
import de.his.zofar.service.valuetype.model.BooleanValueType;
import de.his.zofar.service.valuetype.model.NumberValueType;
import de.his.zofar.service.valuetype.model.StringValueType;
import de.his.zofar.service.valuetype.model.StringValueType.PossibleValues;
import de.his.zofar.service.valuetype.model.possibleValues.PossibleBooleanValue;
import de.his.zofar.service.valuetype.model.possibleValues.PossibleNumberValue;
import de.his.zofar.service.valuetype.model.possibleValues.PossibleStringValue;
import de.his.zofar.service.valuetype.model.possibleValues.PossibleValue;

@Component("uiPossibleValueTableController")
@Scope("request")
public class PossibleValueTableController implements Serializable {

	private static final long serialVersionUID = -1111016173067995246L;
	@Inject
	private ValueType valueTypeBean;
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(PossibleValueTableController.class);

	public class PossibleValueItem {

		private Object key;
		private de.his.zofar.service.valuetype.model.possibleValues.PossibleValue ref;

		private boolean missing;
		private String description;
		private Object value;

		public PossibleValueItem(Object key, PossibleValue ref) {
			super();
			this.key = key;
			this.ref = ref;
		}

		public Object getKey() {
			return key;
		}

		public void setKey(Object key) {
			this.key = key;
		}

		public de.his.zofar.service.valuetype.model.possibleValues.PossibleValue getRef() {
			return ref;
		}

		public void setRef(
				de.his.zofar.service.valuetype.model.possibleValues.PossibleValue ref) {
			this.ref = ref;
		}

		public boolean isMissing() {
			if(this.getRef() != null)return getRef().getIsMissing();
			return false;
		}

		public void setMissing(boolean isMissing) {
			// this.isMissing = isMissing;
		}

		public String getDescription() {
			if(this.getRef() != null)return StringUtils.getInstance().implode(this.getRef().getLabelArray());
//			return getKey() + "";
			return null;
		}

		public void setDescription(String description) {
			// this.description = description;
		}

		public Object getValue() {
			Object back = null;
			if (this.ref != null) {
				if ((PossibleStringValue.class).isAssignableFrom(this.ref
						.getClass())) {
					back = ((PossibleStringValue) this.ref).getValue();
				} else if ((PossibleNumberValue.class)
						.isAssignableFrom(this.ref.getClass())) {
					back = ((PossibleNumberValue) this.ref).getValue();
				} else if ((PossibleBooleanValue.class)
						.isAssignableFrom(this.ref.getClass())) {
					back = ((PossibleBooleanValue) this.ref).getValue();
				}
			}
			return back;
		}

		public void setValue(Object value) {
			// this.value = value;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((key == null) ? 0 : key.hashCode());
			result = prime * result + ((ref == null) ? 0 : ref.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			PossibleValueItem other = (PossibleValueItem) obj;
			if (key == null) {
				if (other.key != null)
					return false;
			} else if (!key.equals(other.key))
				return false;
			if (ref == null) {
				if (other.ref != null)
					return false;
			} else if (!ref.equals(other.ref))
				return false;
			return true;
		}
	};

	List<PossibleValueItem> values = null;

	public PossibleValueTableController() {
		super();
	}
	
	@PostConstruct
	private void init() {
//		LOGGER.info("init");
	}

	public List<PossibleValueItem> getValues() {
		if (values == null) {
			values = new ArrayList<PossibleValueItem>();
			if (valueTypeBean.getCurrent() != null) {
				if (valueTypeBean.isString()) {
					de.his.zofar.service.valuetype.model.StringValueType.PossibleValues possibleValues = ((StringValueType) valueTypeBean.getCurrent()).getPossibleValues();
					if (possibleValues != null) {
						StringValueType.PossibleValues.Entry[] entries = possibleValues.getEntryArray();
						for (StringValueType.PossibleValues.Entry entry : entries) {
							final String key = entry.getKey();
							final PossibleStringValue value = entry.getValue();
							values.add(new PossibleValueItem(key, value));
						}
					}

				} else if (valueTypeBean.isNumber()) {
					de.his.zofar.service.valuetype.model.NumberValueType.PossibleValues possibleValues = ((NumberValueType) valueTypeBean.getCurrent()).getPossibleValues();
					if (possibleValues != null) {
						NumberValueType.PossibleValues.Entry[] entries = possibleValues.getEntryArray();
						for (NumberValueType.PossibleValues.Entry entry : entries) {
							final Long key = entry.getKey();
							final PossibleNumberValue value = entry.getValue();
							values.add(new PossibleValueItem(key, value));
						}
					}

				} else if (valueTypeBean.isBoolean()) {
					de.his.zofar.service.valuetype.model.BooleanValueType.PossibleValues possibleValues = ((BooleanValueType) valueTypeBean.getCurrent()).getPossibleValues();
					if(possibleValues != null){
						BooleanValueType.PossibleValues.Entry[] entries = possibleValues.getEntryArray();
						for (BooleanValueType.PossibleValues.Entry entry : entries) {
							final String key = entry.getKey();
							final PossibleBooleanValue value = entry.getValue();
							values.add(new PossibleValueItem(key, value));
						}
					}
				}
			}
		}

		return values;
	}

	public void setValues(List<PossibleValueItem> values) {
		this.values = values;
	}

}

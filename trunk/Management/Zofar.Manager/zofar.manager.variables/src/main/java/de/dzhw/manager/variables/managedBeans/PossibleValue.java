package de.dzhw.manager.variables.managedBeans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.dzhw.manager.common.utils.XmlObjectUtils;
import de.his.zofar.service.valuetype.model.BooleanValueType.PossibleValues.Entry;
import de.his.zofar.service.valuetype.model.SurveyVariable;
import de.his.zofar.service.valuetype.model.possibleValues.PossibleBooleanValue;
import de.his.zofar.service.valuetype.model.possibleValues.PossibleNumberValue;
import de.his.zofar.service.valuetype.model.possibleValues.PossibleStringValue;

@Component("possibleValue")
@Scope("session")
public class PossibleValue implements Serializable {

	private static final long serialVersionUID = -5289285069560285279L;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PossibleValue.class);
	
	private String label;
	private String importCode;

	@Inject
	private ValueType valueTypeBean;

	private de.his.zofar.service.valuetype.model.possibleValues.PossibleValue current;

	public PossibleValue() {
		super();
	}

	@PostConstruct
	private void init() {
		LOGGER.info("init");
	}

	public boolean currentExist(){
		return current != null;
	}

	public de.his.zofar.service.valuetype.model.possibleValues.PossibleValue getCurrent() {
		return current;
	}

	public void setCurrent(de.his.zofar.service.valuetype.model.possibleValues.PossibleValue current) {
		this.current = current;
	}
	
	public boolean isString(){
		return (currentExist() && ((PossibleStringValue.class).isAssignableFrom(this.current.getClass())));
	}
	
	public boolean isNumber(){
		return (currentExist() && ((PossibleNumberValue.class).isAssignableFrom(this.current.getClass())));
	}
	
	public boolean isBoolean(){
		return (currentExist() && ((PossibleBooleanValue.class).isAssignableFrom(this.current.getClass())));
	}
	
	// Property Methods
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getImportCode() {
		return importCode;
	}

	public void setImportCode(String importCode) {
		this.importCode = importCode;
	}
	
	//Use Case Methods
	
	public void modify(ActionEvent event){
		final de.his.zofar.service.valuetype.model.possibleValues.PossibleValue toModify = (de.his.zofar.service.valuetype.model.possibleValues.PossibleValue)event.getComponent().getAttributes().get("ref");
//		LOGGER.info("modify {}",toModify);
		this.setCurrent(toModify);
	}
	
	public void remove(ActionEvent event){
		final Object toRemoveKey = (Object)event.getComponent().getAttributes().get("key");
		final de.his.zofar.service.valuetype.model.possibleValues.PossibleValue toRemoveRef = (de.his.zofar.service.valuetype.model.possibleValues.PossibleValue)event.getComponent().getAttributes().get("ref");
//		LOGGER.info("remove {} {}",toRemoveKey,toRemoveRef);
		valueTypeBean.removePossibleValue(toRemoveKey,toRemoveRef);
		this.setCurrent(null);
	}
	
	public void addLabel(ActionEvent event){
//		LOGGER.info("add label : {} ==> {}",this.getCurrent(),label);
		if(this.getCurrent() != null)this.getCurrent().addLabel(label);
		resetFields();
	}
	
	public void removeLabel(ActionEvent event){
		final de.his.zofar.service.valuetype.model.possibleValues.PossibleValue ref = (de.his.zofar.service.valuetype.model.possibleValues.PossibleValue)event.getComponent().getAttributes().get("ref");
		final String label = (String)event.getComponent().getAttributes().get("label");
//		LOGGER.info("remove label : {} ==> {}",ref,label);
		
		if((ref != null)&&(ref.getLabelArray() != null)){
			final int count = ref.getLabelArray().length;
			String[] labels = ref.getLabelArray();
			for(int a=0;a<count;a++){
				final String tmp = labels[a];
				if(tmp.equals(label))ref.removeLabel(a);
			}
		}
		resetFields();
	}

	public void create(ActionEvent event){
		valueTypeBean.createPossibleValue();
	}
	
	public void importPV(ActionEvent event){
		SchemaType type = null;
		if(valueTypeBean.isBoolean())type = de.his.zofar.service.valuetype.model.BooleanValueType.type;
		if(valueTypeBean.isNumber())type = de.his.zofar.service.valuetype.model.NumberValueType.type;
		if(valueTypeBean.isString())type = de.his.zofar.service.valuetype.model.StringValueType.type;
		LOGGER.info("Type {}",type);
		
		if(type != null){
			XmlObject imported = XmlObjectUtils.getInstance().importXml(type, this.importCode);
			
			LOGGER.info("imported {}",imported);
			
			
			if(valueTypeBean.isBoolean()){
				de.his.zofar.service.valuetype.model.BooleanValueType importedValues = (de.his.zofar.service.valuetype.model.BooleanValueType)imported;
				de.his.zofar.service.valuetype.model.BooleanValueType.PossibleValues.Entry[] entries = importedValues.getPossibleValues().getEntryArray();
				if(entries != null){
					final int count = entries.length;
					for(int a = 0;a<count;a++){
						final de.his.zofar.service.valuetype.model.BooleanValueType.PossibleValues.Entry entry = entries[a];
						LOGGER.info("entry {}",entry);
						final de.his.zofar.service.valuetype.model.BooleanValueType.PossibleValues.Entry newEntry = ((de.his.zofar.service.valuetype.model.BooleanValueType)valueTypeBean.getCurrent()).getPossibleValues().addNewEntry();
						newEntry.setKey(entry.getKey());
						newEntry.setValue(entry.getValue());
					}
				}
			}
			if(valueTypeBean.isNumber()){
				de.his.zofar.service.valuetype.model.NumberValueType importedValues = (de.his.zofar.service.valuetype.model.NumberValueType)imported;
				de.his.zofar.service.valuetype.model.NumberValueType.PossibleValues.Entry[] entries = importedValues.getPossibleValues().getEntryArray();
				if(entries != null){
					final int count = entries.length;
					for(int a = 0;a<count;a++){
						final de.his.zofar.service.valuetype.model.NumberValueType.PossibleValues.Entry entry = entries[a];
						LOGGER.info("entry {}",entry);
						final de.his.zofar.service.valuetype.model.NumberValueType.PossibleValues.Entry newEntry = ((de.his.zofar.service.valuetype.model.NumberValueType)valueTypeBean.getCurrent()).getPossibleValues().addNewEntry();
						newEntry.setKey(entry.getKey());
						newEntry.setValue(entry.getValue());
					}
				}
			}
			if(valueTypeBean.isString()){
				de.his.zofar.service.valuetype.model.StringValueType importedValues = (de.his.zofar.service.valuetype.model.StringValueType)imported;
				de.his.zofar.service.valuetype.model.StringValueType.PossibleValues.Entry[] entries = importedValues.getPossibleValues().getEntryArray();
				if(entries != null){
					final int count = entries.length;
					for(int a = 0;a<count;a++){
						final de.his.zofar.service.valuetype.model.StringValueType.PossibleValues.Entry entry = entries[a];
						LOGGER.info("entry {}",entry);
						final de.his.zofar.service.valuetype.model.StringValueType.PossibleValues.Entry newEntry = ((de.his.zofar.service.valuetype.model.StringValueType)valueTypeBean.getCurrent()).getPossibleValues().addNewEntry();
						newEntry.setKey(entry.getKey());
						newEntry.setValue(entry.getValue());
					}
				}
			}
			resetFields();
			valueTypeBean.save(event);
//			LOGGER.info("import {}",valueTypeBean.getCurrent());
		}
	}
	
	public void save(ActionEvent event) {
		LOGGER.info("save {}",current);
	}

	private void resetFields(){
		this.label = "";
		this.importCode = "";
	}

}

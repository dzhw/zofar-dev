package de.dzhw.manager.variables.managedBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.dzhw.manager.common.utils.XmlObjectUtils;
import de.dzhw.manager.variables.facades.ValueTypeServiceFacade;
import de.his.zofar.service.valuetype.model.BooleanValueType;
import de.his.zofar.service.valuetype.model.NumberValueType;
import de.his.zofar.service.valuetype.model.StringValueType;
import de.his.zofar.service.valuetype.model.possibleValues.PossibleBooleanValue;
import de.his.zofar.service.valuetype.model.possibleValues.PossibleNumberValue;
import de.his.zofar.service.valuetype.model.possibleValues.PossibleStringValue;
import de.his.zofar.service.valuetype.util.Converter;
import de.his.zofar.service.valuetype.util.Generator;

@Component("valueType")
@Scope("session")
public class ValueType implements Serializable {

	private static final long serialVersionUID = 3241157041948455095L;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ValueType.class);

	private final ValueTypeServiceFacade valueTypeService = new ValueTypeServiceFacade();

	private de.his.zofar.service.valuetype.model.ValueType current;
	
	private enum PossibleType{
		All(de.his.zofar.service.valuetype.model.ValueType.class),
		BooleanValueType(BooleanValueType.class),
		NumberValueType(NumberValueType.class),
		StringValueType(StringValueType.class);
		
		private Class<? extends de.his.zofar.service.valuetype.model.ValueType> clazz;

		private PossibleType(Class<? extends de.his.zofar.service.valuetype.model.ValueType> clazz) {
			this.clazz = clazz;
		}

		public Class<? extends de.his.zofar.service.valuetype.model.ValueType> getClazz() {
			return clazz;
		}

		public void setClazz(Class<? extends de.his.zofar.service.valuetype.model.ValueType> clazz) {
			this.clazz = clazz;
		}
	}
	
	private String currentPossibleType;
	private List<de.his.zofar.service.valuetype.model.ValueType> loadedPossibleTypes;
	
	// Common Creation Fields
	private String identifier;
	private String description;
	private String measurementLevel;
	
	// Special Creation Fields for String ValueType
	private int length;
	private boolean canBeEmpty;
	
	// Special Creation Fields for Number ValueType
	private int decimalPlaces;
	private long minimum;
	private long maximum;
	

	public ValueType() {
		super();
	}

	@PostConstruct
	private void init() {
		LOGGER.info("init");
		resetFields();
		identifier = "defaultbooleantype";
		loadedPossibleTypes = new ArrayList<de.his.zofar.service.valuetype.model.ValueType>();
	}

	public de.his.zofar.service.valuetype.model.ValueType getCurrent() {
		return current;
	}

	public void setCurrent(
			de.his.zofar.service.valuetype.model.ValueType current) {
		this.current = current;
	}

	public String getCurrentPossibleType() {
		return currentPossibleType;
	}

	public void setCurrentPossibleType(String currentPossibleType) {
		this.currentPossibleType = currentPossibleType;
	}

	public List<SelectItem> getPossibleTypes(){
		List<SelectItem> items = new ArrayList<SelectItem>();
	    for(PossibleType g: PossibleType.values()) {
	    	items.add(new SelectItem(g.getClazz().getName(),g.name()));
	    }
		return items;
	}
	
	public  void setPossibleTypes(List<SelectItem> items){
	}
	
	public List<de.his.zofar.service.valuetype.model.ValueType> getLoadedPossibleTypes() {
		return loadedPossibleTypes;
	}

	public void setLoadedPossibleTypes(List<de.his.zofar.service.valuetype.model.ValueType> loadedPossibleTypes) {
//		this.loadedPossibleTypes = loadedPossibleTypes;
	}
	
	// Getter and Setter for Creation Fields
	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMeasurementLevel() {
		return measurementLevel;
	}

	public void setMeasurementLevel(String measurementLevel) {
		this.measurementLevel = measurementLevel;
	}

	public List<String> getMeasurementLevels() {
		return Converter.MEASUREMENTLEVELS;
	}

	public void setMeasurementLevels(List<String> measurementLevels) {
//		this.measurementLevels = measurementLevels;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public boolean isCanBeEmpty() {
		return canBeEmpty;
	}

	public void setCanBeEmpty(boolean canBeEmpty) {
		this.canBeEmpty = canBeEmpty;
	}

	public int getDecimalPlaces() {
		return decimalPlaces;
	}

	public void setDecimalPlaces(int decimalPlaces) {
		this.decimalPlaces = decimalPlaces;
	}

	public long getMinimum() {
		return minimum;
	}

	public void setMinimum(long minimum) {
		this.minimum = minimum;
	}

	public long getMaximum() {
		return maximum;
	}

	public void setMaximum(long maximum) {
		this.maximum = maximum;
	}
	
	
	
	public boolean currentExist(){
		return current != null;
	}
	
	public boolean isString(){
		return (currentExist() && ((StringValueType.class).isAssignableFrom(this.current.getClass())));
	}
	
	public boolean isNumber(){
		return (currentExist() && ((NumberValueType.class).isAssignableFrom(this.current.getClass())));
	}
	
	public boolean isBoolean(){
		return (currentExist() && ((BooleanValueType.class).isAssignableFrom(this.current.getClass())));
	}
	
	public String getCurrentMeasurementLevel(){
		return Converter.getInstance().convertMeasurementLevel(this.current.getMeasurementLevel());
	}
	
	public void setCurrentMeasurementLevel(final String level){
		this.current.setMeasurementLevel(Converter.getInstance().convertMeasurementLevel(level));
	}
	
	public int getCurrentMaximum(){
		if(current == null)return 0;
		if(!isNumber())return 0;
		return ((Long)((NumberValueType)this.current).getMaximum()).intValue();
	}
	
	public int getCurrentMinimum(){
		if(current == null)return 0;
		if(!isNumber())return 0;
		return ((Long)((NumberValueType)this.current).getMinimum()).intValue();
	}
	
	public void setCurrentMaximum(final int maximum){
	}
	
	public void setCurrentMinimum(final int minimum){
	}
	

	public void removePossibleValue(final Object key, final de.his.zofar.service.valuetype.model.possibleValues.PossibleValue toRemove){
		if(isString()){
			StringValueType.PossibleValues.Entry[] entries = ((StringValueType)this.current).getPossibleValues().getEntryArray();
			final int count = entries.length;
			for(int a=0;a<count;a++){
				StringValueType.PossibleValues.Entry entry = entries[a];
				if((key.equals(entry.getKey()))&&(toRemove.equals(entry.getValue()))){
					((StringValueType)this.current).getPossibleValues().removeEntry(a);
				}
			}
		}
		else if(isNumber()){
			NumberValueType.PossibleValues.Entry[] entries = ((NumberValueType)this.current).getPossibleValues().getEntryArray();
			final int count = entries.length;
			for(int a=0;a<count;a++){
				NumberValueType.PossibleValues.Entry entry = entries[a];
				if((key.equals(entry.getKey()))&&(toRemove.equals(entry.getValue()))){
					((NumberValueType)this.current).getPossibleValues().removeEntry(a);
				}
			}
		}
		else if(isBoolean()){
			BooleanValueType.PossibleValues.Entry[] entries = ((BooleanValueType)this.current).getPossibleValues().getEntryArray();			
			final int count = entries.length;
			for(int a=0;a<count;a++){
				BooleanValueType.PossibleValues.Entry entry = entries[a];
				if((key.equals(entry.getKey()))&&(toRemove.equals(entry.getValue()))){
					((BooleanValueType)this.current).getPossibleValues().removeEntry(a);
				}
			}
		}
	}
	
	public void createPossibleValue(){
		if(isString()){
			de.his.zofar.service.valuetype.model.StringValueType.PossibleValues possibleValues = ((StringValueType) this.current).getPossibleValues();
			if (possibleValues == null) possibleValues = ((StringValueType) this.current).addNewPossibleValues();
			de.his.zofar.service.valuetype.model.StringValueType.PossibleValues.Entry entry = possibleValues.addNewEntry();
			entry.setKey("New Possible String Value");
			PossibleStringValue value = entry.addNewValue();
		}
		else if(isNumber()){
			de.his.zofar.service.valuetype.model.NumberValueType.PossibleValues possibleValues = ((NumberValueType) this.current).getPossibleValues();
			if (possibleValues == null) possibleValues = ((NumberValueType) this.current).addNewPossibleValues();
			de.his.zofar.service.valuetype.model.NumberValueType.PossibleValues.Entry entry = possibleValues.addNewEntry();
			entry.setKey(0L);
			PossibleNumberValue value = entry.addNewValue();
		}
		else if(isBoolean()){
			de.his.zofar.service.valuetype.model.BooleanValueType.PossibleValues possibleValues = ((BooleanValueType) this.current).getPossibleValues();
			if (possibleValues == null) possibleValues = ((BooleanValueType) this.current).addNewPossibleValues();
			de.his.zofar.service.valuetype.model.BooleanValueType.PossibleValues.Entry entry = possibleValues.addNewEntry();
			entry.setKey("New Possible Boolean Value");
			PossibleBooleanValue value = entry.addNewValue();
		}
	}

	
	// Use Case Methods
	public void load(ActionEvent event) {
		try{
			this.current = valueTypeService.loadByIdentifier(identifier);
			resetFields();
		}
		catch(Exception e){
			final FacesMessage facesMessage = new FacesMessage(e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
			this.current = null;
		}
	}
	
	public void loadByType(ActionEvent event) {
		Class clazz = null;
		try {
			 clazz = this.getClass().getClassLoader().loadClass(this.getCurrentPossibleType());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		loadedPossibleTypes.clear();
		loadedPossibleTypes.addAll(valueTypeService.loadByType(clazz));
		LOGGER.info("possible Types ({}) : {}",clazz,loadedPossibleTypes.size());
	}
	
	public void loadTableItem(ActionEvent event) {
		final de.his.zofar.service.valuetype.model.ValueType toLoadRef = (de.his.zofar.service.valuetype.model.ValueType)event.getComponent().getAttributes().get("ref");
		this.current = toLoadRef;
	}
	
	public void copyTableItem(ActionEvent event) {
		final de.his.zofar.service.valuetype.model.ValueType toLoadRef = (de.his.zofar.service.valuetype.model.ValueType)event.getComponent().getAttributes().get("ref");
		final de.his.zofar.service.valuetype.model.ValueType copy = XmlObjectUtils.getInstance().copy(toLoadRef);
		copy.setIdentifier("Copy of "+toLoadRef.getIdentifier());
		this.current = copy;
		LOGGER.info("copy {}",this.current);
	}
	
	public void removeTableItem(ActionEvent event) {
		final de.his.zofar.service.valuetype.model.ValueType toRemoveRef = (de.his.zofar.service.valuetype.model.ValueType)event.getComponent().getAttributes().get("ref");
		valueTypeService.removeValueType(toRemoveRef);
		loadByType(event);
	}

	public void createString(ActionEvent event){
		this.current = valueTypeService.createStringValueType(identifier, description, measurementLevel, length, canBeEmpty);
		resetFields();
	}
	
	public void createNumber(ActionEvent event){
		this.current = valueTypeService.createNumberValueType(identifier, description, measurementLevel, decimalPlaces, minimum, maximum);
		resetFields();
	}
	
	public void createBoolean(ActionEvent event){
		this.current = valueTypeService.createBooleanValueType(identifier, description, measurementLevel);
		resetFields();
	}

	public void save(ActionEvent event) {
		this.current = valueTypeService.save(current);
		resetFields();
	}
	
	private void resetFields(){
		this.identifier = "";
		this.description = "";
		this.measurementLevel = "";
		this.length = 0;
		this.canBeEmpty = false;;
		this.decimalPlaces = 0;
		this.minimum = 0;
		this.maximum = 0;
	}
}

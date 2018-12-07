package de.dzhw.manager.variables.managedBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.xmlbeans.SchemaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import de.dzhw.manager.common.utils.XmlObjectUtils;
import de.dzhw.manager.modules.interfaces.Module;
import de.dzhw.manager.variables.facades.VariableServiceFacade;
import de.his.zofar.service.valuetype.model.PanelVariable;
import de.his.zofar.service.valuetype.model.Question;
import de.his.zofar.service.valuetype.model.SurveyVariable;
import de.his.zofar.service.valuetype.util.Generator;
 
@Component("variables")
@Scope("session")
public class Variables implements Module,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4450761253374886307L;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(Variables.class);

	private final VariableServiceFacade variableService = new VariableServiceFacade();

	private de.his.zofar.service.valuetype.model.Variable current;
	
	private enum PossibleType{
		All(de.his.zofar.service.valuetype.model.Variable.class),
		PanelVariable(de.his.zofar.service.valuetype.model.PanelVariable.class),
		SurveyVariable(de.his.zofar.service.valuetype.model.SurveyVariable.class);
		
		private Class<? extends de.his.zofar.service.valuetype.model.Variable> clazz;

		private PossibleType(Class<? extends de.his.zofar.service.valuetype.model.Variable> clazz) {
			this.clazz = clazz;
		}

		public Class<? extends de.his.zofar.service.valuetype.model.Variable> getClazz() {
			return clazz;
		}

		public void setClazz(Class<? extends de.his.zofar.service.valuetype.model.Variable> clazz) {
			this.clazz = clazz;
		}
	}
	
//	private enum PossibleSchema{	
////		All(de.his.zofar.service.valuetype.model.Variable.type),
//		Panel(de.his.zofar.service.valuetype.model.PanelVariable.type),
//		Survey(de.his.zofar.service.valuetype.model.SurveyVariable.type);
//		
//		private SchemaType schema;
//
//		private PossibleSchema(SchemaType schema) {
//			this.schema = schema;
//		}
//
//		public SchemaType getSchema() {
//			return schema;
//		}
//
//		public void setClazz(SchemaType schema) {
//			this.schema = schema;
//		}
//	}
	
	private String currentPossibleType;
	private List<de.his.zofar.service.valuetype.model.Variable> loadedPossibleTypes;
	
	// Getter and Setter for Creation Fields
	private String uuid;
	private String name;	
	private String label;
	private de.his.zofar.service.valuetype.model.ValueType valueTypeElement;
	private Question question;
	
	//Fields for Import
	private String importCode;
//	private SchemaType importSchema;
//	private List<PossibleSchema> loadedPossibleSchema;
	
	List<GrantedAuthority> roles = null;

	public Variables() {
		super();
	}

	@PostConstruct
	private void init() {
		LOGGER.info("init");
		roles = new ArrayList<GrantedAuthority>();
		roles.add(new SimpleGrantedAuthority("ROLE_VARIABLES"));
		loadedPossibleTypes = new ArrayList<de.his.zofar.service.valuetype.model.Variable>();
	}
	
	public de.his.zofar.service.valuetype.model.Variable getCurrent() {
		return current;
	}

	public void setCurrent(
			de.his.zofar.service.valuetype.model.Variable current) {
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
	
	public List<de.his.zofar.service.valuetype.model.Variable> getLoadedPossibleTypes() {
		return loadedPossibleTypes;
	}

	public void setLoadedPossibleTypes(List<de.his.zofar.service.valuetype.model.Variable> loadedPossibleTypes) {
//		this.loadedPossibleTypes = loadedPossibleTypes;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public de.his.zofar.service.valuetype.model.ValueType getValueTypeElement() {
		return valueTypeElement;
	}

	public void setValueTypeElement(
			de.his.zofar.service.valuetype.model.ValueType valueTypeElement) {
		LOGGER.info("set Value Type : {}",this.valueTypeElement);
		this.valueTypeElement = valueTypeElement;
	}

	public String getImportCode() {
		return importCode;
	}

	public void setImportCode(String importCode) {
		LOGGER.info("set Import Code : {}",importCode);
		this.importCode = importCode;
	}

	public boolean currentExist(){
		return current != null;
	}
	
	public boolean isSurvey(){
		return (currentExist() && ((de.his.zofar.service.valuetype.model.SurveyVariable.class).isAssignableFrom(this.current.getClass())));
	}
	
	public boolean isPanel(){
		return (currentExist() && ((de.his.zofar.service.valuetype.model.PanelVariable.class).isAssignableFrom(this.current.getClass())));
	}
	
	// Use Case Methods
	public void createPanelVariable(ActionEvent event){
		this.current = variableService.createPanelVariable(this.getUuid(), this.getName(),this.getLabel(),this.getValueTypeElement());
		save(event);
		LOGGER.info("create Panel Variable {}",this.current);
	}
	
	public void createSurveyVariable(ActionEvent event){
		this.current = variableService.createSurveyVariable(this.getUuid(), this.getName(),this.getLabel(),this.getValueTypeElement(), this.getQuestion());
		save(event);
		LOGGER.info("create Survey Variable {}",this.current);
	}
	
	public void importPanelVariable(ActionEvent event){
		this.current = XmlObjectUtils.getInstance().importXml(PanelVariable.type, this.importCode);
		save(event);
		LOGGER.info("import Variable {}",this.current);
	}
	
	public void importSurveyVariable(ActionEvent event){
		this.current = XmlObjectUtils.getInstance().importXml(SurveyVariable.type, this.importCode);
		save(event);
		LOGGER.info("import Variable {}",this.current);
	}
	
	
	public void loadByType(ActionEvent event) {
		Class clazz = null;
		try {
			 clazz = this.getClass().getClassLoader().loadClass(this.getCurrentPossibleType());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		loadedPossibleTypes.clear();
		loadedPossibleTypes.addAll(variableService.loadByType(clazz));
	}
	
	public void loadTableItem(ActionEvent event) {
		final de.his.zofar.service.valuetype.model.Variable toLoadRef = (de.his.zofar.service.valuetype.model.Variable)event.getComponent().getAttributes().get("ref");
		this.current = toLoadRef;
		LOGGER.info("load {}",this.current);
	}
	
	public void copyTableItem(ActionEvent event) {
		final de.his.zofar.service.valuetype.model.Variable toLoadRef = (de.his.zofar.service.valuetype.model.Variable)event.getComponent().getAttributes().get("ref");
		final de.his.zofar.service.valuetype.model.Variable copy = XmlObjectUtils.getInstance().copy(toLoadRef);
		copy.setName("Copy of "+toLoadRef.getName());
		copy.setUuid(Generator.getInstance().createUUID());
		this.current = copy;
		LOGGER.info("copy {}",this.current);
	}
	
	public void loadValueType(ActionEvent event) {
		final de.his.zofar.service.valuetype.model.ValueType toLoadRef = (de.his.zofar.service.valuetype.model.ValueType)event.getComponent().getAttributes().get("ref");
		this.valueTypeElement = toLoadRef;
		LOGGER.info("Loaded Value Type : {}",this.valueTypeElement.getIdentifier());
	}
	
	public void modifyValueType(ActionEvent event) {
		final de.his.zofar.service.valuetype.model.ValueType toLoadRef = (de.his.zofar.service.valuetype.model.ValueType)event.getComponent().getAttributes().get("ref");
		if(this.current != null)this.current.setValueType(toLoadRef);
		LOGGER.info("Loaded Value Type : {}",this.current.getValueType().getIdentifier());
	}
	
	public void loadQuestion(ActionEvent event) {
		final de.his.zofar.service.valuetype.model.Question toLoadRef = (de.his.zofar.service.valuetype.model.Question)event.getComponent().getAttributes().get("ref");
		this.question = toLoadRef;
		LOGGER.info("Loaded Question : {}",this.question.getUuid());
	}
	
	public void modifyQuestion(ActionEvent event) {
		final de.his.zofar.service.valuetype.model.Question toLoadRef = (de.his.zofar.service.valuetype.model.Question)event.getComponent().getAttributes().get("ref");
		this.question = toLoadRef;
		if((this.current != null)&&(SurveyVariable.class).isAssignableFrom(this.current.getClass()))((SurveyVariable)this.current).setQuestion(toLoadRef);
		LOGGER.info("Loaded Question : {}",((SurveyVariable)this.current).getQuestion().getUuid());
	}
	
	public void removeTableItem(ActionEvent event) {
		final de.his.zofar.service.valuetype.model.Variable toRemoveRef = (de.his.zofar.service.valuetype.model.Variable)event.getComponent().getAttributes().get("ref");
		LOGGER.info("remove {}",toRemoveRef);
		variableService.removeVariable(toRemoveRef);
		loadByType(event);
	}
	
	public void save(ActionEvent event) {
		LOGGER.info("save {}",this.current);
		this.current = variableService.save(current);
		resetFields();
	}
	
	private void resetFields(){
		this.uuid="";
		this.name="";
		this.label="";
		this.valueTypeElement=null;
		this.question=null;
		this.importCode="";
//		this.importSchema=null;
		this.currentPossibleType = "";
	}
	
	
	// Module Methods

	@Override
	public Collection<GrantedAuthority> getRoles() {
		return roles;
	}

	@Override
	public void relocate(final String viewId) {
//		LOGGER.info("relocate : ({}) {}",ModuleUtils.getInstance().getApplicationPath(),viewId);	
		resetFields();
//		ModuleUtils.getInstance().redirectTo(redirect);
		if(viewId.endsWith("variables/valuetypes/load")){
			
		}
	}
	
	
}

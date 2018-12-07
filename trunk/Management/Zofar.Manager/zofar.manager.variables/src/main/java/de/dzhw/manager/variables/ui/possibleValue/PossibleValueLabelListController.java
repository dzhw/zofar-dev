package de.dzhw.manager.variables.ui.possibleValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.dzhw.manager.variables.managedBeans.PossibleValue;
@Component("uiPossibleValueLabelListController")
@Scope("request")
public class PossibleValueLabelListController implements Serializable {


	private static final long serialVersionUID = -3752954882018454676L;
	
	@Inject
	private PossibleValue possibleValue;
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(PossibleValueLabelListController.class);
	
	private List<PossibleValueLabel> labels;
	
	public class PossibleValueLabel {

		private String label;
		private int labelIndex;
		private de.his.zofar.service.valuetype.model.possibleValues.PossibleValue ref;

		public PossibleValueLabel(int labelIndex, de.his.zofar.service.valuetype.model.possibleValues.PossibleValue ref) {
			super();
			this.labelIndex = labelIndex;
			this.ref = ref;
		}

		public String getLabel() {
			if(this.getRef() != null){
				final String[] labelArray = this.getRef().getLabelArray();
				if((labelArray != null)&&(labelArray.length>this.labelIndex)){
					return labelArray[this.labelIndex];
				}
			}
			return "";
		}

		public void setLabel(String label) {
			LOGGER.info("new Label : {} ==> {}",this.getRef(),label);
			if(this.getRef() != null){
				final String[] labelArray = this.getRef().getLabelArray();
				if((labelArray != null)&&(labelArray.length>this.labelIndex)){
					labelArray[this.labelIndex] = label;
				}
				this.getRef().setLabelArray(labelArray);
			}
		}

		public de.his.zofar.service.valuetype.model.possibleValues.PossibleValue getRef() {
			return ref;
		}

		public void setRef(
				de.his.zofar.service.valuetype.model.possibleValues.PossibleValue ref) {
			this.ref = ref;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((label == null) ? 0 : label.hashCode());
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
			PossibleValueLabel other = (PossibleValueLabel) obj;
			if (label == null) {
				if (other.label != null)
					return false;
			} else if (!label.equals(other.label))
				return false;
			if (ref == null) {
				if (other.ref != null)
					return false;
			} else if (!ref.equals(other.ref))
				return false;
			return true;
		}		
	};

	public PossibleValueLabelListController() {
		super();
	}
	
	@PostConstruct
	private void init() {
//		LOGGER.info("init");
	}
	
	public List<PossibleValueLabel> getLabels(){
		LOGGER.info("get Labels");
		if(labels == null){
			labels = new ArrayList<PossibleValueLabel>();
			if(possibleValue == null)return labels;
			if(possibleValue.getCurrent() == null)return labels;
			if(possibleValue.getCurrent().getLabelArray() == null)return labels;
			final String[] labelArray = possibleValue.getCurrent().getLabelArray();
			final int labelCount = labelArray.length;
			for(int a = 0;a<labelCount;a++){
				labels.add(new PossibleValueLabel(a,possibleValue.getCurrent()));
			}
		}
		return labels;
	}
	
	private final static String[] dummy = new String[0];
	
	public void setLabels(final List<PossibleValueLabel> labels){
		LOGGER.info("set Labels {}",labels);
//		if(possibleValue == null)return;
//		if(possibleValue.getCurrent() == null)return;
//		List<String> back = new ArrayList<String>();
//		for(PossibleValueLabel label:labels){
//			back.add(label.getLabel());
//		}
//		
//		LOGGER.info("new labels : {}",back);
//		
//		possibleValue.getCurrent().setLabelArray((String[])back.toArray(dummy));
	}

}

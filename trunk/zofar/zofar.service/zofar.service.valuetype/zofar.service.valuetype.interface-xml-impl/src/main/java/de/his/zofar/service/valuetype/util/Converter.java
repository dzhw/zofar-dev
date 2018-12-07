package de.his.zofar.service.valuetype.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Converter implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8629063080358508707L;
	private static final Converter INSTANCE = new Converter();
	
	public static List<String> MEASUREMENTLEVELS;

	private Converter() {
		super();
		MEASUREMENTLEVELS = new ArrayList<String>();
		MEASUREMENTLEVELS.add("ORDINAL");
		MEASUREMENTLEVELS.add("NOMINAL");
		MEASUREMENTLEVELS.add("INTERVAL");
		MEASUREMENTLEVELS.add("RATIO");
	}
	
	public static Converter getInstance(){
		return INSTANCE;
	}
	
	// Converters for MeasurementLevel
	public de.his.zofar.service.valuetype.model.ValueType.MeasurementLevel.Enum convertMeasurementLevel(final String level){
		if(level == null)return null;
		if(level.equals("INTERVAL"))return de.his.zofar.service.valuetype.model.ValueType.MeasurementLevel.INTERVAL;
		if(level.equals("NOMINAL"))return de.his.zofar.service.valuetype.model.ValueType.MeasurementLevel.NOMINAL;
		if(level.equals("ORDINAL"))return de.his.zofar.service.valuetype.model.ValueType.MeasurementLevel.ORDINAL;
		if(level.equals("RATIO"))return de.his.zofar.service.valuetype.model.ValueType.MeasurementLevel.RATIO;
		return null;
	}
	
	public String convertMeasurementLevel(final de.his.zofar.service.valuetype.model.ValueType.MeasurementLevel.Enum level){
		if(level == null)return null;
		if(level == de.his.zofar.service.valuetype.model.ValueType.MeasurementLevel.INTERVAL) return "INTERVAL";
		if(level == de.his.zofar.service.valuetype.model.ValueType.MeasurementLevel.NOMINAL) return "NOMINAL";
		if(level == de.his.zofar.service.valuetype.model.ValueType.MeasurementLevel.ORDINAL) return "ORDINAL";
		if(level == de.his.zofar.service.valuetype.model.ValueType.MeasurementLevel.RATIO) return "RATIO";
		return null;
	}

}

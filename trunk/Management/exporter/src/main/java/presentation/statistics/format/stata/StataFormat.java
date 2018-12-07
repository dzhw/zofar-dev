package presentation.statistics.format.stata;

import java.util.HashMap;
import java.util.Map;

import model.ValueEntry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import presentation.statistics.format.AbstractFormat;
import service.statistics.StatisticService.TYPE;
import utils.StringUtils;

public class StataFormat extends AbstractFormat {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(StataFormat.class);

	private static StataFormat INSTANCE;
	private static final String BREAK = "\n" ;
	private final Map<String,String> typeMap;
	
	public enum OUTPUT{
		instruction,data
	}

	private StataFormat() {
		super();
		typeMap = new HashMap<String,String>();
		typeMap.put("singleChoice", "int");
		typeMap.put("multipleChoice", "byte");
		typeMap.put("open", "str244");
	}

	public static StataFormat getInstance() {
		if (INSTANCE == null)
			INSTANCE = new StataFormat();
		return INSTANCE;
	}
	

	@Override
	public Object format(Map<TYPE, Object> structure) {
		return format(structure,null);
	}

	@Override
	public Object format(Map<TYPE, Object> structure, final Map<String,String> mapping) {
		if (structure == null)
			return null;
		if (structure.isEmpty())
			return null;

//		@SuppressWarnings("unchecked")
		//final Map<TYPE,Object> structure = (Map<TYPE,Object>) data.get(TYPE.instruction);
		StringBuffer doBuffer = new StringBuffer();
		if (structure != null) {
			doBuffer = writeDoHeader(doBuffer);
			
			@SuppressWarnings("unchecked")
			final Map<String,String> types = (Map<String, String>) structure.get(TYPE.types);
			
			if(types != null){
				doBuffer.append("//Variablenmetadaten"+BREAK);
				doBuffer.append("infile"+BREAK);
				for(final Map.Entry<String,String> entry : types.entrySet()){
					doBuffer.append(typeMap.get(entry.getValue())+" "+entry.getKey()+BREAK);
				}
				doBuffer.append("using data.csv;"+BREAK);
			}

			@SuppressWarnings("unchecked")
			final Map<String, Map<String, ValueEntry>> options = (Map<String, Map<String, ValueEntry>>) structure.get(TYPE.options);
			if(options != null){
				doBuffer.append(BREAK);
				doBuffer.append("//Labelsets"+BREAK);
				for(final Map.Entry<String, Map<String, ValueEntry>> entry : options.entrySet()){
					final String variableName = entry.getKey();
					final Map<String, ValueEntry> optionSet = entry.getValue();
					if((optionSet != null)&&(!optionSet.isEmpty())){
						doBuffer.append("label define "+variableName+"_labelset"+BREAK);
						for(final Map.Entry<String, ValueEntry> option : optionSet.entrySet()){
							final String label = StringUtils.getInstance().cleanedString(option.getValue().getLabel());
							Object value = option.getValue().getValue();
							if((mapping != null)&&(mapping.containsKey(value+"")))value=mapping.get(value+"");
							doBuffer.append(value+" \""+label+"\""+BREAK);
						}
						doBuffer.append(";"+BREAK);
						doBuffer.append("label values "+variableName+" "+variableName+"_labelset;"+BREAK);
						doBuffer.append(BREAK);
					}
				}
			}
		}
		
//		@SuppressWarnings("unchecked")
//		final Set<ParticipantType> dataSet = (Set<ParticipantType>) data.get(TYPE.data);
//		StringBuffer csvBuffer = new StringBuffer();
//		if(dataSet != null){
//			csvBuffer = writeDoHeader(csvBuffer);
//			csvBuffer.append(CsvFormat.getInstance().format(dataSet,',',' '));
//		}
//		
//		Map<OUTPUT,String> back = new HashMap<OUTPUT,String>();
//		back.put(OUTPUT.instruction, doBuffer.toString());
//		back.put(OUTPUT.data, csvBuffer.toString());
		
		return doBuffer.toString();
	}
	
	private StringBuffer writeDoHeader(final StringBuffer buffer){
		buffer.append("// Dieses Skript wurde erzeugt vom Zofar Online Survey System"+BREAK);
		buffer.append("version 10"+BREAK);
		buffer.append("#delimit ;"+BREAK);
		buffer.append("clear;"+BREAK);
		buffer.append(BREAK);
		return buffer;
	}
}

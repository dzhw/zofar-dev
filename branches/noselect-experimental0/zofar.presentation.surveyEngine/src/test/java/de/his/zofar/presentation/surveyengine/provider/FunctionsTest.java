package de.his.zofar.presentation.surveyengine.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.AbstractAnswerBean;
import de.his.zofar.presentation.surveyengine.SingleChoiceAnswerOptionTypeBean;
import de.his.zofar.presentation.surveyengine.StringValueTypeBean;
import de.his.zofar.presentation.surveyengine.controller.SessionController;
import de.his.zofar.presentation.surveyengine.provider.mock.SessionControllerMock;

public class FunctionsTest {
	
	private FunctionProvider functions = null;
	private SessionController session = null;
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(FunctionsTest.class);

	@Before
	public void setUp() throws Exception {
		functions = new FunctionProvider();
		session = new SessionControllerMock();
//		session.setValueForVariablename("var1", "1");
//		session.setValueForVariablename("var2", "2");
//		session.setValueForVariablename("var3", "-97");
//		session.setValueForVariablename("var4", "blub");
//		session.setValueForVariablename("var5", "blau");
//		session.setValueForVariablename("var6", "true");
//		session.setValueForVariablename("var7", "23,5");
	}
	
	
	private SingleChoiceAnswerOptionTypeBean generateSingleChoiceVariable(String name,String alternative,Map<String,Object> options){
		SingleChoiceAnswerOptionTypeBean var = new SingleChoiceAnswerOptionTypeBean(session,name);
		var.setAlternative(name+" alternative");
		var.setOptionValues(options);
		return var;
	}
	
	private StringValueTypeBean generateOpenVariable(String name){
		StringValueTypeBean var = new StringValueTypeBean(session,name);
		var.setAlternative(name+" alternative");
		return var;
	}
	
	@Test
	public void list() {
		List<Object> list = functions.list("Element1",5,4L);
		List<? extends Object> reference = Arrays.asList("Element1",5,new Long(4));
		Assert.assertEquals(reference, list);
	}	
	
	@Test
	public void reverseList() {
		List<?> list = functions.list("Element1",5,4L);
		list = functions.reverseList(list);
		List<?> reference = Arrays.asList(new Long(4),5,"Element1");
		Assert.assertEquals(reference, list);
	}	
	
	@Test
	public void valueOf() {
		session.setValueForVariablename("var4", "blub");
		AbstractAnswerBean variable = generateOpenVariable("var4");
		Assert.assertEquals("blub", functions.valueOf(variable));
	}	
	
	@Test
	@Ignore
	public void optionsOf() {
		session.setValueForVariablename("var1", "1");
		Map<String,Object> options = new LinkedHashMap<String,Object>();
		options.put("1", "1");
		options.put("2", "2");
		options.put("3", "3");
		options.put("4", "4");
		options.put("missing1", "-97");
		
		Map<String,Object> reference = new LinkedHashMap<String,Object>();
		reference.put("1", "1");
		reference.put("2", "2");
		reference.put("3", "3");
		reference.put("4", "4");
		
		AbstractAnswerBean variable = generateSingleChoiceVariable("var1","var1 alternative",options);
		Assert.assertEquals(new ArrayList<Object>(reference.values()), functions.optionsOf(variable));
	}	
	
	@Test
	@Ignore
	public void missingsOf() {
		session.setValueForVariablename("var1", "1");
		Map<String,Object> options = new HashMap<String,Object>();
		options.put("1", "1");
		options.put("2", "2");
		options.put("3", "3");
		options.put("4", "4");
		options.put("missing1", "-97");
		
		Map<String,Object> missings = new HashMap<String,Object>();
//		missings.put("missing1", "-97");
		missings.put("missing2", "");
		AbstractAnswerBean variable = generateSingleChoiceVariable("var1","var1 alternative",options);
		Assert.assertEquals(new ArrayList<Object>(missings.values()), functions.missingsOf(variable));
	}	
	
	@Test
	@Ignore
	public void isMissing() {
		session.setValueForVariablename("var1", "1");
		session.setValueForVariablename("var2", "2");
		Map<String,Object> options = new HashMap<String,Object>();
		options.put("1", "1");
		options.put("2", "2");
		options.put("3", "3");
		options.put("4", "4");
		options.put("missing1", "-97");
		options.put("missing2", "-96");
		AbstractAnswerBean variable1 = generateSingleChoiceVariable("var1","var1 alternative",options);
		variable1.setStringValue("missing2");
		
		AbstractAnswerBean variable2 = generateSingleChoiceVariable("var2","var2 alternative",options);
		variable2.setStringValue("missing1");
		
		List<AbstractAnswerBean> vars = new ArrayList<AbstractAnswerBean>();
		vars.add(variable1);
		vars.add(variable2);
		Assert.assertEquals(new Integer(2), functions.missingCount(vars));
	}	
	
	@Test
	@Ignore
	public void reverseValue() {
		session.setValueForVariablename("var1", "1");
		Map<String,Object> options = new LinkedHashMap<String,Object>();
		options.put("1", "1");
		options.put("2", "2");
		options.put("3", "3");
		options.put("4", "4");
		options.put("missing1", "-97");

		AbstractAnswerBean variable = generateSingleChoiceVariable("var1","var1 alternative",options);
		variable.setStringValue("3");
		Assert.assertEquals("2", functions.reverseValue(variable));
	}
	
	@Test
	public void recodeValue() {
		session.setValueForVariablename("var1", "1");
		Map<String,Object> options = new LinkedHashMap<String,Object>();
		options.put("1", "1");
		options.put("2", "2");
		options.put("3", "3");
		options.put("4", "4");
		options.put("missing1", "-97");
		
		Map<String,Object> recodeMap = new LinkedHashMap<String,Object>();
		recodeMap.put("1", "2");
		recodeMap.put("2", "3");
		recodeMap.put("3", "4");
		recodeMap.put("4", "1");

		AbstractAnswerBean variable = generateSingleChoiceVariable("var1","var1 alternative",options);
		variable.setStringValue("missing1");
		Assert.assertEquals("NULLVALUE", functions.recodeValue(variable,recodeMap,"NULLVALUE"));
		
		variable.setStringValue("3");
		Assert.assertEquals("4", functions.recodeValue(variable,recodeMap,"NULLVALUE"));
	}
	
	@Test
	public void ifthenelse() {
		Assert.assertEquals("4", functions.ifthenelse(1>0,"4","NULLVALUE"));
		Assert.assertNotEquals("4", functions.ifthenelse(1<0,"4","NULLVALUE"));
	}
	
	@Test
	public void asNumber() throws Exception {
		session.setValueForVariablename("var1", "1");
		Map<String,Object> options = new LinkedHashMap<String,Object>();
		options.put("1", "1");
		options.put("2", "2");
		options.put("3", "3");
		options.put("4", "4");
		options.put("missing1", "-97");

		AbstractAnswerBean variable = generateSingleChoiceVariable("var1","var1 alternative",options);
		variable.setStringValue("3");
		Assert.assertEquals(new Double(3), functions.asNumber(variable));
	}
	
	
	@Test
	public void scaleValue() {
		session.setValueForVariablename("var1", "1");
		session.setValueForVariablename("var2", "2");
		session.setValueForVariablename("var3", "-97");
		List<AbstractAnswerBean> vars = new ArrayList<AbstractAnswerBean>();
		List<AbstractAnswerBean> reverseVars = new ArrayList<AbstractAnswerBean>();
		Map<String,Object> options = new LinkedHashMap<String,Object>();
		options.put("1", "1");
		options.put("2", "2");
		options.put("3", "3");
		options.put("4", "4");
		options.put("missing1", "-97");
		vars.add(generateSingleChoiceVariable("var1","var1 alternative",options));
		reverseVars.add(generateSingleChoiceVariable("var2","var2 alternative",options));
		reverseVars.add(generateSingleChoiceVariable("var3","var3 alternative",options));
		
		
		String missing = "-99";
		Object result = functions.scaleValue(vars, reverseVars, missing);
		Assert.assertEquals(2.5D, result);
	}

	
	@Test
	public void nettoNutzen() throws Exception {
		session.setValueForVariablename("v612", "ao2");
		session.setValueForVariablename("v613", "ao2");
		session.setValueForVariablename("v614", "ao2");
		session.setValueForVariablename("v615", "ao3");
		session.setValueForVariablename("v616", "ao1");
		session.setValueForVariablename("v617", "ao1");

		Map<String,AbstractAnswerBean> vars = new HashMap<String,AbstractAnswerBean>();

		Map<String,Object> options = new LinkedHashMap<String,Object>();
		options.put("ao1", "1");
		options.put("ao2", "2");
		options.put("ao3", "3");
		options.put("ao4", "4");
		options.put("ao5", "5");
		options.put("ao6", "6");
		options.put("missing1", "-97");
		
		vars.put("v612",generateSingleChoiceVariable("v612","v612 alternative",options));
		vars.put("v613",generateSingleChoiceVariable("v613","v613 alternative",options));
		vars.put("v614",generateSingleChoiceVariable("v614","v614 alternative",options));
		vars.put("v615",generateSingleChoiceVariable("v615","v615 alternative",options));
		vars.put("v616",generateSingleChoiceVariable("v616","v616 alternative",options));
		vars.put("v617",generateSingleChoiceVariable("v617","v617 alternative",options));

		List<AbstractAnswerBean> variables = new ArrayList<AbstractAnswerBean>();
		variables.add(vars.get("v612"));
		variables.add(vars.get("v613"));
		final Double term1 = (Double) functions.ifthenelse(functions.missingCount(variables)> 0, -9L,functions.asNumber(vars.get("v612"))*functions.asNumber(vars.get("v613")));
//		LOGGER.info("term1 {}",term1);
		
		variables.clear();
		variables.add(vars.get("v614"));
		variables.add(vars.get("v615"));
		final Double term2 = (Double) functions.ifthenelse(functions.missingCount(variables)> 0, -9L,functions.asNumber(vars.get("v614"))*functions.asNumber(vars.get("v615")));
//		LOGGER.info("term2 {}",term2);
		
		variables.clear();
		variables.add(vars.get("v616"));
		variables.add(vars.get("v617"));
		final Double term3 = (Double) functions.ifthenelse(functions.missingCount(variables)> 0, -9L,functions.asNumber(vars.get("v616"))*functions.asNumber(vars.get("v617")));
//		LOGGER.info("term3 {}",term3);
		
		Double count = 0D;
		count = count + (Double)functions.ifthenelse(term1 == -9,0D,1D);
		count = count + (Double)functions.ifthenelse(term2 == -9,0D,1D);
		count = count + (Double)functions.ifthenelse(term3 == -9,0D,1D);
//		LOGGER.info("count {}",count);
		
		Double sum = 0D;
		sum = sum + (Double)functions.ifthenelse(term1 == -9,0D,term1);
		sum = sum + (Double)functions.ifthenelse(term2 == -9,0D,term2);
		sum = sum + (Double)functions.ifthenelse(term3 == -9,0D,term3);
//		LOGGER.info("sum {}",sum);
		
		
//		String missing = "-99";
		Object result = functions.asNumber(sum/count);
//		LOGGER.info("result {}",result);
		Assert.assertEquals(3.6666666666666665, result);
	}
	
}

package eu.dzhw.zofar.testing.condition.components;

import java.io.Serializable;

import eu.dzhw.zofar.testing.condition.term.elements.Element;

public class FunctionProvider extends Element implements Serializable {

	private static final long serialVersionUID = 986132842770991084L;
	private final static FunctionProvider INSTANCE = new FunctionProvider();

	private FunctionProvider() {
		super("Zofar Dummy");
	}

	public static FunctionProvider getInstance() {
		return INSTANCE;
	}
	
	public Object isMissing(Object obj) {
//		System.out.println("isMissing " + obj);
		return false;
	}

	public Object asNumber(Object obj) {
//		System.out.println("#1 as Number " + obj);
		if(obj!= null){
			return Integer.getInteger(obj + "");
		}
		return null;
	}
}

package de.his.zofar.service.valuetype.util;

import java.io.Serializable;
import java.util.UUID;

public class Generator implements Serializable {

	private static final long serialVersionUID = -3025619381957158387L;
	private static final Generator INSTANCE = new Generator();

	private Generator() {
		super();
	}
	
	public static Generator getInstance(){
		return INSTANCE;
	}
	
	public String createUUID(){
		return UUID.randomUUID().toString();
	}

}

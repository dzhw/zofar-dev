package de.his.zofar.presentation.surveyengine.listener.errorHandling;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

/**
 * @author meisner
 * 
 */
public class ZofarExceptionHandlerFactory extends ExceptionHandlerFactory {

	private ExceptionHandlerFactory bypassHandlerFactory;

	public ZofarExceptionHandlerFactory(
			ExceptionHandlerFactory bypassHandlerFactory) {
		this.bypassHandlerFactory = bypassHandlerFactory;
	}

	@Override
	public ExceptionHandler getExceptionHandler() {
		ExceptionHandler handler = new ZofarExceptionHandler(
				bypassHandlerFactory.getExceptionHandler());
		return handler;
	}

}

package eu.dzhw.zofar.management.utils.threading;

import java.util.Observable;
import java.util.Observer;

public abstract class Payload extends Thread {
	
	private class myObservable extends Observable {

		public void changed() {
			this.setChanged();
		}
	};

	private final myObservable observable;

	
	public Payload() {
		super();
		this.observable = new myObservable();
	}

	public void addObserver(final Observer observer) {
		observable.addObserver(observer);
	}
	
	public void changed(){
		observable.changed();
	}
	
	public void notifyObservers(final Object message){
		observable.notifyObservers(message);
	}
	
	public abstract void run();

}

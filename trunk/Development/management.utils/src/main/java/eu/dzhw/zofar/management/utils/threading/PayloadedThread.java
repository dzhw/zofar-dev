package eu.dzhw.zofar.management.utils.threading;

import java.util.Observer;

public class PayloadedThread extends Thread {
	
	private final Payload target;


	public PayloadedThread(Payload target) {
		super(target);
		this.target = target;
	}
	
	public void addObserver(final Observer observer) {
		if(this.target != null)this.target.addObserver(observer);
	}

	public void sleepFor(final long delay) throws InterruptedException {
		if (delay > 0) {
			sleep(delay);
		}
	}
}

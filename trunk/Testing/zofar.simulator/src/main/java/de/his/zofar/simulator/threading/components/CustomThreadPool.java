package de.his.zofar.simulator.threading.components;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CustomThreadPool {

	final ExecutorService executor;

	public CustomThreadPool(final int maxPoolSize) {
		super();
		executor = Executors.newFixedThreadPool(maxPoolSize);
	}

	/**
	 * Here we add our jobs to working queue
	 * 
	 * @param task
	 *            a Runnable task
	 */
	public void runTask(TestThread task) {
		executor.execute(task);
	}

	/**
	 * Shutdown the Threadpool if it's finished
	 */
	public void shutdown() {
		executor.shutdown();
	}
	
	public boolean isTerminated() {
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

}

/*
 * Class of Dummy Methods for Testing
 */
package eu.dzhw.zofar.management.utils.performance;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class DummyClient.
 */
public class DummyClient {

	/** The Constant INSTANCE. */
	private static final DummyClient INSTANCE = new DummyClient();

	/** The Constant LOGGER. */
	final static Logger LOGGER = LoggerFactory.getLogger(DummyClient.class);

	/**
	 * The Class WaitTask.
	 */
	private class WaitTask extends Thread {

		/** The wait. */
		private final long wait;

		/**
		 * Instantiates a new wait task.
		 * 
		 * @param wait
		 *            the wait
		 */
		public WaitTask(final long wait) {
			super();
			this.wait = wait;
		}

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			super.run();
			try {
				LOGGER.info(this + " wait for " + this.wait + " ms");
				sleep(this.wait);
				LOGGER.info(this + " done");
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Instantiates a new dummy client.
	 */
	private DummyClient() {
		super();
	}

	/**
	 * Method with defined runtime.
	 * 
	 * @param timeout
	 *            the timeout
	 */
	public void handbrake(final long timeout) {
		final ExecutorService executor = Executors.newFixedThreadPool(1);
		final Thread task = new WaitTask(timeout);
		executor.execute(task);
		try {
			executor.shutdown();
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}

}

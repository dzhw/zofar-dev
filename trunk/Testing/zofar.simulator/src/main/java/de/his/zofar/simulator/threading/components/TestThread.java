package de.his.zofar.simulator.threading.components;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Stack;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

import de.his.zofar.simulator.io.unit.TestUnit;

public class TestThread extends Thread {

	private final String param;
	private final String token;

	private final TestUnit unit;

	private long minThreadDelay;
	private long maxThreadDelay;
	private int synchron;
	private int pageCount;

	private class myObservable extends Observable {

		public void changed() {
			this.setChanged();
		}
	};

	private final myObservable observable;

	public TestThread(final String proxyUrl, final int proxyPort, final String targetProtocol, final String targetUrl, final int targetPort, final String token, final String param, final Stack<Map<String, Object>> records, final long minThreadDelay, final long maxThreadDelay,
			final int synchron, final int pageCount, final Map<String, Map<String, String>> types) {
		super();
		this.minThreadDelay = minThreadDelay;
		this.maxThreadDelay = maxThreadDelay;
		this.synchron = synchron;
		this.pageCount = pageCount;
		this.param = param;
		this.token = token;
		this.observable = new myObservable();
		unit = new TestUnit(proxyUrl, proxyPort, targetProtocol, targetUrl, targetPort, records, types);
	}

	public void addObserver(final Observer observer) {
		observable.addObserver(observer);
	}

	public String getParam() {
		return param;
	}

	public String getToken() {
		return token;
	}

	public TestUnit getUnit() {
		return this.unit;
	}

	@Override
	public void interrupt() {
		unit.shutdown();
		super.interrupt();
	}

	@Override
	public void run() {
		try {
			long startDelay = (((maxThreadDelay + minThreadDelay) / 2) * pageCount) / synchron;
			System.out.println(this + " (Thread) startup sleep for " + startDelay + " ms");

			startUnit(startDelay);
		} catch (final FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (final MalformedURLException e) {
			e.printStackTrace();
		} catch (final InterruptedException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}

	}

	public void startUnit(final long delay) throws InterruptedException, FailingHttpStatusCodeException, MalformedURLException, IOException {
		sleepFor(delay);
		if ((unit != null) && (param != null)) {
			final Object back = unit.startup(param);
			final Map<String, Object> result = new HashMap<String, Object>();
			result.put("thread", this);
			result.put("token", this.token);
			result.put("status", "started");
			result.put("back", back);
			observable.changed();
			observable.notifyObservers(result);
		}
	}

	public void nextUnit(final long delay) throws InterruptedException, IOException {
		sleepFor(delay);
		if ((unit != null) && (param != null)) {
			final Object back = unit.next();
			final Map<String, Object> result = new HashMap<String, Object>();
			result.put("thread", this);
			result.put("token", this.token);
			result.put("status", "next");
			result.put("back", back);
			observable.changed();
			observable.notifyObservers(result);
		}
	}

	public void previousUnit(final long delay) throws InterruptedException, IOException {
		sleepFor(delay);
		if ((unit != null) && (param != null)) {
			final Object back = unit.previous();
			final Map<String, Object> result = new HashMap<String, Object>();
			result.put("thread", this);
			result.put("token", this.token);
			result.put("status", "previous");
			result.put("back", back);
			observable.changed();
			observable.notifyObservers(result);
		}
	}

	private void sleepFor(final long delay) throws InterruptedException {
		if (delay > 0) {
			// System.out.println(this+" sleep for "+delay+" ms");
			sleep(delay);
		}

	}

	public String getPersistenceLog() {
		if ((unit != null) && (param != null)) {
			return unit.getPersistenceLog();
		}
		return null;
	}

}

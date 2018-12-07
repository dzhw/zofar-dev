package eu.dzhw.zofar.management.dev.file.spider;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.dzhw.zofar.management.dev.file.spider.components.Check;
import eu.dzhw.zofar.management.dev.file.spider.components.DirectoryHandler;
import eu.dzhw.zofar.management.dev.file.spider.components.FileHandler;

public class SpiderClient {
	private static final SpiderClient INSTANCE = new SpiderClient();

	final static Logger LOGGER = LoggerFactory.getLogger(SpiderClient.class);

	private SpiderClient() {
		super();
	}

	public static synchronized SpiderClient getInstance() {
		return INSTANCE;
	}
	
	public void doSpider(final String directory,final boolean quiet,final boolean recursive,final DirectoryHandler dirHandler,final FileHandler fileHandler,String startTag,String stopTag){
		final ExecutorService executor = Executors.newCachedThreadPool();
		final Set<Callable<Object>> tasks =  new LinkedHashSet<Callable<Object>>();
		final HashSet<Future> pointer = new LinkedHashSet<Future>();
		final Check check = new Check(directory,quiet,recursive,executor,dirHandler,fileHandler,startTag,stopTag);
		tasks.add(check);
		try {
			pointer.addAll(executor.invokeAll(tasks));
		} catch (final InterruptedException e1) {
			e1.printStackTrace();
		}
		final Iterator<Future> checkIt = pointer.iterator();
		while(checkIt.hasNext()){
			final Future<?> tmp = checkIt.next();
			if(tmp.isDone()){
				checkIt.remove();
			}
		}
		LOGGER.info("Done");
		executor.shutdown();
	}
	
}

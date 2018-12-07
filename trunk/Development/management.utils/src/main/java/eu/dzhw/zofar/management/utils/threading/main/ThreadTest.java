package eu.dzhw.zofar.management.utils.threading.main;

import eu.dzhw.zofar.management.utils.threading.Payload;
import eu.dzhw.zofar.management.utils.threading.PayloadedThread;
import eu.dzhw.zofar.management.utils.threading.PayloadedThreadPool;

public class ThreadTest {
	
	public static void main(String[] args) {
		new ThreadTest();
	}
	
	public ThreadTest() {
		final PayloadedThreadPool pool = new PayloadedThreadPool(100);
		
		final int count = 100000;
		
		for(int a = 0;a<count;a++){
			final int x = a;
			PayloadedThread task = new PayloadedThread(new Payload(){

				@Override
				public void run() {
					System.out.println(x);
//					this.changed();
//					this.notifyObservers(x+" printed");
				}
				
			});
			pool.runTask(task);
		}
		pool.shutdown();
		if(pool.isTerminated())System.out.println("done");

	}

}

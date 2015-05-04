package com.jtalis.core.threads;

/**
 * Basic ETALIS worker thread. Uses <code>shutdown()</code> to stop the thread
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public abstract class JtalisWorker extends Thread {
	protected int timeOut;
	protected volatile boolean run;

	public JtalisWorker(String name) {
		super(name);

		timeOut = 1;
		run = true;
	}

	public void shutdown() {
		run = false;
	}

	public void shutdownAndJoin() {
		shutdown();
		try {
			join();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

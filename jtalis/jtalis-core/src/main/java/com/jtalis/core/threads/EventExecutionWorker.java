package com.jtalis.core.threads;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.core.plengine.PrologEngineWrapper;
import com.jtalis.core.terms.EventTerm;

/**
 * Prolog event execution thread context. Don't interrupt the thread, because
 * this is the Prolog Engine execution context. If this thread is interrupted
 * Interprolog Process will crash and won't be able to process further events.
 * Instead use the safety {@code shutdown()} method
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class EventExecutionWorker extends JtalisWorker {
	private static final Logger logger = Logger.getLogger(EventExecutionWorker.class.getName());

	private BlockingQueue<EtalisEvent> eventQueue;
	private PrologEngineWrapper<?> wrapper;

	/**
	 * @param eventQueue queue to read events from
	 * @param wrapper engine wrapper to execute events in
	 */
	public EventExecutionWorker(BlockingQueue<EtalisEvent> eventQueue, PrologEngineWrapper<?> wrapper) {
		super("EventExecutionWorker");
		this.eventQueue = eventQueue;
		this.wrapper = wrapper;

		logger.info("Starting " + getName());
		start();
	}

	@Override
	public void run() {
		while (run) {
			try {
				EtalisEvent event = eventQueue.poll(timeOut, TimeUnit.SECONDS);
				if (event != null) {
					wrapper.execute(new EventTerm(event));
				}
			}
			catch (InterruptedException e) {
				break;
			}
		}
		logger.info(String.format("Emptying queue with %d events and exit. ", eventQueue.size()));
		while (!eventQueue.isEmpty()) {
			EtalisEvent event = eventQueue.remove();
			if (event != null) {
				wrapper.execute(new EventTerm(event));
			}
		}
	}
}

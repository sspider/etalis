package com.jtalis.core.threads;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.jtalis.core.JtalisContext;
import com.jtalis.core.event.EtalisEvent;
import com.jtalis.core.event.JtalisOutputEventProvider;

/**
 * {@code JtalisOutputEventProvider} instance execution environment. An instance of this 
 * class is created for each output provider that is registered in {@link JtalisContext}.
 * 
 * @see JtalisOutputEventProvider
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class OutputEventProviderWorker extends JtalisWorker {
	private static final Logger	logger = Logger.getLogger(OutputEventProviderWorker.class.getName());

	private JtalisOutputEventProvider provider;
	private BlockingQueue<EtalisEvent> queue;

	/**
	 * Constructor
	 * 
	 * @param provider provider to put events to.
	 * @param queue event queue to poll
	 */
	public OutputEventProviderWorker(JtalisOutputEventProvider provider, BlockingQueue<EtalisEvent> queue) {
		super("OutputEventProvider [" + provider + "]");
		this.provider = provider;
		this.queue = queue;

		logger.info("Starting " + getName());
		start();
	}

	@Override
	public void run() {
		while (run) {
			try {
				EtalisEvent event = queue.poll(timeOut, TimeUnit.SECONDS);
				if (event != null) {
					provider.outputEvent(event);
					yield();
				}
			}
			catch (InterruptedException e) {
				break;
			}
		}
		logger.info(String.format("Emptying queue for [%s] with %d events and exit. ", provider.toString(), queue.size()));
		while (!queue.isEmpty()) {
			EtalisEvent event = queue.remove();
			if (event != null) {
				provider.outputEvent(event);
			}
		}				
		logger.info("Exit " + getName());
	}
}

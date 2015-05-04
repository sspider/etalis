package com.jtalis.core.threads;

import java.util.logging.Logger;

import com.jtalis.core.JtalisContext;
import com.jtalis.core.event.EtalisEvent;
import com.jtalis.core.event.JtalisInputEventProvider;

/**
 * {@code JtalisInputEventProvider} instance execution environment. An instance of this 
 * class is created for each input provider that is registered in {@link JtalisContext}.
 * 
 * @see JtalisInputEventProvider
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class InputEventProviderWorker extends JtalisWorker {
	private static final Logger	logger = Logger.getLogger(InputEventProviderWorker.class.getName());

	private JtalisInputEventProvider provider;
	private JtalisContext context;
	private long feedDelay;

	/**
	 * 
	 * @param provider provider to take events from.
	 * @param context context to submit events to
	 * @param feedDelay minimum delay between events
	 */
	public InputEventProviderWorker(JtalisInputEventProvider provider, JtalisContext context, long feedDelay) {
		super("InputEventProvider [" + provider + "]");
		this.context = context;
		this.provider = provider;
		this.feedDelay = feedDelay;

		logger.info("Starting " + getName());
		start();
	}

	@Override
	public void run() {
		while (run && provider.hasMore()) {
			try {
				if (feedDelay > 0) {
					sleep(feedDelay);
				}

				EtalisEvent event = provider.getEvent();
				if (event != null) {
					context.pushEvent(event);
					yield();
				}
			}
			catch (InterruptedException e) {
				break;
			}
		}
		logger.info("Exit " + getName());
	}
}

package com.jtalis.core;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

import com.jtalis.core.config.BasicConfig;
import com.jtalis.core.config.JtalisConfig;
import com.jtalis.core.event.EtalisEvent;
import com.jtalis.core.event.EtalisEventListener;
import com.jtalis.core.event.JtalisEventProvider;
import com.jtalis.core.event.JtalisInputEventProvider;
import com.jtalis.core.event.JtalisOutputEventProvider;
import com.jtalis.core.plengine.PrologEngineWrapper;
import com.jtalis.core.terms.ConsultFileTerm;
import com.jtalis.core.terms.SetJavaEngineTerm;
import com.jtalis.core.terms.SetJavaQueueID;
import com.jtalis.core.threads.EventExecutionWorker;
import com.jtalis.core.threads.InputEventProviderWorker;
import com.jtalis.core.threads.OutputEventProviderWorker;

/**
 * <p>This context implementation is based on providers conception. There is one input event queue, 
 * and a personal output event queue for each {@link OutputEventProviderWorker} 
 * 
 * <p>For each registered {@link JtalisInputEventProvider} a designated worker thread is started - 
 * {@link InputEventProviderWorker}. Each {@link InputEventProviderWorker} takes next event from the 
 * provider and submits it to the input queue.</p>
 * 
 * <p>The input queue is being polled by {@link EventExecutionWorker}. If a new event has arrived, it is being
 * executed in the engine in the current worker's thread environment. Note that more than one {@link EventExecutionWorker}s
 * may be started, which means that the events are going to be submitted simultaneously, which currently is NOT supported
 * be ETALIS. So stick to 1 for now.</p>
 * 
 * <p>{@link EtalisEventListener} instance is registered in prolog environment. And on every event that ETALIS generates,
 * this instance would be notified from prolog environment by the method notifyEventReceived(String event). After that
 * the {@link EtalisEventListener} instance submits this event into this context.</p>
 * 
 * <p>Afterwards this context notifies all registered {@link OutputEventProviderWorker}s for the newly fired 
 * event from ETALIS.
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class JtalisContextImpl extends AbstractJtalisContext {
	private static final Logger logger = Logger.getLogger(JtalisContextImpl.class.getName());

	private static final int DEFAULT_QUEUE_SIZE = 100;

	private JtalisConfig config;
	private List<OutputEventProviderWorker> outputProviderWorkers = Collections.synchronizedList(new LinkedList<OutputEventProviderWorker>());
	private List<InputEventProviderWorker> inputProviderWorkers = Collections.synchronizedList(new LinkedList<InputEventProviderWorker>());
	private Set<JtalisEventProvider> allProviders = Collections.synchronizedSet(new HashSet<JtalisEventProvider>());
	private List<EventExecutionWorker> eventExecutionWorkers = Collections.synchronizedList(new LinkedList<EventExecutionWorker>());

	private List<OutputEventQueue> outputQueues = Collections.synchronizedList(new LinkedList<OutputEventQueue>());
	private BlockingQueue<EtalisEvent> inputQueue = new ArrayBlockingQueue<EtalisEvent>(DEFAULT_QUEUE_SIZE);

	private EtalisEventListener etalisEventListener;
	private PrologEngineWrapper<?> engineWrapper;

	public JtalisContextImpl(PrologEngineWrapper<?> ew) {
		this(new BasicConfig(), ew);
	}

	public JtalisContextImpl(JtalisConfig config, PrologEngineWrapper<?> ew) {
		this(config, ew, 1);
	}

	// Private because ETALIS is still NOT thread safe. executionThreads could be only 1 for now
	private JtalisContextImpl(JtalisConfig cnf, PrologEngineWrapper<?> ew, int executionThreads) {
		config = cnf;
		engineWrapper = ew;

		etalisEventListener = new EtalisEventListener(this);

		loadEtalisSources();

		// register EtalisEventListener instance in Prolog Environment
		Object obj = engineWrapper.registerPushNotification(etalisEventListener);
		setEtalisFlags(sJavaNotification, "on");
		engineWrapper.execute(new SetJavaEngineTerm(engineWrapper.getName()));
		engineWrapper.execute(new SetJavaQueueID(obj));

		for (int i = 0; i < executionThreads; i++) {
			eventExecutionWorkers.add(new EventExecutionWorker(inputQueue, getEngineWrapper()));
		}
	}

	public void pushEvent(EtalisEvent event) {
		try {
			inputQueue.put(event);
		}
		catch (InterruptedException e) {
			logger.severe(e + ". " + e.getMessage());
		}
	}

	@Override
	public void outputEvent(EtalisEvent event) {
		for (OutputEventQueue q: outputQueues) {
			try {
				if (q.accepts(event)) {
					q.put(event);					
				}
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void registerInputProvider(JtalisInputEventProvider provider) {
		registerInputProvider(provider, 0);
	}

	@Override
	public void registerInputProvider(JtalisInputEventProvider provider, long feedDelay) {
		allProviders.add(provider);
		inputProviderWorkers.add(new InputEventProviderWorker(provider, this, feedDelay));		
	}

	@Override
	public void registerOutputProvider(JtalisOutputEventProvider ...providers) {
		registerOutputProvider(null ,providers);
	}

	@Override
	public void registerOutputProvider(String regex, JtalisOutputEventProvider... providers) {
		OutputEventQueue q = new OutputEventQueue(regex);
		outputQueues.add(q);
		for (JtalisOutputEventProvider p : providers) {
			allProviders.add(p);
			outputProviderWorkers.add(new OutputEventProviderWorker(p, q));			
		}		
	}

	@Override
	public void registerOutputProvider(JtalisOutputEventProvider provider, int threads) {
		registerOutputProvider(null, provider, threads);
	}

	@Override
	public void registerOutputProvider(String regex, JtalisOutputEventProvider provider, int threads) {
		allProviders.add(provider);
		OutputEventQueue q = new OutputEventQueue(regex);
		outputQueues.add(q);
		for (int i = 0; i < threads; i++) {
			outputProviderWorkers.add(new OutputEventProviderWorker(provider, q));				
		}
	}

	/**
	 * First shuts down all threads as follows: <br>
	 * <ul>
	 * 	<li>{@link InputEventProviderWorker}</li>
	 * 	<li>{@link EventExecutionWorker}</li>
	 * 	<li>{@link OutputEventProviderWorker}</li>
	 * </ul>
	 * Then unregisters the {@link EtalisEventListener} instance from Prolog environment. Finally calls
	 * {@code shutdown()} to all registered providers and shuts down the underlying {@link PrologEngineWrapper}
	 */
	@Override
	public void shutdown() {
		logger.info("Shutting down");
		for (InputEventProviderWorker provider : inputProviderWorkers) {
			provider.shutdownAndJoin();
		}
		for (EventExecutionWorker w : eventExecutionWorkers) {
			w.shutdownAndJoin();
		}
		for (OutputEventProviderWorker provider : outputProviderWorkers) {
			provider.shutdown();
		}
		for (OutputEventProviderWorker provider : outputProviderWorkers) {
			try {
				provider.join();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		getEngineWrapper().unregisterPushNotification(etalisEventListener);
		for (JtalisEventProvider provider : allProviders) {
			provider.shutdown();
		}
		getEngineWrapper().shutdown();

		logger.info("Engine shutdown successfully.");
	}

	@Override
	public PrologEngineWrapper<?> getEngineWrapper() {
		return engineWrapper;
	}

	@Override
	public void waitForInputProviders() {
		logger.info("Waiting for all Input Providers to finish");
		for (InputEventProviderWorker provider : inputProviderWorkers) {
			try {
				provider.join();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		logger.info("All Input Providers are done");
	}

	private void loadEtalisSources() {
		if (!config.getEtalisSourceFile().exists()) {
			logger.severe(config.getEtalisSourceFile() + " does not exist");
			throw new RuntimeException(config.getEtalisSourceFile() + " does not exist");
		}

		if (!engineWrapper.execute(new ConsultFileTerm(config.getEtalisSourceFile()))) {
			logger.severe("Could not consult " + config.getEtalisSourceFile());
			engineWrapper.shutdown();
			throw new RuntimeException("Could not consult " + config.getEtalisSourceFile());
		}
	}

}

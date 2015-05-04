package com.jtalis.core;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.core.event.JtalisInputEventProvider;
import com.jtalis.core.event.JtalisOutputEventProvider;
import com.jtalis.core.plengine.PrologEngineWrapper;

/**
 * Jtalis context base interface. It defines everything related to the event distribution of the engine.
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public interface JtalisContext extends EtalisInterface, Shutdownable {

	/**
	 * Pushes event into the system
	 * @param event the event to push 
	 */
	public void pushEvent(EtalisEvent event);

	/**
	 * Takes the passed event out of the context. In other words, notifies all output providers for this event. 
	 * @param event the event to output
	 */
	public void outputEvent(EtalisEvent event);

	/**
	 * Adds input provider to the context
	 * @param provider event provider to add
	 */
	public void registerInputProvider(JtalisInputEventProvider provider);

	/**
	 * Adds input provider to the context
	 * @param provider event provider to add
	 * @param feedDelay minimum delay between events
	 */
	public void registerInputProvider(JtalisInputEventProvider provider, long feedDelay);

	/**
	 * Adds all the providers to the same queue of the context. Event providers, register on the same queue, will 
	 * compete for a single event. This means that when an event arrives, only one provider from the queue will get the
	 * event.
	 *  
	 * @param providers event providers to add
	 */
	public void registerOutputProvider(JtalisOutputEventProvider ...providers);

	/**
	 * Adds all the providers to the same queue of the context. All providers would be notified only for 
	 * events which name matches the given regular expression. Event providers, register on the same queue, will 
	 * compete for a single event. This means that when an event arrives, only one provider from the queue will get the
	 * event.
	 * 
	 * @param regex event name pattern
	 * @param providers event providers to add
	 */
	public void registerOutputProvider(String regex, JtalisOutputEventProvider ...providers);

	/**
	 * Adds the providers in a new queue in the context, starting up the given number of threads processing it.
	 * @param provider event providers to add
	 * @param threads number of threads for the given provider
	 */
	public void registerOutputProvider(JtalisOutputEventProvider provider, int threads);

	/**
	 * Adds the providers in a new queue in the context, starting up the given number of threads processing it.
	 * All providers would be notified only for events which name matches the given regular expression.
	 *  
	 * @param regex event name pattern
	 * @param provider event providers to add
	 * @param threads number of threads for the given provider
	 */
	public void registerOutputProvider(String regex, JtalisOutputEventProvider provider, int threads);

	/**
	 * Returns the underlying engine wrapper
	 * @return the underlying engine wrapper
	 */
	public PrologEngineWrapper<?> getEngineWrapper();

	/**
	 * Blocks the current thread until all input providers are finished
	 */
	public void waitForInputProviders();

}

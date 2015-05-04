package com.jtalis.core.event;

import com.jtalis.core.JtalisContext;
import com.jtalis.core.threads.InputEventProviderWorker;

/**
 * An instance of this class must provide input source of events to {@link JtalisContext}. 
 * For each {@link JtalisInputEventProvider} object is created {@link InputEventProviderWorker}, 
 * therefore the developer that implements this interface must keep in mind that it's methods are 
 * called in a separate thread environment.
 * 
 * @see InputEventProviderWorker
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public interface JtalisInputEventProvider extends JtalisEventProvider {

	/**
	 * Returns whether this source has more events to produce. 
	 * @return <code>true</code> if this provider has more events to produce. 
	 * <code>false</code> means that this provider will NEVER produce any more events.
	 */
	public boolean hasMore();

	/**
	 * Returns next produced event. Can return null if there is no event at the moment, but ideally the best 
	 * approach is to block or sleep the current thread while the next event is ready to produce. 
	 * 
	 * @return next produced event. Could return <code>null</code> if there is no event for the moment.
	 */
	public EtalisEvent getEvent();

}

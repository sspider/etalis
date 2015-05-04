package com.jtalis.core.event;

import com.jtalis.core.JtalisContext;
import com.jtalis.core.threads.OutputEventProviderWorker;

/**
 * An instance of this class must provide output sink of events from {@link JtalisContext}. 
 * For each {@link JtalisOutputEventProvider} object is created {@link OutputEventProviderWorker}, 
 * therefore the developer that implements this interface must keep in mind that it's methods are 
 * called in a separate thread environment.
 * 
 * @see OutputEventProviderWorker
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public interface JtalisOutputEventProvider extends JtalisEventProvider {

	/**
	 * Notification for a new event
	 * @param event the new event
	 */
	public void outputEvent(EtalisEvent event);
}

package com.jtalis.core.plengine;

import com.jtalis.core.event.EtalisEventListener;
import com.jtalis.core.plengine.logic.Term;

/**
 * Basic interface for Java - Prolog - Java communication. 
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 *
 * @param <SubEngine> the underlying engine type
 */
public interface PrologEngineWrapper<SubEngine> {

	/**
	 * Executes the given term
	 * @param term the term to execute
	 * @return whether the execution was successful
	 */
	public boolean execute(Term term);

	/**
	 * Executes the goal as it is
	 * @param goal prolog goal to execute in prolog
	 * @return status of execution
	 */
	public boolean executeGoal(String goal);

	/**
	 * Registers java instance in prolog environment in order to get push notification functionality.
	 * @param listener the instance to register
	 * @return object representing the registered instance, null if registration was not successful
	 */
	public Object registerPushNotification(EtalisEventListener listener);

	/**
	 * Unregisters the passed listener
	 * @param listener listener to unregister
	 */
	public void unregisterPushNotification(EtalisEventListener listener);

	/**
	 * Shuts the underlying engine down 
	 */
	public void shutdown();

	/**
	 * Adds output listener
	 */
	public void addOutputListener(EngineOutputListener listener);

	/**
	 * Returns engine name
	 * @return engine name
	 */
	public String getName();
}
 
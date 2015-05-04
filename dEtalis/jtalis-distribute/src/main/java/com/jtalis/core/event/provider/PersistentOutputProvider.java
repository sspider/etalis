package com.jtalis.core.event.provider;

import java.util.logging.Logger;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.core.event.JtalisOutputEventProvider;
import com.veskogeorgiev.probin.annotations.Parameter;

/**
 * Basic class for event datastore, e.g. database datastore. It provides buffer functionality for child classes.
 * This means that you can specify the buffer size, which shows the number of events to be stored in a single transaction.
 * If the size is 0, the transaction is closed at the end. The implementing class should, of course, provide implementation
 * for the transaction.   
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public abstract class PersistentOutputProvider implements JtalisOutputEventProvider {

	private static final Logger logger = Logger.getLogger(PersistentOutputProvider.class.getName()); 

	/** Buffer size */
	@Parameter("bufferSize")
	protected int bufferSize;

	/** Local counter, keeps track of non-committed events */
	private int counter;

	/**
	 * Default
	 */
	public PersistentOutputProvider() {
	}

	/**
	 * Creates with specified buffer size
	 */
	public PersistentOutputProvider(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	protected void checkCounter() {
		if (bufferSize > 0 && counter >= bufferSize) {
			try {
				commit();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			counter = 0;
		}
	}

	@Override
	public void shutdown() {
		try {
			commit();
		}
		catch (Exception e) {
			logger.warning("Error while shutting down the provider: " + e.toString());
		}
		try {
			closeConnection();
		}
		catch (Exception e) {
			logger.warning("Error while shutting down the provider: " + e.toString());
		}
	}

	@Override
	public void outputEvent(EtalisEvent event) {
		try {
			save(event);
			counter++;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			checkCounter();
		}	
	}

	/**
	 * Closes whatever connections there are
	 * @throws Exception
	 */
	protected abstract void closeConnection() throws Exception;

	/**
	 * Commits any in-memory events.
	 * @throws Exception
	 */
	protected abstract void commit() throws Exception;

	/**
	 * Stores the passed event.
	 * @param event the event to store
	 * @throws Exception
	 */
	protected abstract void save(EtalisEvent event) throws Exception;

}

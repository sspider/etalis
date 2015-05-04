package com.jtalis.core.event;

import java.util.logging.Logger;

import com.jtalis.core.JtalisContext;

/**
 * Key class that realize Prolog - Java communication. An instance of this class
 * is registered in prolog environment, where {@code notifyEventReceived} is called 
 * to notify this instance for a new event.
 * 
 * TODO think how to deal with index and milliseconds
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class EtalisEventListener {

	private static final Logger logger = Logger.getLogger(EtalisEventListener.class.getName());

	/** Bounded {@link JtalisContext} */
	private JtalisContext context;

	/**
	 * Creates a new 
	 * @param context the context to notify
	 */
	public EtalisEventListener(JtalisContext context) {
		this.context = context;
	}

	/**
	 * Takes the passed event, constructs {@code EtalisEvent} object and 
	 * notifies the context for the new event. This method is designed to 
	 * be called from Prolog environment via reflection. Do not change 
	 * this method's signature 
	 * 
	 * @see EtalisEvent
	 * 
	 * @param eventString
	 * @param timeStarts
	 * @param counterStarts added because of Prolog's lack of milliseconds
	 * @param timeEnds 
	 * @param counterEnds added because of Prolog's lack of milliseconds
	 */
	public void notifyEventReceived(String eventString, double timeStarts, int counterStarts, double timeEnds, int counterEnds) {
		try {
			EtalisEvent event = EventBuilder.buildEventFromString(eventString);
			event.setTimeStarts(new EventTimestamp(timeStarts, counterStarts));
			event.setTimeEnds(new EventTimestamp(timeEnds, counterEnds));
			context.outputEvent(event);
		}
		catch (InvalidEventFormatException e) {
			logger.warning(e.toString());
		}
	}
	
	/**
	 * Takes the passed event, constructs {@code EtalisEvent} object and 
	 * notifies the context for the new event. This method is designed to 
	 * be called from Prolog environment via reflection. Do not change 
	 * this method's signature 
	 * 
	 * @see EtalisEvent
	 * 
	 * @param eventString
	 * @param ruleID
	 * @param timeStarts
	 * @param counterStarts added because of Prolog's lack of milliseconds
	 * @param timeEnds 
	 * @param counterEnds added because of Prolog's lack of milliseconds
	 */
	public void notifyEventWithRuleIDReceived(String eventString, String rid, double timeStarts, int counterStarts, double timeEnds, int counterEnds) {
		try {
			EtalisEvent event = EventBuilder.buildEventFromString(eventString);
			event.setRuleID(rid);
			event.setTimeStarts(new EventTimestamp(timeStarts, counterStarts));
			event.setTimeEnds(new EventTimestamp(timeEnds, counterEnds));
			context.outputEvent(event);
		}
		catch (InvalidEventFormatException e) {
			logger.warning(e.toString());
		}
	}

}

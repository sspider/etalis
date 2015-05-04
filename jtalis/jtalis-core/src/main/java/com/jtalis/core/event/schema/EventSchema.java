package com.jtalis.core.event.schema;

import java.util.List;
import java.util.Set;

/**
 * Contains meta information for each event name  
 * 
 * <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public interface EventSchema {

	/**
	 * Returns all event property for a given event name
	 * 
	 * @param eventName
	 */
	public List<EventProperty> getProperties(String eventName);

	/**
	 * Returns all available event names in this schema
	 */
	public Set<String> getEvents();
}

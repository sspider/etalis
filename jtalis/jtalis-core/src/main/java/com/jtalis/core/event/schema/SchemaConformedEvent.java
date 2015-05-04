package com.jtalis.core.event.schema;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.jtalis.core.event.EtalisEvent;
import com.veskogeorgiev.probin.conversion.CompositeConverter;

/**
 * Provides key access to an event arguments
 *  
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class SchemaConformedEvent {
	private static final Logger logger = Logger.getLogger(SchemaConformedEvent.class.getName());

	/** Positional argument names schema */
	protected ArrayList<EventProperty> properties;

	/** Underlying event */
	protected EtalisEvent event;

	/** Event Schema */
	protected EventSchema schema;

	/** Composite converter */
	protected CompositeConverter converter = new CompositeConverter();

	/**
	 * Creates an a key-value wrapper for the passed event according to the passed key sequence
	 * 
	 * @param event the underlying event
	 * @param schema the event schema to conform to
	 */
	public SchemaConformedEvent(EtalisEvent event, EventSchema schema) throws UnkownEventSchemaException {
		this.event = event;
		this.schema = schema;
		
		List<EventProperty> prop = schema.getProperties(event.getName());

		if (prop == null) {
			throw new UnkownEventSchemaException(event.getName());
		}
		properties = new ArrayList<EventProperty>(prop);
	}

	public EventSchema getSchema() {
		return schema;
	}

	/**
	 * Returns named property. e.g. if this event represents "a('john', 34)" and it has 
	 * schema ["name", "age"], {@code getProperty("name")} would return "john". You actually MUST 
	 * set be aware of the schema before you can use this method. The returned object is actually 
	 * of type conformed to the EventSchema.
	 * 
	 * @param key a key from the set schema
	 * @return value behind key or {@code null} if there is no such key. The returned object is actually of type 
	 * conformed to the EventSchema 
	 */
	public Object getProperty(String key) {
		int idx = getKeyIndex(key);
		if (idx != -1) {
			EventProperty prop = properties.get(idx);
			try {
				return converter.convert(event.getProperty(idx).toString(), prop.getType());
			}
			catch (Exception e) {
				logger.warning(e.toString());
			}
		}
		return null;
	}

	private int getKeyIndex(String key) {
		for (int i = 0; i < properties.size(); i++) {
			if (properties.get(i).getName().equals(key)) {
				return i;
			}
		}
		return -1;
	}
}

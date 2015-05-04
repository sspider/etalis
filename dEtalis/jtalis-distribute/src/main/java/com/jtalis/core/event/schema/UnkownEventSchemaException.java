package com.jtalis.core.event.schema;

/**
 * 
 * <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class UnkownEventSchemaException extends Exception {

	private static final long serialVersionUID = -1490203466651617179L;

	public UnkownEventSchemaException(String eventName) {
		super("Schema for event name " + eventName + " is unavailable");
	}
}

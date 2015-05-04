package com.jtalis.core.event.persistence;

import java.io.IOException;
import java.io.OutputStream;

import com.jtalis.core.event.EtalisEvent;

/**
 * Basic interface for deserializing {@link EtalisEvent}. 
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public interface EventStreamOutput {

	/**
	 * Serializes the given event into the given stream.
	 * 
	 * @param event the event to serialize
	 * @param out the stream to write to
	 * @throws IOException if an error occurred while writing to the stream
	 */
	public void serialize(EtalisEvent event, OutputStream out) throws IOException;

}

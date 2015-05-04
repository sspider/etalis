package com.jtalis.core.event.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.core.event.InvalidEventFormatException;

/**
 * Basic interface for deserializing {@link EtalisEvent}. 
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public interface EventStreamInput {

	/**
	 * Reads as much events as there are in the given stream. May block until events are available. Therefore 
	 * it is only preferred with {@code FileInputStream}.  
	 * 
	 * @param input input stream to read events from
	 * @return all read events
	 * @throws IOException if an error occurred while reading the stream
	 * @throws InvalidEventFormatException if the the content is read, but it is invalid event
	 */
	public Collection<EtalisEvent> deserializeAll(InputStream input) throws IOException, InvalidEventFormatException;

	/**
	 * Reads only one event from the stream. May block until an event is available.
	 * 
	 * @param input input stream to read events from
	 * @return the read event
	 * @throws IOException if an error occurred while reading the stream
	 * @throws InvalidEventFormatException if the the content is read, but it is invalid event
	 */
	public EtalisEvent deserialize(InputStream input) throws IOException, InvalidEventFormatException;

}

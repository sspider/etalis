/**
 * 
 */
package com.jtalis.storm.zstream.channel;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.jtalis.core.event.EtalisEvent;

/**
 * Buffer is a special type of channel. For example, buffer is 
 * appropriate to be used as an end point of an Event Processing 
 * Network. In this case it buffers events that are ready to be 
 * passed to an event consumer.
 *   
 * @author darko
 *
 */
public class Buffer implements IChannel {
	private ConcurrentLinkedQueue<EtalisEvent> buffer = new ConcurrentLinkedQueue<EtalisEvent>();

	public void add(EtalisEvent event) {
		this.buffer.add(event);
	}

	public ConcurrentLinkedQueue<EtalisEvent> getBuffer() {
		return buffer;
	}
}

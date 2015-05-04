package com.jtalis.storm.zstream.channel;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import com.jtalis.core.event.EtalisEvent;

/**
 * @deprecated use {Buffer} instead.
 * 
 * @author darko
 *
 */
public class NodeBuffer {
	private CopyOnWriteArrayList<EtalisEvent> eventBuffer;
	private long timeWindow;

	public NodeBuffer(long timeWindow) {
		this.eventBuffer = new CopyOnWriteArrayList<EtalisEvent>();
		// TODO: this.timeWindow is not used. Implement the NodeBuffer timeWindow
		this.timeWindow = timeWindow;
	}

	public synchronized void removeExpiredEvent(long expiredTimestamp) {
		// remove the expired event according to timestamp
		Iterator<EtalisEvent> iterator = eventBuffer.iterator();
		while(iterator.hasNext()) {
			EtalisEvent tempEvent = iterator.next();
			if (tempEvent.getTimeStarts().getTime() <= expiredTimestamp)
				iterator.remove();
			else
				break;
		}

	}

	public synchronized CopyOnWriteArrayList<EtalisEvent> getSubListAccordingTimestamp() {
		return null;
	}

	public synchronized void addNewEvent(EtalisEvent newEvent) {
		eventBuffer.add(newEvent);
	}

	public synchronized void removeFirstEvent() {
		eventBuffer.remove(0);
	}

	public synchronized CopyOnWriteArrayList<EtalisEvent> getCurrentBuffer() {
		return eventBuffer;
	}

	public synchronized long getBufferSize() {
		return eventBuffer.size();
	}
	
	public synchronized EtalisEvent getEvent(long index) {
		if (index < eventBuffer.size() && index >= 0)
			return eventBuffer.get((int) index);
		else
			return null;
	}
	
	public synchronized Boolean isBufferEmpty() {
		return eventBuffer.isEmpty();
	}

	@Override
	public String toString() {
		return eventBuffer.toString();		
	}
}

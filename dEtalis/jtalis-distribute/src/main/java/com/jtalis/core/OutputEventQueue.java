package com.jtalis.core;

import java.util.concurrent.LinkedBlockingQueue;

import com.jtalis.core.event.EtalisEvent;

public class OutputEventQueue extends LinkedBlockingQueue<EtalisEvent> {

	private static final long serialVersionUID = 3272252688038499198L;

	private String regex;

	public OutputEventQueue() {
		// Default
	}

	public OutputEventQueue(String regex) {
		this.regex = regex;
	}

	public boolean accepts(EtalisEvent event) {
		return regex == null || regex.isEmpty() || event.getName().matches(regex);
	}
	
}

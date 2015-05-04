package com.jtalis.core.event.schema;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BasicEventSchema implements EventSchema {

	private Map<String, List<EventProperty>> schema = new HashMap<String, List<EventProperty>>();

	public BasicEventSchema() {
		// TODO Auto-generated constructor stub
	}

	public BasicEventSchema addEvent(String eventName, EventProperty ...properties) {
		List<EventProperty> l = schema.get(eventName);
		if (l == null) {
			schema.put(eventName, l = new LinkedList<EventProperty>());
		}
		l.addAll(Arrays.asList(properties));
		return this;
	}

	@Override
	public List<EventProperty> getProperties(String eventName) {
		return schema.get(eventName);
	}

	@Override
	public Set<String> getEvents() {
		return schema.keySet();
	}

}

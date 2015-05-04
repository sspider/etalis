package com.jtalis.core.event.schema;

/**
 * Represents event named property.
 *
 * <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class EventProperty {

	private String name;
	private Class<?> type;

	public EventProperty(String name, Class<?> type) {
		this.name = name;
		this.type = type;
	}

	public EventProperty() {
		// Default
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof EventProperty) {
			EventProperty o = (EventProperty) obj;
			return o.getName().equals(getName()) && o.getType().equals(getType());
 		}
		return false;
	}

	@Override
	public int hashCode() {
		return name.hashCode() ^ type.hashCode();
	}

	@Override
	public String toString() {
		return String.format("[%s: %s]", name, type.getName());
	}
}

package com.jtalis.core.event.provider;

import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import com.jtalis.core.event.AbstractJtalisEventProvider;
import com.jtalis.core.event.EtalisEvent;
import com.jtalis.core.event.ProviderSetupException;
import com.jtalis.core.event.schema.EventProperty;
import com.jtalis.core.event.schema.EventSchema;
import com.jtalis.core.event.xml.XMLEventSchema;
import com.veskogeorgiev.probin.annotations.Parameter;

/**
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class RandomEventGenerator extends AbstractJtalisEventProvider {

	private int randExt = 50;
	private Random r;
	private int count;
	private int received;
	private EventSchema schema;

	@Parameter("eventNames")
	private String[] eventNames;
	@Parameter("numberOfEvents")
	private int numberOfEvents;
	@Parameter("eventSchema")
	private File schemaFile;

	public RandomEventGenerator() {
		// TODO Auto-generated constructor stub
	}

	public RandomEventGenerator(int numberOfEvents, EventSchema schema, String... eventNames) throws ProviderSetupException {
		this.numberOfEvents = numberOfEvents;
		r = new Random();
		this.schema = schema;
		this.eventNames = eventNames;
		checkEventNames();
	}

	public RandomEventGenerator(int numberOfEvents, File schemaFile, String... eventNames) throws ProviderSetupException {
		this.numberOfEvents = numberOfEvents;
		this.schemaFile = schemaFile;
		setup();
	}

	@Override
	public void setup() throws ProviderSetupException {
		r = new Random();
		try {
			schema = new XMLEventSchema(schemaFile);
			checkEventNames();
		}
		catch (ParserConfigurationException e) {
			throw new ProviderSetupException(e);
		}
	}

	private void checkEventNames() {
		if (eventNames == null || eventNames.length == 0) {
			Set<String> eventNamesSet = schema.getEvents();
			eventNames = new String[eventNamesSet.size()];
			int i = 0;
			for (String en : eventNamesSet) {
				eventNames[i++] = en;
			}
		}
	}

	@Override
	public boolean hasMore() {
		return count < numberOfEvents;
	}

	@Override
	public void outputEvent(EtalisEvent event) {
		received++;
	}

	@Override
	public EtalisEvent getEvent() {
		count++;
		int eventIdx = r.nextInt(eventNames.length);
		List<EventProperty> props = schema.getProperties(eventNames[eventIdx]);
		Object[] args = new Object[props.size()];

		int i = 0;
		for (EventProperty ep : props) {
			args[i++] = getRandom(ep.getType());
		}
		EtalisEvent e = new EtalisEvent(eventNames[eventIdx], args);
		return e;
	}

	private Object getRandom(Class<?> type) {
		if (String.class.isAssignableFrom(type)) {
			return "string_" + r.nextInt(randExt);
		}
		else if (Integer.class.isAssignableFrom(type)) {
			return r.nextInt(randExt);
		}
		else if (Double.class.isAssignableFrom(type)) {
			return r.nextDouble() * r.nextInt(randExt);
		}
		else if (Long.class.isAssignableFrom(type)) {
			return r.nextLong();
		}
		return "";
	}

	public int getReceived() {
		return received;
	}

	@Override
	public void shutdown() {
		// System.out.println("Received " + received + " events.");
	}
}

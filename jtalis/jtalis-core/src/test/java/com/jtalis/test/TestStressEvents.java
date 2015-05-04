package com.jtalis.test;

import java.util.logging.Logger;

import org.junit.Test;

import com.jtalis.core.JtalisContextImpl;
import com.jtalis.core.event.AbstractJtalisEventProvider;
import com.jtalis.core.event.EtalisEvent;
import com.jtalis.core.event.provider.RandomEventGenerator;
import com.jtalis.core.event.schema.BasicEventSchema;
import com.jtalis.core.plengine.JPLEngineWrapper;
import com.jtalis.core.plengine.PrologEngineWrapper;

public class TestStressEvents {
	private static final Logger logger = Logger.getLogger(TestStressEvents.class.getName());

	private static class Counter extends AbstractJtalisEventProvider {
		int counter;

		@Override
		public void outputEvent(EtalisEvent event) {
			counter++;
		}
	}

	@Test
	public void test50000EventsWithJPL() {
		try {
			long now = System.currentTimeMillis();
			PrologEngineWrapper<?> engine = new JPLEngineWrapper();
			JtalisContextImpl context = new JtalisContextImpl(engine);
			context.addEventTrigger("_");

			Counter c = new Counter();

			RandomEventGenerator generator = new RandomEventGenerator(50000, 
					new BasicEventSchema().addEvent("a").addEvent("b"));

			context.registerInputProvider(generator);
			context.registerOutputProvider(c);
			context.waitForInputProviders();
			context.shutdown();

			logger.info(String.format(c.counter + " events received for %d ms", System.currentTimeMillis() - now));
		}
		catch (Throwable e) {
			logger.severe(e.getMessage());
		}
	}
}

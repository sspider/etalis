package com.jtalis.test;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.jtalis.core.JtalisContext;
import com.jtalis.core.JtalisContextImpl;
import com.jtalis.core.event.AbstractJtalisEventProvider;
import com.jtalis.core.event.EtalisEvent;
import com.jtalis.core.plengine.JPLEngineWrapper;
import com.jtalis.core.plengine.PrologEngineWrapper;

public class TestDynamicEvents {
/*
	@Test
	public void testLoadAndUnloadDynamicEvents() {
		try {
			long delay = 500;
			final List<EtalisEvent> list = new LinkedList<EtalisEvent>();

			PrologEngineWrapper<?> engine = new JPLEngineWrapper();
			JtalisContext context = new JtalisContextImpl(engine);
			context.addEventTrigger("c/1");

			context.registerOutputProvider(new AbstractJtalisEventProvider() {
				@Override
				public void outputEvent(EtalisEvent event) {
					list.add(event);
				}
			});

			context.pushEvent(new EtalisEvent("a", 1));
			context.pushEvent(new EtalisEvent("b", 1));
			Thread.sleep(delay); // wait a little bit for the events to be processed

			Assert.assertTrue(list.isEmpty());

			String ruleId = context.addDynamicRule("c(X) <- a(X) seq b(X)");

			context.pushEvent(new EtalisEvent("a", 1));
			context.pushEvent(new EtalisEvent("b", 1));
			Thread.sleep(delay); // wait a little bit for the events to be processed

			Assert.assertTrue(list.size() == 1);
			Assert.assertTrue(list.get(0).equals(new EtalisEvent("c", 1)));

			context.removeDynamicRule(ruleId);

			context.pushEvent(new EtalisEvent("a", 1));
			context.pushEvent(new EtalisEvent("b", 1));
			Thread.sleep(delay); // wait a little bit for the events to be processed

			context.waitForInputProviders();
			context.shutdown();
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
*/
}

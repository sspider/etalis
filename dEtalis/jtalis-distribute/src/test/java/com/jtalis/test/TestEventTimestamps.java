package com.jtalis.test;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;

import com.jtalis.core.JtalisContextImpl;
import com.jtalis.core.event.AbstractJtalisEventProvider;
import com.jtalis.core.event.EtalisEvent;
import com.jtalis.core.event.EventTimestamp;
import com.jtalis.core.plengine.JPLEngineWrapper;
import com.jtalis.core.plengine.PrologEngineWrapper;

public class TestEventTimestamps {
/*
//	@Test
	public void testTimeStamps() {
		PrologEngineWrapper<?> engine = new JPLEngineWrapper(false);

		JtalisContextImpl ctx = new JtalisContextImpl(engine);
		ctx.setEtalisFlags("out_of_order", "on");
		ctx.addEventTrigger("_");
		ctx.addDynamicRule("c <- b(X) seq a(Y)");

		final List<EtalisEvent> events = new LinkedList<EtalisEvent>();

		ctx.registerOutputProvider(new AbstractJtalisEventProvider() {
			@Override
			public void outputEvent(EtalisEvent event) {
				events.add(event);
			}
		});

		// This way I try to modify the timestamps.
		EventTimestamp e1 = new EventTimestamp(System.currentTimeMillis() / 1000, 0);
		EventTimestamp e2 = new EventTimestamp(System.currentTimeMillis() / 1000, 0);
		EtalisEvent eventa = new EtalisEvent("a", e1, e2, 1);

		// I use "-10" because event b has happened in the past.
		EventTimestamp e3 = new EventTimestamp(System.currentTimeMillis() / 1000 - 10, 0);
		EventTimestamp e4 = new EventTimestamp(System.currentTimeMillis() / 1000 - 10, 0);
		EtalisEvent eventb = new EtalisEvent("b", e3, e4, 1);

		ctx.pushEvent(eventa);
		ctx.pushEvent(eventb);

		ctx.shutdown();

		Assert.assertEquals(3, events.size());
	}

	public static void main(String[] args) {
		TestEventTimestamps t = new TestEventTimestamps();
		t.testTimeStamps();
	}*/
}

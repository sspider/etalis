package com.jtalis.test;

import org.junit.Assert;
import org.junit.Test;

import com.jtalis.core.JtalisContextImpl;
import com.jtalis.core.plengine.JPLEngineWrapper;
import com.jtalis.core.plengine.PrologEngineWrapper;

public class TestEventsWithoutArguments {

	@Test
	public void test10EventsWithoutArguments() {
		try {
			PrologEngineWrapper<?> engine = new JPLEngineWrapper();
			JtalisContextImpl context = new JtalisContextImpl(engine);
			context.addEventTrigger("_");
			context.addDynamicRule("c <- a seq b");

			TestProvider provider = new TestProvider(
				new String[] {"a", "b",      "a", "b",      "b", "b", "a", "b"     },
				new String[] {"a", "b", "c", "a", "b", "c", "b", "b", "a", "b", "c"}
			);
			context.registerOutputProvider(provider);
			context.registerInputProvider(provider);

			context.waitForInputProviders();
			context.shutdown();

			if (!provider.isSuccess()) {
				Assert.fail(provider.getMessage());
			}
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

}

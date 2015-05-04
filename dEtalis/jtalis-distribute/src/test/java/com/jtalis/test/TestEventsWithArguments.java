package com.jtalis.test;

import org.junit.Assert;
import org.junit.Test;

import com.jtalis.core.JtalisContextImpl;
import com.jtalis.core.plengine.JPLEngineWrapper;
import com.jtalis.core.plengine.PrologEngineWrapper;

public class TestEventsWithArguments {
/*
	@Test
	public void test10EventsWithArguments() {
		try {
			PrologEngineWrapper<?> engine = new JPLEngineWrapper();
			JtalisContextImpl context = new JtalisContextImpl(engine);
			context.addEventTrigger("_");
			context.addDynamicRule("c(X) <- a(X) seq b(Y) where Y is X + 1");

			TestProvider provider = new TestProvider(
				new String[] { "a(4)", "b(4)", "a(5)", "b(6)",         "a(1234)", "b(1235)"            },
				new String[] { "a(4)", "b(4)", "a(5)", "b(6)", "c(5)", "a(1234)", "b(1235)", "c(1234)" }
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

*/
}

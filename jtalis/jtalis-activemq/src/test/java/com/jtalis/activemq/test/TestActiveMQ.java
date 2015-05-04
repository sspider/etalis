package com.jtalis.activemq.test;


import junit.framework.TestCase;
import com.jtalis.activemq.ActiveMQInputProvider;
import com.jtalis.activemq.ActiveMQOutputProvider;
import com.jtalis.core.JtalisContextImpl;
import com.jtalis.core.event.ProviderSetupException;
import com.jtalis.core.plengine.JPLEngineWrapper;
import com.jtalis.core.plengine.PrologEngineWrapper;



public class TestActiveMQ extends TestCase {
	private String url;
	private String pubTopic;
	private String subTopic;
	
	public void testActiveMQ() {
		url = "tcp://localhost:61616";
		pubTopic = "testOutputTopic";
		subTopic = "testInputTopic";
		try {
			// configure Etalis engine and rule pattern.
			PrologEngineWrapper<?> engine = new JPLEngineWrapper();
			JtalisContextImpl context = new JtalisContextImpl(engine);
			context.addEventTrigger("c/1");
			context.addDynamicRule("c(X) <- a(X) seq b(X)");
			
			// register an ActiveMQInputProvider and an ActiveMQOutputProvider
			ActiveMQInputProvider amqInputProvider = new ActiveMQInputProvider(url, subTopic);
			ActiveMQOutputProvider amqOutputProvider = new ActiveMQOutputProvider(url, pubTopic);
			context.registerInputProvider(amqInputProvider);
			context.registerOutputProvider(amqOutputProvider);

			
			// Create a subscriber to subscribe the testOutputTopic
			JtalisActiveMQSubscriber subscriber = new JtalisActiveMQSubscriber(url, pubTopic);
			subscriber.run();
						
			// Publish atomic event to the testInputTopic 
			JtalisActiveMQPublisher publisher = JtalisActiveMQPublisher.getTopicPublisherInstance(url,subTopic);
			publisher.publishTextMsg("a(1)");
			Thread.sleep(500);
			publisher.publishTextMsg("b(1)");
			Thread.sleep(500);
			publisher.stopPublisher();
			// Compare the result with expect
			// In this case, Etalis is feeded with a(1), b(1),
			// so the result should contain c(1)
			// And format of the result is like :
			// Event: [c(1), 21:58:03 #1, 21:58:03 #2]
			// 
			
			if (subscriber.receivedEvent != null) {
				if (!subscriber.receivedEvent.contains("c(1)"))
					fail("Result is not correct: " + subscriber.receivedEvent);
			}
			else
				fail("ActiveMQOutputProvider does not work!");
			
			
			subscriber.close();
			amqInputProvider.shutdown();
			amqOutputProvider.shutdown();
			
			
		} catch (ProviderSetupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}

package com.jtalis.activemq;

import java.util.LinkedList;
import java.util.Queue;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.core.event.EventBuilder;
import com.jtalis.core.event.InvalidEventFormatException;
import com.jtalis.core.event.JtalisInputEventProvider;
import com.jtalis.core.event.ProviderSetupException;
import com.veskogeorgiev.probin.annotations.Parameter;

public class ActiveMQInputProvider implements JtalisInputEventProvider, MessageListener {
	@Parameter("url")
	private String url;
	@Parameter("topic")
	private String topic;
	
	private Queue<String> inputEventQueue;
	
	private Connection connection;
	private String password;
	private String user;
	
	private boolean stopSubscriber;
	
	public ActiveMQInputProvider() {
		
	}
	
	public ActiveMQInputProvider(String url, String topic) throws ProviderSetupException {
		this.url = url;
		this.topic = topic;
		stopSubscriber = false;
		setup();
	}
	

	public void setup() throws ProviderSetupException {
		// TODO Auto-generated method stub
		if (url == null || url.length() == 0) {
			url = "tcp://localhost:61616";
		}
		if (topic == null || topic.length() == 0) {
			topic = "JtalisInputEvent";
		}
		
		inputEventQueue = new LinkedList<String>();

		try {
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
					user, password, url);
			Session session = null;
			MessageConsumer consumer = null;
			connection = connectionFactory.createConnection();

			session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
			Topic mytopic = session.createTopic(topic);
			consumer = session.createConsumer(mytopic);
			consumer.setMessageListener(this);
			connection.start();

		} catch (JMSException e) {
			e.printStackTrace();
			throw new ProviderSetupException(e.getMessage());
		}
	}

	public void shutdown() {
		// TODO Auto-generated method stub
		try {
			connection.stop();
			connection.close();
			stopSubscriber = true;
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean hasMore() {
		// TODO Auto-generated method stub
		//return inputEventQueue.isEmpty();
		if (stopSubscriber)
			return false;
		else
			return inputEventQueue.isEmpty();
	}

	public EtalisEvent getEvent() {
		// TODO Auto-generated method stub
		while (true) {
			if (stopSubscriber) 
				break;
										
			if (!inputEventQueue.isEmpty()) {
				String eventString = inputEventQueue.poll();
				//System.out.println("Event from ActiveMQ: " + eventString);
				try {
					return EventBuilder.buildEventFromString(eventString);
				} catch (InvalidEventFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
			}
				
		}
		return null;
	}


	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		try {
			if ((message instanceof TextMessage)) {
				System.out.println(((TextMessage) message).getText());
				inputEventQueue.add(((TextMessage) message).getText());
			} 
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

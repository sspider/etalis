package com.jtalis.activemq;


import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.JMSException;

import javax.jms.MessageProducer;


import org.apache.activemq.ActiveMQConnectionFactory;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.core.event.JtalisOutputEventProvider;
import com.jtalis.core.event.ProviderSetupException;
import com.veskogeorgiev.probin.annotations.Parameter;


import java.util.logging.Logger;


public class ActiveMQOutputProvider implements JtalisOutputEventProvider{
	@Parameter("url")
	private String url;
	@Parameter("topic")
	private String topic;

	private Connection connection;
	private Session session;
	private MessageProducer publisher;
	ActiveMQConnectionFactory factory;
	
	private static final Logger logger = Logger.getLogger(ActiveMQOutputProvider.class.getName());
	
	public ActiveMQOutputProvider() {
		
	}
	
	public ActiveMQOutputProvider(String url, String topic) throws ProviderSetupException {
		logger.info(url + " " + topic);
		this.url = url;
		this.topic = topic;
		
		setup();
	}

	public void setup() throws ProviderSetupException {
		// TODO Auto-generated method stub
		// Create ActiveMQ connection
		if (url == null || url.length() == 0) {
			url = "tcp://localhost:61616";
		}
		if (topic == null || topic.length() == 0) {
			topic = "JtalisFiredEvent";
		}

		try {
			factory = new ActiveMQConnectionFactory(url);
			connection = factory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Topic mytopic = session.createTopic(topic);
			publisher = session.createProducer(mytopic);
			publisher.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			connection.start();
		}
		catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ProviderSetupException(e.getMessage());
		}
	}



	public void shutdown() {
		// TODO Auto-generated method stub
		try {
			connection.stop();
			connection.close();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void outputEvent(EtalisEvent event) {
		// TODO Auto-generated method stub
		try {
			publisher.send(session.createTextMessage(event.toString()));
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	

}

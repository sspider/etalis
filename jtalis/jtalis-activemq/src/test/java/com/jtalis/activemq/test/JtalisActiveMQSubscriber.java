package com.jtalis.activemq.test;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import junit.framework.TestCase;


import org.apache.activemq.ActiveMQConnectionFactory;

public class JtalisActiveMQSubscriber implements MessageListener {
	private String url;
	private String user;
	private String password;
	Connection connection;
    String topic;
    
    boolean sensorstate = true;
    long currenttime = 0;
    long delaytime = 10000;
    
    public String receivedEvent = null;
    
	public JtalisActiveMQSubscriber(String url, String topic) {
		this.url = url;
		this.topic = topic;
	}

	public void run() {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				user, password, url);
		Session session = null;
		MessageConsumer consumer = null;

		try {
			connection = connectionFactory.createConnection();

			session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
			Topic mytopic = session.createTopic(topic);
			consumer = session.createConsumer(mytopic);
			consumer.setMessageListener(this);
			connection.start();

		} catch (JMSException e) {
			e.printStackTrace();
		}

	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public void onMessage(Message message) {

		try {
			if ((message instanceof TextMessage)) {
				System.out.println("RECEIVE " + ((TextMessage) message).getText());
				// Store the result in receivedEvent for the comparing
				receivedEvent = ((TextMessage) message).getText();
			} else
				System.out.println("got a unreadable message!");
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void close() {
		try {
			connection.close();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

}

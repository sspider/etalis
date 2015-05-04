package com.jtalis.activemq.test;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

public class JtalisActiveMQPublisher {

	private Connection connection;
	private Session session;
	private MessageProducer publisher;
	private static String url = "tcp://localhost:61616";
	String messagecontent = "hello world!";
	ActiveMQConnectionFactory factory;
	static String mytopic = "";
	private static JtalisActiveMQPublisher mypublisher = null;

	private JtalisActiveMQPublisher() {
		startPublisher();
	}

	public static JtalisActiveMQPublisher getTopicPublisherInstance(String broker_url,
			String topic) {
		
		if (!url.equals(broker_url) || !mytopic.equals(topic)) {
			url = broker_url;
			mytopic = topic;
			mypublisher = new JtalisActiveMQPublisher();
			return mypublisher;
		}else if(mypublisher == null){
			mypublisher = new JtalisActiveMQPublisher();

		}
		return mypublisher;
	}

	private void startPublisher() {
		try {
			factory = new ActiveMQConnectionFactory(url);
			connection = factory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Topic topic = session.createTopic(mytopic);
			publisher = session.createProducer(topic);
			publisher.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			connection.start();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public MapMessage getMapMessage() throws JMSException {
		return session.createMapMessage();
	}

	public void publishMapMsg(MapMessage message) {
		try {
			publisher.send(message);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void publishTextMsg(String message) throws Exception {

		publisher.send(session.createTextMessage(message));

	}

	public void stopPublisher() {

		try {
			connection.stop();
			connection.close();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

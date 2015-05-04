package com.jtalis.storm.zstream.spout;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.core.event.EventBuilder;
import com.jtalis.core.event.InvalidEventFormatException;
import com.jtalis.storm.zstream.event.EtalisEventBuilder;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.Message;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class KafkaInputSpout extends BaseRichSpout{
	// act as kafka consumer, it will retrieve message from kafka queue broker
	// and emit the message to stream inside topology
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private SpoutOutputCollector collector;
	private ConsumerConnector consumer;
	private String zkConnect, groupId, topic, streamId;
	private boolean spoutStarted = false;
	
	private ConsumerIterator consumerIterator;
	
	public KafkaInputSpout(String zkConnect, String groupId, 
			String topic, String streamId) {
		this.zkConnect = zkConnect;
		this.groupId = groupId;
		this.topic = topic;
		this.streamId = streamId;
	}
	
	@Override
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		// TODO Auto-generated method stub
		this.collector = collector;
		consumer = kafka.consumer.Consumer.createJavaConsumerConnector(
				createConsumerConfig());
		
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(topic, new Integer(1));
		Map<String, List<KafkaStream<Message>>> consumerMap = consumer.createMessageStreams(topicCountMap);
		KafkaStream<Message> stream =  consumerMap.get(topic).get(0);
		consumerIterator = stream.iterator();
	}

	@Override
	public void nextTuple() {
		// TODO Auto-generated method stub
		
		Message msg = (Message) consumerIterator.next().message();
		if (msg != null && msg.isValid()) {
			//EtalisEvent event = EventBuilder.buildEventFromString(this.getMessage(msg));
			EtalisEvent event = EtalisEventBuilder.buildEtalisEventFromString(
					this.getMessage(msg));
			
			collector.emit(streamId, new Values(event));
			
		}
		
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		declarer.declareStream(streamId, new Fields("EtalisEvent"));
	}
	
	private String getMessage(Message message) {
	    ByteBuffer buffer = message.payload();
	    byte [] bytes = new byte[buffer.remaining()];
	    buffer.get(bytes);
	    return new String(bytes);
	  }
	
	private ConsumerConfig createConsumerConfig() {
		Properties props = new Properties();
		props.put("zk.connect", zkConnect);
		props.put("groupid", groupId);
		props.put("zk.sessiontimeout.ms", "400");
		props.put("zk.synctime.ms", "200");
		props.put("autocommit.interval.ms", "1000");

		return new ConsumerConfig(props);

	}

}

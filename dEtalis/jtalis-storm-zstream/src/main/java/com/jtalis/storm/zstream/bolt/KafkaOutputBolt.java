package com.jtalis.storm.zstream.bolt;

import java.util.Map;
import java.util.Properties;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.storm.zstream.event.EtalisEventParser;

import kafka.javaapi.producer.Producer;
import kafka.javaapi.producer.ProducerData;
import kafka.producer.ProducerConfig;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

public class KafkaOutputBolt extends BaseRichBolt{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Producer<String, String> producer;
	private String zkConnect, serializerClass, topic;
	
	public KafkaOutputBolt(String zkConnect, String serializerClass, String topic) {
		this.zkConnect = zkConnect;
		this.serializerClass = serializerClass;
		this.topic = topic;
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		// TODO Auto-generated method stub
		Properties props = new Properties();
		props.put("zk.connect", zkConnect);
		props.put("serializer.class", serializerClass);
		ProducerConfig config = new ProducerConfig(props);
		producer = new Producer<String, String>(config);
		
	}

	@Override
	public void execute(Tuple input) {
		// TODO Auto-generated method stub
		Object obj = input.getValue(0);
		if (obj instanceof EtalisEvent) {
			String msg = EtalisEventParser.buildStringFromEtalisEvent((EtalisEvent) obj);
			System.out.println("KafkaOutputBolt receives : " + msg);
			ProducerData<String, String> data = new ProducerData<String, String>(topic, msg);
			producer.send(data);
		}
		//String msg = input.getString(0);
		
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		
	}
	// emit tuples to kafka queue broker
	
}

package com.jtalis.storm.zstream.topology;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;

import com.jtalis.storm.zstream.bolt.KafkaOutputBolt;
import com.jtalis.storm.zstream.bolt.SimpleBolt;
import com.jtalis.storm.zstream.bolt._SequenceOperatorBolt;
import com.jtalis.storm.zstream.spout.KafkaInputSpout;
import com.jtalis.storm.zstream.spout.KafkaTestSpout;

public class KafkaTestTopology {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
		// TODO Auto-generated method stub


		TopologyBuilder builder = new TopologyBuilder();

		// Spout definition for left child node and right child node
		KafkaTestSpout inputSpout = 
				new KafkaTestSpout(KafkaProperties.zkConnect, 
						KafkaProperties.groupId, 
						"test1", 
						"inputStream");

		builder.setSpout("inputSpout", inputSpout, 1);
		// Bolt definition


		//SequenceBolt seqBolt = new SequenceBolt(rule, regex, timeWindow);
/*		KafkaOutputBolt outputBolt = 
				new KafkaOutputBolt(KafkaProperties.zkConnect, 
						KafkaProperties.serializerClass, 
						"test2");*/
		

		builder.setBolt("enclose1", new SimpleBolt(), 1)
			.shuffleGrouping("inputSpout", "inputStream");

		Config conf = new Config();
		conf.setDebug(true);
		conf.setNumWorkers(2);
		conf.setMaxSpoutPending(100);

		//LocalCluster cluster = new LocalCluster();
		StormSubmitter.submitTopology("kafka-test", conf, builder.createTopology());


	}

}


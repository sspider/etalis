package com.jtalis.storm.zstream.topology;

import com.jtalis.storm.zstream.bolt.KafkaOutputBolt;
import com.jtalis.storm.zstream.bolt.SequenceBolt;
import com.jtalis.storm.zstream.bolt.SequenceOperatorBolt;
import com.jtalis.storm.zstream.spout.KafkaInputSpout;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;


public class JtalisSEQTopology {

	/**
	 * @param args
	 * @throws InvalidTopologyException 
	 * @throws AlreadyAliveException 
	 */
	public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
		// TODO Auto-generated method stub
		if (args.length != 7) {
			System.out.println("Error input parameters.");
		} else {
			
			
			String topologyName = args[0];
			System.out.println("topology name " + topologyName);
			String leftChildTopic = args[1];
			String rightChildTopic = args[2];
			String outputTopic = args[3];
			System.out.println("topology name " + outputTopic);
			String rule = args[4];
			System.out.println("rule " + rule);
			String regex = args[5];
			long timeWindow = Long.valueOf(args[6]);
			System.out.println("Timewindow(ms) " + timeWindow);
			
			
			TopologyBuilder builder = new TopologyBuilder();
			
			// Spout definition for left child node and right child node
			KafkaInputSpout leftChildSpout = 
					new KafkaInputSpout(KafkaProperties.zkConnect, 
							KafkaProperties.groupId, 
							leftChildTopic, 
							"leftChild");
			
			KafkaInputSpout rightChildSpout = 
					new KafkaInputSpout(KafkaProperties.zkConnect, 
							KafkaProperties.groupId, 
							rightChildTopic, 
							"rightChild");
			
			builder.setSpout("leftChildSpout", leftChildSpout, 1);
			builder.setSpout("rightChildSpout", rightChildSpout, 1);
			// Bolt definition
			
			
			//SequenceBolt seqBolt = new SequenceBolt(rule, regex, timeWindow);
			SequenceOperatorBolt seqBolt = new SequenceOperatorBolt(rule, regex, timeWindow);
			
			KafkaOutputBolt outputBolt = 
					new KafkaOutputBolt(KafkaProperties.zkConnect, 
							KafkaProperties.serializerClass, 
							outputTopic);
			
			builder.setBolt("seqBolt", seqBolt, 1)
				.shuffleGrouping("leftChildSpout", "leftChild")
				.shuffleGrouping("rightChildSpout", "rightChild");
			
			builder.setBolt("outputBolt", outputBolt, 1)
				.shuffleGrouping("seqBolt");
			
			Config conf = new Config();
			conf.setDebug(false);
	        conf.setNumWorkers(5);
	        conf.setMaxSpoutPending(100);

	        //LocalCluster cluster = new LocalCluster();
	        StormSubmitter.submitTopology(topologyName, conf, builder.createTopology());
			
		}
	}

}

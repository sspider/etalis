package com.jtalis.storm.zstream.topology;

import com.jtalis.storm.zstream.bolt.SimpleBolt;
import com.jtalis.storm.zstream.bolt.SimpleBolt2;
import com.jtalis.storm.zstream.spout.SimpleSpout;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

public class SimpleTopology {

	/**
	 * @param args
	 * @throws InvalidTopologyException 
	 * @throws AlreadyAliveException 
	 */
	public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
		// TODO Auto-generated method stub
		TopologyBuilder builder = new TopologyBuilder();
        
		builder.setSpout("words", new SimpleSpout(), 1);        
		builder.setBolt("enclose1", new SimpleBolt(), 2)
		        .shuffleGrouping("words");
		builder.setBolt("enclose2", new SimpleBolt2(), 2)
		        .shuffleGrouping("enclose1");
		

		Config conf = new Config();
		conf.setDebug(false);
        conf.setNumWorkers(5);
        conf.setMaxSpoutPending(100);

        //LocalCluster cluster = new LocalCluster();
        StormSubmitter.submitTopology("simple-topology", conf, builder.createTopology());
	}

}

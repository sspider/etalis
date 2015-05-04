package com.jtalis.storm.zstream.topology;

import com.jtalis.storm.zstream.bolt.SimpleBolt;
import com.jtalis.storm.zstream.bolt.ZSteamSeqBolt;
import com.jtalis.storm.zstream.spout.AtomEventSpout;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;

public class ZStreamDemoTopology {

	/**
	 * @param args
	 * @throws InvalidTopologyException 
	 * @throws AlreadyAliveException 
	 */
	public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
		// TODO Auto-generated method stub
		if (args.length != 3) {
			System.out.println("Input Parameters Error");
			System.out.println("Paramerts: Number of workers, AtomEvent Generator Frequence, Time Window");
		} else {
			int workers = Integer.valueOf(args[0]);
			int timeInterval = Integer.valueOf(args[1]);
			long timeWindow  = Long.valueOf(args[2]);
			
			TopologyBuilder builder = new TopologyBuilder();
			
			// Build the topology with 5 sequence operator
			
			// 1. Set 6 atom event spout			
			AtomEventSpout eventA = new AtomEventSpout("a", timeInterval, "eventA");
			AtomEventSpout eventB = new AtomEventSpout("b", timeInterval, "eventB");
			AtomEventSpout eventC = new AtomEventSpout("c", timeInterval, "eventC");
			AtomEventSpout eventD = new AtomEventSpout("d", timeInterval, "eventD");
			AtomEventSpout eventE = new AtomEventSpout("e", timeInterval, "eventE");
			AtomEventSpout eventF = new AtomEventSpout("f", timeInterval, "eventF");
			
			builder.setSpout("eventASpout", eventA, 1);
			builder.setSpout("eventBSpout", eventB, 1);
			builder.setSpout("eventCSpout", eventC, 1);
			builder.setSpout("eventDSpout", eventD, 1);
			builder.setSpout("eventESpout", eventE, 1);
			builder.setSpout("eventFSpout", eventF, 1);
			
			
			// 2. Set 5 sequence operator bolt
			ZSteamSeqBolt seq1 = new ZSteamSeqBolt("ie1", "eventTemp1", 
					timeWindow, "eventA", "eventB");
			ZSteamSeqBolt seq2 = new ZSteamSeqBolt("ie2", "eventTemp2", 
					timeWindow, "eventTemp1", "eventC");
			ZSteamSeqBolt seq3 = new ZSteamSeqBolt("ie3", "eventTemp3", 
					timeWindow, "eventTemp2", "eventD");
			ZSteamSeqBolt seq4 = new ZSteamSeqBolt("ie4", "eventTemp4", 
					timeWindow, "eventTemp3", "eventE");
			ZSteamSeqBolt seq5 = new ZSteamSeqBolt("complexEvent", "eventFinal", 
					timeWindow, "eventTemp4", "eventF");
			
			
			
			
			
			builder.setBolt("seq1Bolt", seq1, 1)
				.shuffleGrouping("eventASpout", "eventA")
				.shuffleGrouping("eventBSpout", "eventB");
			
			builder.setBolt("seq2Bolt", seq2, 1)
				.shuffleGrouping("seq1Bolt", "eventTemp1")
				.shuffleGrouping("eventCSpout", "eventC");
			
			builder.setBolt("seq3Bolt", seq3, 1)
				.shuffleGrouping("seq2Bolt", "eventTemp2")
				.shuffleGrouping("eventDSpout", "eventD");
			
			builder.setBolt("seq4Bolt", seq4, 1)
				.shuffleGrouping("seq3Bolt", "eventTemp3")
				.shuffleGrouping("eventESpout", "eventE");
			
			builder.setBolt("seq5Bolt", seq5, 1)
				.shuffleGrouping("seq4Bolt", "eventTemp4")
				.shuffleGrouping("eventFSpout", "eventF");
					
			
			// 3. Set a log bolt for logging the result
			SimpleBolt bolt = new SimpleBolt();
			
			builder.setBolt("logBolt", bolt, 1)
				.shuffleGrouping("seq5Bolt", "eventFinal");
			
			
			
			Config conf = new Config();
			conf.setDebug(false);
	        conf.setNumWorkers(workers);
	        conf.setMaxSpoutPending(5000);

	        //LocalCluster cluster = new LocalCluster();
	        StormSubmitter.submitTopology("zstream-demo", conf, builder.createTopology());
			
			
		}
		
	}

}

package com.jtalis.storm.zstream.bolt;

import java.util.Map;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.storm.zstream.agent.SequenceOperator;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

public class ZSteamSeqBolt extends BaseRichBolt{

	private String outputEventName;
	private String streamID;
	private long timeWindow;
	private String leftChildStreamID;
	private String rightChildStreamID;
	
	//private OutputCollector collector;
	
	private SequenceOperator seq;
	
	public ZSteamSeqBolt(String outputEventName, 
			String streamID, long timeWindow, 
			String leftChildStreamID, String rightChildStreamID) {
		this.outputEventName = outputEventName;
		this.streamID = streamID;
		this.timeWindow = timeWindow;
		this.leftChildStreamID = leftChildStreamID;
		this.rightChildStreamID = rightChildStreamID;
	}
	
	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		// TODO Auto-generated method stub
		//this.collector = collector;
		
		seq = new SequenceOperator(outputEventName, 
				streamID, timeWindow, collector);
	}

	@Override
	public void execute(Tuple input) {
		// TODO Auto-generated method stub
		Object obj = input.getValue(0);
		if (obj instanceof EtalisEvent) {
			String streamId = input.getSourceStreamId();
			if (streamId.equals(leftChildStreamID)) {
				seq.addEventToLeftChildBuffer((EtalisEvent) obj);
			} else if (streamId.equals(rightChildStreamID)) {
				seq.addEventToRightChildBuffer((EtalisEvent) obj);
			} else {
				// ignore
			}
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		declarer.declareStream(streamID, new Fields("EtalisEvent"));
	}

}

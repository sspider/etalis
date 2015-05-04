package com.jtalis.storm.zstream.spout;

import java.util.Map;
import java.util.Random;

import com.jtalis.core.event.EtalisEvent;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

public class AtomEventSpout extends BaseRichSpout{

	private SpoutOutputCollector collector;
	private String eventName;
	private int timeInterval;
	private String streamID;
	
	public AtomEventSpout(String eventName, int timeInterval, String streamID) {
		this.eventName = eventName;
		this.timeInterval = timeInterval;
		this.streamID = streamID;
	}
	
	@Override
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		// TODO Auto-generated method stub
		this.collector = collector;
	}

	@Override
	public void nextTuple() {
		// TODO Auto-generated method stub
		Utils.sleep(timeInterval);
		
		Random rand = new Random();
		int arg1 = rand.nextInt(10);
		
		EtalisEvent event = new EtalisEvent(eventName, arg1);
	    
	    collector.emit(streamID, new Values(event));
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		declarer.declareStream(streamID, new Fields("EtalisEvent"));
	}

}


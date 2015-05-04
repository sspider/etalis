package com.jtalis.storm.zstream.bolt;

import java.util.Map;

import com.jtalis.core.event.EtalisEvent;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class SimpleBolt extends BaseRichBolt{
	private OutputCollector collector;
	

	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		// TODO Auto-generated method stub
		this.collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		// TODO Auto-generated method stub
		Object obj = input.getValue(0);
		if (obj instanceof EtalisEvent) {
			
			System.out.println("ce: " + (EtalisEvent) obj);
		} else {
			String rtn = "(" + (String) obj + ")";
			//collector.emit(new Values(rtn));
		}
		
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		//declarer.declare(new Fields("word"));
	}

}

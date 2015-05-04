package com.jtalis.storm.zstream.bolt;

import java.util.Map;

import com.jtalis.core.JtalisContextImpl;
import com.jtalis.core.event.EtalisEvent;
import com.jtalis.core.plengine.JPLEngineWrapper;
import com.jtalis.core.plengine.PrologEngineWrapper;
import com.jtalis.storm.zstream.agent.SequenceOperator;
import com.jtalis.storm.zstream.channel.Buffer;
import com.jtalis.storm.zstream.channel.IChannel;
import com.jtalis.storm.zstream.provider.StormOutputProvider;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

public class _SequenceOperatorBolt extends BaseRichBolt{
	
	private transient PrologEngineWrapper<?> engine;
	private transient JtalisContextImpl etalisContext;
	private long timeWindow;
	private String rule;
	private String regex;
	
	private OutputCollector collector;
	
	private SequenceOperator seq;
	private IChannel out_Buffer;
	
	
	public _SequenceOperatorBolt(String rule, String regex, long timeWindow) {
		this.timeWindow = timeWindow;
		this.rule = rule;
		this.regex = regex;
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		// TODO Auto-generated method stub
		this.collector = collector;
		
		
		engine = new JPLEngineWrapper(false);
		etalisContext = new JtalisContextImpl(engine);
		etalisContext.setEtalisFlags("timestamp_mode", "on");
		

		etalisContext.addDynamicRule(rule);
		//ctx.addEventTrigger("_/_");
		etalisContext.addEventTrigger("_/_");
		
		etalisContext.registerOutputProvider(regex, new StormOutputProvider(collector));
		
		
		out_Buffer = new Buffer();
		seq = new SequenceOperator(etalisContext, timeWindow);
		
	}

	@Override
	public void execute(Tuple input) {
		// TODO Auto-generated method stub
		Object obj = input.getValue(0);
		if (obj instanceof EtalisEvent) {
			String streamId = input.getSourceStreamId();
			if (streamId.equals("leftChild")) {
				seq.addEventToLeftChildBuffer((EtalisEvent) obj);
			} else if (streamId.equals("rightChild")) {
				seq.addEventToRightChildBuffer((EtalisEvent) obj);
			} else {
				// ignore
			}
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		declarer.declare(new Fields("EtalisEventString"));
	}

}

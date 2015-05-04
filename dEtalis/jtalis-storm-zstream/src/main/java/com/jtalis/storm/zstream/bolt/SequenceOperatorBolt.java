package com.jtalis.storm.zstream.bolt;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

import com.jtalis.core.JtalisContextImpl;
import com.jtalis.core.event.EtalisEvent;
import com.jtalis.core.plengine.JPLEngineWrapper;
import com.jtalis.core.plengine.PrologEngineWrapper;
import com.jtalis.storm.zstream.agent.SequenceOperator_Old;
import com.jtalis.storm.zstream.channel.NodeBuffer;
import com.jtalis.storm.zstream.provider.StormOutputProvider;

@SuppressWarnings("serial")
public class SequenceOperatorBolt extends BaseRichBolt{
	
	private transient PrologEngineWrapper<?> engine;
	private transient JtalisContextImpl etalisContext;
	private long timeWindow;
	private String rule;
	private String regex;
	
	//
	private NodeBuffer leftChildBuffer;
	private NodeBuffer rightChildBuffer;
	//private NodeBuffer internalBuffer;
	private SequenceOperator_Old sequenceOP;
	
	private OutputCollector collector;
	
	public SequenceOperatorBolt(String rule, String regex, long timeWindow) {
		this.timeWindow = timeWindow;
		this.rule = rule;
		this.regex = regex;
	}
	
	
	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
		
		// initialize the node buffer
		leftChildBuffer = new NodeBuffer(timeWindow);
		rightChildBuffer = new NodeBuffer(timeWindow);
		//internalBuffer = new NodeBuffer(timeWindow);
		
		
		engine = new JPLEngineWrapper(false);
		etalisContext = new JtalisContextImpl(engine);
		etalisContext.setEtalisFlags("timestamp_mode", "on");
		

		etalisContext.addDynamicRule(rule);
		//ctx.addEventTrigger("_/_");
		etalisContext.addEventTrigger("_/_");
		
		etalisContext.registerOutputProvider(regex, new StormOutputProvider(collector));
		
		sequenceOP = new SequenceOperator_Old(leftChildBuffer, rightChildBuffer, timeWindow, etalisContext);
		sequenceOP.start();
		//etalisContext.registerOutputProvider(new ZstreamOutputProvider(internalBuffer));

	}
	@Override
	public void execute(Tuple input) {
		// TODO Auto-generated method stub

		Object obj = input.getValue(0);
		if (obj instanceof EtalisEvent) {
			String streamId = input.getSourceStreamId();
			if (streamId.equals("leftChild")) {
				leftChildBuffer.addNewEvent((EtalisEvent) obj);
			} else if (streamId.equals("rightChild")) {
				rightChildBuffer.addNewEvent((EtalisEvent) obj);
			} else {
				// ignore
			}
			
/*			// do the seqeuence  
			if (!leftChildBuffer.isBufferEmpty() && 
					!rightChildBuffer.isBufferEmpty()) {
				//System.out.println("left Buffer size : " + leftChildBuffer.getBufferSize());
				//System.out.println("right Buffer size : " + rightChildBuffer.getBufferSize());
				sequence(leftChildBuffer, rightChildBuffer);
			}*/
		}
		


		
/*		// out put the result
		if (!internalBuffer.isBufferEmpty()) {
			for (EtalisEvent event : internalBuffer.getCurrentBuffer()) {
				System.out.println("internal buffer has " + event);
				collector.emit("interalStream", new Values(event.getPrologString()));
				internalBuffer.removeFirstEvent();
			}
			
		}*/


	}
	
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		declarer.declare(new Fields("EtalisEventString"));
	}
	
	
	private synchronized void sequence(NodeBuffer leftChildBuffer, 
			NodeBuffer rightChildBuffer) {
		
		for(EtalisEvent finalEvent : rightChildBuffer.getCurrentBuffer()) {
/*			System.out.println(finalEvent.getPrologString() + " " 
						+ finalEvent.getTimeStarts().getTime() );*/
			long EAT = finalEvent.getTimeStarts().getTime() - timeWindow;
			boolean isCombined = false;
/*			System.out.println("EAT " + EAT );*/
			for(EtalisEvent leftEvent : leftChildBuffer.getCurrentBuffer()) {
/*				System.out.println(leftEvent.getTimeStarts().getTime() + " " 
						+ finalEvent.getTimeStarts().getTime());*/
				if (leftEvent.getTimeStarts().getTime() < EAT) {
					leftChildBuffer.getCurrentBuffer().remove(leftEvent);
					continue;
				} else if (leftEvent.getTimeEnds().getTime() 
						>= finalEvent.getTimeStarts().getTime())
					break;
				combineEtalisEvent(leftEvent, finalEvent);
				isCombined = true;
			}
			if (isCombined 
					&& (System.currentTimeMillis() - finalEvent.getTimeStarts().getTime()) > timeWindow)
				rightChildBuffer.getCurrentBuffer().remove(finalEvent);
		}
		
	}
	
	
	private void combineEtalisEvent(EtalisEvent leftEvent, EtalisEvent rightEvent) {
		//combine the event from left child buffer and right child buffer
		//TODO: feed the event to jtalis and store the result to the internal buffer
		//System.out.println(leftEvent.getPrologString() + " " + leftEvent.getTimeStarts().getTime());
		//System.out.println(rightEvent.getPrologString() + " " + rightEvent.getTimeStarts().getTime());
		etalisContext.pushEvent(leftEvent);
		etalisContext.pushEvent(rightEvent);				
	}
	
}
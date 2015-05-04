/**
 * 
 */
package com.jtalis.storm.zstream.agent;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.lang.ArrayUtils;

import backtype.storm.task.OutputCollector;
import backtype.storm.tuple.Values;

import com.jtalis.core.JtalisContext;
import com.jtalis.core.event.EtalisEvent;
import com.jtalis.storm.zstream.channel.IChannel;

/**
 * Designed to be easily extended by other agents (e.g., Meets, During, Overlaps etc.)
 * Designed to be easily nested with operators, and hence to build a tree plan.
 * Event-driven (without threads)
 * With ConcurrentLinkedQueue as NodeBuffers (left and right child buffers)
 * With one-level rightChildBuffer
 * 
 * @author darko
 *
 */
public abstract class AbstractSequenceOperator implements IAgent{
	private static final Object[] Object = null;
	private ConcurrentLinkedQueue<EtalisEvent> leftChildBuffer;
	private ConcurrentLinkedQueue<EtalisEvent> rightChildBuffer;
	private IChannel outChannel;
	private long timeWindow;
	private JtalisContext ctx;	
	private int noResult = 0;
	
	private boolean isStorm = false;
	private String outputEventName;
	private String streamID;
	private OutputCollector collector;
	
	private boolean isKafka = false;
	
		
	public AbstractSequenceOperator (IChannel outChannel, long timeWindow) {
		this.leftChildBuffer = new ConcurrentLinkedQueue<EtalisEvent>();
		this.rightChildBuffer = new ConcurrentLinkedQueue<EtalisEvent>();
		this.outChannel = outChannel;
		this.timeWindow = timeWindow;
	}
	
	public AbstractSequenceOperator(String outputEventName, String streamID,
			long timeWindow, OutputCollector collector) {
		// This constructor is used in Storm
		this.leftChildBuffer = new ConcurrentLinkedQueue<EtalisEvent>();
		this.rightChildBuffer = new ConcurrentLinkedQueue<EtalisEvent>();
		this.isStorm = true;
		
		this.outputEventName = outputEventName;
		this.streamID = streamID;
		this.timeWindow = timeWindow;
		this.collector = collector;
	}
	
	public AbstractSequenceOperator(JtalisContext ctx, long timeWindow) {
		// This constructor is used in Storm with Kafka
		this.leftChildBuffer = new ConcurrentLinkedQueue<EtalisEvent>();
		this.rightChildBuffer = new ConcurrentLinkedQueue<EtalisEvent>();
		
		this.ctx = ctx;
		this.timeWindow = timeWindow;
		this.isKafka = true;
	}
	
	public void execute() {
		while (!this.rightChildBuffer.isEmpty()) {
			sequence();
		}
	}
	
	private void sequence() {
		long EAT;
		EtalisEvent rightEvent;
		EtalisEvent leftEvent;
		Iterator<EtalisEvent> it;
		
		while(!rightChildBuffer.isEmpty()) {
			rightEvent = rightChildBuffer.poll();
			EAT = rightEvent.getTimeEnds().getTime() - timeWindow;
			it = leftChildBuffer.iterator();
			while(it.hasNext()) {
				leftEvent = it.next();
				
				//System.out.println("ce: " + leftEvent + rightEvent);
				if(! evaluateTimeConstraint(
						leftEvent.getTimeStarts().getTime(), leftEvent.getTimeEnds().getTime(), 
						rightEvent.getTimeStarts().getTime(), rightEvent.getTimeEnds().getTime())){
					break;
				}
				if (leftEvent.getTimeStarts().getTime() < EAT) {
					leftChildBuffer.remove(leftEvent);
					continue;
				} 
				combineEtalisEvent(leftEvent, rightEvent);
			}
		}
	}
	
	
	private void combineEtalisEvent(EtalisEvent leftEvent, EtalisEvent rightEvent) {
		//combine the event from left child buffer and right child buffer
		//TODO: feed the event to jtalis and store the result to the internal buffer
		/*ctx.pushEvent(leftEvent);
		ctx.pushEvent(rightEvent);*/
		if (isKafka) {
			ctx.pushEvent(leftEvent);
			ctx.pushEvent(rightEvent);
			
		} else {
			if (isStorm) {
				
				EtalisEvent event = new EtalisEvent(outputEventName, 
						ArrayUtils.addAll(leftEvent.getProperties(), rightEvent.getProperties()));
				collector.emit(this.streamID, new Values(event));
				
			} else {
				System.out.println("ce: " + leftEvent + rightEvent);
				this.noResult++;
				// TODO: correct this!
				this.outChannel.add(leftEvent);
			}
		}
				
	}
	
	protected abstract boolean evaluateTimeConstraint(
			long leftEventStarts, long leftEventEnds, 
			long rightEventStarts, long rightEventEnds);
	
	public long getNoResult() {
		return noResult;
	}
	
	public void addEventToLeftChildBuffer(EtalisEvent event) {
		this.leftChildBuffer.add(event);
	}
	
	public void addEventToRightChildBuffer(EtalisEvent event) {
		this.rightChildBuffer.add(event);
		this.execute();
	}
}

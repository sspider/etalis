/**
 * 
 */
package com.jtalis.storm.zstream.agent;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.jtalis.core.JtalisContext;
import com.jtalis.core.event.EtalisEvent;
import com.jtalis.core.event.EventTimestamp;
import com.jtalis.storm.zstream.channel.IChannel;

/**
 * @author darko
 *
 */
public class ConjunctionOperator implements IAgent{
	private ConcurrentLinkedQueue<EtalisEvent> leftChildBuffer;
	private ConcurrentLinkedQueue<EtalisEvent> rightChildBuffer;
	private ConcurrentLinkedQueue<EtalisEvent> leftLowerChildBuffer;
	private ConcurrentLinkedQueue<EtalisEvent> rightLowerChildBuffer;
	private IChannel outBuffer;
	private long timeWindow;
	private JtalisContext ctx;	
	private int noResult = 0;
	
	private EtalisEvent rightEvent;
	
	public ConjunctionOperator(IChannel outBuffer, long timeWindow) {
		this.leftChildBuffer = new ConcurrentLinkedQueue<EtalisEvent>();
		this.rightChildBuffer = new ConcurrentLinkedQueue<EtalisEvent>();
		this.leftLowerChildBuffer = new ConcurrentLinkedQueue<EtalisEvent>();
		this.rightLowerChildBuffer = new ConcurrentLinkedQueue<EtalisEvent>();
		this.outBuffer = outBuffer;
		this.timeWindow = timeWindow;
	}
	
	public void executeRight() {
		if(! leftLowerChildBuffer.isEmpty()){
			sequenceDown(leftLowerChildBuffer);	
		}
		sequenceUp(leftChildBuffer, leftLowerChildBuffer);
	}
	
	public void executeLeft() {
		if(! rightLowerChildBuffer.isEmpty()) {
			sequenceDown(rightLowerChildBuffer);	
		}
		sequenceUp(rightChildBuffer, rightLowerChildBuffer);
	}
	
	private void sequenceUp(
			ConcurrentLinkedQueue<EtalisEvent> leftBuffer, 
			ConcurrentLinkedQueue<EtalisEvent> leftLowerBuffer) {
		EtalisEvent leftEvent;
		long EAT;
		
		EAT = rightEvent.getTimeEnds().getTime() - timeWindow;
		while(!leftBuffer.isEmpty()){
			leftEvent = leftBuffer.poll();
			if (leftEvent.getTimeStarts().getTime() > EAT) {
				leftLowerBuffer.add(leftEvent);
				combineEtalisEvent(leftEvent, rightEvent);
				continue;
			} 
		}
	}
	
	private void sequenceDown(
			ConcurrentLinkedQueue<EtalisEvent> leftLowerBuffer) {
		EtalisEvent leftEvent;
		long EAT;
		Iterator<EtalisEvent> it;
		
		EAT = rightEvent.getTimeEnds().getTime() - timeWindow;
		it = leftLowerBuffer.iterator();
		while(it.hasNext()) {
			leftEvent = it.next();
			if (leftEvent.getTimeStarts().getTime() > EAT) {
				combineEtalisEvent(leftEvent, rightEvent);
			}else{
				leftLowerBuffer.remove(leftEvent);
			}
		}
	}
	
	private void combineEtalisEvent(EtalisEvent leftEvent, EtalisEvent rightEvent) {
		//combine the event from left child buffer and right child buffer
		//TODO: feed the event to jtalis and store the result to the internal buffer
		/*ctx.pushEvent(leftEvent);
		ctx.pushEvent(rightEvent);*/
		//System.out.println("ce: " + leftEvent + rightEvent);
		this.noResult++;
		
		EventTimestamp t1 = leftEvent.getTimeStarts().getTime() < rightEvent.getTimeStarts().getTime() ? leftEvent.getTimeStarts() : rightEvent.getTimeStarts();
		EventTimestamp t2 = leftEvent.getTimeEnds().getTime() > rightEvent.getTimeEnds().getTime() ? leftEvent.getTimeEnds() : rightEvent.getTimeEnds();
		Object[] ret = new Object[leftEvent.getArity()+rightEvent.getArity()];
		for (int i = 0; i < leftEvent.getArity(); i++) {
			ret[i] = leftEvent.getProperty(i);
		}
		for (int i = 0; i < rightEvent.getArity(); i++) {
			ret[leftEvent.getArity() + i] = rightEvent.getProperty(i);
		}
		EtalisEvent e = 
				new EtalisEvent(leftEvent.getName() + "-" + rightEvent.getName(), t1, t2, ret);
		System.out.println("ce: " + e);
		// TODO: correct this!
		this.outBuffer.add(e);
	}
	
	public long getNoResult() {
		return noResult;
	}
	
	public void addEventToLeftChildBuffer(EtalisEvent event) {
		this.leftChildBuffer.add(event);
		this.rightEvent = event;
		this.executeLeft();
	}
	
	public void addEventToRightChildBuffer(EtalisEvent event) {
		this.rightChildBuffer.add(event);
		this.rightEvent = event;
		this.executeRight();
	}
}

package com.jtalis.storm.zstream.agent;


import com.jtalis.core.JtalisContext;
import com.jtalis.core.event.EtalisEvent;
import com.jtalis.storm.zstream.channel.NodeBuffer;

/**
 * @deprecated use {SequenceOperator} instead.
 * 
 * @author darko
 *
 */
public class SequenceOperator_Old extends Thread{
	private NodeBuffer leftChildBuffer;
	private NodeBuffer rightChildBuffer;
	private long timeWindow;
	private JtalisContext ctx;	
	private int noResult = 0;
	
	public SequenceOperator_Old(NodeBuffer leftChildBuffer,
			NodeBuffer rightChildBuffer,
			long timeWindow,
			JtalisContext ctx) {
		this.leftChildBuffer = leftChildBuffer;
		this.rightChildBuffer = rightChildBuffer;
		this.timeWindow = timeWindow;
		this.ctx = ctx;
		//ctx.addDynamicRule(etalisRule);
		
		
	}
	
	public void run() {
		while(true) {
			if (!rightChildBuffer.getCurrentBuffer().isEmpty() 
					&& !leftChildBuffer.getCurrentBuffer().isEmpty()) {
				
				sequence(leftChildBuffer, rightChildBuffer);
			}
		}
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
			if (isCombined)
				rightChildBuffer.getCurrentBuffer().remove(finalEvent);
		}
		
	}
	
	
	private void combineEtalisEvent(EtalisEvent leftEvent, EtalisEvent rightEvent) {
		//combine the event from left child buffer and right child buffer
		//TODO: feed the event to jtalis and store the result to the internal buffer
		/*ctx.pushEvent(leftEvent);
		ctx.pushEvent(rightEvent);*/
		System.out.println("ce: " + leftEvent + rightEvent);
		this.noResult++;
	}

	public int getNoResult() {
		return noResult;
	}
	
	
}


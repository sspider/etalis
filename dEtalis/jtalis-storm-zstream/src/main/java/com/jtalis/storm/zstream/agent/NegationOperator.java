/**
 * 
 */
package com.jtalis.storm.zstream.agent;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.storm.zstream.channel.IChannel;

/**
 * This is an implementation of the negation operator via 
 * the NSEQ operator and the SEQ operator. 
 * 
 * @author darko
 *
 */
public class NegationOperator implements ITernaryAgent {
	private ConcurrentLinkedQueue<EtalisEvent> leftChildBuffer;
	private LinkedBlockingDeque<EtalisEvent> negatedChildBuffer;
	private ConcurrentLinkedQueue<EtalisEvent> rightChildBuffer;
	private ConcurrentLinkedQueue<EtalisEventPair> nseqChildBuffer;
	private IChannel outBuffer;
	private long timeWindow;
	private int noResult = 0;

	public NegationOperator(IChannel outBuffer, long timeWindow) {
		this.leftChildBuffer = new ConcurrentLinkedQueue<EtalisEvent>();
		this.negatedChildBuffer = new LinkedBlockingDeque<EtalisEvent>();
		this.rightChildBuffer = new ConcurrentLinkedQueue<EtalisEvent>();
		this.nseqChildBuffer = new ConcurrentLinkedQueue<EtalisEventPair>();
		this.outBuffer = outBuffer;
		this.timeWindow = timeWindow;
	}

	private void execute() {
		while (!this.rightChildBuffer.isEmpty()) {
			nsequence();
			sequence();
		}
	}
	
	private void sequence() {
		long EAT;
		
		for (EtalisEventPair rightEventPair : this.nseqChildBuffer) {
			EAT = rightEventPair.e2.getTimeEnds().getTime() - timeWindow;
			for (EtalisEvent leftEvent : this.leftChildBuffer) {
				if (leftEvent.getTimeEnds().getTime() < rightEventPair.e2.getTimeStarts().getTime()) {
					if (leftEvent.getTimeStarts().getTime() < EAT) {
						leftChildBuffer.remove(leftEvent);
						continue;
					}
					
					if(rightEventPair.e1 == null ||
						leftEvent.getTimeStarts().getTime() > rightEventPair.e1.getTimeEnds().getTime()){
							combineEtalisEvent(leftEvent, rightEventPair);
					}
				}
			}
			this.nseqChildBuffer.remove(rightEventPair);
		}
	}
	
	private void nsequence() {
		//EtalisEvent negatedEvent;
		long EAT;
		
		for (EtalisEvent rightEvent : this.rightChildBuffer) {
			EAT = rightEvent.getTimeEnds().getTime() - timeWindow;
			//for (int j = this.negatedChildBuffer.size() - 1; j >= 0; j--) {
			for(EtalisEvent negatedEvent : this.negatedChildBuffer){	
				//negatedEvent = negatedChildBuffer.get(j);
				if (negatedEvent.getTimeStarts().getTime() < EAT) {
					this.nseqChildBuffer.add(this.new EtalisEventPair(null, rightEvent));
					this.negatedChildBuffer.removeAll(this.negatedChildBuffer);
					break;
				}
				if (negatedEvent.getTimeEnds().getTime() < rightEvent.getTimeStarts().getTime()) {
					this.nseqChildBuffer.add(this.new EtalisEventPair(negatedEvent, rightEvent));
					break;
				}
			}
			if(this.negatedChildBuffer.isEmpty()){
				this.nseqChildBuffer.add(this.new EtalisEventPair(null, rightEvent));
			}
		}
		//TODO: check out whether removal of the current object is sufficient:
		//this.rightChildBuffer.remove(rightEvent);
		this.rightChildBuffer.removeAll(this.rightChildBuffer);
	}
	
	private void combineEtalisEvent(EtalisEvent leftEvent,
			EtalisEventPair rightEventPair) {
		// combine the event from left child buffer and right child buffer
		// TODO: feed the event to jtalis and store the result to the internal
		// buffer
		/*
		 * ctx.pushEvent(leftEvent); ctx.pushEvent(rightEvent);
		 */
		System.out.println("ce: " + leftEvent + rightEventPair.e1 + rightEventPair.e2);
		this.noResult++;
		
		// TODO: correct this!
		this.outBuffer.add(leftEvent);
	}

	public long getNoResult() {
		return noResult;
	}

	public void addEventToLeftChildBuffer(EtalisEvent event) {
		this.leftChildBuffer.add(event);
	}

	@Override
	public void addEventToMiddleChildBuffer(EtalisEvent event) {
		this.negatedChildBuffer.add(event);
	}
	
	public void addEventToRightChildBuffer(EtalisEvent event) {
		this.rightChildBuffer.add(event);
		this.execute();
	}
	
	/**
	 * @author darko
	 * Use a pair of events to express NSEQ as attributes of a negated event (i.e., e1) could be
	 * used in a WHERE clause for further inspection. 
	 */
	private class EtalisEventPair {
		private EtalisEvent e1;
		private EtalisEvent e2;

		public EtalisEventPair(EtalisEvent e1, EtalisEvent e2) {
			this.e1 = e1;
			this.e2 = e2;
		}
	}
}


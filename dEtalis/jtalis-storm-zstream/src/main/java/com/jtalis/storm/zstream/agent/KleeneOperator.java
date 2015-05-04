/**
 * 
 */
package com.jtalis.storm.zstream.agent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.jtalis.core.JtalisContext;
import com.jtalis.core.event.EtalisEvent;
import com.jtalis.storm.zstream.channel.IChannel;
import com.jtalis.storm.zstream.channel.SumBuffer;

/**
 * @author darko
 *
 */
public class KleeneOperator implements ITernaryAgent {
	private ConcurrentLinkedQueue<EtalisEvent> leftChildBuffer;
	private ConcurrentLinkedQueue<EtalisEvent> middleChildBuffer;
	private ConcurrentLinkedQueue<EtalisEvent> rightChildBuffer;
	private IChannel outBuffer;
	private long timeWindow;
	private JtalisContext ctx;	
	private int noResult = 0;
	
	public KleeneOperator(IChannel outBuffer, long timeWindow) {
		this.leftChildBuffer = new ConcurrentLinkedQueue<EtalisEvent>();
		this.middleChildBuffer = new ConcurrentLinkedQueue<EtalisEvent>();
		this.rightChildBuffer = new ConcurrentLinkedQueue<EtalisEvent>();
		this.outBuffer = outBuffer;
		this.timeWindow = timeWindow;
	}
	
	public void execute() {
		while (!this.rightChildBuffer.isEmpty()) {
			kleene_sequence();
		}
	}
	
	public void execute(int size) {
		while (!this.rightChildBuffer.isEmpty()) {
			kleene_sequence(size);
		}
	}
	
	private void kleene_sequence() {
		long EAT;
		EtalisEvent rightEvent;
		EtalisEvent middleEvent;
		EtalisEvent leftEvent;
		Iterator<EtalisEvent> itL;
		Iterator<EtalisEvent> itM;
		
		while(!rightChildBuffer.isEmpty()) {
			rightEvent = rightChildBuffer.poll();
			EAT = rightEvent.getTimeEnds().getTime() - timeWindow;
			itL = leftChildBuffer.iterator();
			while(itL.hasNext()) {
				leftEvent = itL.next();
				if (leftEvent.getTimeEnds().getTime() >= rightEvent.getTimeStarts().getTime()){
					break;
				}
				if (leftEvent.getTimeStarts().getTime() < EAT) {
					leftChildBuffer.remove(leftEvent);
					continue;
				}
				itM = middleChildBuffer.iterator();
				while(itM.hasNext()) {
					middleEvent = itM.next();
					if (middleEvent.getTimeStarts().getTime() < EAT ||
						middleEvent.getTimeStarts().getTime() <= leftEvent.getTimeEnds().getTime()) {
						middleChildBuffer.remove(middleEvent);
						continue;
					}
					if(middleEvent.getTimeEnds().getTime() < rightEvent.getTimeStarts().getTime()){
						combineEtalisEvent(leftEvent, middleEvent, rightEvent);	
					}
				}
			}
		}
	}
	
	/**
	 * @param size - the size of the count window
	 */
	private void kleene_sequence(int size) {
		long EAT;
		EtalisEvent rightEvent;
		EtalisEvent middleEvent;
		EtalisEvent leftEvent;
		Iterator<EtalisEvent> itL;
		Iterator<EtalisEvent> itM;
		ArrayList<EtalisEvent> l;
		SumBuffer sum;
		
		while(!rightChildBuffer.isEmpty()) {
			rightEvent = rightChildBuffer.poll();
			EAT = rightEvent.getTimeEnds().getTime() - timeWindow;
			itL = leftChildBuffer.iterator();
			while(itL.hasNext()) {
				leftEvent = itL.next();
				if (leftEvent.getTimeEnds().getTime() >= rightEvent.getTimeStarts().getTime()){
					break;
				}
				if (leftEvent.getTimeStarts().getTime() < EAT) {
					leftChildBuffer.remove(leftEvent);
					continue;
				}
				l = new ArrayList<EtalisEvent>(size);
				sum = new SumBuffer(size);
				
				itM = middleChildBuffer.iterator();
				while(itM.hasNext()) {
					middleEvent = itM.next();
					if (middleEvent.getTimeStarts().getTime() < EAT ||
						middleEvent.getTimeStarts().getTime() <= leftEvent.getTimeEnds().getTime()) {
						middleChildBuffer.remove(middleEvent);
						continue;
					}
					if(middleEvent.getTimeEnds().getTime() < rightEvent.getTimeStarts().getTime()){
						combineEtalisEvent(leftEvent, middleEvent, rightEvent);	
						l.add(middleEvent);
						sum.add(middleEvent.getProperty(1));
						if(sum.isFull()){
							
						}
						
						if(l.size()==size) {
							System.out.println("Kleene ce: " + leftEvent + rightEvent);
							for(EtalisEvent e:l){
								System.out.println("Me: " + e);
							}
							l.remove(0);
						}
					}else break;
				}
				l.removeAll(l);
			}
			
		}
	}
	
	private void combineEtalisEvent(EtalisEvent leftEvent, EtalisEvent middleEvent, EtalisEvent rightEvent) {
		//combine the event from left child buffer and right child buffer
		//TODO: feed the event to jtalis and store the result to the internal buffer
		/*ctx.pushEvent(leftEvent);
		ctx.pushEvent(rightEvent);*/
		//System.out.println("ce: " + leftEvent + middleEvent + rightEvent);
		this.noResult++;
		
		// TODO: correct this!
		this.outBuffer.add(middleEvent);
	}
	
	public long getNoResult() {
		return noResult;
	}
	
	public void addEventToLeftChildBuffer(EtalisEvent event) {
		this.leftChildBuffer.add(event);
	}
	
	public void addEventToMiddleChildBuffer(EtalisEvent event) {
		this.middleChildBuffer.add(event);
	}
	
	public void addEventToRightChildBuffer(EtalisEvent event) {
		this.rightChildBuffer.add(event);
		this.execute(2);
	}
}

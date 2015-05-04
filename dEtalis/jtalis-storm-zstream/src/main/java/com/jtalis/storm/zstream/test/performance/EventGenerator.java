/**
 * 
 */
package com.jtalis.storm.zstream.test.performance;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.storm.zstream.agent.IAgent;
import com.jtalis.storm.zstream.agent.ITernaryAgent;

/**
 * @author darko
 *
 */
public class EventGenerator {
	private IAgent agent;
	
	private long startTime = 0;
	private long endTime = 0;
	private long time = 0;
	private int arity;
	
	public EventGenerator (IAgent agent) {
		this.agent = agent;
		this.arity = 2;
	}
	
	public EventGenerator (ITernaryAgent agent) {
		this.agent = agent;
		this.arity = 3;
	}
	
	public void generate(int streamSize) throws InterruptedException {
		switch (this.arity){
			case 2:	generateBin(streamSize);
					break;
			case 3:	generateTer(streamSize);
					break;		
		}
	}
	
	public void generateBin(int streamSize) throws InterruptedException {
		EtalisEvent event1;
		EtalisEvent event2;
		this.startTime = System.currentTimeMillis();
		for(int i=0; i<streamSize; i++) {
			event1 = new EtalisEvent("a", i);
			//System.out.println("a: " + event1);
			this.agent.addEventToLeftChildBuffer(event1);
			
			//Thread.sleep(10);
			event2 = new EtalisEvent("b", i);
			//System.out.println("b: " + event2);
			this.agent.addEventToRightChildBuffer(event2);
		}
		this.endTime = System.currentTimeMillis();
		this.time = endTime-startTime;
		System.out.println("Test executed in: " + this.time + " ms");
	}
	
	public void generateTer(int streamSize) throws InterruptedException {
		EtalisEvent event1;
		EtalisEvent event2;
		EtalisEvent event3;
		
		this.startTime = System.currentTimeMillis();
		for(int i=0; i<streamSize; i++) {
			event1 = new EtalisEvent("a", i);
			this.agent.addEventToLeftChildBuffer(event1);
			
			//Thread.sleep(500);
			event2 = new EtalisEvent("b", i);
			((ITernaryAgent)this.agent).addEventToMiddleChildBuffer(event2);
			
			//Thread.sleep(500);
			event3 = new EtalisEvent("c", i);
			this.agent.addEventToRightChildBuffer(event3);
		}
		this.endTime = System.currentTimeMillis();
		this.time = endTime-startTime;
		System.out.println("Test executed in: " + this.time + " ms");
	}
	
	public long getTime() {
		return time;
	}
}


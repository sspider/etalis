package com.jtalis.storm.zstream.test.performance;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.storm.zstream.channel.NodeBuffer;

/**
 * @deprecated use {EventGenerator} instead.
 * 
 * @author darko
 *
 */
public class EventGenerator_Old {
	private NodeBuffer leftChildBuffer;
	private NodeBuffer rightChildBuffer;
	
	private long startTime = 0;
	private long endTime = 0;
	private long time = 0;
	
	public EventGenerator_Old (NodeBuffer leftChildBuffer, NodeBuffer rightChildBuffer) {
		this.leftChildBuffer = leftChildBuffer;
		this.rightChildBuffer = rightChildBuffer;
	}
	
	public void generate(int streamSize) throws InterruptedException {
		EtalisEvent event1;
		EtalisEvent event2;
		this.startTime = System.currentTimeMillis();
		for(int i=0; i<streamSize; i++) {
			event1 = new EtalisEvent("a", i);
			System.out.println("a: " + event1);
			this.leftChildBuffer.addNewEvent(event1);
			
			Thread.sleep(10);
			event2 = new EtalisEvent("b", i);
			System.out.println("b: " + event2);
			this.rightChildBuffer.addNewEvent(event2);
		}
		this.endTime = System.currentTimeMillis();
		this.time = endTime-startTime;
		System.out.println("Test executed in: " + this.time + " ms");
	}

	public long getTime() {
		return time;
	}

}

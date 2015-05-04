package com.jtalis.storm.zstream.test;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.storm.zstream.agent.ConjunctionOperator_Old;
import com.jtalis.storm.zstream.channel.NodeBuffer;

/**
 * @deprecated use {ConjunctionTest} instead.
 * 
 * @author darko
 *
 */
public class ConjunctionTest_Old {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		NodeBuffer leftChildBuffer = new NodeBuffer(2900);
		NodeBuffer rightChildBuffer = new NodeBuffer(2900);
		
		ConjunctionOperator_Old conj = new ConjunctionOperator_Old(leftChildBuffer, rightChildBuffer, 2900);
		conj.start();
		
		Thread.sleep(500);
		EtalisEvent event1 = new EtalisEvent("a", 1);
		leftChildBuffer.addNewEvent(event1);
		
		Thread.sleep(500);
		EtalisEvent event2 = new EtalisEvent("a", 2);
		leftChildBuffer.addNewEvent(event2);
		
		Thread.sleep(500);
		EtalisEvent event3 = new EtalisEvent("a", 3);
		leftChildBuffer.addNewEvent(event3);
		
		Thread.sleep(500);
		EtalisEvent event4 = new EtalisEvent("b", 4);
		rightChildBuffer.addNewEvent(event4);
		
		Thread.sleep(500);
		EtalisEvent event5 = new EtalisEvent("a", 5);
		leftChildBuffer.addNewEvent(event5);
		
		Thread.sleep(500);
		EtalisEvent event6 = new EtalisEvent("b", 6);
		rightChildBuffer.addNewEvent(event6);
		
		/*Thread.sleep(500);
		EtalisEvent event7 = new EtalisEvent("a", 7);
		leftChildBuffer.addNewEvent(event7);
		
		Thread.sleep(500);
		EtalisEvent event8 = new EtalisEvent("b", 8);
		rightChildBuffer.addNewEvent(event8);
		
		Thread.sleep(500);
		EtalisEvent event9 = new EtalisEvent("a", 9);
		leftChildBuffer.addNewEvent(event9);
		
		Thread.sleep(500);
		EtalisEvent event10 = new EtalisEvent("b", 10);
		rightChildBuffer.addNewEvent(event10);*/
		
		Thread.sleep(500);
		System.out.println("NoResult: " + conj.getNoResult());
	}

}

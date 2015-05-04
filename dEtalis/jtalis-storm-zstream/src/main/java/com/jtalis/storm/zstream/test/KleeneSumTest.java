/**
 * 
 */
package com.jtalis.storm.zstream.test;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.storm.zstream.agent.KleeneOperator;
import com.jtalis.storm.zstream.channel.Buffer;
import com.jtalis.storm.zstream.channel.IChannel;

/**
 * @author darko
 *
 */
public class KleeneSumTest {
	private IChannel out1_Buffer;
	private KleeneOperator kleene_1;
	
	private int timeWindow = 100000;
		
	public KleeneSumTest () {
		out1_Buffer = new Buffer();
		this.kleene_1 = new KleeneOperator(out1_Buffer, timeWindow);
	}
	
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		
		KleeneSumTest test = new KleeneSumTest ();
		
		Thread.sleep(500);
		EtalisEvent event1 = new EtalisEvent("a", 1);
		test.kleene_1.addEventToLeftChildBuffer(event1);
		
		Thread.sleep(500);
		EtalisEvent event2 = new EtalisEvent("b", 2);
		test.kleene_1.addEventToMiddleChildBuffer(event2);
		
		Thread.sleep(500);
		EtalisEvent event3 = new EtalisEvent("b", 3);
		test.kleene_1.addEventToMiddleChildBuffer(event3);
		
		Thread.sleep(500);
		EtalisEvent event6 = new EtalisEvent("c", 4);
		test.kleene_1.addEventToRightChildBuffer(event6);
		
	}

}


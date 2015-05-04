/**
 * 
 */
package com.jtalis.storm.zstream.test;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.storm.zstream.agent.KleeneOperator;
import com.jtalis.storm.zstream.agent.SequenceOperator;
import com.jtalis.storm.zstream.channel.Buffer;
import com.jtalis.storm.zstream.channel.IChannel;
import com.jtalis.storm.zstream.channel.LeftLink;
import com.jtalis.storm.zstream.channel.MiddleLink;

/**
 * @author darko
 *
 */
public class KleeneTest {
	private IChannel out1_Buffer;
	private IChannel from_2_to_3;
	private IChannel from_1_to_2;
	
	private SequenceOperator seq_3;
	private KleeneOperator kleene_2;
	private KleeneOperator kleene_1;
	
	private int timeWindow = 100000;
		
	public KleeneTest () {
		out1_Buffer = new Buffer();
		
		this.seq_3 = new SequenceOperator(out1_Buffer, timeWindow);
		
		this.from_2_to_3 = new LeftLink(this.seq_3);
		
		this.kleene_2 = new KleeneOperator(from_2_to_3, timeWindow);
		
		this.from_1_to_2 = new MiddleLink(this.kleene_2);
		
		this.kleene_1 = new KleeneOperator(from_1_to_2, timeWindow);
	}
	
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		
		KleeneTest test = new KleeneTest ();
		
		Thread.sleep(500);
		EtalisEvent event7 = new EtalisEvent("a", 7);
		test.kleene_2.addEventToLeftChildBuffer(event7);
		
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
		
		Thread.sleep(500);
		EtalisEvent event8 = new EtalisEvent("c", 5);
		test.kleene_2.addEventToRightChildBuffer(event8);
		
		Thread.sleep(500);
		EtalisEvent event9 = new EtalisEvent("d", 6);
		test.seq_3.addEventToRightChildBuffer(event9);
	}

}


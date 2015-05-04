/**
 * 
 */
package com.jtalis.storm.zstream.test;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.storm.zstream.agent.ConjunctionOperator;
import com.jtalis.storm.zstream.channel.Buffer;
import com.jtalis.storm.zstream.channel.IChannel;
import com.jtalis.storm.zstream.channel.RightLink;

/**
 * @author darko
 *
 */
public class ConjunctionTest {
	private IChannel out1_Buffer;
	private IChannel from_2_to_3;
	private IChannel from_1_to_2;
	
	private ConjunctionOperator con_3;
	private ConjunctionOperator con_2;
	private ConjunctionOperator con_1;
	
	private int timeWindow = 100000;
	
	/**
	 * no = 1 - one agent tested only - the basic test
	 * no = 2 - three agents tested - events to the second- and third agent added from the right child
	 */
	private int no = 1;
	
	public ConjunctionTest () {
		
		switch(no){
		case 1:	constructTest_1 ();
			break;
		case 2:	constructTest_2 ();
			break;
		}
	}
	
	public void constructTest_1 () {
		out1_Buffer = new Buffer();
		
		this.con_1 = new ConjunctionOperator(out1_Buffer, timeWindow);
	}
	
	public void constructTest_2 () {
		out1_Buffer = new Buffer();
		
		this.con_3 = new ConjunctionOperator(out1_Buffer, timeWindow);
		
		this.from_2_to_3 = new RightLink(this.con_3);
		
		this.con_2 = new ConjunctionOperator(from_2_to_3, timeWindow);
		
		this.from_1_to_2 = new RightLink(this.con_2);
		
		this.con_1 = new ConjunctionOperator(from_1_to_2, timeWindow);
	}
	
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		
		ConjunctionTest test = new ConjunctionTest ();
		
		Thread.sleep(500);
		EtalisEvent event1 = new EtalisEvent("a", 1);
		test.con_1.addEventToLeftChildBuffer(event1);
		
		Thread.sleep(500);
		EtalisEvent event2 = new EtalisEvent("a", 2);
		test.con_1.addEventToLeftChildBuffer(event2);
		
		Thread.sleep(500);
		EtalisEvent event3 = new EtalisEvent("a", 3);
		test.con_1.addEventToLeftChildBuffer(event3);
		
		switch(test.no){
		// one agent tested only - the basic test
		case 1:
			Thread.sleep(500);
			EtalisEvent event4 = new EtalisEvent("b", 4);
			test.con_1.addEventToRightChildBuffer(event4);
			
			Thread.sleep(500);
			EtalisEvent event5 = new EtalisEvent("a", 5);
			test.con_1.addEventToLeftChildBuffer(event5);
			break;
		// events to the second- and third agent added from the right child
		case 2:	
			Thread.sleep(1500);
			EtalisEvent event6 = new EtalisEvent("c", 6);
			test.con_2.addEventToLeftChildBuffer(event6);
			
			Thread.sleep(1500);
			EtalisEvent event7 = new EtalisEvent("d", 7);
			test.con_3.addEventToLeftChildBuffer(event7);
			
			Thread.sleep(1500);
			EtalisEvent event8 = new EtalisEvent("b", 8);
			test.con_1.addEventToRightChildBuffer(event8);
			
			Thread.sleep(500);
			EtalisEvent event9 = new EtalisEvent("a", 9);
			test.con_1.addEventToLeftChildBuffer(event9);
		}
	}
}

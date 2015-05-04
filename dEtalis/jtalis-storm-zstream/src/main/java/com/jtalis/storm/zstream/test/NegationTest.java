/**
 * 
 */
package com.jtalis.storm.zstream.test;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.storm.zstream.agent.NegationOperator;
import com.jtalis.storm.zstream.agent.SequenceOperator;
import com.jtalis.storm.zstream.channel.Buffer;
import com.jtalis.storm.zstream.channel.IChannel;
import com.jtalis.storm.zstream.channel.LeftLink;
import com.jtalis.storm.zstream.channel.RightLink;

/**
 * @author darko
 *
 */
public class NegationTest {
	private IChannel out1_Buffer;
	private IChannel from_2_to_3;
	private IChannel from_1_to_2;
	
	private SequenceOperator seq_3;
	private NegationOperator not_2;
	private NegationOperator not_1;
	
	private int timeWindow = 100000;
	
	/**
	 * no = 1 - one agent tested only - the basic test
	 * no = 2 - three agents tested - events to the third agent added from the right child
	 * 								- events to the second agent added from the right child
	 * no = 3 - three agents tested - events to the third agent added from the left child
	 * 								- events to the second agent added from the left child
	 */
	private int no = 1;
	
	public NegationTest () {
		switch(no){
		case 1:	constructTest_1 ();
			break;
		case 2:	constructTest_2 ();
			break;
		case 3:	constructTest_3 ();
		break;
		}
	}
	
	public void constructTest_1 () {
		out1_Buffer = new Buffer();
		
		this.not_1 = new NegationOperator(out1_Buffer, 100000);
	}
	
	public void constructTest_2 () {
		out1_Buffer = new Buffer();
		
		this.seq_3 = new SequenceOperator(out1_Buffer, timeWindow);
		
		this.from_2_to_3 = new RightLink(this.seq_3);
		
		this.not_2 = new NegationOperator(from_2_to_3, timeWindow);
		
		this.from_1_to_2 = new RightLink(this.not_2);
		
		this.not_1 = new NegationOperator(from_1_to_2, timeWindow);
	}
	
	public void constructTest_3 () {
		out1_Buffer = new Buffer();
		
		this.seq_3 = new SequenceOperator(out1_Buffer, timeWindow);
		
		this.from_2_to_3 = new LeftLink(this.seq_3);
		
		this.not_2 = new NegationOperator(from_2_to_3, timeWindow);
		
		this.from_1_to_2 = new LeftLink(this.not_2);
		
		this.not_1 = new NegationOperator(from_1_to_2, timeWindow);
	}
	
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		
		NegationTest test = new NegationTest ();
		
		switch(test.no){
		// one agent tested only - the basic test
		case 1:
			Thread.sleep(100);
			EtalisEvent event1 = new EtalisEvent("a", 1);
			test.not_1.addEventToLeftChildBuffer(event1);
			
			Thread.sleep(100);
			EtalisEvent event2 = new EtalisEvent("b", 2);
			test.not_1.addEventToMiddleChildBuffer(event2);
			
			Thread.sleep(100);
			EtalisEvent event3 = new EtalisEvent("c", 3);
			test.not_1.addEventToRightChildBuffer(event3);
			
			Thread.sleep(100);
			EtalisEvent event4 = new EtalisEvent("a", 4);
			test.not_1.addEventToLeftChildBuffer(event4);
			
			Thread.sleep(100);
			EtalisEvent event5 = new EtalisEvent("c", 5);
			test.not_1.addEventToRightChildBuffer(event5);
			break;
		/* events to the third agent added to the right child
		 * events to the second agent added to the right child
		*/
		case 2:	
			Thread.sleep(100);
			EtalisEvent event6 = new EtalisEvent("f", 6);
			test.seq_3.addEventToLeftChildBuffer(event6);
			
			Thread.sleep(100);
			EtalisEvent event7 = new EtalisEvent("e", 7);
			test.not_2.addEventToLeftChildBuffer(event7);
			
			Thread.sleep(100);
			EtalisEvent event8 = new EtalisEvent("a", 8);
			test.not_1.addEventToLeftChildBuffer(event8);
			
			Thread.sleep(100);
			EtalisEvent event9 = new EtalisEvent("c", 9);
			test.not_1.addEventToRightChildBuffer(event9);
			break;
		/*
		 * events to the third agent added to the left child
		 * events to the second agent added to the left child
		*/
		case 3:
			Thread.sleep(100);
			EtalisEvent event10 = new EtalisEvent("a", 10);
			test.not_1.addEventToLeftChildBuffer(event10);
			
			Thread.sleep(100);
			EtalisEvent event11 = new EtalisEvent("b", 11);
			test.not_1.addEventToMiddleChildBuffer(event11);
			
			Thread.sleep(100);
			EtalisEvent event12 = new EtalisEvent("c", 12);
			test.not_1.addEventToRightChildBuffer(event12);
			
			Thread.sleep(100);
			EtalisEvent event13 = new EtalisEvent("b", 13);
			test.not_1.addEventToMiddleChildBuffer(event13);
			
			Thread.sleep(100);
			EtalisEvent event14 = new EtalisEvent("a", 14);
			test.not_1.addEventToLeftChildBuffer(event14);
			
			Thread.sleep(100);
			EtalisEvent event15 = new EtalisEvent("c", 15);
			test.not_1.addEventToRightChildBuffer(event15);
			
			Thread.sleep(100);
			EtalisEvent event16 = new EtalisEvent("e", 16);
			test.not_2.addEventToRightChildBuffer(event16);
			
			Thread.sleep(100);
			EtalisEvent event17 = new EtalisEvent("f", 17);
			test.seq_3.addEventToRightChildBuffer(event17);
		}
	}

}
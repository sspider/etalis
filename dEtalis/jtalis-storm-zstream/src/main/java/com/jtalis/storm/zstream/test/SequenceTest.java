/**
 * 
 */
package com.jtalis.storm.zstream.test;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.storm.zstream.agent.SequenceOperator;
import com.jtalis.storm.zstream.channel.Buffer;
import com.jtalis.storm.zstream.channel.IChannel;
import com.jtalis.storm.zstream.channel.LeftLink;
import com.jtalis.storm.zstream.channel.RightLink;

/**
 * @author darko
 *
 */
public class SequenceTest {
	private IChannel out1_Buffer;
	private IChannel from_2_to_3;
	private IChannel from_1_to_2;
	
	private SequenceOperator seq_3;
	private SequenceOperator seq_2;
	private SequenceOperator seq_1;
	
	private int timeWindow = 100000;
	
	/**
	 * no = 1 - one agent tested only - the basic test
	 * no = 2 - three agents tested - events to the third agent added from the left child
	 * no = 3 - three agents tested - events to the third agent added from the right child
	 */
	private int no = 1;
	
	public SequenceTest () {
		
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
		
		this.seq_1 = new SequenceOperator(out1_Buffer, 29000);
	}
	
	public void constructTest_2 () {
		out1_Buffer = new Buffer();
		
		this.seq_3 = new SequenceOperator(out1_Buffer, timeWindow);
		
		this.from_2_to_3 = new LeftLink(this.seq_3);
		
		this.seq_2 = new SequenceOperator(from_2_to_3, timeWindow);
		
		this.from_1_to_2 = new LeftLink(this.seq_2);
		
		this.seq_1 = new SequenceOperator(from_1_to_2, timeWindow);
	}
	
	public void constructTest_3 () {
		out1_Buffer = new Buffer();
		
		this.seq_3 = new SequenceOperator(out1_Buffer, timeWindow);
		
		this.from_2_to_3 = new RightLink(this.seq_3);
		
		this.seq_2 = new SequenceOperator(from_2_to_3, timeWindow);
		
		this.from_1_to_2 = new LeftLink(this.seq_2);
		
		this.seq_1 = new SequenceOperator(from_1_to_2, timeWindow);
	}
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		
		SequenceTest test = new SequenceTest ();
		
		switch(test.no){
			case 1:
				Thread.sleep(500);
				EtalisEvent event1 = new EtalisEvent("a", 1);
				test.seq_1.addEventToLeftChildBuffer(event1);
				
				Thread.sleep(500);
				EtalisEvent event2 = new EtalisEvent("a", 2);
				test.seq_1.addEventToLeftChildBuffer(event2);
				
				Thread.sleep(500);
				EtalisEvent event3 = new EtalisEvent("a", 3);
				test.seq_1.addEventToLeftChildBuffer(event3);
				
				Thread.sleep(500);
				EtalisEvent event4 = new EtalisEvent("b", 4);
				test.seq_1.addEventToRightChildBuffer(event4);
				
				Thread.sleep(500);
				EtalisEvent event5 = new EtalisEvent("a", 5);
				test.seq_1.addEventToLeftChildBuffer(event5);
				
				Thread.sleep(500);
				EtalisEvent event6 = new EtalisEvent("b", 6);
				test.seq_1.addEventToRightChildBuffer(event6);
				
				Thread.sleep(500);
				EtalisEvent event7 = new EtalisEvent("a", 7);
				test.seq_1.addEventToLeftChildBuffer(event7);
				
				Thread.sleep(500);
				EtalisEvent event8 = new EtalisEvent("b", 8);
				test.seq_1.addEventToRightChildBuffer(event8);
				
				Thread.sleep(500);
				EtalisEvent event9 = new EtalisEvent("a", 9);
				test.seq_1.addEventToLeftChildBuffer(event9);
				
				Thread.sleep(500);
				EtalisEvent event10 = new EtalisEvent("b", 10);
				test.seq_1.addEventToRightChildBuffer(event10);
				break;
			case 2:	
				Thread.sleep(500);
				EtalisEvent event11 = new EtalisEvent("a", 1);
				test.seq_1.addEventToLeftChildBuffer(event11);
				
				Thread.sleep(500);
				EtalisEvent event12 = new EtalisEvent("b", 2);
				test.seq_1.addEventToRightChildBuffer(event12);
				
				Thread.sleep(500);
				EtalisEvent event13 = new EtalisEvent("c", 3);
				test.seq_2.addEventToRightChildBuffer(event13);
				
				Thread.sleep(500);
				EtalisEvent event14 = new EtalisEvent("d", 4);
				test.seq_3.addEventToRightChildBuffer(event14);
				break;
			case 3:	
				Thread.sleep(500);
				EtalisEvent event15 = new EtalisEvent("d", 4);
				test.seq_3.addEventToLeftChildBuffer(event15);
				
				Thread.sleep(500);
				EtalisEvent event16 = new EtalisEvent("a", 1);
				test.seq_1.addEventToLeftChildBuffer(event16);
				
				Thread.sleep(500);
				EtalisEvent event17 = new EtalisEvent("b", 2);
				test.seq_1.addEventToRightChildBuffer(event17);
				
				Thread.sleep(500);
				EtalisEvent event18 = new EtalisEvent("c", 3);
				test.seq_2.addEventToRightChildBuffer(event18);
				break;
		}
	}
	
}

/**
 * 
 */
package com.jtalis.storm.zstream.test;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.storm.zstream.agent.ConjunctionOperator;
import com.jtalis.storm.zstream.agent.DisjunctionOperator;
import com.jtalis.storm.zstream.agent.SequenceOperator;
import com.jtalis.storm.zstream.channel.Buffer;
import com.jtalis.storm.zstream.channel.IChannel;
import com.jtalis.storm.zstream.channel.LeftLink;
import com.jtalis.storm.zstream.channel.RightLink;
import com.jtalis.storm.zstream.event.EtalisEventBuilder;

/**
 * @author darko
 *
 */
public class DisjunctionTest {
	private IChannel out1_Buffer;
	private IChannel from_2_to_3;
	private IChannel from_1_to_2;
	
	private ConjunctionOperator con_3;
	private DisjunctionOperator or_2;
	private SequenceOperator seq_1;
	
	private int timeWindow = 100000;
	
	/**
	 * no = 1 - one agent tested only - the basic test
	 * no = 2 - three agents tested 
	 */
	private int no = 1;
	
	public DisjunctionTest () {
		
		switch(no){
		case 1:	constructTest_1 ();
			break;
		case 2:	constructTest_2 ();
			break;
		}
	}
	
	public void constructTest_1 () {
		out1_Buffer = new Buffer();
		
		this.or_2 = new DisjunctionOperator(out1_Buffer, 1002);
	}
	
	public void constructTest_2 () {
		out1_Buffer = new Buffer();
		
		this.con_3 = new ConjunctionOperator(out1_Buffer, timeWindow);
		
		this.from_2_to_3 = new RightLink(this.con_3);
		
		this.or_2 = new DisjunctionOperator(from_2_to_3, timeWindow);
		
		this.from_1_to_2 = new LeftLink(this.or_2);
		
		this.seq_1 = new SequenceOperator(from_1_to_2, timeWindow);
	}
	
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		
		DisjunctionTest test = new DisjunctionTest ();
		
		switch(test.no){
			case 1:
				EtalisEventBuilder builder = new EtalisEventBuilder();
				EtalisEvent event0 = builder.buildEtalisEventFromString("event(dd(1)[1,1002])");
				System.out.println(event0.getTimeStarts().getTime());
				
				test.or_2.addEventToLeftChildBuffer(event0);
				
				Thread.sleep(500);
				EtalisEvent event1 = new EtalisEvent("a", 1);
				test.or_2.addEventToLeftChildBuffer(event1);
				
				Thread.sleep(500);
				EtalisEvent event2 = new EtalisEvent("a", 2);
				test.or_2.addEventToLeftChildBuffer(event2);
				
				Thread.sleep(500);
				EtalisEvent event3 = new EtalisEvent("a", 3);
				test.or_2.addEventToLeftChildBuffer(event3);
				
				Thread.sleep(500);
				EtalisEvent event4 = new EtalisEvent("b", 4);
				test.or_2.addEventToRightChildBuffer(event4);
				
				Thread.sleep(500);
				EtalisEvent event5 = new EtalisEvent("a", 5);
				test.or_2.addEventToLeftChildBuffer(event5);
				break;
			case 2:	
				Thread.sleep(500);
				EtalisEvent event11 = new EtalisEvent("a", 1);
				test.seq_1.addEventToLeftChildBuffer(event11);
				
				Thread.sleep(3000);
				EtalisEvent event12 = new EtalisEvent("b", 2);
				test.seq_1.addEventToRightChildBuffer(event12);
				
				Thread.sleep(500);
				EtalisEvent event13 = new EtalisEvent("c", 3);
				test.or_2.addEventToRightChildBuffer(event13);
				
				Thread.sleep(500);
				EtalisEvent event14 = new EtalisEvent("b", 4);
				test.seq_1.addEventToRightChildBuffer(event14);

				Thread.sleep(500);
				EtalisEvent event15 = new EtalisEvent("d", 5);
				test.con_3.addEventToLeftChildBuffer(event15);
				break;
		}
	}
}


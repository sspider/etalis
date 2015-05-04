/**
 * 
 */
package com.jtalis.storm.zstream.test;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.storm.zstream.agent.Starts;
import com.jtalis.storm.zstream.channel.Buffer;
import com.jtalis.storm.zstream.channel.IChannel;
import com.jtalis.storm.zstream.event.EtalisEventBuilder;

/**
 * @author darko
 *
 */
public class FinishesTest {
	private IChannel out1_Buffer;
	private Starts finish_1;
	
	private int timeWindow = 100000;
	
	public FinishesTest () {
		constructTest_1 ();
	}
	
	public void constructTest_1 () {
		out1_Buffer = new Buffer();
		
		this.finish_1 = new Starts(out1_Buffer, timeWindow);
	}
	
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws InterruptedException {
		
		FinishesTest test = new FinishesTest ();
		EtalisEventBuilder builder = new EtalisEventBuilder();
		
		EtalisEvent event0 = builder.buildEtalisEventFromString("event(a(1)[2000,3000])");
		test.finish_1.addEventToLeftChildBuffer(event0);
		
		EtalisEvent event1 = builder.buildEtalisEventFromString("event(b(1)[1000,3000])");
		test.finish_1.addEventToRightChildBuffer(event1);
		}
}

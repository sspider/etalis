/**
 * 
 */
package com.jtalis.storm.zstream.test;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.storm.zstream.agent.During;
import com.jtalis.storm.zstream.channel.Buffer;
import com.jtalis.storm.zstream.channel.IChannel;
import com.jtalis.storm.zstream.event.EtalisEventBuilder;

/**
 * @author darko
 *
 */
public class DuringTest {
	private IChannel out1_Buffer;
	private During during_1;
	
	private int timeWindow = 100000;
	
	public DuringTest () {
		constructTest_1 ();
	}
	
	public void constructTest_1 () {
		out1_Buffer = new Buffer();
		
		this.during_1 = new During(out1_Buffer, timeWindow);
	}
	
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws InterruptedException {
		
		DuringTest test = new DuringTest ();
		EtalisEventBuilder builder = new EtalisEventBuilder();
		
		EtalisEvent event0 = builder.buildEtalisEventFromString("event(a(1)[2000,3000])");
		test.during_1.addEventToLeftChildBuffer(event0);
		
		EtalisEvent event1 = builder.buildEtalisEventFromString("event(b(1)[1000,5000])");
		test.during_1.addEventToRightChildBuffer(event1);
		}
}

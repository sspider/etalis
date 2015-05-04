/**
 * 
 */
package com.jtalis.storm.zstream.test;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.storm.zstream.agent.Overlaps;
import com.jtalis.storm.zstream.channel.Buffer;
import com.jtalis.storm.zstream.channel.IChannel;
import com.jtalis.storm.zstream.event.EtalisEventBuilder;

/**
 * @author darko
 *
 */
public class OverlapsTest {
	private IChannel out1_Buffer;
	private Overlaps over_1;
	
	private int timeWindow = 100000;
	
	public OverlapsTest () {
		constructTest_1 ();
	}
	
	public void constructTest_1 () {
		out1_Buffer = new Buffer();
		
		this.over_1 = new Overlaps(out1_Buffer, timeWindow);
	}
	
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws InterruptedException {
		
		OverlapsTest test = new OverlapsTest ();
		EtalisEventBuilder builder = new EtalisEventBuilder();
		
		EtalisEvent event0 = builder.buildEtalisEventFromString("event(a(1)[1000,3000])");
		test.over_1.addEventToLeftChildBuffer(event0);
		
		EtalisEvent event1 = builder.buildEtalisEventFromString("event(b(1)[2000,5000])");
		test.over_1.addEventToRightChildBuffer(event1);
		}
}


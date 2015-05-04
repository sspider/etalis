/**
 * 
 */
package com.jtalis.storm.zstream.test;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.storm.zstream.agent.Meets;
import com.jtalis.storm.zstream.channel.Buffer;
import com.jtalis.storm.zstream.channel.IChannel;
import com.jtalis.storm.zstream.event.EtalisEventBuilder;

/**
 * @author darko
 *
 */
public class MeetsTest {
	private IChannel out1_Buffer;
	private Meets meet_1;
	
	private int timeWindow = 100000;
	
	public MeetsTest () {
		constructTest_1 ();
	}
	
	public void constructTest_1 () {
		out1_Buffer = new Buffer();
		
		this.meet_1 = new Meets(out1_Buffer, timeWindow);
	}
	
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws InterruptedException {
		
		MeetsTest test = new MeetsTest ();
		EtalisEventBuilder builder = new EtalisEventBuilder();
		
		EtalisEvent event0 = builder.buildEtalisEventFromString("event(a(1)[1000,3000])");
		test.meet_1.addEventToLeftChildBuffer(event0);
		
		EtalisEvent event1 = builder.buildEtalisEventFromString("event(b(1)[3000,5000])");
		test.meet_1.addEventToRightChildBuffer(event1);
		}
}

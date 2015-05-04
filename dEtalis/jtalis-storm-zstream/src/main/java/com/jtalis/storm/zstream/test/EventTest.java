package com.jtalis.storm.zstream.test;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.storm.zstream.event.EtalisEventBuilder;
import com.jtalis.storm.zstream.event.EtalisEventParser;

public class EventTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EtalisEventBuilder builder = new EtalisEventBuilder();
		EtalisEvent tt = builder.buildEtalisEventFromString("event(a(1,2,3)[1231232,12322123])");
		System.out.println(tt.getTimeStarts().getTime());
		
		EtalisEventParser parse = new EtalisEventParser();
		String t2 = parse.buildStringFromEtalisEvent(tt);
		EtalisEvent t3 = builder.buildEtalisEventFromString(t2);
		System.out.println(parse.buildStringFromEtalisEvent(t3));
	}

}

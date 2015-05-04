package com.jtalis.storm.zstream.event;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.core.event.EventBuilder;
import com.jtalis.core.event.EventTimestamp;
import com.jtalis.core.event.InvalidEventFormatException;


public class EtalisEventBuilder {
	// String format:
	// event(EventBody[Timestamp1,Timestamp2])
	public static EtalisEvent buildEtalisEventFromString(String s) {
		try {
			int idx1 = s.indexOf("(");
			int idx2 = s.indexOf("[");
			int idx3 = s.indexOf("]");		
			String eventBody = s.substring(idx1 + 1, idx2);
			String ts = s.substring(idx2+1, idx3);
			//System.out.println(eventBody);
			//System.out.println(ts);
			int idx4 = ts.indexOf(",");
			long startTime = Long.valueOf(ts.substring(0, idx4));
			long endTime = Long.valueOf(ts.substring(idx4 + 1, ts.length()));
			EtalisEvent rtn = EventBuilder.buildEventFromString(eventBody);
			rtn.setTimeStarts(new EventTimestamp(startTime));
			rtn.setTimeEnds(new EventTimestamp(endTime));
			
			return rtn;
		} catch (InvalidEventFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}

}

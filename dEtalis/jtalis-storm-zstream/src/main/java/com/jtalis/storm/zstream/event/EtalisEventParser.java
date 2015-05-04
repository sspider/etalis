package com.jtalis.storm.zstream.event;

import com.jtalis.core.event.EtalisEvent;

public class EtalisEventParser {
	public static String buildStringFromEtalisEvent(EtalisEvent event) {
		StringBuilder builder = new StringBuilder("event(");
		builder.append(event.getPrologString().trim());
		builder.append("[");
		builder.append(event.getTimeStarts().getTime());
		builder.append(",");
		builder.append(event.getTimeEnds().getTime());
		builder.append("])");
		return builder.toString();
	}
}

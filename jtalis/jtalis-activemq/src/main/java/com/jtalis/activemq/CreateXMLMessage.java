package com.jtalis.activemq;

import com.jtalis.core.event.EtalisEvent;

public class CreateXMLMessage {
	public static String buildXMLMessageFromEtalisEvent(EtalisEvent event) {
		String ruleID;
		if (event.getRuleID() != null)
			ruleID = event.getRuleID();
		else
			ruleID = "unlabeled";
		
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
				"<s:Envelope xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\" " +
				"xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\"" +
				" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\">" +
				"<s:Header></s:Header>" +
				"<s:Body>" +
				"<wsnt:Notify>" +
				"<wsnt:NotificationMessage>" +
				"<wsnt:Topic></wsnt:Topic>" +
				"<wsnt:ProducerReference>" +
				"<wsa:Address>ETALIS</wsa:Address>" +
				"</wsnt:ProducerReference>" +
				"<wsnt:Message>" +
				"<ns1:event xmlns:ns1=\"http://www.alert-project.eu/\" " +
				"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
				"xsi:schemaLocation=\"http://www.alert-project.eu/alert-root.xsd\" " +
				"xmlns:e=\"http://www.alert-project.eu/ontoevents-mdservice\">" +
				"<ns1:head>" +
				"<ns1:sender>ETALIS</ns1:sender>" +
				"<ns1:timestamp>" + event.getTimeEnds().getTime() + "</ns1:timestamp>" +
				"<ns1:sequencenumber>1</ns1:sequencenumber>" +
				"</ns1:head>" +
				"<ns1:payload>" +
				"<ns1:meta>" +
				"<ns1:startTime>" + event.getTimeStarts().getTime() + "</ns1:startTime>" +
				"<ns1:endTime>" + event.getTimeEnds().getTime() + "</ns1:endTime>" +
				"<!-- name of event -->" +
				"<ns1:eventName>EtalisPatternUpdate</ns1:eventName>" +
				"<ns1:eventId>5748</ns1:eventId>" +
				"<ns1:eventType>EtalisPatternUpdate</ns1:eventType>" +
				"</ns1:meta>" +
				"<ns1:eventData>" +
				"<e:patternId>" + ruleID + "</e:patternId>" +
				"<e:eventType>" + event.getPrologString() + "</e:eventType>" +
				"</ns1:eventData>" +
				"</ns1:payload>" +
				"</ns1:event>" +
				"</wsnt:Message>" +
				"</wsnt:NotificationMessage>" +
				"</wsnt:Notify>" +
				"</s:Body>" +
				"</s:Envelope>";		
		
		
		return xml;
	}
}

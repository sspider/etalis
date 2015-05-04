package com.jtalis.test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.xml.transform.OutputKeys;

import org.junit.Assert;
import org.junit.Test;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.core.event.schema.EventProperty;
import com.jtalis.core.event.schema.EventSchema;
import com.jtalis.core.event.xml.XMLEventIO;
import com.jtalis.core.event.xml.XMLEventSchema;

public class TestXMLIO {
	private static final Logger logger = Logger.getLogger(TestXMLIO.class.getName());

	@Test
	public void testXMLEventSchema() {
		try {
			Set<String> events = new HashSet<String>(Arrays.asList("user", "address"));
			List<EventProperty> userProperties = Arrays.asList(
					new EventProperty("name", String.class),
					new EventProperty("age", Integer.class),
					new EventProperty("address", EtalisEvent.class));
			List<EventProperty> addressProperties = Arrays.asList(
					new EventProperty("street", String.class),
					new EventProperty("number", Integer.class));

			EventSchema schema = new XMLEventSchema(getClass().getResourceAsStream("UserEventSchema.xsd"));
			Assert.assertEquals(schema.getEvents(), events);
			Assert.assertEquals(schema.getProperties("user"), userProperties);
			Assert.assertEquals(schema.getProperties("address"), addressProperties);
		}
		catch (Exception e) {
			logger.severe(e.toString());
			Assert.fail(e.toString());
		}
	}

	public void testSerializeXMLEvent() {
		try {
			EventSchema schema = new XMLEventSchema(getClass().getResourceAsStream("UserEventSchema.xsd"));
			XMLEventIO io = new XMLEventIO(schema);
			io.setOutputProperty(OutputKeys.INDENT, "no");

			// serialize an event into XML
			EtalisEvent event = new EtalisEvent("user", "john", 34, new EtalisEvent("address", "street name", 25));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			io.serialize(event, out);
			String res = new String(out.toByteArray());
			String exp = 
					"<user>"                          + 
					"<name>john</name>"               + 
					"<age>34</age>"                   + 
					"<address>"                       + 
					"<street>street name</street>"    + 
					"<number>25</number>"             + 
					"</address>"                      + 
					"</user>"; 
			Assert.assertEquals(res, exp);
		}
		catch (Exception e) {
			logger.severe(e.toString());
			Assert.fail(e.toString());
		}
	}

	public void testDeserializeSingleXMLEvent() {
		try {
			EventSchema schema = new XMLEventSchema(getClass().getResourceAsStream("UserEventSchema.xsd"));
			XMLEventIO io = new XMLEventIO(schema);

			// serialize an event into XML
			List<EtalisEvent> expected = Arrays.asList(
					new EtalisEvent("user", "john", 34, new EtalisEvent("address", "street", 34)),
					new EtalisEvent("user", "john", 35, new EtalisEvent("address", "street", 35)),
					new EtalisEvent("user", "john", 36, new EtalisEvent("address", "street", 36)));

			InputStream is = getClass().getResourceAsStream("xml-events.xml");
			List<EtalisEvent> res = new ArrayList<EtalisEvent>();
			res.add(io.deserialize(is));
			res.add(io.deserialize(is));
			res.add(io.deserialize(is));

			Assert.assertEquals(res, expected);
		}
		catch (Exception e) {
			logger.severe(e.toString());
			Assert.fail(e.toString());
		}
	}

	public void testDeserializeAllXMLEvent() {
		try {
			EventSchema schema = new XMLEventSchema(getClass().getResourceAsStream("UserEventSchema.xsd"));
			XMLEventIO io = new XMLEventIO(schema);

			// serialize an event into XML
			List<EtalisEvent> expected = Arrays.asList(
					new EtalisEvent("user", "john", 34, new EtalisEvent("address", "street", 34)),
					new EtalisEvent("user", "john", 35, new EtalisEvent("address", "street", 35)),
					new EtalisEvent("user", "john", 36, new EtalisEvent("address", "street", 36)));

			InputStream is = getClass().getResourceAsStream("xml-events.xml");
			List<EtalisEvent> res = new ArrayList<EtalisEvent>();
			res.addAll(io.deserializeAll(is));

			Assert.assertEquals(res, expected);
		}
		catch (Exception e) {
			logger.severe(e.toString());
			Assert.fail(e.toString());
		}
	}

}

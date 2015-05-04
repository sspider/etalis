package com.jtalis.core.event.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.core.event.persistence.EventStreamInput;
import com.jtalis.core.event.persistence.EventStreamOutput;
import com.jtalis.core.event.schema.EventProperty;
import com.jtalis.core.event.schema.EventSchema;
import com.veskogeorgiev.probin.conversion.CompositeConverter;

/**
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class XMLEventIO implements EventStreamOutput, EventStreamInput {

	private static final Logger logger = Logger.getLogger(XMLEventIO.class.getName());

	private EventSchema schema;

	/* Input Part */

	private SAXParser saxParser;
	private CompositeConverter converter = new CompositeConverter();

	private Map<InputStream, Queue<EtalisEvent>> cach = new HashMap<InputStream, Queue<EtalisEvent>>();

	/* Output Part */

	private DOMImplementation impl;
	private Transformer serializer;
	private String xmlSchemaUri;

	private SAXParserFactory saxFactory;

	public XMLEventIO(File xsd) throws Exception {
		this(new XMLEventSchema(xsd));
	}

	public XMLEventIO(EventSchema xmlSchema) throws Exception {
		this.schema = xmlSchema;

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		impl = builder.getDOMImplementation();

		serializer = TransformerFactory.newInstance().newTransformer();
		serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		serializer.setOutputProperty(OutputKeys.INDENT, "yes");

		saxFactory = SAXParserFactory.newInstance();
		saxParser = saxFactory.newSAXParser();
	}

	public void setOutputProperty(String key, String value) {
		serializer.setOutputProperty(key, value);
	}

	public EventSchema getXmlSchema() {
		return schema;
	}

	// /////////////////////////////////////////////////////////////////////////
	// Input Part
	// /////////////////////////////////////////////////////////////////////////

	@Override
	public Collection<EtalisEvent> deserializeAll(InputStream input) throws IOException {
		Handler h = new Handler();
		try {
			saxParser = saxFactory.newSAXParser();
			saxParser.parse(input, h);
		}
		catch (SAXException e) {
			// we're cool. whatever's parsed is returned
		}
		catch (ParserConfigurationException e) {
			// can't really do anything
		}
		return h.events;
	}

	@Override
	public EtalisEvent deserialize(InputStream input) throws IOException {
		Queue<EtalisEvent> q = getCach(input);
		try {
			q.addAll(deserializeAll(input));
		}
		catch (IOException e) {
			if (q.isEmpty()) {
				throw e;
			}
		}
		if (!q.isEmpty()) {
			return q.poll();
		}
		return null;
	}

	private Queue<EtalisEvent> getCach(InputStream is) {
		Queue<EtalisEvent> ret = cach.get(is);
		if (ret == null) {
			cach.put(is, ret = new LinkedList<EtalisEvent>());
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	private EtalisEvent createEvent(String eventName, Map<String, Object> props) {
		List<EventProperty> properties = schema.getProperties(eventName);
		Object[] args = new Object[properties.size()];

		int i = 0;
		for (EventProperty ep : properties) {
			Object propValue = props.get(ep.getName());
			if (propValue != null) {
				try {
					args[i++] = ep.getType().equals(EtalisEvent.class) ? 
							createEvent(ep.getName(), (Map<String, Object>) propValue) : 
							converter.convert(propValue.toString(), ep.getType());
				}
				catch (Exception e) {
					logger.warning(e.toString());
				}				
			}
			else {
				args[i++] = null;
			}
		}
		return new EtalisEvent(eventName, args);
	}

	private class Handler extends DefaultHandler {
		private Stack<String> stack = new Stack<String>();
		private String eventName;
		private Stack<Map<String, Object>> propertiesStack = new Stack<Map<String, Object>>();
		private List<EtalisEvent> events = new LinkedList<EtalisEvent>();

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			if (stack.isEmpty()) {
				if (!schema.getEvents().contains(qName)) {
					return;
				}
				eventName = qName;
				propertiesStack.push(new HashMap<String, Object>());
			}
			else if (getParentEventSchema() != null && getParentEventSchema().get(qName) == EtalisEvent.class) {
				propertiesStack.push(new HashMap<String, Object>());
			}
			stack.push(qName);
		}

		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			String value = new String(ch, start, length);
			if (!value.contains("\n")) {
				if (!propertiesStack.isEmpty()) {
					propertiesStack.peek().put(stack.peek(), value);
				}
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			if (!stack.isEmpty()) {
				stack.pop();
				if (stack.size() > 0) {
					if (getParentEventSchema() != null) {
						if (EtalisEvent.class == getParentEventSchema().get(qName)) {
							Map<String, Object> props = propertiesStack.pop();
							propertiesStack.peek().put(qName, props);
						}
					}
				}
				else {
					events.add(createEvent(eventName, propertiesStack.peek()));
					stack = new Stack<String>();
					propertiesStack = new Stack<Map<String, Object>>();
				}
			}
		}

		private Map<String, Class<?>> getParentEventSchema() {
			List<EventProperty> props = schema.getProperties(stack.peek());
			if (props != null) {
				Map<String, Class<?>> eventSchema = new HashMap<String, Class<?>>();
				for (EventProperty ep : props) {
					eventSchema.put(ep.getName(), ep.getType());
				}
				return eventSchema;
			}
			return null;
		}
	}

	// /////////////////////////////////////////////////////////////////////////
	// Output Part
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * If you set this property, the specified URI will be implied into each
	 * generated XML node as XML Schema address. This is optional. e.g <br>
	 * <br>
	 * {@code <event xsi:noNamespaceSchemaLocation=xmlSchemaUri> } <br>
	 * {@code ... } <br>
	 * {@code </event> } <br>
	 * 
	 * @param xmlSchemaUri
	 *            URI of the XML schema to be implied in each XML node
	 */
	public void setXmlSchemaUri(String xmlSchemaUri) {
		this.xmlSchemaUri = xmlSchemaUri;
	}

	@Override
	public void serialize(EtalisEvent event, OutputStream out) throws IOException {
		Document doc = impl.createDocument(null, null, null);

		Element eventNode = serializeToXmlElement(event, doc);
		doc.appendChild(eventNode);

		try {
			serializer.transform(new DOMSource(doc), new StreamResult(out));
		}
		catch (TransformerException e) {
			if (e.getCause() instanceof IOException || e.getCause() instanceof SocketException || e.getMessage().contains(IOException.class.getName()) || e.getMessage().contains(SocketException.class.getName())) {
				throw new IOException();
			}
			logger.warning(e.toString());
		}
	}

	/**
	 * Creates an xml node for a given event
	 * 
	 * @param event
	 *            the event to create a node for
	 * @param doc
	 *            used to create the element
	 * @return xml node for a given event
	 */
	public Element serializeToXmlElement(EtalisEvent event, Document doc) {
		Element eventNode = doc.createElement(event.getName());
		if (xmlSchemaUri != null) {
			eventNode.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			eventNode.setAttribute("xsi:noNamespaceSchemaLocation", xmlSchemaUri);
		}

		List<EventProperty> properties = schema.getProperties(eventNode.getNodeName());

		for (int j = 0; j < event.getArity(); j++) {
			EventProperty epd = properties != null && j < properties.size() ? properties.get(j) : null;

			String pName = epd != null ? epd.getName() : "property" + j;
			Class<?> pType = epd != null ? epd.getType() : event.getProperty(j).getClass();
			Object pValue = event.getProperty(j);

			if (pValue instanceof EtalisEvent) {
				Element propertyNode = serializeToXmlElement((EtalisEvent) pValue, doc);
				eventNode.appendChild(propertyNode);
			}
			else {
				Element propertyNode = doc.createElement(pName);
				eventNode.appendChild(propertyNode);

				Object convetedValue = null;
				try {
					convetedValue = converter.convert(pValue.toString(), pType);
					Text n = doc.createTextNode(converter.convertToString(convetedValue));
					propertyNode.appendChild(n);
				}
				catch (Exception e) {
					logger.warning(e.toString());
					Text n = doc.createTextNode("");
					propertyNode.appendChild(n);;
				}
			}
		}
		return eventNode;
	}

}

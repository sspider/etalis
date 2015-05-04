package com.jtalis.core.event.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.core.event.schema.EventProperty;
import com.jtalis.core.event.schema.EventSchema;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSModelGroup;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.parser.XSOMParser;

/**
 * Xml event schema
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class XMLEventSchema implements EventSchema {

	private Map<String, Class<?>> types = new HashMap<String, Class<?>>();
	{
		types.put("string", String.class);
		types.put("integer", Integer.class);
		types.put("int", Integer.class);
		types.put("long", Long.class);
		types.put("short", Short.class);
		types.put("decimal", java.math.BigDecimal.class);
		types.put("float", Float.class);
		types.put("double", Double.class);
		types.put("boolean", Boolean.class);
		types.put("byte", Byte.class);
		types.put("QName", javax.xml.namespace.QName.class);
		types.put("dateTime", javax.xml.datatype.XMLGregorianCalendar.class);
		types.put("base64Binary", byte[].class);
		types.put("hexBinary", byte[].class);
		types.put("unsignedInt", Long.class);
		types.put("unsignedShort", Integer.class);
		types.put("unsignedByte", Short.class);

		types.put("time", javax.xml.datatype.XMLGregorianCalendar.class);
		types.put("date", javax.xml.datatype.XMLGregorianCalendar.class);
		types.put("g", javax.xml.datatype.XMLGregorianCalendar.class);

		types.put("anySimpleType", java.lang.Object.class);
		types.put("anySimpleType", java.lang.String.class);

		types.put("duration", javax.xml.datatype.Duration.class);
		types.put("NOTATION", javax.xml.namespace.QName.class);
	}

	private Map<String, List<EventProperty>> eventNameToProperties;

	public XMLEventSchema(String xsdFile) throws ParserConfigurationException {
		this(new File(xsdFile));
	}

	public XMLEventSchema(File xsd) throws ParserConfigurationException {
		eventNameToProperties = new HashMap<String, List<EventProperty>>();
		InputStream in = null;

		try {
			in = new FileInputStream(xsd);
			readSchema(in);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (in != null) {
				try {
					in.close();
				}
				catch (IOException e) {
				}
			}
		}
	}

	public XMLEventSchema(InputStream is) throws ParserConfigurationException, SAXException, IOException {
		eventNameToProperties = new HashMap<String, List<EventProperty>>();
		readSchema(is);
	}

	@Override
	public List<EventProperty> getProperties(String eventName) {
		return eventNameToProperties.get(eventName);
	}

	@Override
	public Set<String> getEvents() {
		return eventNameToProperties.keySet();
	}

	private void readSchema(InputStream xsd) throws SAXException, IOException {
		XSOMParser parser = new XSOMParser();
		parser.parse(xsd);

		XSSchema s = parser.getResult().getSchema(1);

		for (Entry<String, XSElementDecl> entry : s.getElementDecls().entrySet()) {
			XSElementDecl element = entry.getValue();
			String eventName = element.getName();

			if (element.getType().isComplexType()) {
				processComplexType(element.getType().asComplexType(), eventName);
			}
			else {
				// TODO simple type
			}
		}
		for (Entry<String, XSComplexType> entry : s.getComplexTypes().entrySet()) {
			XSComplexType element = entry.getValue();
			String eventName = element.getName();

			processComplexType(element, eventName);
		}
	}

	private void processComplexType(XSComplexType cxType, String eventName) {
		if (!eventNameToProperties.containsKey("eventName")) {			
			eventName = eventName.toLowerCase();
			XSParticle sequence = cxType.getContentType().asParticle();
			
			if (sequence.getTerm().isModelGroup()) {
				XSModelGroup model = sequence.getTerm().asModelGroup();
				List<EventProperty> propList = new ArrayList<EventProperty>(model.getSize());
				eventNameToProperties.put(eventName, propList);

				for (Iterator<XSParticle> it = model.iterator(); it.hasNext();) {
					XSElementDecl propertyElement = it.next().getTerm().asElementDecl();
					String propName = propertyElement.getName();

					Class<?> propType = null;
					if (propertyElement.getType().isSimpleType()) {
						propType = types.get(propertyElement.getType().asSimpleType().getName());
					}
					else {
						// complex type
						propType = EtalisEvent.class;
						processComplexType(propertyElement.getType().asComplexType(), propertyElement.getType().getName());
					}
					propList.add(new EventProperty(propName, propType));
				}
			}
			else {
				// TODO not a model group
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, List<EventProperty>> e : eventNameToProperties.entrySet()) {
			sb.append(e).append("\n");
		}
		return sb.toString();
	}
}

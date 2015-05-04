package com.scadaview.jtalis.server;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.core.event.JtalisOutputEventProvider;
import com.jtalis.core.event.ProviderSetupException;
import com.jtalis.core.event.xml.XMLEventIO;
import com.jtalis.core.event.xml.XMLEventSchema;
import com.scadaview.jtalis.shared.DataContainer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;

import org.w3c.dom.DOMImplementation;

import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.service.RemoteEventServiceServlet;

public class OPCXMLOutputProvider implements JtalisOutputEventProvider{

	private RemoteEventServiceServlet servlet;
	private File xsd;
	private XMLEventIO io;


	public OPCXMLOutputProvider(RemoteEventServiceServlet servlet, File xsd) throws ProviderSetupException {
		// TODO Auto-generated constructor stub
		this.servlet = servlet;
		this.xsd = xsd;
		setup();
	}

	@Override
	public void setup() throws ProviderSetupException {
		// TODO Auto-generated method stub
		try {
			io = new XMLEventIO(new XMLEventSchema(xsd));
			io.setOutputProperty(OutputKeys.INDENT, "no");

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}

	@Override
	public void outputEvent(EtalisEvent event) {
		// TODO Auto-generated method stub
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			io.serialize(event, out);
			String res = new String(out.toByteArray());
			DataContainer data = new DataContainer();
			data.setData(res);
			//data.setData(event.toString());
			Event theEvent = data;		
			servlet.addEvent(DataContainer.VISUAL_DOMAIN, theEvent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

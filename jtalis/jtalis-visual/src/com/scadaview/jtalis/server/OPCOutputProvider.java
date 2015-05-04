package com.scadaview.jtalis.server;

import org.json.JSONObject;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.core.event.JtalisOutputEventProvider;
import com.jtalis.core.event.ProviderSetupException;
import com.scadaview.jtalis.shared.DataContainer;

import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.service.RemoteEventServiceServlet;

public class OPCOutputProvider implements JtalisOutputEventProvider{
	private RemoteEventServiceServlet servlet;

	public OPCOutputProvider(RemoteEventServiceServlet servlet) throws ProviderSetupException {
		this.servlet = servlet;
		setup();
	}
	
	@Override
	public void setup() throws ProviderSetupException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void outputEvent(EtalisEvent event) {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		json.put("EventName", event.getName());
		Object[] eventProperty = event.getProperties();
		for (int i = 0; i < eventProperty.length; i++) {
			json.put("Property" + String.valueOf(i), event.getProperty(i));
		}
		
		DataContainer data = new DataContainer();
		data.setData(json.toString());
		//data.setData(event.toString());
		Event theEvent = data;		
		servlet.addEvent(DataContainer.VISUAL_DOMAIN, theEvent);
		
		//System.out.println("event sent !");
	}

}

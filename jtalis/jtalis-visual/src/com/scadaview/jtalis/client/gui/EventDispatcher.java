package com.scadaview.jtalis.client.gui;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.scadaview.jtalis.client.gui.ui.*;

/**
 * Register and Dispatch Etalis Events and trigger the corresponding GUI Element.
 * @author Ahmed Khalil Hafsi, ahmed@hafsi.org 
 */
public class EventDispatcher {
	Map<String, GenericUI> EventMapping;
	boolean debug_out;
	/**
	 * 
	 */
	public EventDispatcher() {
		EventMapping = new HashMap<String, GenericUI>();
		debug_out=true;
		
	}

	/**
	 * Register a new Mapping of an ETALIS Event and a GUI Element
	 * 
	 * @param EtalisEventSpec
	 *            This a Signature of an Etalis Event in the following Format :
	 *            Header/Arity
	 * @param GUIElement
	 *            A GUI Element that is associated with the Etalis Event.
	 */
	public void RegisterNewEventMapping(String EtalisEventSpec,
			GenericUI GUIElement) {
		//System.out.println("regist " + EtalisEventSpec);
		EventMapping.put(EtalisEventSpec, GUIElement);

	}

	/**
	 * @param EtalisEventSpec
	 */
	public void RemoveEventMapping(String EtalisEventSpec) {
		EventMapping.remove(EtalisEventSpec);
	}

/*	*//**
	 * When an ETALIS event is fired, we dispatch the GUI Action using Dispatch
	 * 
	 * @param EtalisEvent
	 *            The ETALIS Event to be dispatched, for example :
	 *            water_tank(2,2)
	 *//*
	public void Dispatch(String EtalisEvent) {

		GenericUI temp = EventMapping.get(getEventSpec(EtalisEvent));
		if(temp != null)
		{
			temp.update(EtalisEvent);
			if (debug_out)
			{
				//TODO report all events in a raw into the GUI console.
				System.out.println(EtalisEvent);
			}
		}
		else
			GWT.log(getEventSpec(EtalisEvent) + " is not registered !");
	}*/
	
	/**
	 * When an ETALIS event is fired, we dispatch the GUI Action using Dispatch
	 * 
	 * @param EtalisEvent
	 *            The ETALIS Event to be dispatched, for example :
	 *            water_tank(2,2)
	 */
	public void Dispatch(JSONObject EtalisEvent) {

		GenericUI temp = EventMapping.get(getEventSpec(EtalisEvent));
		//System.out.println("Dispatch to " + getEventSpec(EtalisEvent));
		if(temp != null)
		{
			temp.update(EtalisEvent);
			if (debug_out)
			{
				//TODO report all events in a raw into the GUI console.
				System.out.println(EtalisEvent);
			}
		}
		else {
			System.out.println(getEventSpec(EtalisEvent) + " is not registered !");
			GWT.log(getEventSpec(EtalisEvent) + " is not registered !");
		}
			
	}
	
	/**
	 * An internal function that finds the Event Signature of an ETALIS Event.
	 * 
	 * @param Event
	 * @return Signature
	 */
	private String getEventSpec(JSONObject Event) {
		// Property0 of Etalis Event is the Component ID
		JSONString json = (JSONString) Event.get("Property0");
		String EventSpec = json.stringValue();
		//System.out.println("Event Spec : " + EventSpec);
		return EventSpec;
	}
	

	/**
	 * An internal function that finds the Event Signature of an ETALIS Event.
	 * 
	 * @param Event
	 * @return Signature
	 */
	private String getEventSpec(String Event) {
		String Header = Event.split("[(]+")[0];
		int arity = Event.split("[,]+").length;
		String EventSpec = Header + "/"+arity;
		System.out.println("Event Spec : " + EventSpec);
		return EventSpec;
	}
	


}

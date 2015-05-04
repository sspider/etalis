package com.scadaview.jtalis.client.gui.ui;

import org.vectomatic.dom.svg.OMElement;
import org.vectomatic.dom.svg.OMSVGSVGElement;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.scadaview.jtalis.client.DataServiceAsync;

public class EventButton extends GenericUI{
	OMElement button;
	String eventID;
	DataServiceAsync eventService;
	
	public EventButton(OMSVGSVGElement root, String buttonID, DataServiceAsync myService) {
		super(null);
		this.eventID = buttonID;
		this.button = root.getElementById(buttonID);
		this.eventService = myService;
		
		this.button.addDomHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				eventService.sendEvent(eventID, 
						new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						
					}

					@Override
					public void onFailure(Throwable caught) {

					}
				});
				
			}
		}, ClickEvent.getType());
	}

	@Override
	public void update(JSONObject Event) {
		// TODO Auto-generated method stub
		
	}

}

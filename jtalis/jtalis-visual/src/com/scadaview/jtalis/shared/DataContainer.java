package com.scadaview.jtalis.shared;

import de.novanic.eventservice.client.event.*;
import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.domain.DomainFactory;

public class DataContainer implements Event{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2349387930682539000L;
	public static final Domain VISUAL_DOMAIN = DomainFactory.getDomain("visualDomain"); 
	
	private String data="";
	private String etalis_event="";

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	

}

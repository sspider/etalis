package com.scadaview.jtalis.client;

import com.scadaview.jtalis.shared.DataContainer;

import de.novanic.eventservice.client.event.listener.RemoteEventListener;

/**
 * @author Ahmed Khalil Hafsi, ahmed@hafsi.org
 *
 */
public interface EventUserListener extends RemoteEventListener{
	
	void OnMessage(DataContainer data);
	

}

package com.scadaview.jtalis.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


/**
 * The main Interface for the exachange of data between a client and a server.
 * @author Ahmed Khalil Hafsi, ahmed@hafsi.org
 *
 */
@RemoteServiceRelativePath("DataService")
public interface DataService extends RemoteService{

	void start();
	void startdemo();
	void sendEvent(String event);
	
}

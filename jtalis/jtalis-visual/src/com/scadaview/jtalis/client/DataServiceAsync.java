package com.scadaview.jtalis.client;

import java.io.File;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Ahmed Khalil Hafsi, ahmed@hafsi.org
 *
 */
public interface DataServiceAsync {

	void start(AsyncCallback<Void> callback);

	void startdemo(AsyncCallback<Void> callback);
	
	void sendEvent(String event, AsyncCallback<Void> callback);

}

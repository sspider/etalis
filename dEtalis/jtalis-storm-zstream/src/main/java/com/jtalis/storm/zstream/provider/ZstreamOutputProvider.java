package com.jtalis.storm.zstream.provider;


import com.jtalis.core.event.EtalisEvent;
import com.jtalis.core.event.JtalisOutputEventProvider;
import com.jtalis.core.event.ProviderSetupException;


public class ZstreamOutputProvider implements JtalisOutputEventProvider{
	private com.jtalis.storm.zstream.channel.NodeBuffer internalNode;
	
	public ZstreamOutputProvider(com.jtalis.storm.zstream.channel.NodeBuffer internalNode) {
		this.internalNode = internalNode;
	}
	

	public void setup() throws ProviderSetupException {
		// TODO Auto-generated method stub
		
	}

	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

	public void outputEvent(EtalisEvent event) {
		// TODO Auto-generated method stub
		System.out.println("Event received from etalis: " + event);
		internalNode.addNewEvent(event);
	}

}

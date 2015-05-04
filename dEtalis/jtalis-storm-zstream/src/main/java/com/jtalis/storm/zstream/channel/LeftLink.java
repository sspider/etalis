/**
 * 
 */
package com.jtalis.storm.zstream.channel;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.storm.zstream.agent.IAgent;

/**
 * @author darko
 *
 */
public class LeftLink implements IChannel{
	private IAgent agent;
	
	/**This is a connector used to connect two agents (a source- and a destination agent). 
	 * An instance of LeftLink is normally used as an output buffer (outBuffer) of an 
	 * agent. This is a source agent. An event produced by the source agent is then fed 
	 * into a destination agent. The destination agent is specified by the IAgent 
	 * argument. By default an event will be placed into the left child buffer of that 
	 * agent.   
	 * 
	 * @param the destination agent.
	 */
	public LeftLink (IAgent agent){
		this.agent = agent; 
	}

	public void add(EtalisEvent event) {
		this.agent.addEventToLeftChildBuffer(event);
	}
}
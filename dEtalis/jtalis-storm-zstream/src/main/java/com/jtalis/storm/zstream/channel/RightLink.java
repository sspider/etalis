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
public class RightLink implements IChannel{
	private IAgent agent;
	
	/**This is a connector used to connect two agents (a source- and a destination agent). 
	 * An instance of RightLink is normally used as an output buffer (outBuffer) of an 
	 * agent. This is a source agent. An event produced by the source agent is then fed 
	 * into a destination agent. The destination agent is specified by the IAgent 
	 * argument. By default an event will be placed into the right child buffer of that 
	 * agent.   
	 * 
	 * @param the destination agent.
	 */
	public RightLink (IAgent agent){
		this.agent = agent; 
	}

	public void add(EtalisEvent event) {
		this.agent.addEventToRightChildBuffer(event);
	}
}

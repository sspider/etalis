/**
 * 
 */
package com.jtalis.storm.zstream.channel;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.storm.zstream.agent.ITernaryAgent;

/**
 * @author darko
 *
 */
public class MiddleLink implements IChannel{
	private ITernaryAgent agent;
	
	/**This is a connector used to connect two agents (a source- and a destination agent). 
	 * An instance of MiddleLink is normally used as an output buffer (outBuffer) of an 
	 * agent. This is a source agent. An event produced by the source agent is then fed 
	 * into a destination agent. The destination agent is specified by the ITernaryAgent 
	 * argument. By default an event will be placed into the middle child buffer of that 
	 * agent. A ternary agent is for example an agent implementing the negation operation 
	 * or the Kleene operator. 
	 *  
	 * @param the destination agent.
	 */
	public MiddleLink (ITernaryAgent agent){
		this.agent = agent; 
	}

	public void add(EtalisEvent event) {
		this.agent.addEventToMiddleChildBuffer(event);
	}
}
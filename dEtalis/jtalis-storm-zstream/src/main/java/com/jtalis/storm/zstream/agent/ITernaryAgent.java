/**
 * 
 */
package com.jtalis.storm.zstream.agent;

import com.jtalis.core.event.EtalisEvent;

/**
 * @author darko
 *
 */
public interface ITernaryAgent extends IAgent{
	
	public void addEventToMiddleChildBuffer(EtalisEvent event);

}

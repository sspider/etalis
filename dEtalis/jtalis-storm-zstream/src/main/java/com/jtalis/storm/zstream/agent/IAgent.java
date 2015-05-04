/**
 * 
 */
package com.jtalis.storm.zstream.agent;

import com.jtalis.core.event.EtalisEvent;

/**
 * @author darko
 *
 */
public interface IAgent {
	
	public void addEventToLeftChildBuffer(EtalisEvent event);
	
	public void addEventToRightChildBuffer(EtalisEvent event);
}

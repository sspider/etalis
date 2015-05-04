/**
 * 
 */
package com.jtalis.storm.zstream.channel;

import com.jtalis.core.event.EtalisEvent;

/**
 * @author darko
 *
 */
public interface IChannel {
	
	public void add(EtalisEvent event);
}

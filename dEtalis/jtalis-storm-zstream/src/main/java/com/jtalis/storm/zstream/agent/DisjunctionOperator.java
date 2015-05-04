/**
 * 
 */
package com.jtalis.storm.zstream.agent;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.storm.zstream.channel.IChannel;

/**
 * @author darko
 *
 */
public class DisjunctionOperator implements IAgent{
	private IChannel outChannel;
	private long timeWindow;
	private int noResult = 0;
		
	public DisjunctionOperator (IChannel outChannel, long timeWindow) {
		this.outChannel = outChannel;
		this.timeWindow = timeWindow;
	}
	
	private void disjunction(EtalisEvent event) {
		if((event.getTimeEnds().getTime()- event.getTimeStarts().getTime()) < this.timeWindow) {
			combineEtalisEvent(event);
		}
	}
	
	private void combineEtalisEvent(EtalisEvent event) {
		//if an event either from the left child buffer or from the right child 
		//buffer meets value constraints combine it and possibly propagate further
		//TODO: feed the event to jtalis and store the result to the internal buffer
		/*ctx.pushEvent(leftEvent);
		ctx.pushEvent(rightEvent);*/
		System.out.println("disj ce: " + event);
		this.noResult++;
		// TODO: correct this!
		this.outChannel.add(event);
	}
	
	@Override
	public void addEventToLeftChildBuffer(EtalisEvent event) {
		disjunction(event);
	}

	@Override
	public void addEventToRightChildBuffer(EtalisEvent event) {
		disjunction(event);
	}

}

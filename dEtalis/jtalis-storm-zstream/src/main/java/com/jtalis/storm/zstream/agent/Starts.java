/**
 * 
 */
package com.jtalis.storm.zstream.agent;

import com.jtalis.storm.zstream.channel.IChannel;

/**
 * This is an agent that detects when an event (from left-child-buffer) 
 * "starts" another event (from right-child-buffer) according to the 
 * Allen's Interval Algebra.
 * The agent is implemented as an {AbstractSequenceOperator} agent where 
 * the time constraint is different (see the method: evaluateTimeConstraint).
 * 
 * @author darko
 *
 */
public class Starts extends AbstractSequenceOperator {

	public Starts(IChannel outChannel, long timeWindow) {
		super(outChannel, timeWindow);
	}
	
	@Override
	protected boolean evaluateTimeConstraint(
			long leftEventStarts, long leftEventEnds, 
			long rightEventStarts, long rightEventEnds) {
		
		if (leftEventStarts == rightEventStarts && leftEventEnds < rightEventEnds){
			return true;
		}else{
			return false;
		}
	}
}

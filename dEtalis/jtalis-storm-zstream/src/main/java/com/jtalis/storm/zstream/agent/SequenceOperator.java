/**
 * 
 */
package com.jtalis.storm.zstream.agent;

import backtype.storm.task.OutputCollector;

import com.jtalis.core.JtalisContext;
import com.jtalis.storm.zstream.channel.IChannel;

/**
 * 
 * @author darko
 *
 */
//<<<<<<< .mine
public class SequenceOperator extends AbstractSequenceOperator {

	public SequenceOperator(IChannel outChannel, long timeWindow) {
		super(outChannel, timeWindow);
	}
	
	public SequenceOperator(String outputEventName, String streamID,
			long timeWindow, OutputCollector collector) {
		super(outputEventName, streamID, timeWindow, collector);
	}
	
	public SequenceOperator(JtalisContext ctx, long timeWindow) {
		super(ctx, timeWindow);
	}

	
	@Override
	protected boolean evaluateTimeConstraint(
			long leftEventStarts, long leftEventEnds, 
			long rightEventStarts, long rightEventEnds) {
		
		if (leftEventEnds < rightEventStarts){
			return true;
		}else{
			return false;
		}
	}
}





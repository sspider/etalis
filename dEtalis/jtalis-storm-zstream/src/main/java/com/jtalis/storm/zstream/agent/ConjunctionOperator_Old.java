package com.jtalis.storm.zstream.agent;

import com.jtalis.core.JtalisContextImpl;
import com.jtalis.core.event.EtalisEvent;
import com.jtalis.storm.zstream.channel.NodeBuffer;

/**
 * @deprecated use {ConjunctionOperator} instead.
 * 
 * @author darko
 *
 */
public class ConjunctionOperator_Old extends Thread{
	private NodeBuffer leftChildBuffer;
	private NodeBuffer rightChildBuffer;
	private long timeWindow;
	private JtalisContextImpl ctx;
	private int noResult = 0;
	
	private long leftIdx;
	private long rightIdx;

	public ConjunctionOperator_Old(NodeBuffer leftChildBuffer,
			NodeBuffer rightChildBuffer,
			long timeWindow) {
		this.leftChildBuffer = leftChildBuffer;
		this.rightChildBuffer = rightChildBuffer;
		this.timeWindow = timeWindow;
		this.leftIdx = 0;
		this.rightIdx = 0;
	}

	public ConjunctionOperator_Old(NodeBuffer leftChildBuffer,
			NodeBuffer rightChildBuffer,
			long timeWindow,
			JtalisContextImpl ctx) {
		this.leftChildBuffer = leftChildBuffer;
		this.rightChildBuffer = rightChildBuffer;
		this.timeWindow = timeWindow;
		this.ctx = ctx;
		//ctx.addDynamicRule(etalisRule);
		this.leftIdx = 0;
		this.rightIdx = 0;

	}

	public void run() {
		while(true) {
			if (!rightChildBuffer.getCurrentBuffer().isEmpty() 
					&& !leftChildBuffer.getCurrentBuffer().isEmpty()) {

				conjunction(leftChildBuffer, rightChildBuffer);
			}
		}
	}

	private synchronized void conjunction(NodeBuffer leftChildBuffer, 
			NodeBuffer rightChildBuffer) {
		long tmpLeftIdx = leftChildBuffer.getBufferSize();
		long tmpRightIdx = rightChildBuffer.getBufferSize();

		while ((leftIdx < tmpLeftIdx || rightIdx < tmpRightIdx) 
				&& leftIdx >= 0 && rightIdx >= 0) {
			EtalisEvent rightEvent = rightChildBuffer.getEvent(rightIdx);
			EtalisEvent leftEvent = leftChildBuffer.getEvent(leftIdx);
			if (rightEvent == null) {
				long EAT = leftEvent.getTimeEnds().getTime() - timeWindow;

				for (int i = 0; i < rightIdx; ) {
					EtalisEvent tmpEvent = rightChildBuffer.getEvent(i);
					if (tmpEvent.getTimeStarts().getTime() < EAT) {
						rightChildBuffer.getCurrentBuffer().remove(tmpEvent);
						rightIdx--;
						tmpRightIdx--;
						continue;
					} else {
						if (leftEvent.getTimeStarts().getTime() >= tmpEvent.getTimeEnds().getTime())
							combineEtalisEvent(tmpEvent, leftEvent);
						i++;
					}
				}
				leftIdx++;				
				
			} else if (leftEvent == null) {
				long EAT = rightEvent.getTimeEnds().getTime() - timeWindow;
				for (int i = 0; i < leftIdx; ) {
					EtalisEvent tmpEvent = leftChildBuffer.getEvent(i);
					if (tmpEvent.getTimeStarts().getTime() < EAT) {
						leftChildBuffer.getCurrentBuffer().remove(tmpEvent);
						leftIdx--;
						tmpLeftIdx--;
						continue;
					} else {
						if (rightEvent.getTimeStarts().getTime() >= tmpEvent.getTimeEnds().getTime())
							combineEtalisEvent(tmpEvent, rightEvent);
						i++;
					}
				}
				rightIdx++;
				
			} else {
				if (leftEvent.getTimeEnds().getTime() > rightEvent.getTimeEnds().getTime()){
					// left event's end time > right event's end time
					// right event match with the event in left buffer
					long EAT = rightEvent.getTimeEnds().getTime() - timeWindow;
					for (int i = 0; i < leftIdx; ) {
						EtalisEvent tmpEvent = leftChildBuffer.getEvent(i);
						if (tmpEvent.getTimeStarts().getTime() < EAT) {
							leftChildBuffer.getCurrentBuffer().remove(tmpEvent);
							leftIdx--;
							tmpLeftIdx--;
							continue;
						} else {
							//if (rightEvent.getTimeStarts().getTime() >= tmpEvent.getTimeEnds().getTime())
								combineEtalisEvent(tmpEvent, rightEvent);
							i++;
						}
					}
					rightIdx++;

				} else {
					long EAT = leftEvent.getTimeEnds().getTime() - timeWindow;

					for (int i = 0; i < rightIdx; ) {
						EtalisEvent tmpEvent = rightChildBuffer.getEvent(i);
						if (tmpEvent.getTimeStarts().getTime() < EAT) {
							rightChildBuffer.getCurrentBuffer().remove(tmpEvent);
							rightIdx--;
							tmpRightIdx--;
							continue;
						} else {
							//if (leftEvent.getTimeStarts().getTime() >= tmpEvent.getTimeEnds().getTime())
								combineEtalisEvent(tmpEvent, leftEvent);
							i++;
						}
					}
					leftIdx++;
				}
			}
			//System.out.println(rightEvent);
			/*        	long EAT = rightEvent.getTimeStarts().getTime() - timeWindow;


        	if (leftEvent.getTimeStarts().getTime() < EAT) {
        		leftChildBuffer.getCurrentBuffer().remove(leftEvent);
        		tmpLeftIdx--;
        		continue;
        	}*/



		}

	}


	private void combineEtalisEvent(EtalisEvent leftEvent, EtalisEvent rightEvent) {
		//combine the event from left child buffer and right child buffer
		//TODO: feed the event to jtalis and store the result to the internal buffer
		//ctx.pushEvent(leftEvent);
		//ctx.pushEvent(rightEvent);

		System.out.println("conj(" + leftEvent.getPrologString() + ", " + rightEvent.getPrologString() + ")");
		this.noResult++;
	}
	
	public int getNoResult() {
		return noResult;
	}
}

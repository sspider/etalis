package com.jtalis.core.event;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

/**
 * Represents event timestamp. Main purpose is to hide the counter in Prolog ETALIS, which counts how many 
 * events have emerged within the same second in order to handle the milliseconds. 
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class EventTimestamp extends Date implements Serializable {

	private static final long serialVersionUID = -8939549196656694822L;
	
	/** Index of this event timestamp in the it's second */
	private long index;
	
	public EventTimestamp(double time, long index) {
		super((long) time * 1000);
		this.index = index;
	}
	
	public EventTimestamp(long time) {
		super(time);
		this.index = 0;
	}

	public EventTimestamp() {
		super(System.currentTimeMillis());
	}

	public long getIndex() {
		return index;
	}
	
	public void setIndex(long index) {
		this.index = index;
	}
	
	public void setTime(long time){
		super.setTime(time);
	}
	
	public long getTimeWithoutIndex(){
		return super.getTime();
	}

	@Override
	public long getTime() {
		return super.getTime() + index;
	}

	@Override
	public String toString() {
		DateFormat df = DateFormat.getTimeInstance();
		return df.format(this) + " #" + index;
	}
}

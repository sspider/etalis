/**
 * 
 */
package com.jtalis.storm.zstream.channel;

import org.apache.commons.collections.buffer.BoundedFifoBuffer;


/**
 * @author darko
 *
 */
public class SumBuffer {
	private BoundedFifoBuffer buffer;
	//ConcurrentLinkedQueue<EtalisEvent> buffer;
	private float value = 0;
	//private int index = 0;
			
	public SumBuffer(int size) {
		this.buffer = new BoundedFifoBuffer(size);
		//this.buffer = new ConcurrentLinkedQueue();
		//this.index = index;
	}

	public float add (Object value) {
		if(this.buffer.isFull()){
			Object oldValue = this.buffer.remove();
			this.value = this.value - (Float) oldValue;
		}
		this.buffer.add(value);
		this.value = this.value + (Float) value;
		return this.value;
	}
	
	/*public void add (EtalisEvent event) {
		event.getTerm(index);
		this.value = this.value + (Float)value;
	}*/
	
	public boolean isFull() {
		return this.buffer.isFull();
	}
	
	public float getValue() {
		return this.value;
	}
}

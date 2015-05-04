/**
 * 
 */
package com.jtalis.storm.zstream.test.performance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import com.jtalis.core.event.EtalisEvent;

/**
 * Test: ConcurrentLinkedQueue vs. ArrayList - for 100K elements, 
 * ConcurrentLinkedQueue is approx. 10 times faster than ArrayList 
 * and 50 times faster than CopyOnWriteArrayList 
 * when executing operations we need for the project (ArrayList was in 
 * the fastest mode, i.e., buffer predefined and iterated in the 
 * reverse mode).
 * 
 * @author darko
 *
 */
public class BufferTest {
	private Collection<EtalisEvent> eventBuffer;
	private ArrayList<EtalisEvent> eventBuffer1;
	private ConcurrentLinkedQueue<EtalisEvent> eventBuffer2;
	private CopyOnWriteArrayList<EtalisEvent> eventBuffer3;
	
	private EventOperator operator;
	
	private long startTime = 0;
	private long endTime = 0;
	private long time = 0;
	private int bufferNo;
	private int streamSize;
	private static int threadMode;
	private int result=0;
	private static boolean print = false;
	
	/**
	 * @param bufferNo	- set 1 (ArrayList), 2 (ConcurrentLinkedQueue) or 3 (CopyOnWriteArrayList).
	 * @param threadMode- set 1 (no threading, first generate then process and delete), 
	 * 							2 (with threading, i.e., the generating then the processing threads 
	 * 							run concurrently).
	 *@param streamSize- set the stream size, e.g., 1000000 events in a stream 
	 */
	public BufferTest(int bufferNo, int threadMode, int streamSize) {
		this.bufferNo = bufferNo;
		this.threadMode = threadMode;
		this.streamSize = streamSize;
		
		switch (bufferNo){
			case 1:	this.eventBuffer1 = new ArrayList<EtalisEvent>(this.streamSize);
					this.operator = new EventOperator(bufferNo, this.eventBuffer1);
					break;
			case 2:	this.eventBuffer2 = new ConcurrentLinkedQueue<EtalisEvent>();
					this.operator = new EventOperator(bufferNo, this.eventBuffer2);
					break;
			case 3:	this.eventBuffer3 = new CopyOnWriteArrayList<EtalisEvent>();
					this.operator = new EventOperator(bufferNo, this.eventBuffer3);
					break;
		}
	}
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		BufferTest test = new BufferTest(2, 1, 100000);
		
		test.startTime = System.currentTimeMillis();
		
		switch (threadMode){
		case 1:	test.generate();
				test.operator.process();
				break;
		case 2:	new Thread(new GeneratingThread(test)).start();
				Thread.sleep(500);
				new Thread(new ProcessingThread(test)).start();
				break;	
		}
		
		test.endTime = System.currentTimeMillis();
		test.time = test.endTime-test.startTime;
		System.out.println("No. of generated events: " + test.result);
		System.out.println("Test executed in: " + test.time + " ms");
	}
	
	public void generate() throws InterruptedException {
		EtalisEvent event;
		switch (bufferNo){
			case 1:	this.eventBuffer = this.eventBuffer1;
					break;
			case 2:	this.eventBuffer = this.eventBuffer2;
					break;
			case 3:	this.eventBuffer = this.eventBuffer3;
					break;
		}
		for(int i=0; i<streamSize; i++) {
			event = new EtalisEvent("a", i);
			if(print) System.out.println("generate: " + event);
			this.eventBuffer.add(event);
			result++;
			//Thread.sleep(10);
		}
	}
	
	private class EventOperator {
		private ArrayList<EtalisEvent> eventBuffer1;
		private ConcurrentLinkedQueue<EtalisEvent> eventBuffer2;
		private CopyOnWriteArrayList<EtalisEvent> eventBuffer3;
		
		private int bufferNo;
		private int resultProcess=0;
		
		public EventOperator(int bufferNo, Collection<EtalisEvent> eventBuffer) {
			this.bufferNo = bufferNo;
			switch (bufferNo){
				case 1:	this.eventBuffer1 = (ArrayList<EtalisEvent>) eventBuffer;
						break;
				case 2:	this.eventBuffer2 = (ConcurrentLinkedQueue<EtalisEvent>) eventBuffer;
						break;
				case 3:	this.eventBuffer3 = (CopyOnWriteArrayList<EtalisEvent>) eventBuffer;
						break;
			}
		}
		
		public void process() throws InterruptedException {
			switch (this.bufferNo){
				case 1:	this.process1();
						break;
				case 2:	process2();
						break;
				case 3:	process3();
						break;
			}
		}
		
		private void process1() throws InterruptedException {
			EtalisEvent event;
			
			// in reverse order, an ArrayList is faster
			//for(int i=0; i<this.eventBuffer1.size(); i++) {
			for(int i=this.eventBuffer1.size()-1; i>=0; i--) {
				event = eventBuffer1.get(i);
				if(print) System.out.println("process: " + event + "result no.: " + resultProcess);
				this.eventBuffer1.remove(0);
				//i++;
				resultProcess++;
				//Thread.sleep(10);
			}
		}
		
		private void process2() throws InterruptedException {
			EtalisEvent event;
			while(!this.eventBuffer2.isEmpty()) {
				event = eventBuffer2.poll();
				if(print) System.out.println("process: " + event + "result no.: " + resultProcess);
				resultProcess++;
				//Thread.sleep(10);
			}
			// Equally fast solution
			/*while(!this.eventBuffer2.isEmpty()) {
				event = eventBuffer2.peek();
				eventBuffer2.remove(event);
				if(print) System.out.println("process: " + event + "result no.: " + resultProcess);
				resultProcess++;
				//Thread.sleep(10);
			}*/
			// Equally fast solution
			/*Iterator<EtalisEvent> it = this.eventBuffer2.iterator();
			while(it.hasNext()) {
				event = it.next();
				it.remove();
				if(print) System.out.println("process: " + event + "result no.: " + resultProcess);
				resultProcess++;
				//Thread.sleep(10);
			}*/
		}
		
		private void process3() throws InterruptedException {
			//EtalisEvent event;
			//for(EtalisEvent leftEvent : leftChildBuffer.getCurrentBuffer()) {
			for(EtalisEvent event : this.eventBuffer3) {
				if(print) System.out.println("process: " + event + "result no.: " + resultProcess);
				this.eventBuffer3.remove(0);
				//Thread.sleep(10);
			}
		}
	}
	
	private static class GeneratingThread extends Thread{
		private BufferTest test = null;
		
		public GeneratingThread(BufferTest test) {
			this.test = test;
		}
		
		public void run(){
			try {
				//Thread.sleep(1);
				test.generate();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private static class ProcessingThread extends Thread{
		private BufferTest test;
		
		public ProcessingThread(BufferTest test) {
			this.test = test;
		}
		
		public void run(){
			try {
				//Thread.sleep(1);
				test.operator.process();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}

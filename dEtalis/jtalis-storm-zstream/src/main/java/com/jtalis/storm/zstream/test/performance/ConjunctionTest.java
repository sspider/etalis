/**
 * 
 */
package com.jtalis.storm.zstream.test.performance;

import com.jtalis.storm.zstream.agent.ConjunctionOperator;
import com.jtalis.storm.zstream.channel.Buffer;
import com.jtalis.storm.zstream.channel.IChannel;

/**
 * @author darko
 *
 */
public class ConjunctionTest {
	private IChannel out1_Buffer;
	private ConjunctionOperator conj;
	
	private static int streamSize = 10000;
	private int timeWindow = 10;
	
	public ConjunctionTest () {
		out1_Buffer = new Buffer();
		this.conj = new ConjunctionOperator(out1_Buffer, timeWindow);
	}
	
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		ConjunctionTest test = new ConjunctionTest ();
		
		EventGenerator generator = new EventGenerator(test.conj);
		generator.generate(streamSize);
		
		System.out.println("Number of Result: " + test.conj.getNoResult());
		double throughput = streamSize/generator.getTime() * 1000; 
		System.out.println("Input throughput: " + throughput + " event/s");
		
		throughput = test.conj.getNoResult()/generator.getTime() * 1000; 
		System.out.println("Output throughput: " + throughput + " event/s");
	}

}


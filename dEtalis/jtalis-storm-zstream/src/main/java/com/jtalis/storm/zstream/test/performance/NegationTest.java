/**
 * 
 */
package com.jtalis.storm.zstream.test.performance;

import com.jtalis.storm.zstream.agent.NegationOperator;
import com.jtalis.storm.zstream.channel.Buffer;
import com.jtalis.storm.zstream.channel.IChannel;

/**
 * @author darko
 *
 */
public class NegationTest {
	private IChannel out1_Buffer;
	private NegationOperator not_1;
	
	private static int streamSize = 100000;
	private int timeWindow = 10;
	
	public NegationTest () {
		out1_Buffer = new Buffer();
		this.not_1 = new NegationOperator(out1_Buffer, timeWindow);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws InterruptedException {
		NegationTest test = new NegationTest ();
		
		EventGenerator generator = new EventGenerator(test.not_1);
		generator.generate(streamSize);
		
		System.out.println("Number of Result: " + test.not_1.getNoResult());
		double throughput = streamSize/generator.getTime() * 1000; 
		System.out.println("Input throughput: " + throughput + " event/s");
		
		throughput = test.not_1.getNoResult()/generator.getTime() * 1000; 
		System.out.println("Output throughput: " + throughput + " event/s");
	}

}


/**
 * 
 */
package com.jtalis.storm.zstream.test.performance;

import com.jtalis.storm.zstream.agent.SequenceOperator;
import com.jtalis.storm.zstream.channel.Buffer;
import com.jtalis.storm.zstream.channel.IChannel;

/**
 * @author darko
 *
 */
public class SequenceTest {
	private IChannel out1_Buffer;
	private SequenceOperator seq_1;
	
	private static int streamSize = 100000;
	private int timeWindow = 10;
	
	public SequenceTest () {
		out1_Buffer = new Buffer();
		this.seq_1 = new SequenceOperator(out1_Buffer, timeWindow);
	}
	
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		SequenceTest test = new SequenceTest ();
		
		EventGenerator generator = new EventGenerator(test.seq_1);
		generator.generate(streamSize);
		
		System.out.println("Number of Result: " + test.seq_1.getNoResult());
		double throughput = streamSize/generator.getTime() * 1000; 
		System.out.println("Input throughput: " + throughput + " event/s");
		
		throughput = test.seq_1.getNoResult()/generator.getTime() * 1000; 
		System.out.println("Output throughput: " + throughput + " event/s");
	}

}



package com.jtalis.storm.zstream.test.performance;

import com.jtalis.storm.zstream.agent.SequenceOperator_Old;
import com.jtalis.storm.zstream.channel.NodeBuffer;

/**
 * @deprecated use {SequenceTest} instead.
 * 
 * @author darko
 *
 */
public class SequenceTest_Old {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		long window = 1000;
		NodeBuffer leftChildBuffer = new NodeBuffer(1000000);
		NodeBuffer rightChildBuffer = new NodeBuffer(1000000);
		
		SequenceOperator_Old seq = new SequenceOperator_Old(leftChildBuffer, rightChildBuffer, window, null);
		seq.start();
		
		EventGenerator_Old generator = new EventGenerator_Old(leftChildBuffer, rightChildBuffer);
		generator.generate(10);
		
		System.out.println("Number of Result: " + seq.getNoResult());
		double throughput = seq.getNoResult()/generator.getTime() * 1000; 
		System.out.println("Throughput: " + throughput + " event/s");
	}

}

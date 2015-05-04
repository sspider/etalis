package com.jtalis.storm.zstream.test.performance;

import com.jtalis.storm.zstream.agent.ConjunctionOperator_Old;
import com.jtalis.storm.zstream.channel.NodeBuffer;

/**
 * @deprecated use {ConjunctionTest} instead.
 * 
 * @author darko
 *
 */
public class ConjunctionTest_Old {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		long window = 100000;
		NodeBuffer leftChildBuffer = new NodeBuffer(1000000);
		NodeBuffer rightChildBuffer = new NodeBuffer(1000000);
		
		ConjunctionOperator_Old conj = new ConjunctionOperator_Old(leftChildBuffer, rightChildBuffer, 2900);
		conj.start();
		
		EventGenerator_Old generator = new EventGenerator_Old(leftChildBuffer, rightChildBuffer);
		generator.generate(10);
		
		System.out.println("Number of Result: " + conj.getNoResult());
		double throughput = conj.getNoResult()/generator.getTime() * 1000; 
		System.out.println("Throughput: " + throughput + " event/s");
	}

}

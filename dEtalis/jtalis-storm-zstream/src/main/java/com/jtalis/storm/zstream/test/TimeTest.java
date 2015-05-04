package com.jtalis.storm.zstream.test;

import com.jtalis.core.JtalisContext;
import com.jtalis.core.JtalisContextImpl;
import com.jtalis.core.event.EtalisEvent;
import com.jtalis.core.event.InvalidEventFormatException;
import com.jtalis.core.event.provider.DefaultOutputProvider;
import com.jtalis.core.plengine.JPLEngineWrapper;
import com.jtalis.core.plengine.PrologEngineWrapper;

public class TimeTest {

	/**
	 * @param args
	 * @throws InvalidEventFormatException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InvalidEventFormatException, InterruptedException {
		// TODO Auto-generated method stub
		PrologEngineWrapper<?> engine = new JPLEngineWrapper();
		JtalisContext ctx = new JtalisContextImpl(engine);
		ctx.setEtalisFlags("timestamp_mode", "on");
		
		
		ctx.addDynamicRule("c(X,Y) <- a(X) seq b(Y)");
		ctx.addEventTrigger("_/_");
		ctx.registerOutputProvider(new DefaultOutputProvider());
		
		EtalisEvent a = new EtalisEvent("a", 10);
		//EventTerm at = new EventTerm(a);
		//System.out.println(at.getPrologString());
		ctx.pushEvent(a);
		Thread.sleep(10);
		EtalisEvent b = new EtalisEvent("b", 10);
		ctx.pushEvent(b);
		Thread.sleep(10);
		
		ctx.shutdown();
		
	}

}

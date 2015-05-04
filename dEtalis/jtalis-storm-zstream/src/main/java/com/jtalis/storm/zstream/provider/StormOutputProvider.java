package com.jtalis.storm.zstream.provider;

import backtype.storm.task.OutputCollector;
import backtype.storm.tuple.Values;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.core.event.JtalisOutputEventProvider;
import com.jtalis.core.event.ProviderSetupException;

public class StormOutputProvider implements JtalisOutputEventProvider{

	private OutputCollector collector;
	
	public StormOutputProvider(OutputCollector collector) {
		this.collector = collector;
	}

	
	@Override
	public void setup() throws ProviderSetupException {
		// TODO Auto-generated method stub
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void outputEvent(EtalisEvent event) {
		// TODO Auto-generated method stub
		System.out.println(event.getPrologString() 
				+ " " + event.getTimeStarts().getTime() 
				+ " " + event.getTimeEnds().getTime());
		collector.emit(new Values(event));
	}
}

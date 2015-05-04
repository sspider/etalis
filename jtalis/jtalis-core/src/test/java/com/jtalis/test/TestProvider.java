package com.jtalis.test;

import java.util.ArrayList;
import java.util.Arrays;

import com.jtalis.core.event.AbstractJtalisEventProvider;
import com.jtalis.core.event.EtalisEvent;
import com.jtalis.core.event.EventBuilder;
import com.jtalis.core.event.InvalidEventFormatException;

public class TestProvider extends AbstractJtalisEventProvider {

	private String[] input;
	private String[] expectedOutput;

	private ArrayList<String> output = new ArrayList<String>();
	private int eventIdx;
	private boolean success = true;
	private String message;

	public TestProvider() {
		// Default
	}

	public TestProvider(String[] input, String[] expectedOutput) {
		this.input = input;
		this.expectedOutput = expectedOutput;
	}

	@Override
	public void outputEvent(EtalisEvent event) {
		output.add(event.getPrologString());
	}

	@Override
	public boolean hasMore() {
		return eventIdx < input.length;
	}

	@Override
	public EtalisEvent getEvent() {
		try {
			return (eventIdx < input.length) ? EventBuilder.buildEventFromString(input[eventIdx++]) : null;
		}
		catch (InvalidEventFormatException e) {
			return null;
		}
	}

	@Override
	public void shutdown() {
		String[] str = output.toArray(new String[] {});
		success = Arrays.equals(str, expectedOutput);

		message = (success) ? 
			"Received " + Arrays.toString(str) : 
			"Expected events are " + Arrays.toString(expectedOutput) + ". Received " + Arrays.toString(str) + " instead";
	}

	public boolean isSuccess() {
		return success;
	}

	public String getMessage() {
		return message;
	}
}

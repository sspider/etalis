package com.jtalis.core.event.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Logger;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.core.event.EventBuilder;
import com.jtalis.core.event.InvalidEventFormatException;

/**
 * ETALIS default event format serialization and deserialization. It always adds "\n" at the end of each event.
 * It also requires the new line when reading.
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class DefaultEventIO implements EventStreamInput, EventStreamOutput {

	private static final Logger logger = Logger.getLogger(DefaultEventIO.class.getName());

	private Map<InputStream, Scanner> scanners = new HashMap<InputStream, Scanner>();

	@Override
	public void serialize(EtalisEvent event, OutputStream out) throws IOException {
		out.write((event.getPrologString() + "\n").getBytes());
		out.flush();
	}

	@Override
	public Collection<EtalisEvent> deserializeAll(InputStream input) throws IOException, InvalidEventFormatException {
		Scanner sc = getScanner(input);
		Collection<EtalisEvent> ret = new LinkedList<EtalisEvent>();

		while (sc.hasNextLine()) {
			try {
				ret.add(getEvent(sc.nextLine()));
			}
			catch (InvalidEventFormatException e) {
				if (ret.size() > 0) {
					logger.warning(e + " occured. But current list is not empty so it will be returned.");
					return ret;
				}
				throw e;
			}
		}
		return ret;
	}

	@Override
	public EtalisEvent deserialize(InputStream input) throws IOException, InvalidEventFormatException {
		Scanner sc = getScanner(input);
		if(!sc.hasNextLine()) throw new InvalidEventFormatException("empty InputStream"); 
		try {
			return getEvent(sc.nextLine());
		}
		catch (NoSuchElementException e) {
			throw new IOException(e);
		}
	}

	private EtalisEvent getEvent(String line) throws InvalidEventFormatException {
		if (line != null && !line.isEmpty()) {
			if (line.endsWith(".")) {
				line = line.substring(0, line.length() - 1);
			}
			return EventBuilder.buildEventFromString(line);
		}
		return null;
	}

	private Scanner getScanner(InputStream is) {
		Scanner sc = scanners.get(is);
		if (sc == null) {
			scanners.put(is, sc = new Scanner(is));
		}
		return sc;
	}
}

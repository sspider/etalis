package com.jtalis.core.event;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jtalis.core.plengine.logic.TermBuilder;

/**
 * Builds events out of strings
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 * @author Stefan Obermeier
 * @author Roland Stühmer
 */
public class EventBuilder {

	private static final Logger logger = Logger.getLogger(EventBuilder.class.getName());

	public static final String sEventRegex = "(['\\w:/]+)\\((.+)\\)";
	public static final Pattern sEventPattern = Pattern.compile(sEventRegex);

	/**
	 * Parses strings in the format of ETALIS event e.g.,
	 * {@code beer('Heineken', 2.30)}. Also Prolog-escaped Strings are legal as
	 * event names e.g. when using a URI as name:
	 * {@code 'http://exmample.com/beer'('Heineken', 2.30)}.
	 * 
	 * @param eventString
	 *            string event
	 * @return {@link EtalisEvent} representing the given string
	 */
	public static EtalisEvent buildEventFromString(String eventString) throws InvalidEventFormatException {
		Matcher m = sEventPattern.matcher(eventString);

		if (!m.find()) {
			if (eventString.matches("['\\w:/]+")) {
				return new EtalisEvent(eventString);
			}
			throw new InvalidEventFormatException("'" + eventString + "' is not valid event string");
		}

		String eventName = m.group(1);
		String argStr = m.group(2);
		List<String> args = new LinkedList<String>();

		int parenthesisCount = 0;
		int lastSeparator = 0;

		for (int i = 0; i < argStr.length(); i++) {
			char ch = argStr.charAt(i);
			if (ch == '(') {
				parenthesisCount++;
			}
			else if (ch == ')') {
				parenthesisCount--;
			}
			else if (ch == ',') {
				if (parenthesisCount == 0) {
					args.add(argStr.substring(lastSeparator, i).trim());
					lastSeparator = i + 1;
				}
			}
		}
		if (lastSeparator < argStr.length()) {
			args.add(argStr.substring(lastSeparator, argStr.length()).trim());
		}
		Object[] objs = getObjectArgs(args.toArray(new String[0]));
		return new EtalisEvent(eventName, objs);
	}

	/**
	 * Transforms the passed strings into event arguments. If some of the strings is actually conform to a
	 * nested event it is transformed to one.
	 * @param args strings to transform to values.
	 */
	public static Object[] getObjectArgs(String[] args) {
		Object[] argObjects = new Object[args.length];

		for (int i = 0; i < args.length; i++) {
			String s = args[i].trim();

			if (s.matches(sEventRegex)) {
				try {
					argObjects[i] = buildEventFromString(s);
				}
				catch (InvalidEventFormatException e) {
					logger.warning(e.toString());
					argObjects[i] = null;
				}
			}
			else {
				argObjects[i] = TermBuilder.transformAtomToObject(s);
			}
		}
		return argObjects;
	}
}

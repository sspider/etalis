package com.jtalis.core.plengine.logic;

import java.io.File;

public class TermBuilder {

	/**
	 * Transforms the passed string into String, Integer or Double
	 * @param s the string to transform
	 */
	public static Object transformAtomToObject(String s) {
		if (s.startsWith("'") || s.startsWith("\"")) {
			return s.substring(1, s.length() - 1);
		}
		try {
			return new Integer(s);
		}
		catch (NumberFormatException e1) {
		}

		try {
			return new Double(s);
		}
		catch (NumberFormatException e) {
			return s;
		}
	}

	/**
	 * Transforms each element with transformAtomToObject. 
	 * @param strings to transform
	 * @return array with the transformed objects
	 */
	public static Object[] transformAtomsToObjects(String[] strings) {
		Term[] objs = new Term[strings.length];

		for (int i = 0; i < strings.length; i++) {
			objs[i] = transformObjectToTerm(strings[i]);
		}
		return objs;
	}

	/**
	 * Constructs term for a value: e.g. {@link IntegerAtom} if Integer is Passed. 
	 * If null is passed {@link QuotedAtom} with value 'null' is returned.
	 * @param obj value to transform
	 */
	public static Term transformObjectToTerm(Object obj) {
		if (obj instanceof String || obj instanceof Character) {
			String tmp = obj.toString();
			if (tmp.contains("[") && tmp.contains("]")) {
				// obj is a ListAtom
				return new ListAtom(tmp);
			} else
				return new QuotedAtom(obj.toString());
		}
		else if (obj instanceof Boolean) {
			return new Atom<Boolean>((Boolean) obj);
		}
		else if (obj instanceof Double || obj instanceof Float) {
			return new Atom<Double>(Double.parseDouble(obj.toString()));
		}
		else if (obj instanceof Integer || obj instanceof Byte || obj instanceof Short || obj instanceof Long) {
			return new Atom<Long>(Long.parseLong(obj.toString()));
		}
		else if (obj instanceof File) {
			return new QuotedAtom(((File) obj).getAbsolutePath().replace("\\", "/"));
		}
		else if (obj instanceof Term) {
			return (Term) obj;
		} 
		return new Atom<String>(obj + ""); // null goes like string
	}

	/**
	 * Transforms each element with transformObjectToTerm. 
	 * @param objects to transform
	 * @return array with the transformed objects
	 */
	public static Term[] transformObjectsToTerms(Object[] objects) {
		Term[] terms = new Term[objects.length];

		for (int i = 0; i < objects.length; i++) {
			terms[i] = transformObjectToTerm(objects[i]);
		}
		return terms;
	}

	/**
	 * Builds a compound term with the passed name and arguments  
	 * @param name the name of the term
	 * @param objects arguments
	 */
	public static CompoundTerm buildCompoundTerm(String name, Object ... objects) {
		return new CompoundTerm(name, transformObjectsToTerms(objects));
	}
}

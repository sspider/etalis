package com.jtalis.core.plengine.logic;

import java.io.Serializable;

/**
 * Representation of logical atom variable.
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 *
 * @param <T>
 */
public class Atom<T> implements Term, Serializable{

	private static final long serialVersionUID = -6106204822345580149L;

	private T value;

	public Atom() {
		super();
	}

	public Atom(T value) {
		this.value = value;
	}

	public T getValue() {
		return value;
	}

	/**
	 * Sets the value of this atom
	 * @param value
	 */
	public void setValue(T value) {
		this.value = value;
	}

	@Override
	public int getArity() {
		return 0;
	}

	@Override
	public Term getTerm(int index) {
		throw new IndexOutOfBoundsException();
	}

	@Override
	public Term[] getTerms() {
		return null;
	}

	@Override
	public String getPrologString() {
		return getValue() != null ? getValue().toString() : "";
	}

}

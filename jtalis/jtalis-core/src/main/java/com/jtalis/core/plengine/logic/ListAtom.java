package com.jtalis.core.plengine.logic;

import java.io.Serializable;

public class ListAtom implements Term, Serializable {

	private static final long serialVersionUID = 3344946900313764687L;
	
	private String value;
	
	public ListAtom() {
		super();
	}

	public ListAtom(String value) {
		this.value = value;
	}

	@Override
	public int getArity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Term getTerm(int index) {
		// TODO Auto-generated method stub
		throw new IndexOutOfBoundsException();
	}

	@Override
	public Term[] getTerms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getValue() {
		// TODO Auto-generated method stub
		return value;
	}

	@Override
	public String getPrologString() {
		// TODO Auto-generated method stub
		return getValue() != null ? getValue().toString() : "";
	}
	
	public void setValue(String value) {
		this.value = value;
	}

}

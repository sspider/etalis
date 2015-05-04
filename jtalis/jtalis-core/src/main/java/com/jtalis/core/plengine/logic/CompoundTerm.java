package com.jtalis.core.plengine.logic;

import java.io.Serializable;
import java.util.Arrays;
/**
 * Representation of a logical compound term.
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class CompoundTerm implements Term, Serializable {

	private static final long serialVersionUID = -4017347831210541733L;
	private Term[] terms;
	private String name;

	public CompoundTerm(String name, Term ... terms) {
		this.name = name;
		this.terms = Arrays.copyOf(terms, terms.length);
	}

	public CompoundTerm() {
		super();
	}

	/**
	 * Returns the name of this term
	 */
	public String getName() {
		return name;
	}

	@Override
	public int getArity() {
		return terms.length;
	}
	
	@Override
	public Term getTerm(int index) {
		if (index < 0 || index >= terms.length) {
			throw new IndexOutOfBoundsException();
		}
		return terms[index];
	}

	@Override
	public Term[] getTerms() {
		return terms;
	}

	@Override
	public Object getValue() {
		return this;
	}

	@Override
	public String getPrologString() {
		if (getArity() == 0) {
			return name;
		}
		StringBuilder builder = new StringBuilder(name).append("(");
		for (Term t : terms) {
			builder.append(t.getPrologString()).append(", ");
		}
		builder.replace(builder.length() - 2, builder.length(), ")");

		return builder.toString();
	}

	@Override
	public String toString() {
		return getPrologString();
	}

	public void setTerms(Term[] terms) {
		this.terms = terms;
	}

	public void setName(String name) {
		this.name = name;
	}

	
}

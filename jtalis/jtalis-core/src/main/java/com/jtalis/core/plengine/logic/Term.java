package com.jtalis.core.plengine.logic;

import java.io.Serializable;

/**
 * Representation of a logical term.
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public interface Term extends Serializable {

	/**
	 * Returns the arity of this term.
	 */
	public int getArity();

	/**
	 * Returns the term with position the passed index 
	 * @param index the position of the term to return
	 */
	public Term getTerm(int index);

	/**
	 * Returns all constructing terms of this term
	 */
	public Term[] getTerms();

	/**
	 * Returns the value of this term 
	 */
	public Object getValue();

	/**
	 * Returns prolog string representation of this term
	 */
	public String getPrologString();

}

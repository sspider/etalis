package com.jtalis.core.terms;

import java.io.Serializable;

import com.jtalis.core.plengine.logic.CompoundTerm;
import com.jtalis.core.plengine.logic.Term;

/**
 * AssertTerm
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class AssertTerm extends CompoundTerm implements Serializable {
	
	private static final long serialVersionUID = 7818823724732803026L;

	public AssertTerm(){
		super();
	}

	public AssertTerm(Term... terms) {
		super("assert", terms);
	}

}

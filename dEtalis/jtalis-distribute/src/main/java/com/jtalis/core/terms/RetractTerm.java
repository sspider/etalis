package com.jtalis.core.terms;

import java.io.Serializable;

import com.jtalis.core.plengine.logic.CompoundTerm;
import com.jtalis.core.plengine.logic.Term;

public class RetractTerm extends CompoundTerm implements Serializable {

	private static final long serialVersionUID = -7433054871052703565L;

	public RetractTerm(){
		super();
	}

	public RetractTerm(Term... terms) {
		super("retract", terms);
	}

}

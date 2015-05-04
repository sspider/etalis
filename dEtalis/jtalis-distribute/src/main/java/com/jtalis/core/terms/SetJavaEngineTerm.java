package com.jtalis.core.terms;

import java.io.Serializable;

import com.jtalis.core.plengine.logic.CompoundTerm;
import com.jtalis.core.plengine.logic.QuotedAtom;

public class SetJavaEngineTerm extends CompoundTerm implements Serializable {

	private static final long serialVersionUID = -7390822113179259092L;

	public SetJavaEngineTerm(){
		super();
	}

	public SetJavaEngineTerm(String engine) {
		super("set_java_engine", new QuotedAtom(engine));
	}

}

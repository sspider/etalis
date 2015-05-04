package com.jtalis.core.terms;

import java.io.Serializable;

import com.jtalis.core.plengine.logic.Atom;
import com.jtalis.core.plengine.logic.CompoundTerm;

/**
 * SetEtalisFlagTerm
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class SetEtalisFlagTerm extends CompoundTerm implements Serializable {
	
	private static final long serialVersionUID = -5780073811382347165L;

	public SetEtalisFlagTerm(){
		super();
	}

	public SetEtalisFlagTerm(String flag, String value) {
		super("set_etalis_flag", new Atom<String>(flag), new Atom<String>(value));
	}

}

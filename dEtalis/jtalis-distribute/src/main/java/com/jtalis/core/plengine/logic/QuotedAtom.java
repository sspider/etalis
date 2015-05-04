package com.jtalis.core.plengine.logic;

import java.io.Serializable;

/**
 * Represents atom term with string value.
 *  
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class QuotedAtom extends Atom<String> implements Serializable {

	private static final long serialVersionUID = 8242709077171019134L;

	public QuotedAtom(){
		super();
	}
	
	public QuotedAtom(String value) {
		super(value);
	}

	@Override
	public String getPrologString() {
		return "'" + getValue() + "'";
	}

}

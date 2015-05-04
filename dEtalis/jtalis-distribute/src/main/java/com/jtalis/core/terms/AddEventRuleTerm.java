package com.jtalis.core.terms;

import java.io.Serializable;

import com.jtalis.core.plengine.logic.Atom;
import com.jtalis.core.plengine.logic.CompoundTerm;

/**
 * AddEventRuleTerm
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class AddEventRuleTerm extends CompoundTerm implements Serializable {

	private static final long serialVersionUID = -7746811911657447125L;

	public AddEventRuleTerm(){
		super();
	}
	
	public AddEventRuleTerm(String id, String rule) {
		super("ins_event_rule", new Atom<String>(id + " 'rule:' " + rule));

	}

}

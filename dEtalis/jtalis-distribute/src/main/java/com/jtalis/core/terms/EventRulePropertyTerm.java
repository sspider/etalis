package com.jtalis.core.terms;

import java.io.Serializable;

import com.jtalis.core.plengine.logic.Atom;
import com.jtalis.core.plengine.logic.CompoundTerm;

/**
 * EventRulePropertyTerm
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class EventRulePropertyTerm extends CompoundTerm implements Serializable {

	private static final long serialVersionUID = 1558735052851984780L;

	public EventRulePropertyTerm(){
		super();
	}

	public EventRulePropertyTerm(String id, String name, Object value) {
		super("event_rule_property", new Atom<String>(id), new Atom<String>(name), new Atom<String>(value.toString()));
	}

}

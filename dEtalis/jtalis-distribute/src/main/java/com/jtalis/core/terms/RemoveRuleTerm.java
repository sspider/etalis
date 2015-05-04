package com.jtalis.core.terms;

import java.io.Serializable;

import com.jtalis.core.plengine.logic.Atom;
import com.jtalis.core.plengine.logic.CompoundTerm;

/**
 * RemoveRuleTerm
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class RemoveRuleTerm extends CompoundTerm implements Serializable {

	private static final long serialVersionUID = 3844427437748017679L;

	public RemoveRuleTerm(){
		super();
	}

	public RemoveRuleTerm(String id, String rule) {
		super("del_event_rule", new Atom<String>(id + " 'rule:' " + rule));
	}

}

package com.jtalis.core.terms;

import java.io.Serializable;

import com.jtalis.core.plengine.logic.Atom;
import com.jtalis.core.plengine.logic.CompoundTerm;

/**
 * PrintTriggerTerm
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class PrintTriggerTerm extends CompoundTerm implements Serializable {

	private static final long serialVersionUID = -2682172705308191269L;

	public PrintTriggerTerm(){
		super();
	}
	
	public PrintTriggerTerm(String eventTrigger) {
		super("print_trigger", new Atom<String>(eventTrigger));
	}

}

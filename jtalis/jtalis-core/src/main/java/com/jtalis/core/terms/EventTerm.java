package com.jtalis.core.terms;

import java.io.Serializable;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.core.plengine.logic.CompoundTerm;

/**
 * AssertTEventTermerm
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class EventTerm extends CompoundTerm implements Serializable {

	private static final long serialVersionUID = -4711210975087839013L;

	public EventTerm(){
		super();
	}

	public EventTerm(EtalisEvent event) {
		super("event", event);
	}

}

package com.jtalis.core.terms;

import java.io.Serializable;

import com.jtalis.core.plengine.logic.Atom;
import com.jtalis.core.plengine.logic.CompoundTerm;

public class SetJavaQueueID extends CompoundTerm implements Serializable {

	private static final long serialVersionUID = -2987700042440735916L;

	public SetJavaQueueID(){
		super();
	}

	public SetJavaQueueID(Object id) {
		super("set_java_queue_id", new Atom<Object>(id));
	}

}

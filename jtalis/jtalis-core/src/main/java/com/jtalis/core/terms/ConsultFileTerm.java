package com.jtalis.core.terms;

import java.io.File;
import java.io.Serializable;

import com.jtalis.core.plengine.logic.CompoundTerm;
import com.jtalis.core.plengine.logic.QuotedAtom;

/**
 * ConsultFileTerm
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class ConsultFileTerm extends CompoundTerm implements Serializable {

	private static final long serialVersionUID = 368130063696018604L;

	public ConsultFileTerm(){
		super();
	}
	
	public ConsultFileTerm(File file) {
		super("consult", new QuotedAtom(file.getAbsolutePath().replaceAll("\\\\", "/")));
	}

}

package com.jtalis.core.terms;

import java.io.File;
import java.io.Serializable;

import com.jtalis.core.plengine.logic.CompoundTerm;
import com.jtalis.core.plengine.logic.QuotedAtom;

/**
 * CompileEventFile
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class CompileEventFile extends CompoundTerm implements Serializable {
	
	private static final long serialVersionUID = 506329251363700049L;

	public CompileEventFile(){
		super();
	}

	public CompileEventFile(File file) {
		super("compile_event_file", new QuotedAtom(file.getAbsolutePath().replaceAll("\\\\", "/")));
	}

}

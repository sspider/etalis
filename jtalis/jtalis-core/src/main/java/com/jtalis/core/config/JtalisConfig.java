package com.jtalis.core.config;

import java.io.File;

/**
 * Configuration for Jtalis means to specify where are ETALIS sources. 
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public interface JtalisConfig {

	/**
	 * Returns a file pointing to etlias.P - the main ETALIS prolog source
	 */
	public File getEtalisSourceFile();
}

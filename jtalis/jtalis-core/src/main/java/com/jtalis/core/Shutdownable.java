package com.jtalis.core;

/**
 * Performs shutdown
 * 
 * <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public interface Shutdownable {

	/**
	 * Must free all used resources e.g. opened sockets or files
	 */
	public void shutdown();
}

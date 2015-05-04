package com.jtalis.core.event;

public class ProviderSetupException extends Exception {

	private static final long serialVersionUID = 6572889383829654912L;

	public ProviderSetupException() {
		super();
	}

	public ProviderSetupException(String msg) {
		super(msg);
	}

	public ProviderSetupException(Exception ex) {
		super(ex);
	}
}


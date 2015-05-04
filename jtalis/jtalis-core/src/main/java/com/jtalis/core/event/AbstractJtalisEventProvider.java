package com.jtalis.core.event;

/**
 * Abstract realization for both input and output event providers.
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public abstract class AbstractJtalisEventProvider implements JtalisInputEventProvider, JtalisOutputEventProvider {

	@Override
	public void setup() throws ProviderSetupException {
		// default
	}

	@Override
	public void shutdown() {
		// default
	}

	@Override
	public void outputEvent(EtalisEvent event) {
		// default
	}

	@Override
	public boolean hasMore() {
		// default
		return false;
	}

	@Override
	public EtalisEvent getEvent() {
		// default
		return null;
	}

	@Override
	public String toString() {
		return getClass() + " Provider";
	}

}

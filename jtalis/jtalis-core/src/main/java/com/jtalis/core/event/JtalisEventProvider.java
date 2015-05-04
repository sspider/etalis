package com.jtalis.core.event;

import com.jtalis.core.Shutdownable;
import com.jtalis.core.config.JtalisConfig;
import com.jtalis.core.config.JtalisExtendedConfig;
import com.veskogeorgiev.probin.annotations.Parameter;

/**
 * Basic interface for {@link JtalisConfig} event provider. 
 * 
 * <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public interface JtalisEventProvider extends Shutdownable {

	/**
	 * This method is called when the context is created with {@link JtalisExtendedConfig}. When called the fields
	 * annotated with {@link Parameter} are already binded and ready to use.  
	 * 
	 * @throws ProviderSetupException 
	 */
	public void setup() throws ProviderSetupException;

}

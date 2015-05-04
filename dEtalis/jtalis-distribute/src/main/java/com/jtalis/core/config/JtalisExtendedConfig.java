package com.jtalis.core.config;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.jtalis.core.JtalisContext;
import com.jtalis.core.config.beans.EtalisFlagType;
import com.jtalis.core.config.beans.EventRuleType;
import com.jtalis.core.event.JtalisInputEventProvider;
import com.jtalis.core.event.JtalisOutputEventProvider;

/**
 * Extended configuration for Jtalis provides methods for automatic
 * {@link JtalisContext} creation. e.g. specifying providers to be registered,
 * event triggers, etc.
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public interface JtalisExtendedConfig extends JtalisConfig {

	/**
	 * Returns event pattern files to compile
	 */
	public List<File> getEventFiles();

	/**
	 * Returns event rules to registered in the context
	 */
	public List<EventRuleType> getEventRules();

	/**
	 * Returns event triggers
	 */
	public List<String> getEventTriggers();

	/**
	 * Returns providers to register in the context
	 */
	public List<InputProviderDescriptor> getInputProviders();

	/**
	 * Returns providers to register in the context
	 */
	public List<OutputQueueDescriptor> getOutputQueues();

	/**
	 * Returns whether the engine is in debug mode, that is to print each
	 * executed predicate
	 */
	public boolean isDebug();

	/**
	 * Returns initial flags to set
	 */
	public List<EtalisFlagType> getFlags();

	/**
	 * Returns any predicates to be executed
	 */
	public List<String> getPredicates();

	/**
	 * Returns files to consult
	 */
	public List<File> getConsultFiles();

	public static class OutputQueueDescriptor {
		private String regex;
		private List<JtalisOutputEventProvider> providers;

		public List<JtalisOutputEventProvider> getProviders() {
			return providers;
		}

		public String getRegex() {
			return regex;
		}

		public OutputQueueDescriptor(String regex, JtalisOutputEventProvider... providers) {
			this.regex = regex;
			this.providers = new LinkedList<JtalisOutputEventProvider>();
			this.providers.addAll(Arrays.asList(providers));
		}

		public void addProvider(JtalisOutputEventProvider p) {
			providers.add(p);
		}
	}

	public static class InputProviderDescriptor {
		private long feedDelay;
		private JtalisInputEventProvider provider;

		public long getFeedDelay() {
			return feedDelay;
		}

		public JtalisInputEventProvider getProvider() {
			return provider;
		}

		public InputProviderDescriptor(long feedDelay, JtalisInputEventProvider provider) {
			this.feedDelay = feedDelay;
			this.provider = provider;
		}
	}
}

package com.jtalis.core.config;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;

import com.jtalis.core.JtalisContext;
import com.jtalis.core.config.beans.ConfigType;
import com.jtalis.core.config.beans.EtalisFlagType;
import com.jtalis.core.config.beans.EventRuleType;
import com.jtalis.core.config.beans.OutputQueueType;
import com.jtalis.core.config.beans.ParameterType;
import com.jtalis.core.config.beans.ProviderType;
import com.jtalis.core.event.JtalisEventProvider;
import com.jtalis.core.event.JtalisInputEventProvider;
import com.jtalis.core.event.JtalisOutputEventProvider;
import com.jtalis.core.event.ProviderSetupException;
import com.veskogeorgiev.probin.BindingException;
import com.veskogeorgiev.probin.ProbinFactory;
import com.veskogeorgiev.probin.conversion.FileConverter;

/**
 * This class provides XML configuration of {@link JtalisContext}. The XML
 * Schema is in scripts/XmlConfig.xsd. Uses all classes from
 * com.jtalis.core.config.beans to bind the information from the schema. If
 * <etalisSrcFile> is not provided it uses {@link BasicConfig} to retrieve the
 * prolog files from the jar/class-path resources.
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class XmlConfig implements JtalisExtendedConfig {

	private static final Logger logger = Logger.getLogger(XmlConfig.class.getName());

	private LinkedList<File> eventFiles;
	private ConfigType config;
	private List<InputProviderDescriptor> inputProviders;
	private List<OutputQueueDescriptor> outputQueues;
	private List<EventRuleType> eventRules;
	private List<String> eventTriggers;
	private List<EtalisFlagType> flags;
	private List<String> predicates;
	private List<File> consultFiles;
	private File etalisSourceFile;
	private boolean debug;
	private ProbinFactory probin = new ProbinFactory();

	@SuppressWarnings("unchecked")
	public XmlConfig(final File xmlFile) throws Exception {
		probin.getConverter().putConverter(File.class, new FileConverter() {
			@Override
			public File convert(String str) {
				File f = new File(str);
				if (!f.isAbsolute()) {
					f = new File(xmlFile.getParentFile(), f.getPath());
				}
				return f;
			}
		});
		JAXBContext jc = JAXBContext.newInstance("com.jtalis.core.config.beans");
		Unmarshaller u = jc.createUnmarshaller();
		config = ((JAXBElement<ConfigType>) u.unmarshal(xmlFile)).getValue();
		debug = (config.isDebug() != null) ? config.isDebug() : false;

		eventFiles = new LinkedList<File>();
		if (config.getCompileEventFiles() != null && config.getCompileEventFiles().getFile() != null) {
			for (String s : config.getCompileEventFiles().getFile()) {
				File f = probin.getConverter().convert(s, File.class);
				eventFiles.add(f);
			}
		}
		eventRules = new LinkedList<EventRuleType>();
		if (config.getEventRules() != null && config.getEventRules().getEventRule() != null) {
			eventRules.addAll(config.getEventRules().getEventRule());
		}
		eventTriggers = new LinkedList<String>();
		if (config.getEventTriggers() != null && config.getEventTriggers().getTrigger() != null) {
			eventTriggers.addAll(config.getEventTriggers().getTrigger());
		}
		flags = new LinkedList<EtalisFlagType>();
		if (config.getFlags() != null && config.getFlags().getFlag() != null) {
			flags.addAll(config.getFlags().getFlag());
		}
		predicates = new LinkedList<String>();
		if (config.getPredicates() != null && config.getPredicates().getPredicate() != null) {
			predicates.addAll(config.getPredicates().getPredicate());
		}
		consultFiles = new LinkedList<File>();
		if (config.getConsult() != null && config.getConsult().getFile() != null) {
			for (String f : config.getConsult().getFile()) {
				consultFiles.add(probin.getConverter().convert(f, File.class));
			}
		}
		inputProviders = new LinkedList<InputProviderDescriptor>();
		outputQueues = new LinkedList<OutputQueueDescriptor>();

		if (config.getProviders() != null && config.getProviders().getProvider() != null) {
			for (ProviderType pt : config.getProviders().getProvider()) {
				try {
					processProvider(pt);
				}
				catch (Exception e) {
					logger.warning(e.toString());
				}
			}
		}
		if (config.getOutputQueues() != null && config.getOutputQueues().getQueue() != null) {
			for (OutputQueueType qt : config.getOutputQueues().getQueue()) {
				OutputQueueDescriptor oqd = new OutputQueueDescriptor(qt.getRegex());
				if (qt.getProviders() != null && qt.getProviders().getProvider() != null) {
					for (ProviderType pt : qt.getProviders().getProvider()) {
						try {
							JtalisEventProvider p = createProvider(pt);
							if ("input".equals(pt.getType())) {
								addInputProvider(p, pt.getFeedDelay());
							}
							else if ("output".equals(pt.getType())) {
								oqd.addProvider((JtalisOutputEventProvider) p);
							}
							else if ("both".equals(pt.getType())) {
								addInputProvider(p, pt.getFeedDelay());
								oqd.addProvider((JtalisOutputEventProvider) p);
							}
							else {
								if (p instanceof JtalisInputEventProvider) {
									addInputProvider(p, pt.getFeedDelay());
								}
								if (p instanceof JtalisOutputEventProvider) {
									oqd.addProvider((JtalisOutputEventProvider) p);
								}
							}
						}
						catch (Exception e) {
							logger.warning(e.toString());
						}
					}
				}
				outputQueues.add(oqd);
			}
		}
		BasicConfig config = new BasicConfig();
		etalisSourceFile = config.getEtalisSourceFile();
	}

	public void processProvider(ProviderType pt) throws ClassNotFoundException, InstantiationException, IllegalAccessException, ProviderSetupException {
		JtalisEventProvider p = createProvider(pt);
		if ("input".equals(pt.getType())) {
			addInputProvider(p, pt.getFeedDelay());
		}
		else if ("output".equals(pt.getType())) {
			addOutputProvider(pt.getRegex(), p);
		}
		else if ("both".equals(pt.getType())) {
			addInputProvider(p, pt.getFeedDelay());
			addOutputProvider(pt.getRegex(), p);
		}
		else {
			if (p instanceof JtalisInputEventProvider) {
				addInputProvider(p, pt.getFeedDelay());
			}
			if (p instanceof JtalisOutputEventProvider) {
				addOutputProvider(pt.getRegex(), p);
			}
		}
	}

	private JtalisEventProvider createProvider(ProviderType pt) throws ClassNotFoundException, InstantiationException, IllegalAccessException, ProviderSetupException {
		Class<?> cls = Class.forName(pt.getClazz());
		JtalisEventProvider p = (JtalisEventProvider) cls.newInstance();
		if (pt.getParameters() != null) {
			try {
				probin.bind(p, getMap(pt.getParameters().getParameter()));
			}
			catch (BindingException e) {
				logger.warning(e.toString());
			}
		}
		p.setup();
		return p;
	}

	private void addOutputProvider(String regex, JtalisEventProvider p) {
		if (p instanceof JtalisOutputEventProvider) {
			outputQueues.add(new OutputQueueDescriptor(regex, (JtalisOutputEventProvider) p));
		}
		else {
			logger.warning(p.getClass() + " is registered as an output provider, but does not implement " + JtalisOutputEventProvider.class);
		}
	}

	private void addInputProvider(JtalisEventProvider p, Long feedDelay) {
		if (p instanceof JtalisInputEventProvider) {
			long delay = 0;
			if (feedDelay != null) {
				delay = feedDelay;
			}
			inputProviders.add(new InputProviderDescriptor(delay, (JtalisInputEventProvider) p));
		}
		else {
			logger.warning(p.getClass() + " is registered as an input provider, but does not implement " + JtalisInputEventProvider.class);
		}
	}

	public XmlConfig(String xmlFile) throws Exception {
		this(new File(xmlFile));
	}

	@Override
	public List<File> getEventFiles() {
		return eventFiles;
	}

	@Override
	public File getEtalisSourceFile() {
		return etalisSourceFile;
	}

	public List<InputProviderDescriptor> getInputProviders() {
		return inputProviders;
	}

	@Override
	public List<OutputQueueDescriptor> getOutputQueues() {
		return outputQueues;
	}

	private Map<String, String> getMap(List<ParameterType> parameters) {
		Map<String, String> ret = new HashMap<String, String>();
		if (parameters != null) {
			for (ParameterType p : parameters) {
				ret.put(p.getName(), p.getValue());
			}
		}
		return ret;
	}

	@Override
	public List<EventRuleType> getEventRules() {
		return eventRules;
	}

	@Override
	public List<String> getEventTriggers() {
		return eventTriggers;
	}

	@Override
	public boolean isDebug() {
		return debug;
	}

	public List<EtalisFlagType> getFlags() {
		return flags;
	}

	public List<String> getPredicates() {
		return predicates;
	}

	public List<File> getConsultFiles() {
		return consultFiles;
	}

}

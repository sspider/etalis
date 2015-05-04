package com.jtalis.core;

import java.io.File;

import com.jtalis.core.config.JtalisExtendedConfig;
import com.jtalis.core.config.JtalisExtendedConfig.InputProviderDescriptor;
import com.jtalis.core.config.JtalisExtendedConfig.OutputQueueDescriptor;
import com.jtalis.core.config.XmlConfig;
import com.jtalis.core.config.beans.EtalisFlagType;
import com.jtalis.core.config.beans.EventRuleType;
import com.jtalis.core.event.JtalisOutputEventProvider;
import com.jtalis.core.plengine.JPLEngineWrapper;
import com.jtalis.core.plengine.PrologEngineWrapper;

/**
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class JtalisMain {

	public static void run(JtalisExtendedConfig config) throws Exception {
		PrologEngineWrapper<?> engine = new JPLEngineWrapper(config.isDebug());
		final JtalisContextImpl context = new JtalisContextImpl(config, engine);

		for (File f : config.getConsultFiles()) {
			context.consultFile(f);
		}
		for (String et : config.getEventTriggers()) {
			context.addEventTrigger(et);
		}
		for (EtalisFlagType flag : config.getFlags()) {
			context.setEtalisFlags(flag.getName(), flag.getValue());
		}
		for (File ef : config.getEventFiles()) {
			context.compileEvenFile(ef);
		}
		for (String pred : config.getPredicates()) {
			context.getEngineWrapper().executeGoal(pred);
		}
		for (EventRuleType ert : config.getEventRules()) {
			StringBuilder rule = new StringBuilder();
			rule.append(ert.getConsequence().trim()).append(" <- ").append(ert.getAntecedent().trim());

			if (ert.getRuleId() != null) {
				context.addDynamicRule(ert.getRuleId(), rule.toString());
			}
			else {
				context.addDynamicRule(rule.toString());
			}
		}
		// first output and then input providers
		for (OutputQueueDescriptor d : config.getOutputQueues()) {
			context.registerOutputProvider(d.getRegex(), d.getProviders().toArray(new JtalisOutputEventProvider[0]));
		}
		for (InputProviderDescriptor p : config.getInputProviders()) {
			context.registerInputProvider(p.getProvider(), p.getFeedDelay());
		}
		context.waitForInputProviders();
		context.shutdown();
	}

	public static void run(String file) throws Exception {
		run(new XmlConfig(new File(file)));
	}

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.err.println("No arguments provided");
			return;
		}
		run(args[0]);
	}

}

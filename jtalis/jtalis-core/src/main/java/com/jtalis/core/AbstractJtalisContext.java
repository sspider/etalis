package com.jtalis.core;

import java.io.File;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import com.jtalis.core.terms.AddEventRuleTerm;
import com.jtalis.core.terms.AssertTerm;
import com.jtalis.core.terms.CompileEventFile;
import com.jtalis.core.terms.ConsultFileTerm;
import com.jtalis.core.terms.EventRulePropertyTerm;
import com.jtalis.core.terms.PrintTriggerTerm;
import com.jtalis.core.terms.RemoveRuleTerm;
import com.jtalis.core.terms.RetractTerm;
import com.jtalis.core.terms.SetEtalisFlagTerm;

/**
 * Basic implementation from {@link EtalisInterface} methods only 
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public abstract class AbstractJtalisContext implements JtalisContext {
	private static final Logger logger = Logger.getLogger(AbstractJtalisContext.class.getName());

	private Map<String, String> dynamicRules = new ConcurrentHashMap<String, String>();;

	private Random random = new Random();

	@Override
	public boolean consultFile(File pFile) {
		return getEngineWrapper().execute(new ConsultFileTerm(pFile));
	}

	@Override
	public boolean compileEvenFile(File file) {
		if (file.exists()) {
			return getEngineWrapper().execute(new CompileEventFile(file));
		}
		logger.warning("The file " + file + " does not exists.");
		return false;
	}

	@Override
	public boolean setEtalisFlags(String flag, String value) {
		return getEngineWrapper().execute(new SetEtalisFlagTerm(flag, value));
	}

	@Override
	public void addEventTrigger(String... eventNames) {
		for (String event : eventNames) {
			getEngineWrapper().execute(new AssertTerm(new PrintTriggerTerm(event)));
		}
	}

	@Override
	public void addDynamicRuleWithId(String id, String rule) {
		dynamicRules.put(id, rule);
		getEngineWrapper().execute(new AddEventRuleTerm(id, rule));
	}

	@Override
	public String addDynamicRule(String rule) {
		String id = null;
		while (dynamicRules.containsKey(id = "rule" + Math.abs(random.nextInt())));
		addDynamicRuleWithId(id, rule);

		return id;
	}

	@Override
	public String addDynamicRule(String sequence, String consequence) {
		return addDynamicRule(consequence + " <- " + sequence);
	}

	@Override
	public void removeDynamicRule(String id) {
		getEngineWrapper().execute(new RemoveRuleTerm(id, dynamicRules.remove(id)));
	}

	@Override
	public void setEventRuleProperty(String id, String name, Object value) {
		getEngineWrapper().execute(new AssertTerm(new EventRulePropertyTerm(id, name, value)));
	}

	@Override
	public void removeEventRuleProperty(String id, String name, Object value) {
		getEngineWrapper().execute(new RetractTerm(new EventRulePropertyTerm(id, name, value)));		
	}

	@Override
	public boolean loadOntology(File file) {
		throw new RuntimeException("Method not implemented yet");
		// return getEngineWrapper().execute("rdf_load", file.getAbsolutePath());
	}

	@Override
	public boolean loadLibrary(File file) {
		throw new RuntimeException("Method not implemented yet");
		// return getEngineWrapper().executeGoal(String.format("library('%s')", file.getAbsolutePath()));
	}
}

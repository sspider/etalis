package com.jtalis.core.plengine;

import java.util.logging.Logger;

import com.declarativa.interprolog.PrologOutputListener;
import com.declarativa.interprolog.SWISubprocessEngine;
import com.jtalis.core.event.EtalisEventListener;
import com.jtalis.core.plengine.logic.Term;

/**
 * Interprolog implementation of the engine
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class InterprologEngineWrapper implements PrologEngineWrapper<SWISubprocessEngine> {
	private static final Logger logger = Logger.getLogger(InterprologEngineWrapper.class.getName());

	private SWISubprocessEngine engine;
	private boolean debug;

	public InterprologEngineWrapper(String path) {
		this(path, false);
	}

	public InterprologEngineWrapper(String path, boolean debug) {
		engine = new SWISubprocessEngine(path);
		this.debug = debug;
	}

	@Override
	public boolean execute(Term term) {
		String cmd = term.toString();
		if (debug) {
			logger.info(cmd);
		}
		return executeGoal(cmd);
	}

	@Override
	public boolean executeGoal(String cmd) {
		if (debug) {
			logger.info(cmd);
		}
		return engine.deterministicGoal(cmd);
	}

	public Object registerPushNotification(EtalisEventListener listener) {
		return engine.registerJavaObject(listener);
	}

	@Override
	public void unregisterPushNotification(EtalisEventListener listener) {
		engine.unregisterJavaObject(listener);
	}

	@Override
	public void shutdown() {
		engine.shutdown();
	}

	@Override
	public void addOutputListener(final EngineOutputListener listener) {
		engine.addPrologOutputListener(new PrologOutputListener() {
			@Override
			public void print(String s) {
				listener.print(s);
			}
		});
	}

	@Override
	public String getName() {
		return "Interprolog";
	}
}

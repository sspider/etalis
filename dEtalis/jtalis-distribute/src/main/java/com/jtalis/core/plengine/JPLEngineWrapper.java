package com.jtalis.core.plengine;

import java.util.logging.Logger;

import jpl.JPL;
import jpl.Query;

import com.jtalis.core.event.EtalisEventListener;
import com.jtalis.core.plengine.logic.Term;

/**
 * JPL engine implementation
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class JPLEngineWrapper implements PrologEngineWrapper<Object> {
	private static final Logger logger = Logger.getLogger(JPLEngineWrapper.class.getName());

	private boolean debug;

	public JPLEngineWrapper() {
		// does nothing
	}

	public JPLEngineWrapper(boolean debug) {
		this.debug = debug;
	}

	@Override
	public void addOutputListener(EngineOutputListener listener) {
		// nothing
	}

	@Override
	public boolean execute(Term term) {
		return executeGoal(term.toString());
	}

	@Override
	public boolean executeGoal(String command) {
		Query q = new Query(command);
		if (debug) logger.info(q.toString());
		//System.out.println(q.toString());
		return q.hasSolution();
	}

	@Override
	public Object registerPushNotification(EtalisEventListener listener) {
		return JPL.newJRef(listener);
	}

	@Override
	public void shutdown() {
		// does nothing
	}

	@Override
	public void unregisterPushNotification(EtalisEventListener listener) {
		// does nothing
	}

	@Override
	public String getName() {
		return "JPL";
	}

}

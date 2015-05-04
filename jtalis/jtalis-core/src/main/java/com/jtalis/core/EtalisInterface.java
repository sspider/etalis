package com.jtalis.core;

import java.io.File;

/**
 * ETALIS basic interface. Defines everything related to what ETALIS itself could do.
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public interface EtalisInterface {

	public static final String sJavaNotification = "java_notification";
	
	/**
	 * Consults the passed file
	 * 
	 * @param pFile file to consult
	 * @return status of consultation
	 */
	public boolean consultFile(File pFile);

	/**
	 * Compiles the passed event
	 * @param file the event file to compile
	 * @return status of compilation
	 */
	public boolean compileEvenFile(File file);

	/**
	 * Sets ETALIS flag
	 * 
	 * @param flag
	 * @param value
	 * @return status of operation
	 */
	public boolean setEtalisFlags(String flag, String value);

	/**
	 * Sets which events to fire back to the output providers
	 * @param eventNames the event names
	 */
	public void addEventTrigger(String... eventNames);

	/**
	 * Adds a dynamic rule like "c <- a seq b"
	 * @param rule the rule to be loaded
	 * @param id ruleId
	 */
	public void addDynamicRuleWithId(String id, String rule);

	/**
	 * Adds a dynamic rule like "c <- a seq b". Same as {@link EtalisInterface#addDynamicRuleWithId(String id, String rule)}, 
	 * but the id is automatically generated and returned
	 * 
	 * @param rule the rule to be loaded
	 * @return the automatically generated id for this rule
	 */
	public String addDynamicRule(String rule);

	/**
	 * Same as {@link EtalisInterface#addDynamicRule(rule)}, but the left and the right parts are given separately.
	 * e.g. c <- a seq b, "c" is consequence "a seq b" is sequence
	 * 
	 * @param consequence left part of the rule
	 * @param sequence right part of the rule
	 * @return the automatically generated id for this rule
	 */
	public String addDynamicRule(String consequence, String sequence);

	/**
	 * Remove a dynamic rule by id
	 * @param id the rule's id
	 */
	public void removeDynamicRule(String id);

	/**
	 * Sets an event rule property.
	 * 
	 * @param id ruleId that relates to
	 * @param name property name
	 * @param value property value
	 */
	public void setEventRuleProperty(String id, String name, Object value);

	/**
	 * Removes a property from an event rule
	 * 
	 * @param id ruleId that relates to
	 * @param name property name
	 * @param value property value
	 */
	public void removeEventRuleProperty(String id, String name, Object value);

	/**
	 * Loads the passed ontology file 
	 * @param file
	 * @return status of operation
	 */
	public boolean loadOntology(File file);

	/**
	 * Loads the passed library 
	 * @param file
	 * @return status f operation
	 */
	public boolean loadLibrary(File file);

}

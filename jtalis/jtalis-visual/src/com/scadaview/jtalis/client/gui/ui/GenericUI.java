package com.scadaview.jtalis.client.gui.ui;

import org.vectomatic.dom.svg.OMNode;

import com.google.gwt.json.client.JSONObject;

/**
 * An abstract class that represents a GUI SVG Element.
 * @author Ahmed Khalil Hafsi, ahmed@hafsi.org 
 * 
 */
public abstract class GenericUI {

	OMNode svgNode;
	boolean enabled=true;
	
	/**
	 * @param svgNode
	 */
	public GenericUI(OMNode svgNode) {
		super();
		this.svgNode = svgNode;
	}

	/**
	 * @return
	 */
	public OMNode getPath() {
		return svgNode;
	};

	/**
	 * @param path
	 */
	public void setPath(OMNode path) {
		this.svgNode = path;
	};
	
	public void enable()
	{
		enabled=true;
	}
	public void disable()
	{
		enabled=false;
	}

/*	*//**
	 * @param Event
	 *//*
	public abstract void update(String Event);*/
	
	/**
	 * @param Event
	 */
	public abstract void update(JSONObject Event);
	
	
	
}

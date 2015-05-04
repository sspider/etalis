package com.scadaview.jtalis.client.gui.ui;

import org.vectomatic.dom.svg.OMNode;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * @author Ahmed Khalil Hafsi, ahmed@hafsi.org
 *
 */
public class Dashboard extends GenericUI {

	/**
	 * @param svgNode
	 */
	public Dashboard(OMNode svgNode) {
		super(svgNode);
		
	}

/*	 (non-Javadoc)
	 * @see com.scadaview.jtalis.client.gui.ui.GenericUI#update(java.lang.String)
	 
	@Override
	public void update(String Event) {
		this.svgNode.setNodeValue(Event);

	}*/
	
	/* (non-Javadoc)
	 * @see com.scadaview.jtalis.client.gui.ui.GenericUI#update(com.google.gwt.json.client.JSONObject)
	 */
	@Override
	public void update(JSONObject Event) {
		// TODO Auto-generated method stub
		JSONString json = (JSONString) Event.get("Property1");
		String info = json.stringValue();
		this.svgNode.setNodeValue(info);
	}

}

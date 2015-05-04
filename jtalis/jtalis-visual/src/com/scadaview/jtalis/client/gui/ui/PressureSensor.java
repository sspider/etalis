package com.scadaview.jtalis.client.gui.ui;

import org.vectomatic.dom.svg.OMNode;

import com.google.gwt.json.client.JSONObject;

/**
 * @author Ahmed Khalil Hafsi, ahmed@hafsi.org
 *
 */
public class PressureSensor extends GenericUI{

	public PressureSensor(OMNode svgNode) {
		super(svgNode);
		
	}

	/* (non-Javadoc)
	 * @see com.scadaview.jtalis.client.gui.ui.GenericUI#update(com.google.gwt.json.client.JSONObject)
	 */
	@Override
	public void update(JSONObject Event) {
		// TODO Auto-generated method stub
		// update the flow label
		String pressure = Event.get("Property1").toString();
		this.svgNode.setNodeValue(pressure + " l/sec");
	}

}

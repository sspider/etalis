package com.scadaview.jtalis.client.gui.ui;

import org.vectomatic.dom.svg.OMElement;
import org.vectomatic.dom.svg.OMSVGSVGElement;

import com.google.gwt.json.client.JSONObject;

/**
 * @author Ahmed Khalil Hafsi, ahmed@hafsi.org
 *
 */
public class Valve extends GenericUI {
	OMElement valve;

	public Valve(OMSVGSVGElement root, String valveID) {
		super(null);
		this.valve = root.getElementById(valveID);
	}

	/* (non-Javadoc)
	 * @see com.scadaview.jtalis.client.gui.ui.GenericUI#update(com.google.gwt.json.client.JSONObject)
	 */
	@Override
	public void update(JSONObject Event) {
		// TODO Auto-generated method stub
		//String style = this.valve.getAttribute("style");
		String styleOn = "fill:#d8ff00;fill-opacity:1;fill-rule:evenodd;stroke:#000000;stroke-width:1.75588822px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1;display:inline";
		String styleOff = "fill:#ff0000;fill-opacity:1;fill-rule:evenodd;stroke:#000000;stroke-width:1.75588822px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1;display:inline";
		String status = Event.get("Property1").toString();
		//System.out.println(style);
		//System.out.println(status);
		if (status.equals("1")) {
			// valve is on
			this.valve.setAttribute("style", styleOn);
		} else {
			// valve is off
			this.valve.setAttribute("style", styleOff);
		}
		
	}

}

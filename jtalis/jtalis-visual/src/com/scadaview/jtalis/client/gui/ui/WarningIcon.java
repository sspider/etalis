package com.scadaview.jtalis.client.gui.ui;

import org.vectomatic.dom.svg.OMElement;
import org.vectomatic.dom.svg.OMNode;
import org.vectomatic.dom.svg.OMSVGSVGElement;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class WarningIcon extends GenericUI{
	OMElement icon;
	OMElement label;
	OMNode warningLabel;

	public WarningIcon(OMSVGSVGElement root, String warningIconID, String warningLabelID) {
		super(null);
		this.icon = root.getElementById(warningIconID);
		this.label = root.getElementById(warningLabelID);
		this.warningLabel = label.getChildNodes()
				.getItem(0).getFirstChild();
		this.icon.setAttribute("visibility", "hidden");
		this.label.setAttribute("visibility", "hidden");
		
		this.icon.addDomHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				icon.setAttribute("visibility", "hidden");
				label.setAttribute("visibility", "hidden");
				
			}
		}, ClickEvent.getType());
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(JSONObject Event) {
		// TODO Auto-generated method stub
		this.icon.setAttribute("visibility", "visible");
		this.label.setAttribute("visibility", "visible");
		JSONString json = (JSONString) Event.get("Property1");
		String info = json.stringValue();
		this.warningLabel.setNodeValue(info);
	}

}

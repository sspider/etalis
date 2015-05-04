package com.scadaview.jtalis.client.gui.ui;

import org.vectomatic.dom.svg.OMElement;
import org.vectomatic.dom.svg.OMNode;
import org.vectomatic.dom.svg.OMSVGSVGElement;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;

/**
 * @author Ahmed Khalil Hafsi, ahmed@hafsi.org 
 * 
 */
public class WaterTank extends GenericUI {

	OMElement waterLevel;
	OMElement waterCap;
	OMNode waterLevelLabel;
	
	private float capPosition;
	private float waterHeight;
	

	/**
	 * @param root
	 */	
	public WaterTank(OMSVGSVGElement root, String waterLevelID, String waterCapID, String waterLevelLabel) {
		super(null);
		this.waterLevel = root.getElementById(waterLevelID);
		this.waterCap = root.getElementById(waterCapID);
		this.waterLevelLabel = root.getElementById(waterLevelLabel).getChildNodes()
				.getItem(0).getFirstChild();
		
		waterHeight = Float.valueOf(this.waterLevel.getAttribute("height"));
		//System.out.println(waterHeight);
		//setWaterLevelLabel(this.waterLevel.getAttribute("height"));
		capPosition = getCapPosition();
		//System.out.println(capPosition);
		
		if(waterCap == null)
			GWT.log("could not find cap !");
	}
	
	/* (non-Javadoc)
	 * @see com.scadaview.jtalis.client.gui.ui.GenericUI#update(com.google.gwt.json.client.JSONObject)
	 */
	@Override
	public void update(JSONObject Event) {
		// TODO Auto-generated method stub
		// regulate the height of water level
		String height = Event.get("Property1").toString();
		//System.out.println(height);
		waterLevel.setAttribute("height", height);
		
		// get the latest water level
		float dynamicHeight = Float.valueOf(height);
		// calculate the movement of the cap
		float relativeMovement = waterHeight - dynamicHeight;
		waterHeight = dynamicHeight;
		
		// regulate the position of cap
		setCapPosition(relativeMovement);
		
		// dynamic change the text of label
		setWaterLevelLabel(height);
		//waterLevelLabel.setNodeValue(height + " Liter");
	}
	
	private float getCapPosition() {
		String transform = waterCap.getAttribute("transform");
		int beginIndex = transform.lastIndexOf(",");
		String position = transform.substring(beginIndex + 1, transform.length() - 1);
		return Float.valueOf(position);
	}
	
	private void setCapPosition(float relativeMovement) {
		capPosition += relativeMovement;
		String transform = waterCap.getAttribute("transform");
		int endIndex = transform.lastIndexOf(",");
		String newTransform = transform.substring(0, endIndex);
		newTransform += "," + capPosition + ")";
		//System.out.println(newTransform);
		waterCap.setAttribute("transform", newTransform);
	}
	
	private void setWaterLevelLabel(String height) {
		float realHeight = Float.valueOf(height) / 10;
		waterLevelLabel.setNodeValue(realHeight + " Liter");
	}

}

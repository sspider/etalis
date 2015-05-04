package com.scadaview.jtalis.client.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.vectomatic.dev.svg.impl.gen.ExternalSVGResourceGenerator;
import org.vectomatic.dom.svg.OMElement;
import org.vectomatic.dom.svg.OMNode;
import org.vectomatic.dom.svg.OMNodeList;
import org.vectomatic.dom.svg.OMSVGDocument;
import org.vectomatic.dom.svg.OMSVGImageElement;
import org.vectomatic.dom.svg.OMSVGSVGElement;
import org.vectomatic.dom.svg.utils.AsyncXmlLoader;
import org.vectomatic.dom.svg.utils.AsyncXmlLoaderCallback;
import org.vectomatic.dom.svg.utils.HttpRequestXmlLoader;
import org.vectomatic.dom.svg.utils.OMSVGParser;
import org.vectomatic.dom.svg.ui.ExternalSVGResource;
import org.vectomatic.dom.svg.ui.SVGImage;
import org.vectomatic.dom.svg.ui.SVGResource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.scadaview.jtalis.client.DataServiceAsync;

/**
 * @author Ahmed Khalil Hafsi, ahmed@hafsi.org 
 * The main class for GUI creation and Manipulation.
 */
public class MainInterface {

	OMSVGSVGElement root;
	SVGImage Background;
	boolean finished = false;
	EtalisChemicalPlant myPlant;
	EventDispatcher dispatcher;
	DataServiceAsync myService;

	/**
	 * @return
	 */
	public EventDispatcher getDispatcher() {
		return dispatcher;
	}

	/**
	 * @param dispatcher
	 */
	public void setDispatcher(EventDispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	/**
	 * 
	 */
	public MainInterface(DataServiceAsync myDataService) {
		this.myService = myDataService;
	};

	public void SetupGUI() {

	}

	public boolean isReady() {
		return finished;
	}

	/**
	 * Create the GUI Interface and load via an XML Async Call the internal GUI Files
	 * This the main function to get the GUI running, it is chained to the whole process. 
	 */
	public void MakeInterface() {

		AsyncXmlLoader loader = GWT.create(HttpRequestXmlLoader.class);
		loader.loadResource(GWT.getModuleBaseURL()
				+ "image/fzi_plant.svg.2012_04_09_19_36_20.0.svg",
				new AsyncXmlLoaderCallback() {

					@Override
					public void onSuccess(String resourceName, Element image) {

						finished = true;
						populateGUI(OMNode.<OMSVGSVGElement> convert(image));

						GWT.log("ETALIS FZI GUI is loaded");
					}

					@Override
					public void onError(String resourceName, Throwable error) {
						GWT.log("Could not find GUI !");

					}
				});		
	};

	/**
	 * @return
	 */
	public OMSVGSVGElement getGUI() {
		return root;
	}

	/**
	 * 
	 */
	public void updateGUI() {
		if (isReady()) {
			return;
		}
	}

	/**
	 * Transform the svg ROOT Element into a viewable Image.
	 * This function is a part of the GUI chain.
	 * @param svg
	 */
	public void displayMe(OMSVGSVGElement svg) {

		root = svg;		
		myPlant = new EtalisChemicalPlant(root, myService);
		dispatcher = new EventDispatcher();
		myPlant.SetupPlant(dispatcher);

		Background = new SVGImage(root);		
		
		Background.setPixelSize(com.google.gwt.user.client.Window.getClientWidth(), com.google.gwt.user.client.Window.getClientHeight());
		FlowPanel flow = new FlowPanel();
		
		flow.add(Background);
		flow.setStyleName("center");
		RootPanel.get("app").add(flow);
		//update("init(all).");
	}

	/**
	 * @param svg
	 */
	private void populateGUI(OMSVGSVGElement svg) {

		displayMe(svg);
	}

	/**
	 * this function calls the appropriate event dispatcher to update the GUI.
	 * @param event : A JSONObject representing an ETALIS event
	 */
	public void update(JSONObject event) {		
		//System.out.println(event);
		dispatcher.Dispatch(event);
	}

}

package com.scadaview.jtalis.client;

import org.vectomatic.dom.svg.OMSVGSVGElement;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.Gauge;
import com.google.gwt.visualization.client.visualizations.Gauge.Options;

import com.scadaview.jtalis.client.DataService;
import com.scadaview.jtalis.client.gui.MainInterface;
import com.scadaview.jtalis.shared.DataContainer;

import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.client.event.RemoteEventService;
import de.novanic.eventservice.client.event.RemoteEventServiceFactory;

/**
 * The main Class for the ETALIS Webapp
 * 
 * @author 
 * Ahmed Khalil Hafsi : ahmed@hafsi.org
 * Jia Ding
 * 
 */
public class Jtalis_visual implements EntryPoint {
	private Panel panel;	
	OMSVGSVGElement svgRoot;
	boolean GUI_loaded;
	boolean Service_started;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		GUI_loaded = false;
		Service_started = false;

		DataServiceAsync myDataService = GWT.create(DataService.class);
		// startdemo() starts an jtalis emulation while start() starts a real
		// jtalis worker.
		myDataService.start(new AsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				Service_started = true;
			}

			@Override
			public void onFailure(Throwable caught) {

			}
		});
		
		

		panel = RootPanel.get("app");
		final MainInterface gui = new MainInterface(myDataService);
		final VerticalPanel vPanel = new VerticalPanel();
		vPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		final FlexTable loginPanel = new FlexTable();
		FlexCellFormatter cellFormatter = loginPanel.getFlexCellFormatter();
		loginPanel.addStyleName("cw-FlexTable");
		loginPanel.setWidth("32em");
		loginPanel.setCellSpacing(5);
		loginPanel.setCellPadding(3);

		Label welcomeLabel = new Label("ETALIS AUTOMATION DEMO");
		Button startButton = new Button("Connect to ETALIS",
				new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						loginPanel.setVisible(false);
						gui.MakeInterface();
						GUI_loaded = true;

					}
				});
		loginPanel.setWidget(0, 0, welcomeLabel);
		loginPanel.setWidget(1, 0, startButton);
		vPanel.add(loginPanel);
		panel.add(vPanel);

		// Start the Async Data Service (this is the servlet-service that pushes
		// the data to the client)

		// Create the infrastructure for the remote Event service
		RemoteEventService res = RemoteEventServiceFactory.getInstance()
				.getRemoteEventService();
		// Register a listener for the incoming data
		res.addListener(DataContainer.VISUAL_DOMAIN, new EventUserListener() {
			@Override
			public void apply(Event anEvent) {
				DataContainer myEvent;
				if (anEvent instanceof DataContainer) {
					myEvent = (DataContainer) anEvent;
					if (GUI_loaded) {
						//System.out.println("Receive: " + myEvent.getData());						
						JSONObject jsonEvent = (JSONObject) JSONParser.parseStrict(myEvent.getData());
						gui.update(jsonEvent);
					}
				}

			}

			@Override
			public void OnMessage(DataContainer data) {

			}
		});

		// Create a callback to be called when the visualization API has been
		// loaded.
		Runnable onLoadCallback = new Runnable() {
			public void run() {

				// Create a pie chart visualization.
				HorizontalPanel horizontalPanel = new HorizontalPanel();
				Gauge gaugeTemperature = new Gauge(createGaugeTable(
						"Temperature", 40), createGaugeOptions());
				Gauge gaugeWaterPressure = new Gauge(createGaugeTable(
						"Pressure", 80), createGaugeOptions());

				horizontalPanel.add(gaugeTemperature);
				horizontalPanel.add(gaugeWaterPressure);

				panel.add(horizontalPanel);
			}
		};

		// we don't need these ugly gauges, I only left them here in case we
		// need them in the future.
		// VisualizationUtils.loadVisualizationApi(onLoadCallback,
		// Gauge.PACKAGE);

	}

	private Options createGaugeOptions() {
		Options options = Options.create();

		options.setWidth(400);
		options.setHeight(240);
		options.setGaugeRange(0, 100);
		options.setGreenRange(0, 70);
		options.setYellowRange(70, 90);
		options.setRedRange(90, 100);
		options.setMinorTicks(10);

		return options;
	}

	private AbstractDataTable createGaugeTable(String label, int value) {
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING, "Label");
		data.addColumn(ColumnType.NUMBER, "Value");
		data.addRows(1);
		data.setValue(0, 0, label);
		data.setValue(0, 1, value);

		return data;
	}

}

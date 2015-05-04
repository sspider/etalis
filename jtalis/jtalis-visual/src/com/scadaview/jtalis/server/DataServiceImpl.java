package com.scadaview.jtalis.server;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.service.RemoteEventServiceServlet;

import com.jtalis.core.JtalisContextImpl;
import com.jtalis.core.event.EtalisEvent;
import com.jtalis.core.event.ProviderSetupException;
import com.jtalis.core.event.provider.DefaultOutputProvider;

import com.jtalis.core.plengine.JPLEngineWrapper;
import com.jtalis.core.plengine.PrologEngineWrapper;
import com.scadaview.jtalis.client.*;
import com.scadaview.jtalis.shared.DataContainer;

/**
 * The main entry point class for the Server Service
 * 
 * @author Ahmed Khalil Hafsi, ahmed@hafsi.org
 *
 */
public class DataServiceImpl extends RemoteEventServiceServlet implements
		DataService {

	
	private static final long serialVersionUID = 2983506663743628372L;

	private static Timer myServerClock;
	private static Timer alertEvent;
	private static int count;
	private static int count2;
	private static PrologEngineWrapper<?> engine;
	JtalisContextImpl ctx;

	@Override
	public synchronized void start() {

		try {
			engine = new JPLEngineWrapper(false);
			ctx = new JtalisContextImpl(engine);
			count = 0;
			
			
			ctx.addEventTrigger("_/_");
			// add UI regulation rules
			ctx.addDynamicRuleWithId("water_level",
					"water_level(TankID, Level) <- waterLevelSensor(TankID, Level)");
			
			ctx.addDynamicRuleWithId("valve_status",
					"valve_status(ValveID, Status) <- valveSensor(ValveID, Status)");
			
			ctx.addDynamicRuleWithId("pressure_value1",
					"pressure_sensor('pressure_sensor1', DurchflussValue) <- durchfluss_sensor(DurchflussValue)");
			
			ctx.addDynamicRuleWithId("pressure_value2",
					"pressure_sensor('pressure_sensor2', DurchflussValue) <- durchfluss_sensor(DurchflussValue)");
			
			
			ctx.addDynamicRuleWithId("warning_druck", 
					"warning_info('warning', 'Pressure value is out of range!') <- alert_druck(X) ");
			
			ctx.addDynamicRuleWithId("warning_temperature", 
					"warning_info('warning', 'Temperature value is out of range!') <- alert_temperature(Y) ");
			
			ctx.addDynamicRuleWithId("warning_durchfluss", 
					"warning_info('warning', 'Flow value is below the threshold!') <- alert_durchfluss(Z) ");
			
			ctx.addDynamicRuleWithId("warning_heating", 
					"warning_info('warning', 'Heating Error!') <- heating_Error ");
			
			ctx.addDynamicRuleWithId("warning_v112", 
					"warning_info('warning', 'V112 Error!') <- v112_Error ");
			
			ctx.addDynamicRuleWithId("warning_water_level", 
					"warning_info('warning', 'Water Level B402 Error!') <- water_Level_B402_Error ");
			
			ctx.addDynamicRuleWithId("warning_bus_connection", 
					"warning_info('warning', 'Bus Connection Error!') <- bus_Connection_Error ");
			
			
			// add Ontoprise rules
			ctx.addDynamicRuleWithId("durchfluss", 
					"alert_durchfluss(DurchflussValue) <- durchfluss_sensor(DurchflussValue) " +
					"where ( DurchflussValue<50)");
			
			ctx.addDynamicRuleWithId("temperature", 
					"alert_temperature(TemperatureValue) <- temperature_sensor(TemperatureValue) " +
					"where ( TemperatureValue<20; TemperatureValue>30)");
			
			ctx.addDynamicRuleWithId("druck", 
					"alert_druck(DruckValue) <- druck_sensor(DruckValue) " +
					"where ( DruckValue<10; DruckValue>20 )");
			
			ctx.addDynamicRuleWithId("v112_Error([property(event_rule_window,5)])", 
					"v112_Error <- heating_Error and water_Level('low') and s116('true')");
			
			ctx.addDynamicRuleWithId("bus_Connection_Error([property(event_rule_window,5)])", 
					"bus_Connection_Error <- water_Level_B402_Error and p401('true') and water_Level('low') and p101('true') and s116('true')");
			
			// register UI regulation OutputPrivider
			//ctx.registerOutputProvider("water_tank1|water_tank2|water_tank3|water_tank4", new OPCOutputProvider(this));
			ctx.registerOutputProvider("water_level|valve_status|information|pressure_sensor|warning_info", new OPCOutputProvider(this));
			
			// register Ontoprise OutputPrivider
			ctx.registerOutputProvider("heating_Error|v112_Error|water_Level_B402_Error|bus_Connection_Error", new OntopriseOutputProvider("localhost", 11122));
			
			myServerClock = new Timer();
			myServerClock.schedule(new TimerTask() {				
				@Override
				public void run() {
					// Test Watertank
					Random generator = new Random();
					int level1 = generator.nextInt(50) + 50;
					int level2 = generator.nextInt(50) + 50;
					int level3 = generator.nextInt(50) + 50;
					int level4 = generator.nextInt(50) + 50;
					ctx.pushEvent(new EtalisEvent("waterLevelSensor", "water_tank1", level1));
					ctx.pushEvent(new EtalisEvent("waterLevelSensor", "water_tank2", level2));
					ctx.pushEvent(new EtalisEvent("waterLevelSensor", "water_tank3", level3));
					ctx.pushEvent(new EtalisEvent("waterLevelSensor", "water_tank4", level4));

					int flow1 = generator.nextInt(20);
					int flow2 = generator.nextInt(20);
					
					ctx.pushEvent(new EtalisEvent("pressure_sensor", "pressure_sensor1", flow1));
					ctx.pushEvent(new EtalisEvent("pressure_sensor", "pressure_sensor2", flow2));
					
					
					// Test Valve
					if (count % 3 == 1) {
						ctx.pushEvent(new EtalisEvent("valveSensor", "valve101", 1));
						ctx.pushEvent(new EtalisEvent("valveSensor", "valve102", 0));
					}
					else {
						ctx.pushEvent(new EtalisEvent("valveSensor", "valve101", 0));
						ctx.pushEvent(new EtalisEvent("valveSensor", "valve102", 1));
					}
					
					// Test Dashboard
					//ctx.pushEvent(new EtalisEvent("information", "info", "Count is " + count));
										
					
					
					count++;
				}
			}, 5000,1000);
			
			
			/*// Generate Alert Event
			count2 = 0;
			alertEvent = new Timer();
			alertEvent.schedule(new TimerTask() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Random generator = new Random();
					// Flow
					if (count2 % 180 == 30) {
						ctx.pushEvent(new EtalisEvent("durchfluss_sensor", 30));
					} else {
						int flow = generator.nextInt(50) + 50;
						ctx.pushEvent(new EtalisEvent("durchfluss_sensor", flow));
					}
					
					// Temperature
					if (count2 % 180 == 90) {
						ctx.pushEvent(new EtalisEvent("temperature_sensor", 40));
					} else {
						int temperature = generator.nextInt(10) + 20;
						ctx.pushEvent(new EtalisEvent("temperature_sensor", temperature));
					}
					
					// Pressure
					if (count2 % 180 == 150) {
						ctx.pushEvent(new EtalisEvent("druck_sensor", 6));
					} else {
						int pressure = generator.nextInt(10) + 10;
						ctx.pushEvent(new EtalisEvent("druck_sensor", pressure));
					}
					
					count2++;
				}
			}, 5000, 1000);*/
			
			
		} catch (ProviderSetupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}

	@Override
	public synchronized void startdemo() {
		try {			

			System.out.println("Server side started !");
			count = 0;
			myServerClock = new Timer();
			myServerClock.schedule(new TimerTask() {

				@Override
				public synchronized void run() {
					// feed Jtalis two Event
					// ctx.pushEvent(new EtalisEvent("a", count));
					// ctx.pushEvent(new EtalisEvent("b", count));
					// count++;
					DataContainer data = new DataContainer();
					data.setData("info(" + "1," + count + ").");
					count++;
					Event theEvent = data;
					addEvent(DataContainer.VISUAL_DOMAIN, theEvent);

					Random generator = new Random();
					DataContainer waterLevel = new DataContainer();
					int level = generator.nextInt(50) + 50;
					waterLevel.setData("water_tank1(1," + level + ")");
					Event water_tank1 = waterLevel;
					addEvent(DataContainer.VISUAL_DOMAIN, water_tank1);

				}
			}, 20000, 1000);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Override
	public synchronized void sendEvent(String event) {
		if (event.equals("btn_heating_Error")) 
			ctx.pushEvent(new EtalisEvent("heating_Error"));
		else if (event.equals("btn_water_Level_B402_Error"))
			ctx.pushEvent(new EtalisEvent("water_Level_B402_Error"));
		else if (event.equals("btn_water_Level_Low"))
			ctx.pushEvent(new EtalisEvent("water_Level", "low"));
		else if (event.equals("btn_valve_S116_Open"))
			ctx.pushEvent(new EtalisEvent("s116", "true"));
		else if (event.equals("btn_pump_101_Open"))
			ctx.pushEvent(new EtalisEvent("p101", "true"));
		else if (event.equals("btn_pump_401_Open"))
			ctx.pushEvent(new EtalisEvent("p401", "true"));

	}

}

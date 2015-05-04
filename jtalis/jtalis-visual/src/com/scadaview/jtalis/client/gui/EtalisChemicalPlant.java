package com.scadaview.jtalis.client.gui;

import org.vectomatic.dom.svg.OMSVGSVGElement;

import com.scadaview.jtalis.client.DataServiceAsync;
import com.scadaview.jtalis.client.gui.ui.*;


/**
 * A logical representation of the GUI, allows a flexible indexed way of making
 * the internal functions of the GUI map with the svg tree nodes.
 * 
 * @author Ahmed Khalil Hafsi, ahmed@hafsi.org
 *
 */
public class EtalisChemicalPlant {

	// A preliminary structure of the plant.
	private WaterTank B101;
	private WaterTank B102;
	private WaterTank B201;
	private WaterTank B301;

	private PressureSensor P101;
	private PressureSensor P201;

	private Valve V101;
	private Valve V102;
	private Valve V105;
	private Valve V301;
	private Valve V302;

	private Dashboard myDash;
	
	private WarningIcon icon;
	
	private EventButton btn_heating_Error;
	private EventButton btn_water_Level_B402_Error;
	
	private EventButton btn_water_Level_Low;
	private EventButton btn_water_Level_Normal;
	private EventButton btn_water_Level_High;
	
	private EventButton btn_pump_101_Open;
	private EventButton btn_pump_101_Close;
	private EventButton btn_pump_401_Open;
	private EventButton btn_pump_401_Close;
	
	private EventButton btn_valve_S116_Open;
	private EventButton btn_valve_S116_Close;

	/**
	 * 
	 */
	public EtalisChemicalPlant(OMSVGSVGElement rootSvg, DataServiceAsync myService) {
		// B101 = new WaterTank();
		// System.out.println(rootSvg.getElementById("tank0header"));
		//B102 = new WaterTank(rootSvg);
		B102 = new WaterTank(rootSvg, "waterLevelB102", "tankHeaderB102", "levelLabelB102");
		B101 = new WaterTank(rootSvg, "waterLevelB101", "tankHeaderB101", "levelLabelB101");
		B201 = new WaterTank(rootSvg, "waterLevelB201", "tankHeaderB201", "levelLabelB201");
		B301 = new WaterTank(rootSvg, "waterLevelB301", "tankHeaderB301", "levelLabelB301");
		// B201 = new WaterTank();
		// B301 = new WaterTank();
		
		P101 = new PressureSensor(rootSvg.getElementById("flowLabel101").getChildNodes()
				.getItem(0).getFirstChild());
		P201 = new PressureSensor(rootSvg.getElementById("flowLabel201").getChildNodes()
				.getItem(0).getFirstChild());
		
		V101 = new Valve(rootSvg, "valve101");
		V102 = new Valve(rootSvg, "valve102");
		// V105 = new Valve();
		// V301 = new Valve();
		// V302 = new Valve();
		myDash = new Dashboard(rootSvg.getElementById("test").getChildNodes()
				.getItem(0).getFirstChild());
		
		icon = new WarningIcon(rootSvg, "warningIcon", "warningLabel");
		
		//Button
		btn_heating_Error = new EventButton(rootSvg, "btn_heating_Error", myService);
		btn_water_Level_B402_Error = new EventButton(rootSvg, "btn_water_Level_B402_Error", myService);
		
		btn_water_Level_Low = new EventButton(rootSvg, "btn_water_Level_Low", myService);
		btn_water_Level_Normal = new EventButton(rootSvg, "btn_water_Level_Normal", myService);
		btn_water_Level_High = new EventButton(rootSvg, "btn_water_Level_High", myService);
		
		btn_pump_101_Open = new EventButton(rootSvg, "btn_pump_101_Open", myService);
		btn_pump_101_Close = new EventButton(rootSvg, "btn_pump_101_Close", myService);
		btn_pump_401_Open = new EventButton(rootSvg, "btn_pump_401_Open", myService);
		btn_pump_401_Close = new EventButton(rootSvg, "btn_pump_401_Close", myService);
		
		btn_valve_S116_Open = new EventButton(rootSvg, "btn_valve_S116_Open", myService);
		btn_valve_S116_Close = new EventButton(rootSvg, "btn_valve_S116_Close", myService);
	}

	/**
	 * @param b101
	 * @param b102
	 * @param b201
	 * @param b301
	 * @param p101
	 * @param p201
	 * @param v105
	 * @param v301
	 * @param v302
	 * @param myDash
	 */
	public EtalisChemicalPlant(WaterTank b101, WaterTank b102, WaterTank b201,
			WaterTank b301, PressureSensor p101, PressureSensor p201,
			Valve v105, Valve v301, Valve v302, Dashboard myDash) {
		super();
		B101 = b101;
		B102 = b102;
		B201 = b201;
		B301 = b301;
		P101 = p101;
		P201 = p201;
		V105 = v105;
		V301 = v301;
		V302 = v302;
		this.myDash = myDash;
	}

	/**
	 * Setup the plant and register event signatures to the corresponding GUI
	 * elements.
	 * 
	 * @param evDis
	 *            an Event dispatcher has be to created and passed to this
	 *            method.
	 */
	public void SetupPlant(EventDispatcher evDis) {
/*		evDis.RegisterNewEventMapping("water_tank1/2", B102);
		evDis.RegisterNewEventMapping("water_tank2/2", B101);
		evDis.RegisterNewEventMapping("water_tank3/2", B201);
		evDis.RegisterNewEventMapping("water_tank4/2", B301);

		evDis.RegisterNewEventMapping("pressure_sensor1/1", P101);
		evDis.RegisterNewEventMapping("pressure_sensor2/1", P201);

		evDis.RegisterNewEventMapping("info/2", myDash);*/
		
		evDis.RegisterNewEventMapping("water_tank1", B102);
		evDis.RegisterNewEventMapping("water_tank2", B101);
		evDis.RegisterNewEventMapping("water_tank3", B201);
		evDis.RegisterNewEventMapping("water_tank4", B301);

		evDis.RegisterNewEventMapping("pressure_sensor1", P101);
		evDis.RegisterNewEventMapping("pressure_sensor2", P201);
		
		evDis.RegisterNewEventMapping("valve101", V101);
		evDis.RegisterNewEventMapping("valve102", V102);

		evDis.RegisterNewEventMapping("info", myDash);

		evDis.RegisterNewEventMapping("warning", icon);
	}

	/**
	 * @return the b101
	 */
	public WaterTank getB101() {
		return B101;
	}

	/**
	 * @param b101
	 *            the b101 to set
	 */
	public void setB101(WaterTank b101) {
		B101 = b101;
	}

	/**
	 * @return the b102
	 */
	public WaterTank getB102() {
		return B102;
	}

	/**
	 * @param b102
	 *            the b102 to set
	 */
	public void setB102(WaterTank b102) {
		B102 = b102;
	}

	/**
	 * @return the b201
	 */
	public WaterTank getB201() {
		return B201;
	}

	/**
	 * @param b201
	 *            the b201 to set
	 */
	public void setB201(WaterTank b201) {
		B201 = b201;
	}

	/**
	 * @return the b301
	 */
	public WaterTank getB301() {
		return B301;
	}

	/**
	 * @param b301
	 *            the b301 to set
	 */
	public void setB301(WaterTank b301) {
		B301 = b301;
	}

	/**
	 * @return the p101
	 */
	public PressureSensor getP101() {
		return P101;
	}

	/**
	 * @param p101
	 *            the p101 to set
	 */
	public void setP101(PressureSensor p101) {
		P101 = p101;
	}

	/**
	 * @return the p201
	 */
	public PressureSensor getP201() {
		return P201;
	}

	/**
	 * @param p201
	 *            the p201 to set
	 */
	public void setP201(PressureSensor p201) {
		P201 = p201;
	}

	/**
	 * @return the v105
	 */
	public Valve getV105() {
		return V105;
	}

	/**
	 * @param v105
	 *            the v105 to set
	 */
	public void setV105(Valve v105) {
		V105 = v105;
	}

	/**
	 * @return the v301
	 */
	public Valve getV301() {
		return V301;
	}

	/**
	 * @param v301
	 *            the v301 to set
	 */
	public void setV301(Valve v301) {
		V301 = v301;
	}

	/**
	 * @return the v302
	 */
	public Valve getV302() {
		return V302;
	}

	/**
	 * @param v302
	 *            the v302 to set
	 */
	public void setV302(Valve v302) {
		V302 = v302;
	}

	/**
	 * @return the myDash
	 */
	public Dashboard getMyDash() {
		return myDash;
	}

	/**
	 * @param myDash
	 *            the myDash to set
	 */
	public void setMyDash(Dashboard myDash) {
		this.myDash = myDash;
	}

}

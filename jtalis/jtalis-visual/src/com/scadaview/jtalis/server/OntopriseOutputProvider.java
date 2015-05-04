package com.scadaview.jtalis.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.SocketFactory;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.core.event.JtalisOutputEventProvider;
import com.jtalis.core.event.ProviderSetupException;
import com.jtalis.core.event.persistence.DefaultEventIO;

public class OntopriseOutputProvider implements JtalisOutputEventProvider{
	private Socket s;
	private String host;
	private int port;
	private DefaultEventIO io;
	private PrintWriter out;
	
	private static String searchURL = "http://holl-guide.fzi.de/semanticguide/portal/search?search=";
	//private static String druckCase = "Druck";
	//private static String durchflussCase = "Durchfluss";
	//private static String temperatureCase = "Temperature";
	private static String heating_Error_URL = "http://holl-guide.fzi.de/semanticguide/guide/start?caseId=%5Esg%3Asymptom%3A6";
	private static String v112_Error_URL = searchURL + "v112";
	private static String water_Level_B402_Error_URL = "http://holl-guide.fzi.de/semanticguide/guide/start?caseId=%5Esg%3Asymptom%3A7";
	private static String bus_Connection_Error_URL = searchURL + "Connect+bus+cable";
	
	public OntopriseOutputProvider(String host, int port) throws ProviderSetupException {
		this.host = host;
		this.port = port;
		setup();
	}
	
	
	@Override
	public void setup() throws ProviderSetupException {
		// TODO Auto-generated method stub
		io = new DefaultEventIO();
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		try {
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void outputEvent(EtalisEvent event) {
		// TODO Auto-generated method stub
		try {
			//"heating_Error|v112_Error|water_Level_B402_Error|bus_Connection_Error"
			s = SocketFactory.getDefault().createSocket(host, port);
			out = new PrintWriter(s.getOutputStream(), true);
			String rtn;
			if (event.getName().equals("heating_Error"))
				rtn = heating_Error_URL;
			else if (event.getName().equals("v112_Error"))
				rtn = v112_Error_URL;
			else if (event.getName().equals("water_Level_B402_Error"))
				rtn = water_Level_B402_Error_URL;
			else 
				rtn = bus_Connection_Error_URL;
			out.println(rtn);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				s.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

}

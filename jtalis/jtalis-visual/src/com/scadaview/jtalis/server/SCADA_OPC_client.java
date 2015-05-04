package com.scadaview.jtalis.server;

import java.net.UnknownHostException;
import java.util.concurrent.Executors;

import org.openscada.opc.lib.common.AlreadyConnectedException;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.common.NotConnectedException;
import org.openscada.opc.lib.da.AccessBase;
import org.openscada.opc.lib.da.AddFailedException;
import org.openscada.opc.lib.da.DataCallback;
import org.openscada.opc.lib.da.DuplicateGroupException;
import org.openscada.opc.lib.da.Item;
import org.openscada.opc.lib.da.ItemState;
import org.openscada.opc.lib.da.Server;
import org.openscada.opc.lib.da.SyncAccess;
import org.jinterop.dcom.common.JIException;

/**
 * @author Ahmed Khalil Hafsi, ahmed@hafsi.org 
 * 
 */
public class SCADA_OPC_client {
	final ConnectionInformation ci;
	String Host;
	String Domain;
	String User;
	String Password;
	String ClsID;

	/**
	 * @param Host
	 * @param Domain
	 * @param User
	 * @param Password
	 * @param ClsID
	 */
	public SCADA_OPC_client(String Host, String Domain, String User,
			String Password, String ClsID) {
		ci = new ConnectionInformation();
		this.Host = Host;
		this.Domain = Domain;
		this.User = User;
		this.Password = Password;
		this.ClsID = ClsID;
		ci.setHost(this.Host);
		ci.setDomain(Domain);
		ci.setUser(User);
		ci.setClsid(ClsID);
		ci.setPassword(Password);
	}

	/**
	 * @throws IllegalArgumentException
	 * @throws UnknownHostException
	 * @throws NotConnectedException
	 * @throws DuplicateGroupException
	 * @throws InterruptedException
	 */
	public void Connect() throws IllegalArgumentException,
			UnknownHostException, NotConnectedException,
			DuplicateGroupException, InterruptedException {
		String itemId = "Saw-toothed Waves.Int2";
		Server server = new Server(ci,
				Executors.newSingleThreadScheduledExecutor());
		try {
			// connect to server
			try {
				server.connect();
			} catch (IllegalArgumentException e1) {

				e1.printStackTrace();
			} catch (UnknownHostException e1) {

				e1.printStackTrace();
			} catch (AlreadyConnectedException e1) {

				e1.printStackTrace();
			}

			// add sync access

			final AccessBase access = new SyncAccess(server, 100);
			try {
				access.addItem(itemId, new DataCallback() {

					@Override
					public void changed(Item item, ItemState itemState) {

						System.out.println(String
								.format("Item: %s, Value: %s, Timestamp: %tc, Quality: %d",
										item.getId(), itemState.getValue(),
										itemState.getTimestamp(),
										itemState.getQuality()));

						/*
						 * try { VariantDumper.dumpValue ( "\t",
						 * itemState.getValue () ); } catch ( final JIException
						 * e ) { e.printStackTrace (); }
						 */
					}
				});
			} catch (AddFailedException e) {

				e.printStackTrace();
			}

			// start reading
			access.bind();

			// wait a little bit
			Thread.sleep(10 * 1000);

			// stop reading
			access.unbind();
		} catch (final JIException e) {

		}

	}

}

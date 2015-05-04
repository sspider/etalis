package com.jtalis.core.event.provider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URL;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.SocketFactory;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.core.event.InvalidEventFormatException;
import com.jtalis.core.event.JtalisInputEventProvider;
import com.jtalis.core.event.ProviderSetupException;
import com.jtalis.core.event.persistence.DefaultEventIO;
import com.veskogeorgiev.probin.annotations.Parameter;

/**
 * Input event provider for ETALIS default event format e.g. beer('BECKS', 2.50)
 *
 * <ul>
 * 	<li><b>file</b>: If this is specified all output will be appended to this file. If this is not specified existence of
 * the next properties will be checked</li>
 * 	<li><b>url</b>: If this is specified, a connection the specified URL will be opened and the events will be expected 
 * from it. Possible value is: "http://localhost:8888" or the protocol could be omitted like: "localhost:8888"</li>
 *  <li>If neither of those is specified then the events will be expected from Standard Input</li>
 *  <li><b>restoreConnection</b>: Specifies whether to try to restore the connection if it is broken. (if url connection is used)</li>
 * </ul>
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class DefaultInputProvider implements JtalisInputEventProvider {

	private static final Logger logger = Logger.getLogger(DefaultInputProvider.class.getName());

	@Parameter
	private String url;
	@Parameter
	private File file;
	@Parameter("restoreConnection")
	private boolean restore;

	private InputStream inputStream;
	private String message;
	private DefaultEventIO io = new DefaultEventIO();

	public DefaultInputProvider() {
		inputStream = System.in;
		message = "Reading from Standard Input";
	}

	public DefaultInputProvider(File f) throws ProviderSetupException {
		this.file = f;
		setup();
	}

	public DefaultInputProvider(String url) throws ProviderSetupException {
		this.url = url;
		setup();
	}

	@Override
	public void setup() throws ProviderSetupException {
		inputStream = null;
		if (file != null) {
			try {
				inputStream = new FileInputStream(file);
				message = "Reading from " + file;
			}
			catch (FileNotFoundException e) {
				throw new ProviderSetupException(e);
			}
		}
		else if (url != null) {
			Pattern p = Pattern.compile("(([a-z]+)://)?([a-zA-Z\\.\\d\\p{Punct}]+):(\\d+)");
			Matcher m = p.matcher(url);
			if (m.matches()) {
				String protocol = m.group(2);
				String host = m.group(3);
				int port = Integer.parseInt(m.group(4));
				if (protocol == null) {
					try {
						Socket s = SocketFactory.getDefault().createSocket(host, port);
						inputStream = s.getInputStream();
						message = "Connected to " + url;
					}
					catch (Exception e) {
						throw new ProviderSetupException(e);
					}
				}
				else {
					try {
						URL urlAddress = new URL(url);
						inputStream = urlAddress.openConnection().getInputStream();
						message = "Connected to " + url;
					}
					catch (Exception e) {
						throw new ProviderSetupException(e);
					}
				}
			}
		}
		else {
			logger.info("No input source is specified. Binding to Standard Input.");
			inputStream = System.in;
			message = "Reading from Standard Input";
		}
	}

	@Override
	public EtalisEvent getEvent() {
		try {
			return io.deserialize(inputStream);
		}
		catch (IOException e) {
			if (inputStream instanceof FileInputStream) {
				inputStream = null;
				return null;
			}
			logger.warning("Connection interrupted.");
			if (inputStream != null) {
				try {
					inputStream.close();
				}
				catch (IOException e1) {
					//
				}
			}
			if (restore) { 
				while (true) {
					try {
						setup();
						logger.info("Connection restored. ");
						break;
					}
					catch (ProviderSetupException e1) {
						logger.warning("Exception while trying to restore connection. " + e1 + " Trying again in 5 seconds.");
					}
	
					try {
						Thread.sleep(5000);
					}
					catch (InterruptedException e1) {
					}
				}
			}
		}
		catch (InvalidEventFormatException e) {
			logger.warning(e + ". Returning null and continuing reading.");
			return null;
		}
		return null;
	}

	@Override
	public boolean hasMore() {
		return inputStream != null;
	}

	@Override
	public void shutdown() {
		if (inputStream != null) {
			try {
				inputStream.close();
			}
			catch (IOException e) {
				//
			}
		}
	}

	@Override
	public String toString() {
		return getClass().getName() + ": " + message != null ? message : "";
	}
}

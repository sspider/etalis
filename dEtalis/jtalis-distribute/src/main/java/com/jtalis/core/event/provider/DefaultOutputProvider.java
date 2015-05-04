package com.jtalis.core.event.provider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ServerSocketFactory;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.core.event.JtalisOutputEventProvider;
import com.jtalis.core.event.ProviderSetupException;
import com.jtalis.core.event.persistence.DefaultEventIO;
import com.veskogeorgiev.probin.annotations.Parameter;

/**
 * Output event provider for ETALIS default event format. It has the following parameters:
 * 
 * <ul>
 * 	<li><b>file</b>: If this is specified the events will be read from this file. If this is not specified existence of
 * the next properties will be checked</li>
 * 	<li><b>file</b>: If this is specified all output will be appended to this file. If this is not specified existence of
 * the following next will be checked</li>
 * 	<li><b>url</b>: If this is specified, a server socket will be opened on this address and will wait for connections 
 * to send the events to. Possible value is: "localhost:8888"</li>
 *  <li>If neither of those is specified then the events will be printed on the Standard Output</li>
 * </ul> 
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class DefaultOutputProvider implements JtalisOutputEventProvider {

	private static final Logger logger = Logger.getLogger(DefaultOutputProvider.class.getName());

	@Parameter
	private String url;
	@Parameter
	private File file;

	private OutputStream outputStream;
	private Set<Socket> sockets = new HashSet<Socket>();
	private DefaultEventIO io = new DefaultEventIO();
	private String message;

	public DefaultOutputProvider() {
		outputStream = System.out;
		message = "Writing to Standard output";
	}

	public DefaultOutputProvider(File f) throws ProviderSetupException {
		this.file = f;
		setup();
	}

	public DefaultOutputProvider(String url) throws ProviderSetupException {
		this.url = url;
		setup();
	}

	@Override
	public void setup() throws ProviderSetupException {
		outputStream = null;
		if (file != null) {
			try {
				outputStream = new FileOutputStream(file, true);
				message = "Writing to " + file;
			}
			catch (FileNotFoundException e) {
				throw new ProviderSetupException(e);
			}
		}
		else if (url != null) {
			Pattern p = Pattern.compile("([a-zA-Z\\d\\p{Punct}]+):(\\d+)");
			Matcher m = p.matcher(url);

			if (m.matches()) {
				String host = m.group(1);
				int port = Integer.parseInt(m.group(2));

				try {
					final ServerSocket socket = ServerSocketFactory.getDefault().createServerSocket(port, 16, InetAddress.getByName(host));
					message = "Bound to " + url;
					new Thread() {
						public void run() {
							while (true) {
								try {
									Socket s = socket.accept();
									sockets.add(s);
									logger.info(s.getInetAddress() + " accepted");
								}
								catch (IOException e) {
									e.printStackTrace();
								}
							}
						};
					}.start();
				}
				catch (Exception e) {
					logger.warning(e.toString());
				}
			}
		}
		else {
			logger.info("No output source specified. Binding to Standard Output.");
			outputStream = System.out;
			message = "Writing to Standard output";
		}
	}

	@Override
	public void outputEvent(EtalisEvent event) {
		if (outputStream != null) {
			if (outputStream == System.out) {
				System.out.print(getClass().getSimpleName() + ": ");
			}
			try {
				io.serialize(event, outputStream);
			}
			catch (IOException e) {
				logger.warning(e.toString());
			}
		}
		for (Iterator<Socket> it = sockets.iterator(); it.hasNext();) {
			Socket s = it.next();
			try {
				io.serialize(event, s.getOutputStream());
			}
			catch (IOException e) {
				logger.warning(s.getInetAddress() + " is not responding. It's connection will be closed.");
				try {
					it.remove();
					s.close();
				}
				catch (IOException e1) {
					// that's the life
				}
			}
		}
	}

	@Override
	public void shutdown() {
		if (outputStream != null) {
			try {
				outputStream.close();
			}
			catch (IOException e) {
				//
			}
		}
		for (Socket s : sockets) {
			try {
				s.close();
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

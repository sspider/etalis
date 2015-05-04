package com.jtalis.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;

import org.junit.Assert;
import org.junit.Test;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.core.event.InvalidEventFormatException;
import com.jtalis.core.event.persistence.DefaultEventIO;
import com.jtalis.core.event.persistence.EventStreamOutput;

public class TestDefaultIO {
/*
	private static final Logger logger = Logger.getLogger(TestDefaultIO.class.getName());

	private static final List<EtalisEvent> expected = Arrays.asList(new EtalisEvent("a", 12), new EtalisEvent("b", 11), new EtalisEvent("c", 10));

	static class ServerThread extends Thread {
		int port;
		EventStreamOutput io;

		private ServerThread(int port, EventStreamOutput io) {
			this.port = port;
			this.io = io;
		}

		public void run() {
			ServerSocket server = null;
			try {
				server = ServerSocketFactory.getDefault().createServerSocket(port);
				Socket client = null;

				try {
					client = server.accept();
					for (EtalisEvent e : expected) {
						io.serialize(e, client.getOutputStream());
						try {
							Thread.sleep(200);
						}
						catch (InterruptedException e1) {
						}
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				finally {
					if (client != null) {
						client.close();
					}
				}
			}
			catch (IOException e) {
				logger.severe(e + ". " + e.getMessage());
			}
			finally {
				try {
					if (server != null) {
						server.close();
					}
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
	}

	static abstract class ClientThread extends Thread {
		int port;

		private ClientThread(int port) {
			this.port = port;
		}

		abstract void testIO(InputStream is);

		public void run() {
			Socket socket = null;
			try {
				socket = SocketFactory.getDefault().createSocket(InetAddress.getLocalHost(), port);
				InputStream is = socket.getInputStream();

				testIO(is);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				if (socket != null) {
					try {
						socket.close();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Test
	public void testReadingSingleEventFromFile() {
		DefaultEventIO io = new DefaultEventIO();
		try {
			InputStream is = getClass().getResourceAsStream("simple-events.txt");
			List<EtalisEvent> received = new LinkedList<EtalisEvent>();
			received.add(io.deserialize(is));
			received.add(io.deserialize(is));
			received.add(io.deserialize(is));

			if (!received.equals(expected)) {
				Assert.fail(expected + " exptected. Instead, received: " + received);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e + ". " + e.getMessage());
		}
	}

	@Test
	public void testReadingAllEventFromFile() {
		DefaultEventIO io = new DefaultEventIO();
		try {
			InputStream is = getClass().getResourceAsStream("simple-events.txt");
			List<EtalisEvent> received = new LinkedList<EtalisEvent>();
			received.addAll(io.deserializeAll(is));

			if (!received.equals(expected)) {
				Assert.fail(expected + " exptected. Instead, received: " + received);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void testSingleIOFromSocket() {
		final DefaultEventIO io = new DefaultEventIO();
		final int port = 48273;

		final List<EtalisEvent> received = new LinkedList<EtalisEvent>();

		try {
			ServerThread server = new ServerThread(port, io);
			ClientThread client = new ClientThread(port) {
				@Override
				void testIO(InputStream is) {
					try {
						received.add(io.deserialize(is));
						received.add(io.deserialize(is));
						received.add(io.deserialize(is));
					}
					catch (IOException e) {
						e.printStackTrace();
					}
					catch (InvalidEventFormatException e) {
						e.printStackTrace();
					}
				}
			};
			client.start();
			server.start();

			client.join();
			server.join();

			if (!received.equals(expected)) {
				Assert.fail(expected + " exptected. Instead, received: " + received);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void testAllIOFromSocket() {
		final DefaultEventIO io = new DefaultEventIO();
		final int port = 48274;
		final List<EtalisEvent> received = new LinkedList<EtalisEvent>();

		try {
			ServerThread server = new ServerThread(port, io);
			ClientThread client = new ClientThread(port) {

				@Override
				void testIO(InputStream is) {
					try {
						received.addAll(io.deserializeAll(is));
					}
					catch (IOException e) {
						e.printStackTrace();
					}
					catch (InvalidEventFormatException e) {
						e.printStackTrace();
					}
				}
			};
			client.start();
			server.start();

			client.join();
			server.join();

			Assert.assertEquals(received, expected);
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void testWrite() {
		DefaultEventIO io = new DefaultEventIO();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		EtalisEvent e = new EtalisEvent("beer", "BECKS", 2.50);
		try {
			io.serialize(e, out);
			Assert.assertEquals(e.getPrologString() + "\n", new String(out.toByteArray()));
		}
		catch (IOException e1) {
			e1.printStackTrace();
			Assert.fail(e1.toString());
		}
	}*/
}

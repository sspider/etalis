package com.jtalis.core.event.provider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.core.event.ProviderSetupException;
import com.jtalis.core.event.xml.XMLEventIO;
import com.jtalis.core.event.xml.XMLEventSchema;
import com.veskogeorgiev.probin.annotations.Parameter;

/**
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class XMLOutputProvider extends PersistentOutputProvider {

	@Parameter("file")
	private File file;

	@Parameter("xsd")
	private File xsd;

	private OutputStream stream;
	private XMLEventIO io;

	public XMLOutputProvider() {
		// Default
	}

	public XMLOutputProvider(File file, File xsd) throws ProviderSetupException {
		this.file = file;
		this.xsd = xsd;
		setup();
	}

	@Override
	public void setup() throws ProviderSetupException {
		try {
			stream = new FileOutputStream(file, true);
			io = new XMLEventIO(new XMLEventSchema(xsd));
		}
		catch (Exception e) {
			throw new ProviderSetupException(e);
		}
	}

	@Override
	protected void closeConnection() throws Exception {
		if (stream != null) {
			stream.close();
		}
	}

	@Override
	protected void commit() throws Exception {
		closeConnection();
		stream = new FileOutputStream(file, true);
	}

	@Override
	protected void save(EtalisEvent event) throws Exception {
		io.serialize(event, stream);
	}
}

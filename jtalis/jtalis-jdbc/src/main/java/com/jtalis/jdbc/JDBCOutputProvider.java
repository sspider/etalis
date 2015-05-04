package com.jtalis.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.core.event.ProviderSetupException;
import com.jtalis.core.event.provider.PersistentOutputProvider;
import com.veskogeorgiev.probin.annotations.Parameter;

/**
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class JDBCOutputProvider extends PersistentOutputProvider {

	@Parameter("url")
	private String url;
	@Parameter("user")
	private String user;
	@Parameter("password")
	private String password;

	protected Connection connection;
	protected JDBCDatastore db;

	public JDBCOutputProvider() {
	}

	public JDBCOutputProvider(String url, String user, String password, int bufferSize) throws ProviderSetupException {
		super(bufferSize);
		this.url = url;
		this.user = user;
		this.password = password;
		this.bufferSize = bufferSize;

		setup();
	}

	public void setup() throws ProviderSetupException {
		if (user == null || user.length() == 0) {
			user = "sa";
		}
		if (password == null || user.length() == 0) {
			password = "";
		}

		Properties props = new Properties();
		props.put("user", user);
		props.put("password", password);

		try {
			connection = DriverManager.getConnection(url, props);
			db = new JDBCDatastore(connection);
		}
		catch (SQLException e) {
			e.printStackTrace();
			throw new ProviderSetupException(e.getMessage());
		}
	}

	@Override
	protected void closeConnection() throws Exception {
		db.close();
		connection.close();
	}

	@Override
	protected void commit() throws Exception {
		connection.commit();
	}

	@Override
	protected void save(EtalisEvent event) throws Exception {
		db.persist(event);
	}
}

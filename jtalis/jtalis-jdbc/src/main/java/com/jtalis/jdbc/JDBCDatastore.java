package com.jtalis.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.logging.Logger;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.core.plengine.logic.Term;

/**
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class JDBCDatastore {

	private static final Logger logger = Logger.getLogger(JDBCDatastore.class.getName());

	private static final String K_ID = "ID";

	private static final String TABLE_PERSISTENT_EVENT = "persistentevent";
	private static final String K_RULEID = "RULEID";
	private static final String K_DATEEND = "DATEEND";
	private static final String K_DATESTART = "DATESTART";

	private static final String TABLE_PERSISTENT_TERM = "persistentterm";
	private static final String K_VALUE = "VALUE";
	private static final String K_PROPERTIES_ID_OWN = "PROPERTIES_ID_OWN";
	private static final String K_PROPERTIES_INTEGER_IDX = "PROPERTIES_INTEGER_IDX";

	protected Connection connection;
	protected Statement statement;
	protected PreparedStatement insertEventStatement;
	protected PreparedStatement insertTermStatement;

	public JDBCDatastore(Connection connection) throws SQLException {
		this.connection = connection;
		connection.setAutoCommit(false);
		statement = connection.createStatement();
		createTables();
		createPrparedStatements();
	}

	protected void createTables() throws SQLException {
		String q1 = new CreateTableStatement(TABLE_PERSISTENT_TERM)
			.addColumn(K_ID, "bigint(20) NOT NULL AUTO_INCREMENT")
			.addColumn(K_VALUE, "varchar(256) DEFAULT NULL")
			.addColumn(K_PROPERTIES_ID_OWN, "bigint(20) DEFAULT NULL")
			.addColumn(K_PROPERTIES_INTEGER_IDX, "int(20) DEFAULT NULL")
			.addPrimaryKey(K_ID)
			.addForeignKey(K_PROPERTIES_ID_OWN, TABLE_PERSISTENT_TERM, K_ID)
			.toString();

		String q2 = new CreateTableStatement(TABLE_PERSISTENT_EVENT)
			.addColumn(K_ID, "bigint(20) NOT NULL")
			.addColumn(K_RULEID, "varchar(256) DEFAULT NULL")
			.addColumn(K_DATESTART, "timestamp DEFAULT NULL")
			.addColumn(K_DATEEND, "timestamp DEFAULT NULL")
			.addPrimaryKey(K_ID)
			.addForeignKey(K_ID, TABLE_PERSISTENT_TERM, K_ID)
			.toString();

		logger.info(q1);
		statement.executeUpdate(q1);
		logger.info(q2);
		statement.executeUpdate(q2);
	}

	protected void createPrparedStatements() throws SQLException {
		insertEventStatement = connection.prepareStatement(String.format(
			"INSERT INTO `%s` (`%s`, `%s`, `%s`, `%s`) VALUES (?, ?, ?, ?);",  TABLE_PERSISTENT_EVENT, K_DATEEND, K_DATESTART, K_RULEID, K_ID
		), Statement.RETURN_GENERATED_KEYS);

		insertTermStatement = connection.prepareStatement(String.format(
			"INSERT INTO `%s` (`%s`, `%s`, `%s`) VALUES (?, ?, ?);", TABLE_PERSISTENT_TERM, K_VALUE, K_PROPERTIES_ID_OWN, K_PROPERTIES_INTEGER_IDX
		), Statement.RETURN_GENERATED_KEYS);
	}

	private long insertTerm(String value, Long parentId, Integer idx) throws SQLException {
		insertTermStatement.setString(1, value);
		if (parentId != null) {
			insertTermStatement.setLong(2, parentId);
		}
		else {
			insertTermStatement.setNull(2, Types.BIGINT);
		}

		if (idx != null) {
			insertTermStatement.setInt(3, idx);
		}
		else {
			insertTermStatement.setNull(3, Types.INTEGER);			
		}
	
		insertTermStatement.executeUpdate();

		ResultSet set = insertTermStatement.getGeneratedKeys();
		set.next();

		return set.getLong(1);		
	}

	private void insertEvent(String rid, Timestamp starts, Timestamp ends, Long id) throws SQLException {
		insertEventStatement.setTimestamp(1, ends);
		insertEventStatement.setTimestamp(2, starts);
		insertEventStatement.setString(3, rid);
		insertEventStatement.setLong(4, id);
		insertEventStatement.executeUpdate();
	}

	public void close() throws SQLException {
		if (statement != null) {
			try {
				statement.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (insertEventStatement != null) {
			try {
				insertEventStatement.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (insertTermStatement != null) {
			try {
				insertTermStatement.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void persist(EtalisEvent event) throws SQLException {
		long id = insertTerm(event.getName(), null, null);
		int idx = 0;
		for (Term t : event.getTerms()) {
			insertTerm(t.getPrologString(), id, idx++);
		}
		
		insertEvent(event.getRuleID(), new Timestamp(event.getTimeStarts().getTime()), new Timestamp(event.getTimeEnds().getTime()), id);
	}
}

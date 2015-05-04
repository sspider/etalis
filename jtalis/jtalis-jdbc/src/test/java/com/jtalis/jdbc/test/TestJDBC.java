package com.jtalis.jdbc.test;


import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import com.jtalis.core.JtalisContextImpl;
import com.jtalis.core.event.EtalisEvent;
import com.jtalis.core.plengine.JPLEngineWrapper;
import com.jtalis.core.plengine.PrologEngineWrapper;
import com.jtalis.jdbc.JDBCOutputProvider;

import junit.framework.TestCase;

public class TestJDBC extends TestCase {
	
	private Connection connection;
	private String user;
	private String pwd;
	private String url;
	
	private static final String K_ID = "ID";

	private static final String TABLE_PERSISTENT_EVENT = "persistentevent";

	private static final String TABLE_PERSISTENT_TERM = "persistentterm";
	private static final String K_VALUE = "VALUE";
	private static final String K_RULEID = "RULEID";
	
	protected PreparedStatement queryEvent;
	protected PreparedStatement queryRuleID;
	
	
	public void testJtalisJDBC() {
		// configure the database 
		user = "sa";
		pwd = "";
		url = "jdbc:h2:file:~/jdbc-test-seq-events";
		
		
		try {
			// connect the database
			JDBCOutputProvider jdbcOutputProvider = new JDBCOutputProvider(url, user, pwd, 0);
			long delay = 500;
			
			// configure Etalis engine and rule pattern.
			PrologEngineWrapper<?> engine = new JPLEngineWrapper();
			JtalisContextImpl context = new JtalisContextImpl(engine);
			context.setEtalisFlags("save_ruleId", "on");
			context.registerOutputProvider(jdbcOutputProvider);			
			context.addEventTrigger("_/_");
			context.addDynamicRuleWithId("r1", "c(X) <- a(X) seq b(X)");
			
			// feed Etalis events
			context.pushEvent(new EtalisEvent("a", 1));
			context.pushEvent(new EtalisEvent("b", 1));
			Thread.sleep(delay);
			
			context.waitForInputProviders();
			context.shutdown();
			
			// Query the database and compare with expect
			Properties props = new Properties();
			props.put("user", user);
			props.put("password", pwd);
			
			// Query the persistent_term table
			connection = DriverManager.getConnection(url, props);
			queryEvent = connection.prepareStatement(String.format(
					"SELECT `%s` FROM `%s` WHERE `%s` = ?;", K_VALUE, TABLE_PERSISTENT_TERM, K_ID));
			queryEvent.setInt(1, 1);
			ResultSet set = queryEvent.executeQuery();
			set.next();
			
			assertEquals("a", set.getString(1));			
			set.close();
			
			queryEvent.setInt(1, 3);
			set = queryEvent.executeQuery();
			set.next();
			assertEquals("b", set.getString(1));
			set.close();
			
			queryEvent.setInt(1, 5);
			set = queryEvent.executeQuery();
			set.next();
			assertEquals("c", set.getString(1));
			set.close();
			
			// query the persistent_event table
			queryRuleID = connection.prepareStatement(String.format(
					"SELECT `%s` FROM `%s` WHERE `%s` = ?;", K_RULEID, TABLE_PERSISTENT_EVENT, K_ID));
			queryRuleID.setInt(1, 1);
			set = queryRuleID.executeQuery();
			set.next();
			assertEquals("r1", set.getString(1));			
			set.close();
			
			queryRuleID.setInt(1, 3);
			set = queryRuleID.executeQuery();
			set.next();
			assertEquals("r1", set.getString(1));
			set.close();
			
			queryRuleID.setInt(1, 5);
			set = queryRuleID.executeQuery();
			set.next();
			assertEquals("r1", set.getString(1));
			set.close();
			
			connection.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		
		
	}

}

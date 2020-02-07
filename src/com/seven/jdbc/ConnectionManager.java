package com.seven.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
	
	private static ConnectionManager instance = null;
	
	private Connection conn = null; 
	
	// In a real application, the below would be in a configuration file
	// NOT hard-coded
	private static final String USERNAME = "dbadmin";
	private static final String PASSWORD = "dbpassword";
	private static final String CONN_STRING = "jdbc:mysql://localhost/explorecalifornia";

	private ConnectionManager() {
		
	}
	
	public static ConnectionManager getInstance() {
		if (instance == null) {
			instance = new ConnectionManager();
		}
		
		return instance;
	}
	
	private boolean openConnection() {
		try {
			conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
			
			return true;
			
		} catch (SQLException e) {
			processException(e);
			
			return false;
		}
	}
	
	public Connection getConnection() throws SQLException {
		if (conn != null) {
			return conn;
		}
		
		if (openConnection()) {
			System.out.println("Connection opened");
			return conn;
		}
		
		return null;
	}
	
	public void close() {
		System.out.println("Closing connection");
		
		try {
			conn.close();
		} catch (SQLException e) {
			processException(e);
		}

		conn = null;
	}

	// Demonstrates JDBC and also vendor-specific error codes and states
	public static void processException(SQLException e) {
		System.err.println("Error message: " + e.getMessage());
		System.err.println("Error code: " + e.getErrorCode());
		System.err.println("Error state: " + e.getSQLState());
	}
}

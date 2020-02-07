package com.seven.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.seven.jdbc.ConnectionManager;
import com.seven.jdbc.Constants;

// DataAccessObject is our abstraction between JDBC and our application
public abstract class DataAccessObject <T extends DataTransferObject> {

	protected final Connection connection;
	protected final static String NEXT_ID = "SELECT AUTO_INCREMENT FROM INFORMATION_SCHEMA.TABLES"; 
	
	public DataAccessObject(Connection connection) {
		super();
		this.connection = connection;
	}
	
	public abstract T findById(long id);
	public abstract List<T> findAll();
	public abstract T update(T dto);
	public abstract T create(T dto);
	public abstract void delete(T dto);
	
	protected long getNextId(String tableName) {
		String sql = NEXT_ID + " WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ?";

		try (PreparedStatement stmt = connection.prepareStatement(sql);) {
			stmt.setString(1, Constants.DATABASE_NAME);
			stmt.setString(2, tableName);
			ResultSet rs = stmt.executeQuery();
			
			return rs.getLong(1);
			
		} catch (SQLException e) {
			ConnectionManager.processException(e);
			throw new RuntimeException(e);
		}
	}
}

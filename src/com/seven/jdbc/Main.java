package com.seven.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.seven.jdbc.beans.State;
import com.seven.jdbc.beans.Tour;
import com.seven.jdbc.dao.TourDAO;
import com.seven.jdbc.tables.States;
import com.seven.jdbc.tables.Tours;

public class Main {

	private static Connection conn;
	
	// Java7+ has try-with-resources
	public static void main(String[] args) throws SQLException {

		try {
			conn = ConnectionManager.getInstance().getConnection();
			
			doOpenConnection_Java6Style();
			doOpenConnection_TryWithResourcesStyle();
			
			doSelectStmt();
			doSelectStmtWithGenericGetters();
			doSelectStmtWithBoundParams();
			doSelectStmtIntoBeans();
			doSelectStoredProc();
			doSelectStoredProcWithOutputParams();
			
			doInsertStmt();
			doUpdateStmt();
			doUpdateAndRollback();
			doUpdateAndCommit();
			doDeleteStmt();
			
			doListDbTables();
			
			// BEGIN - Using DAO pattern
			doInsertUsingDAOPattern();
			// END - Using DAO patter
			
			ConnectionManager.getInstance().close();
			
		} catch (SQLException e) {
			ConnectionManager.processException(e);
		}
	}
	
	// Java6 style, before try-with-resources.
	// Opens a connection and executes a simple SELECT statement
	private static void doOpenConnection_Java6Style() throws SQLException {
		
		System.out.println("doOpenConnection_Java6Style");
		
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery("SELECT STATEID, STATENAME FROM STATES");

			rs.last();
			int numRows = rs.getRow();
			System.out.println("Number of rows: " + numRows);

		} catch (SQLException e) {
			ConnectionManager.processException(e);
		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
		}
		
		System.out.println("#doOpenConnection_Java6Style");
		System.out.println();
	}

	// Java7 style, using try-with-resources.
	// Opens a connection and executes a simple SELECT statement
	private static void doOpenConnection_TryWithResourcesStyle() {
		
		System.out.println("doOpenConnection_TryWithResourcesStyle");

		try (Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = stmt.executeQuery("SELECT STATEID, STATENAME FROM STATES");) {

			rs.last();
			int numRows = rs.getRow();
			System.out.println("Number of rows: " + numRows);

		} catch (SQLException e) {
			ConnectionManager.processException(e);
		}
		
		System.out.println("#doOpenConnection_TryWithResourcesStyle");
		System.out.println();
	}

	// Simple SELECT statement without any bound parameters
	private static void doSelectStmt() throws SQLException {

		System.out.println("doSelectStmt");

		ResultSet rs = null;

		try (Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);) {

			String sql = SqlStatements.getAllStatesSql();
			rs = stmt.executeQuery(sql);
			States.displayData(rs);

		} catch (SQLException e) {
			ConnectionManager.processException(e);
		} finally {
			if (rs != null)
				rs.close();
		}

		System.out.println("#doSelectStmt");
		System.out.println();
	}

	// Simple SELECT statement, demonstrating Java7 generic value getters
	// i.e. ResultSet.getObject() instead of ResultSet.getInt(), ResultSet.getString(), etc.
	private static void doSelectStmtWithGenericGetters() throws SQLException {

		System.out.println("doSelectStmtWithGenericGetters");
		
		ResultSet rs = null;

		try (Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);) {

			String sql = SqlStatements.getAllToursSql();
			rs = stmt.executeQuery(sql);
			Tours.displayDataWithGenericGetters(rs);

		} catch (SQLException e) {
			ConnectionManager.processException(e);
		} finally {
			if (rs != null)
				rs.close();
		}

		System.out.println("#doSelectStmtWithGenericGetters");
		System.out.println();
	}

	// Executes a SELECT statement with a bound parameter
	private static void doSelectStmtWithBoundParams() throws SQLException {

		System.out.println("doSelectStmtWithBoundParams");
		
		final double maxPrice = 150.0;

		String sql = SqlStatements.getToursByMaxPrice();
		ResultSet rs = null;

		try (PreparedStatement prepStmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);) {

			prepStmt.setDouble(1, maxPrice);
			rs = prepStmt.executeQuery();
			Tours.displayData(rs);

		} catch (SQLException e) {
			ConnectionManager.processException(e);
		} finally {
			if (rs != null)
				rs.close();
		}

		System.out.println("#doSelectStmtWithBoundParams");
		System.out.println();
	}
	
	// Executes a SELECT statement, and places the results into a collection
	// of JavaBeans
	private static void doSelectStmtIntoBeans() throws SQLException {

		System.out.println("doSelectStmtIntoBeans");
		
		String sql = SqlStatements.getAllStatesSql();
		
		try (Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = stmt.executeQuery(sql);
				) {
			
			List<State> stateList = new ArrayList<State>();
			
			while (rs.next()) {
				State state = new State();
				state.setStateId(rs.getString("stateId"));
				state.setStateName(rs.getString("stateName"));
				
				stateList.add(state);
			}
			
			for (State state : stateList) {
				state.display();
			}
			
			System.out.println();
		}
		
		System.out.println("#doSelectStmtIntoBeans");
		System.out.println();
	}

	// Executes a stored procedure with only IN parameters (wrapping a SELECT
	// statement).
	private static void doSelectStoredProc() throws SQLException {

		System.out.println("doStoredProcSelect");
		
		final double maxPrice = 150.0;

		String sql = SqlStatements.getToursByMaxPriceStoredProc();
		ResultSet rs = null;

		try (CallableStatement storedProcStmt = conn.prepareCall(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);) {

			storedProcStmt.setDouble(1, maxPrice);
			rs = storedProcStmt.executeQuery();
			Tours.displayData(rs);

		} catch (SQLException e) {

			ConnectionManager.processException(e);

		} finally {
			if (rs != null) {
				rs.close();
			}
		}
		
		System.out.println("#doStoredProcSelect");
		System.out.println();
	}

	// Executes a stored procedure with IN and OUT parameters (wrapping a SELECT
	// statement).
	private static void doSelectStoredProcWithOutputParams() throws SQLException {

		System.out.println("doStoredProcSelectWithOutputParams");
		
		final double maxPrice = 150.0;

		String sql = SqlStatements.getToursAndCountByMaxPriceStoredProc();
		ResultSet rs = null;

		try (CallableStatement storedProcStmt = conn.prepareCall(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);) {

			storedProcStmt.setDouble(1, maxPrice);
			storedProcStmt.registerOutParameter("total", Types.INTEGER);
			rs = storedProcStmt.executeQuery();
			Tours.displayData(rs);
			int numTours = storedProcStmt.getInt("total");
			System.out.println(
					"#Stored proc getToursAndCountByMaxPrice(maxPrice: " + maxPrice + ", numTours: " + numTours + ")");

		} catch (SQLException e) {

			ConnectionManager.processException(e);

		} finally {
			if (rs != null) {
				rs.close();
			}
		}
		
		System.out.println("#doStoredProcSelectWithOutputParams");
		System.out.println();
	}

	// Executes an INSERT statement
	private static void doInsertStmt() throws SQLException {

		System.out.println("doInsertStmt");
		
		String sql = SqlStatements.getInsertTourSql();
		ResultSet rs = null;

		try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

			Tour tour = Tour.createDummyTourBean();
			SqlStatements.setInsertTourSqlParams(stmt, tour);
			
			int numRowsInserted = stmt.executeUpdate();
			
			if (numRowsInserted == 1) {
				rs = stmt.getGeneratedKeys();
				rs.next();
				int newKey = rs.getInt(1);

				System.out.println("New tourId: " + newKey);
				tour.setTourId(newKey);

			} else {
				System.err.println("Row *not* inserted");
			}

		} catch (SQLException e) {
			ConnectionManager.processException(e);
		} finally {
			if (rs != null)
				rs.close();
		}
		
		System.out.println("#doInsertStmt");
		System.out.println();
	}

	// Executes an UPDATE statement
	private static void doUpdateStmt() {
		
		System.out.println("doUpdateStmt");
		
		String sql = SqlStatements.getUpdateTourByTourNameSql();

		try (PreparedStatement stmt = conn.prepareStatement(sql);) {

			Tour tour = Tour.createDummyTourBean();
			SqlStatements.setUpdateTourByTourNameSqlParams(stmt, tour);
			
			int numRowsUpdated = stmt.executeUpdate();
			System.out.println("Number of rows updated: " + numRowsUpdated);

		} catch (SQLException e) {
			ConnectionManager.processException(e);
		}
		
		System.out.println("#doUpdateStmt");
		System.out.println();		
	}

	// Executes an UPDATE statement in a transaction, and rolls back
	private static void doUpdateAndRollback() {

		System.out.println("doUpdateAndRollback");
				
		String sql = SqlStatements.getUpdateTourByTourNameSql();

		try (PreparedStatement stmt = conn.prepareStatement(sql);) {

			conn.setAutoCommit(false);

			Tour tour = Tour.createDummyTourBean();
			SqlStatements.setUpdateTourByTourNameSqlParams(stmt, tour);
			
			int numRowsUpdated = stmt.executeUpdate();
			System.out.println("Number of rows updated: " + numRowsUpdated);
			
			conn.rollback();

		} catch (SQLException e) {
			ConnectionManager.processException(e);
		}
		
		System.out.println("#doUpdateAndRollback");
		System.out.println();		
	}

	// Executes an UPDATE statement in a transaction, and commits
	private static void doUpdateAndCommit() {
		
		System.out.println("doUpdateAndCommit");
		
		String sql = SqlStatements.getUpdateTourByTourNameSql();

		try (PreparedStatement stmt = conn.prepareStatement(sql);) {

			conn.setAutoCommit(false);

			Tour tour = Tour.createDummyTourBean();
			SqlStatements.setUpdateTourByTourNameSqlParams(stmt, tour);
			
			int numRowsUpdated = stmt.executeUpdate();
			System.out.println("Number of rows updated: " + numRowsUpdated);
			
			conn.commit();

		} catch (SQLException e) {
			ConnectionManager.processException(e);
		}
		
		System.out.println("#doUpdateAndCommit");
		System.out.println();		
	}

	// Executes a DELETE statement
	private static void doDeleteStmt() {

		System.out.println("doDeleteStmt");
		
		String sql = SqlStatements.getDeleteTourByTourNameSql();

		try (PreparedStatement stmt = conn.prepareStatement(sql);) {

			Tour tour = Tour.createDummyTourBean();
			SqlStatements.setDeleteTourByTourNameSqlParams(stmt, tour);
			
			int numRowsDeleted = stmt.executeUpdate();
			System.out.println("Number of rows deleted: " + numRowsDeleted);

		} catch (SQLException e) {
			ConnectionManager.processException(e);
		}
		
		System.out.println("#doDeleteStmt");
		System.out.println();		
	}

	// Fetches a list of tables for a database.
	private static void doListDbTables() throws SQLException {
		
		System.out.println("doListDbTables");

		final String[] tableTypes = { "TABLE" };
		final String TABLES_TABLE_NAME = "TABLE_NAME";
		final String COLUMNS_COLUMN_NAME = "COLUMN_NAME";
		final String COLUMNS_TYPE_NAME = "TYPE_NAME";

		ResultSet rsTables = null; 
		ResultSet rsColumns = null;
		
		try {
			DatabaseMetaData metaData = conn.getMetaData();
			rsTables = metaData.getTables(null, "%", "%", tableTypes);
			List<String> tableNames = new ArrayList<String>();
			
			while (rsTables.next()) {
				String tableName = rsTables.getString(TABLES_TABLE_NAME);
				tableNames.add(tableName);
			}
			
			for (String tableName : tableNames) {
				System.out.println("Table: " + tableName);
				System.out.println("-------------------------------------------");
				
				rsColumns = metaData.getColumns(null, "%", tableName, "%");
				
				while (rsColumns.next()) {
					StringBuffer buf = new StringBuffer();
					
					buf.append(rsColumns.getString(COLUMNS_COLUMN_NAME));
					buf.append(": ");
					buf.append(rsColumns.getString(COLUMNS_TYPE_NAME));
					
					System.out.println(buf.toString());
				}
				
				System.out.println();
			}
			
		} catch (SQLException e) {
			ConnectionManager.processException(e);
		} finally {
			if (rsTables != null) {
				rsTables.close();
			}
			if (rsColumns != null) {
				rsColumns.close();
			}
		}
		
		System.out.println("#doListDbTables");
		System.out.println("");
	}

	// Does an insert using DAO/DTO pattern
	private static void doInsertUsingDAOPattern() throws SQLException {

		System.out.println("doInsertUsingDAOPattern");

		conn.setAutoCommit(false);
		
		TourDAO tourDAO = new TourDAO(conn);
		com.seven.jdbc.dao.Tour tour = com.seven.jdbc.dao.Tour.createDummyTour();
		tourDAO.create(tour);

		conn.rollback();

		System.out.println("#doInsertUsingDAOPattern");
		System.out.println();
	}

}

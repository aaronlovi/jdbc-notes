package com.seven.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.seven.jdbc.beans.Tour;

public class SqlStatements {

	public static final int UPDATE_TOUR_BY_NAME_NEW_LENGTH = 2;

	public static String getAllStatesSql() {
		return "SELECT STATEID, STATENAME FROM STATES";
	}
	
	public static String getAllToursSql() {
		return "SELECT TOURID, TOURNAME, PRICE FROM TOURS";
	}
	
	public static String getToursByMaxPrice() {
		return "SELECT TOURID, TOURNAME, PRICE FROM TOURS WHERE PRICE <= ?";
	}
	
	public static String getToursByMaxPriceStoredProc() {
		return "{call GetToursByPrice(?)}";
	}

	public static String getToursAndCountByMaxPriceStoredProc() {
		return "{call GetToursWithCountByPrice(?, ?)}";
	}

	public static String getInsertTourSql() {
		return "INSERT INTO TOURS"
				+ " (PACKAGEID,TOURNAME,BLURB,DESCRIPTION,PRICE,DIFFICULTY,GRAPHIC,LENGTH,REGION,KEYWORDS)"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?)";
	}
	
	public static void setInsertTourSqlParams(PreparedStatement stmt, Tour tour) throws SQLException {
		stmt.setInt(1, tour.getPackageId());
		stmt.setString(2, tour.getTourName());
		stmt.setString(3, tour.getBlurb());
		stmt.setString(4, tour.getDescription());
		stmt.setDouble(5, tour.getPrice());
		stmt.setString(6, tour.getDifficulty());
		stmt.setString(7, tour.getGraphic());
		stmt.setInt(8, tour.getLength());
		stmt.setString(9, tour.getRegion());
		stmt.setString(10, tour.getKeywords());
	}
		
	public static String getUpdateTourByTourNameSql() {
		return "UPDATE TOURS SET LENGTH = " + UPDATE_TOUR_BY_NAME_NEW_LENGTH + " WHERE TOURNAME = ?";
	}
	
	public static void setUpdateTourByTourNameSqlParams(PreparedStatement stmt, Tour tour) throws SQLException {
		stmt.setString(1, tour.getTourName());
	}

	public static String getDeleteTourByTourNameSql() {
		return "DELETE FROM TOURS WHERE TOURNAME = ?";
	}
	
	public static void setDeleteTourByTourNameSqlParams(PreparedStatement stmt, Tour tour) throws SQLException {
		stmt.setString(1, tour.getTourName());
	}
}

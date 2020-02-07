package com.seven.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.seven.jdbc.ConnectionManager;
import com.seven.jdbc.SqlStatements;

public class TourDAO extends DataAccessObject<Tour> {

	public TourDAO(Connection connection) {
		super(connection);
	}

	@Override
	public Tour findById(long id) {
		return null;
	}

	@Override
	public List<Tour> findAll() {
		return null;
	}

	@Override
	public Tour update(Tour dto) {
		return null;
	}

	@Override
	public Tour create(Tour dto) {
		String sql = SqlStatements.getInsertTourSql();
		
		try(PreparedStatement stmt = connection.prepareStatement(sql);) {
			stmt.setInt(1, dto.getPackageId());
			stmt.setString(2, dto.getTourName());
			stmt.setString(3, dto.getBlurb());
			stmt.setString(4, dto.getDescription());
			stmt.setDouble(5, dto.getPrice());
			stmt.setString(6, dto.getDifficulty());
			stmt.setString(7, dto.getGraphic());
			stmt.setInt(8, dto.getLength());
			stmt.setString(9, dto.getRegion());
			stmt.setString(10, dto.getKeywords());

			stmt.execute();
			
			return null;
			
		} catch (SQLException e) {
			ConnectionManager.processException(e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void delete(Tour dto) {

	}

}

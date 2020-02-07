package com.seven.jdbc.tables;

import java.sql.ResultSet;
import java.sql.SQLException;

public class States {
	public static void displayData(ResultSet rs) throws SQLException {
		while (rs.next()) {
			String str = rs.getString("stateId") + ": " + rs.getString("stateName");
			System.out.println(str);
		}
		
		System.out.println();
	}
}

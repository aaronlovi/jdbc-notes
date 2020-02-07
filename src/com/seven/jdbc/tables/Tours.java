package com.seven.jdbc.tables;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;

public class Tours {

	public static void displayData(ResultSet rs) throws SQLException {
		rs.last();
		int numRows = rs.getRow();

		if (numRows == 0) {
			System.out.println("No tours were found");
			return;
		}

		rs.beforeFirst();
		while (rs.next()) {
			StringBuffer sb = new StringBuffer();

			sb.append("Tour " + rs.getInt("tourId") + ": ");
			sb.append(rs.getString("tourName"));

			double price = rs.getDouble("price");
			NumberFormat nf = NumberFormat.getCurrencyInstance();
			String formattedPrice = nf.format(price);
			sb.append(" (" + formattedPrice + ")");

			System.out.println(sb.toString());
		}
		
		System.out.println("Number of tours: " + numRows);
		System.out.println();
	}

	public static void displayDataWithGenericGetters(ResultSet rs) throws SQLException {
		rs.last();
		int numRows = rs.getRow();

		if (numRows == 0) {
			System.out.println("No tours were found");
			return;
		}

		rs.beforeFirst();
		while (rs.next()) {
			StringBuffer sb = new StringBuffer();

			int tourId = rs.getObject("TOURID", Integer.class);
			String tourName = rs.getObject("TOURNAME", String.class);
			BigDecimal price = rs.getObject("PRICE", BigDecimal.class);
			
			sb.append("Tour " + tourId + ": ");
			sb.append(tourName);

			NumberFormat nf = NumberFormat.getCurrencyInstance();
			String formattedPrice = nf.format(price);
			sb.append(" (" + formattedPrice + ")");

			System.out.println(sb.toString());
		}
		
		System.out.println("Number of tours: " + numRows);
		System.out.println();
	}
}

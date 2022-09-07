package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBconn {

	private static Connection dbConn = null;

	public static Connection getDBConn() {

		return dbConn;
	}

	public static void connect() {

		try {

			String url = "jdbc:mysql://localhost/accountbook?serverTimezone=UTC";

			String db = "com.mysql.cj.jdbc.Driver";

			String id = "cmy";
			String pw = "moon";

			Class.forName(db);

			dbConn = DriverManager.getConnection(url, id, pw);

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	public static void close(PreparedStatement pstmt, ResultSet rs) {

		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}

		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}
	}

	public static void close() throws SQLException {
		if (dbConn != null) {
			dbConn.close();
		}
		dbConn = null;
	}

}
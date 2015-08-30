package com.zj.platform.gamecenter.utils.autoGen.oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBUtils {

	private static String pwd = "game";

	private static String url = "jdbc:oracle:thin:@localhost:1521:orcl";

	private static String user = "root";

	private static String driverClass = "oracle.jdbc.driver.OracleDriver";

	public static void closeConn(Connection conn, Statement st, ResultSet rs) {
		try {
			if (conn != null) {
				conn.close();
			}
			if (st != null) {
				st.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Connection getConn() {
		Connection conn = null;
		try {
			Class.forName(driverClass);
			conn = DriverManager.getConnection(url, user, pwd);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
}

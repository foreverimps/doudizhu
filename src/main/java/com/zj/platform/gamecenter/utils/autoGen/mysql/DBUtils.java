package com.zj.platform.gamecenter.utils.autoGen.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBUtils {

	private static String pwd = "game123";

	private static String url = "jdbc:mysql://192.168.0.144/game?useUnicode=true&&characterEncoding=utf-8&autoReconnect = true";

	private static String user = "root";

	private static String driverClass = "com.mysql.jdbc.Driver";

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

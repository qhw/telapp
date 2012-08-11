package com.cn.telapp.sql;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class DAOHelper {

	private String url = null;
	private String user = null;
	private String passwd = null;
	private Statement stmt = null;
	private Connection conn = null;

	public DAOHelper() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
		}

		url = "jdbc:mysql://219.245.92.195:3306/telapp";
		user = "root";
		passwd = "192052";

		try {
			// conn= (Connection) DriverManager.getConnection(url, user,
			// passwd); //Á´½ÓÊý¾Ý¿â
			
			url = "jdbc:mysql://219.245.92.195:3306/telapp?user=root&password=192052&useUnicode=true&characterEncoding=utf8";
			conn = (Connection) DriverManager.getConnection(url);
			stmt = (Statement) conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean execute(String sql) {
		try {
			stmt.execute(sql);
			return true;
		} catch (SQLException e) {
			// TODO: handle exception
			return false;
		}
	}

	public ResultSet query(String sql) {
		try {
			return stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public int update(String sql) {
		try {
			return stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

	public void closeConn() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void closeState() {
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void closeRs(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

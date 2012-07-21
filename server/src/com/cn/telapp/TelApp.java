package com.cn.telapp;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

import com.mysql.jdbc.Connection;


public class TelApp {
	
	
	private String url = null;
	private String user = null;
	private String passwd = null;
	private Statement stmt = null;
	private Connection conn = null;
	
	
	public TelApp() throws SQLException
	{
		try {
        	Class.forName("com.mysql.jdbc.Driver");	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		url ="jdbc:mysql://219.245.92.83:3306/telapp";
		user = "root";
		passwd = "192052";
		
		try {
			conn= (Connection) DriverManager.getConnection(url, user, passwd); //Á´½ÓÊý¾Ý¿â
			stmt=(Statement) conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public String getShopInfo(String latitude, String longitude)
	{	 
		double lat = Double.parseDouble(latitude);
		double lng = Double.parseDouble(longitude);
		
		ArrayList<String> reslist = new ArrayList<String>();
		String sql = "select * from restaurant";
		ResultSet rSet = query(sql);
		
		try {
			while(rSet.next())
			{
				String restaurant ="";
				
				int distance = Distance.GetDistance(lat, lng,
						rSet.getDouble("reslat"), rSet.getDouble("reslong"));
				if (distance > 10000) continue;
				restaurant += rSet.getInt("id") +";";
				
				
				restaurant += rSet.getString("resname") +";";
				restaurant += rSet.getString("resimage") +";";
				restaurant += rSet.getString("resphone") +";";
				restaurant += rSet.getString("resaddr") +";";
				restaurant += rSet.getDouble("reslat") +";";
				restaurant += rSet.getDouble("reslong") +";";
				restaurant += rSet.getString("other") +";";
				restaurant += distance;
				reslist.add(restaurant);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Collections.sort(reslist, new ListComparator.MemberListComparator());
		
		JSONArray array = new JSONArray();
		for (String res : reslist)
		{
			Map map = new HashMap();
			String[] info = res.split(";");
			map.put("shopid", info[0]);
			map.put("shopname", info[1]);
			map.put("shopimg", info[2]);
			map.put("shopphone", info[3]);
			map.put("shopaddr", info[4]);
			map.put("shoplat", info[5]);
			map.put("shoplng", info[6]);
			map.put("shopother", info[7]);
			map.put("shopdistance", info[8]);
			array.put(map);
		}
		return array.toString();
	}
	
	@SuppressWarnings("unchecked")
	public String getShopMenu(int shopid)
	{
		JSONArray array = new JSONArray();
		String sql = "select * from menu where resid = " + shopid;
		ResultSet rSet = query(sql);
		try {
			while(rSet.next())
			{
				Map map = new HashMap();
				map.put("menuid", rSet.getInt("id"));
				map.put("shopid", rSet.getInt("resid"));
				map.put("menuname", rSet.getString("menuname"));
				map.put("menuimage", rSet.getString("menuimage"));
				map.put("menuprice", rSet.getFloat("menuprice"));
				array.put(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return array.toString();
	}
	

	public boolean execute(String sql)
	{
		try {
			return stmt.execute(sql);
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	
	
	public ResultSet query(String sql)
	{
		try {
			return stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public int update(String sql)
	{
		try {
			return stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	
	public void closeConn()
	{
		try {
			if (conn != null)
			{
				conn.close();
			} 
		}catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	public void closeState()
	{
		try {
			if (stmt != null)
			{
				stmt.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void closeRs(ResultSet rs)
	{
		try {
			if (rs != null)
			{
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] agrs)
	{
		TelApp telApp = null;
		
		try {
			telApp = new TelApp();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		String restaurants = telApp.getShopInfo("108.913", "34.234");
		System.out.println(".................start res..................");
		System.out.println(restaurants);
		System.out.println(".................end res..................");
		
		
		String menus = telApp.getShopMenu(1);
		System.out.println(menus);
		
	}

	//
}
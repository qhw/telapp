import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
			conn= (Connection) DriverManager.getConnection(url, user, passwd); //链接数据库
			stmt=(Statement) conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> getShopInfo(String latitude, String longitude)
	{
		double lat = Double.parseDouble(latitude);
		double lng = Double.parseDouble(longitude);
		
		ArrayList<String> reslist = new ArrayList<String>();
		ArrayList<Member> list = new ArrayList<TelApp.Member>();
		String sql = "select * from restaurant";
		ResultSet rSet = query(sql);
		try {
			while(rSet.next())
			{
				String restaurant ="";
				//int distance = new Distance().GetDistance(lat, lng,
				//		rSet.getDouble("reslat"), rSet.getDouble("reslong"));
				restaurant += rSet.getInt("id") +";";
				restaurant += rSet.getString("resname") +";";
				restaurant += rSet.getString("resimage") +";";
				restaurant += rSet.getString("resphone") +";";
				restaurant += rSet.getString("resaddr") +";";
				restaurant += rSet.getDouble("reslat") +";";
				restaurant += rSet.getDouble("reslong") +";";
				restaurant += rSet.getString("other") +";";
				//restaurant += distance;
				reslist.add(restaurant);
				//Member member = new Member(restaurant, distance);
				//list.add(member);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	/*	Collections.sort(list, new ListComparator().new MemberListComparator());
		for(Member m : list)
		{
			reslist.add(m.getContent());
			System.out.println(m.getDistance());
		}
		*/
		return reslist;
	}

	public List<String> getShopMenu(int shopid)
	{
		List<String> menulist = new ArrayList<String>();
		String sql = "select * from menu where resid = " + shopid;
		ResultSet rSet = query(sql);
		try {
			while(rSet.next())
			{
				String menu = "";
				menu += rSet.getInt("id") +";";
				menu += rSet.getInt("resid") + ";";
				menu += rSet.getString("menuname") +";";
				menu += rSet.getString("menuimage") +";";
				menu += rSet.getFloat("menuprice");
				menulist.add(menu);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return menulist;
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
	
	/*
	public static void main(String[] agrs)
	{
		List<String> restaurants = new ArrayList<String>();
		TelApp telApp = null;
		
		try {
			telApp = new TelApp();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		restaurants = telApp.getShopInfo("108.913", "34.234");
		System.out.println(".................start res..................");
		for (int i = 0; i < restaurants.size(); i++)
		{
			String[] restaurant = restaurants.get(i).split(";");
			System.out.println(restaurant[1] + "   " + restaurant[8]);
		}
		System.out.println(".................end res..................");
		
		List<String> menus = new ArrayList<String>();
		menus = telApp.getShopMenu(1);
		
		for (int k = 0; k < menus.size(); k++)
		{
			String[] menu = menus.get(k).split(";");
			System.out.println(menu[menu.length-1]);
		}
		
	}
	*/
	
	public class Member {

		public Member(String content, Integer distance)
		{
			this.content = content;
			this.distance = distance;
		}
		
		public Integer getDistance()
		{
			return distance;
		}
		
		public String getContent()
		{
			return content;
		}
		private String content;
		private Integer distance;
	}
	
	//
	public class ListComparator {
		
		public  class MemberListComparator implements Comparator{

			public int compare(Object o1, Object o2) {
				Member obj1 = (Member)o1;
				Member obj2 = (Member)o2;			
				return obj1.getDistance().compareTo(obj2.getDistance());
			}
		}
	}
	//
	public class Distance {

		private final  double EARTH_RADIUS = 6378137;//地球半径(米)
		private  double rad(double d)
		{
		   return d * Math.PI / 180.0;
		}
		//计算两个经纬度之间的距离
		public  int GetDistance(double lat1, double lng1, double lat2, double lng2)
		{
		   double radLat1 = rad(lat1);
		   double radLat2 = rad(lat2);
		   double a = radLat1 - radLat2;
		   double b = rad(lng1) - rad(lng2);
		   double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
		    Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
		   s = s * EARTH_RADIUS;
		   s = Math.round(s * 10000) / 10000;
		   return (int)s;
		}
		
	}
	//
}
package com.cn.shopapp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

import com.cn.telapp.common.Common;
import com.cn.telapp.common.Distance;
import com.cn.telapp.common.ListComparator;
import com.cn.telapp.sql.DAOHelper;

public class ShopApp {

	private DAOHelper daoHelper = null;
	private Common common = null;
	public ShopApp()
	{
		daoHelper = new DAOHelper();
		common = new Common();
	}
	
	
	//获取指定id的交易记录
	public String getBusiness(String businessid) {
		Map<String, String> map = null;
		JSONArray array = new JSONArray();
		String sql = "select * from business where id = " + businessid;
		ResultSet rSet = daoHelper.query(sql);
		try {
			if (rSet.next()) {
				map = new HashMap<String, String>();
				map.put("businessid", rSet.getString("id"));
				map.put("title", rSet.getString("title"));
				map.put("detail", rSet.getString("detail"));
				map.put("price", rSet.getString("price"));
				map.put("businessimg", rSet.getString("img"));
				map.put("linker", rSet.getString("linker"));
				map.put("phone", rSet.getString("phone"));
				map.put("qq", rSet.getString("qq"));
				map.put("pubtime", common.getPubTime(rSet.getString("pubtime")));
				map.put("flag", rSet.getString("flag"));
				array.put(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			map = null;
		}
		return array.toString();
	}
	
	
	//获取指定用户id的求购记录
	public String getMyBuy(String first, String pagesize, String userid) {
		int size = Integer.parseInt(pagesize);
		int firstPage = Integer.parseInt(first);
		Map<String, String> map = null;
		JSONArray array = new JSONArray();
		String sql = "select * from business where flag = 0 and " +
				"userid=" + userid +" order by pubtime desc";
		ResultSet rSet = daoHelper.query(sql);
		int length = 0;
		try {
			while (rSet.next()) {
				if (length >= size){
					break;
				}
				length++;
				if (length < firstPage) continue;
				map = new HashMap<String, String>();
				map.put("businessid", rSet.getString("id"));
				map.put("title", rSet.getString("title"));
				map.put("detail", rSet.getString("detail"));
				map.put("price", rSet.getString("price"));
				map.put("distance", "0");
				map.put("pubtime", common.getPubTime(rSet.getString("pubtime")));
				array.put(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			map = null;
		}
		return array.toString();
	}
	//获取范围内所有未结贴的求购记录
	public String getAllBuy(String first, String pagesize, String userid, String latitude, String longitude)
	{
		int size = Integer.parseInt(pagesize);
		int firstPage = Integer.parseInt(first);
		double lat = Double.parseDouble(latitude);
		double lng = Double.parseDouble(longitude);
		ArrayList<String> reslist = new ArrayList<String>();
		Map<String, String> map = null;
		JSONArray array = new JSONArray();
		String sql = "select business.id id, photo, title, detail, pubtime, price, lat, lng" +
				" from business, user where flag = 0 and over = 0 and user.id = " +
				"business.userid and business.userid !=" + userid;
		ResultSet rSet = daoHelper.query(sql);
		try {
			while (rSet.next()) {
				int dist = Distance.GetDistance(lat, lng, rSet
						.getDouble("lat"), rSet.getDouble("lng"));
				String business = "";
				business += rSet.getString("photo") + ";";
				business += rSet.getString("id") + ";";
				business += rSet.getString("title") + ";";
				business += rSet.getString("detail") + ";";
				business += rSet.getString("price") + ";";
				business += rSet.getString("lat") + ";";
				business += rSet.getString("lng") + ";";
				business += common.getPubTime(rSet.getString("pubtime")) + ";";
				business += dist;
				reslist.add(business);			
			}
		} catch (SQLException e) {
			e.printStackTrace();
			map = null;
		}
		Collections.sort(reslist, new ListComparator.MemberListComparator());

		int length = 0;
		for (String res : reslist) {
			if (length >= size){
				break;
			}
			length++;
			if (length < firstPage) continue;
			map = new HashMap<String, String>();
			String[] info = res.split(";");
			map.put("userimg", info[0]);
			map.put("businessid", info[1]);
			map.put("title", info[2]);
			map.put("detail", info[3]);
			map.put("price", info[4]);
			map.put("pubtime", info[7]);
			map.put("distance", info[8]);
			array.put(map);
		}
		return array.toString();
	}
	
	//
	//获取指定用户id的出售记录
	public String getMySell(String first, String pagesize, String userid) {
		int size = Integer.parseInt(pagesize);
		int firstPage = Integer.parseInt(first);
		Map<String, String> map = null;
		JSONArray array = new JSONArray();
		String sql = "select * from business where flag = 1 and " +
				"userid=" + userid +" order by pubtime desc";
		ResultSet rSet = daoHelper.query(sql);
		int length = 0;
		try {
			while (rSet.next()) {
				if (length >= size){
					break;
				}
				length++;
				if (length < firstPage) continue;
				map = new HashMap<String, String>();
				map.put("businessid", rSet.getString("id"));
				map.put("title", rSet.getString("title"));
				map.put("detail", rSet.getString("detail"));
				map.put("price", rSet.getString("price"));
				map.put("distance", "0");
				map.put("pubtime", common.getPubTime(rSet.getString("pubtime")));
				array.put(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			map = null;
		}
		return array.toString();
	}
	//获取范围内所有未结贴的出售记录
	public String getAllSell(String first, String pagesize, String userid, String latitude, String longitude)
	{
		int size = Integer.parseInt(pagesize);
		int firstPage = Integer.parseInt(first);
		double lat = Double.parseDouble(latitude);
		double lng = Double.parseDouble(longitude);
		Map<String, String> map = null;
		ArrayList<String> reslist = new ArrayList<String>();
		JSONArray array = new JSONArray();
		String sql = "select business.id id, photo, title, detail, price, pubtime, lat, lng" +
				" from business, user where flag = 1 and over = 0 and user.id = " +
				"business.userid and business.userid != " + userid;
		ResultSet rSet = daoHelper.query(sql);
		try {
			while (rSet.next()) {
				int dist = Distance.GetDistance(lat, lng, rSet
						.getDouble("lat"), rSet.getDouble("lng"));
				String business = "";
				business += rSet.getString("photo") + ";";
				business += rSet.getString("id") + ";";
				business += rSet.getString("title") + ";";
				business += rSet.getString("detail") + ";";
				business += rSet.getString("price") + ";";
				business += rSet.getString("lat") + ";";
				business += rSet.getString("lng") + ";";
				business += common.getPubTime(rSet.getString("pubtime")) + ";";
				business += dist;
				reslist.add(business);			
			}
		} catch (SQLException e) {
			e.printStackTrace();
			map = null;
		}
		Collections.sort(reslist, new ListComparator.MemberListComparator());

		int length = 0;
		for (String res : reslist) {
			if (length >= size){
				break;
			}
			length++;
			if (length < firstPage) continue;
			map = new HashMap<String, String>();
			String[] info = res.split(";");
			map.put("userimg", info[0]);
			map.put("businessid", info[1]);
			map.put("title", info[2]);
			map.put("detail", info[3]);
			map.put("price", info[4]);
			map.put("pubtime", info[7]);
			map.put("distance", info[8]);
			array.put(map);
		}
		return array.toString();
	}
	
	//添加求购或者出售信息
	public boolean addBusiness(Map map)
	{
		String pubtime = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss")
		.format(Calendar.getInstance().getTime()); 
		String sql = "insert into business(userid, title, detail, price, img," +
				" linker, phone, qq, pubtime, flag, over)values(" + map.get("userid").toString() +
				",'" + map.get("title").toString() +"','" + map.get("detail").toString() +"'," +
				map.get("price").toString() + ", '" + map.get("img").toString() + "','" +
				map.get("linker").toString() +"','" + map.get("phone").toString() + "','" + 
				map.get("qq").toString() + "','" + pubtime +"'," + map.get("flag").toString() + ",0)";
		
		boolean success = daoHelper.execute(sql);
		return success;
	}
	
	//结贴
	public boolean overBusiness(String businessid)
	{
		String sql = "update business set over = 1 where id = " + businessid;
		boolean success = daoHelper.execute(sql);
		return success;
	}
	//用户注册
	public String addUser(Map map)
	{
		String sql = "insert into user(username, password, photo, lat, lng, phone, qq) values('" +
				map.get("username").toString() + "','" + map.get("password").toString() + "','" + 
				map.get("userimg").toString() + "'," + map.get("lat").toString() + "," + map.get("lng").toString() +
				",'" + map.get("phone").toString() + "','" + map.get("qq").toString() + "')";
		boolean success = daoHelper.execute(sql);
		if (success){
			//推送用户表
			sql = "insert into apn_user(username, password) values('" +
			map.get("username").toString() + "','" + map.get("password").toString() +
			"')";
			daoHelper.execute(sql);
		}
		String value = "";
		if (success)
		{
			value = loginUser(map);
		}
		return value;
	}
	//更新用户信息
	public boolean updateUser(Map map)
	{
		String sql = "update user set photo='" + map.get("userimg").toString() + "', lat=" + map.get("lat").toString() + 
		", lng = " + map.get("lng").toString() + ",'" + map.get("phone").toString() + "', qq='" + map.get("qq").toString()+
		"' where id = " + map.get("userid").toString();
		boolean success = daoHelper.execute(sql);
		return success;
	}
	//用户登陆
	public String loginUser(Map map)
	{
		Map<String, String> person = null;
		JSONArray array = new JSONArray();
		String sql = "select * from user where username='" + map.get("username").toString() + 
					"' and password='" + map.get("password").toString() + "'";
		ResultSet rSet = daoHelper.query(sql);
		try {
			if (rSet.next()) {
				person = new HashMap<String, String>();
				person.put("userid", rSet.getString("id"));
				person.put("username", rSet.getString("username"));
				person.put("password", rSet.getString("password"));
				person.put("userimg", rSet.getString("photo"));
				person.put("phone", rSet.getString("phone"));
				person.put("qq", rSet.getString("qq"));
				person.put("isopenshop", rSet.getString("isopenshop"));
				person.put("lat", rSet.getString("lat"));
				person.put("lng", rSet.getString("lng"));

				array.put(person);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return array.toString();
	}
	//更新经纬度
	public boolean updatePosition(String userid, String lat, String lng)
	{
		String sql = "update user set lat = " + lat + ", lng = " + lng + " where id = " + userid;
		boolean success = daoHelper.execute(sql);
		return success;
	}
	
	//获取用户信息
	public String getUser(String userid)
	{
		Map<String, String> person = null;
		JSONArray array = new JSONArray();
		String sql = "select * from user where id=" + userid;
		ResultSet rSet = daoHelper.query(sql);
		try {
			if (rSet.next()) {
				person = new HashMap<String, String>();
				person.put("userid", rSet.getString("id"));
				person.put("username", rSet.getString("username"));
				person.put("userimg", rSet.getString("photo"));
				person.put("phone", rSet.getString("phone"));
				person.put("qq", rSet.getString("qq"));
				person.put("userimg", rSet.getString("photo"));
				array.put(person);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return array.toString();
	}
}

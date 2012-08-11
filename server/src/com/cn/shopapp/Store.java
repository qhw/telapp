package com.cn.shopapp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

import com.cn.telapp.common.Common;
import com.cn.telapp.common.Distance;
import com.cn.telapp.common.ListComparator;
import com.cn.telapp.sql.DAOHelper;

public class Store {

	private DAOHelper daoHelper = null;
	private Common common = null;
	public Store()
	{
		daoHelper = new DAOHelper();
		common = new Common();
	}
	
	//判断用户是否开通了店铺
	public String isOpenStore(String userid) {
		Map<String, String> map = null;
		JSONArray array = new JSONArray();
		String sql = "select * from user where id = " + userid;
		ResultSet rSet = daoHelper.query(sql);
		try {
			if (rSet.next()) {
				map = new HashMap<String, String>();
				map.put("isopenshop", rSet.getString("isopenshop"));
				array.put(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return array.toString();
	}
	//设置开通店铺
	private boolean setOpenStore(String userid)
	{
		String sql = "update user set isopenshop = 1 where id = " + userid;
		boolean success = daoHelper.execute(sql);
		return success;
	}
	//获取店铺信息
	public String getStore(String userid) {
		Map<String, String> map = null;
		JSONArray array = new JSONArray();
		String sql = "select * from shop where userid = " + userid;
		ResultSet rSet = daoHelper.query(sql);
		try {
			if (rSet.next()) {
				map = new HashMap<String, String>();
				map.put("shopid", rSet.getString("id"));
				map.put("shopname", rSet.getString("shopname"));
				map.put("shopdesc", rSet.getString("shopdesc"));
				map.put("shopaddr", rSet.getString("shopaddr"));
				map.put("shopimg", rSet.getString("shopimg"));
				map.put("linker", rSet.getString("linker"));
				map.put("phone", rSet.getString("phone"));
				map.put("qq", rSet.getString("qq"));
				array.put(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return array.toString();
	}
	
	//开通店铺
	public boolean openStore(Map map)
	{
		String sql = "insert into shop(userid, shopname, shopdesc, shopaddr, shopimg, linker, phone, qq, lat, lng) values(" +
			map.get("userid").toString() + ",'" + map.get("shopname").toString() + "','" + map.get("shopdesc").toString() +
			"','" + map.get("shopaddr").toString() + "','" + map.get("shopimg").toString() + "','" + map.get("linker").toString() +
			"','" + map.get("phone").toString() + "','" + map.get("qq").toString() + "'," + map.get("lat").toString() + "," + 
			map.get("lng").toString() + ")";
		boolean success = daoHelper.execute(sql);
		setOpenStore(map.get("userid").toString());
		return success;
	}
	
	//获取商品
	public String getItem(String itemid)
	{
		Map<String, String> map = null;
		JSONArray array = new JSONArray();
		String sql = "select * from shopitem where id = " + itemid;
		ResultSet rSet = daoHelper.query(sql);
		try {
			if (rSet.next()) {
				map = new HashMap<String, String>();
				map.put("itemname", rSet.getString("itemname"));
				map.put("itemdesc", rSet.getString("itemdesc"));
				map.put("itemprice", rSet.getString("itemprice"));
				map.put("itemimg", rSet.getString("itemimg"));
				array.put(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return array.toString();
	}
	//添加商品
	public boolean addItem(Map map)
	{
		String sql = "insert into shopitem(shopid, itemname, itemdesc, itemprice, itemimg)values(" +
		map.get("shopid").toString() + ",'" + map.get("itemname").toString() + "','" + map.get("itemdesc").toString() + 
		"'," + map.get("itemprice").toString() + ",'" + map.get("itemimg").toString() + "')";
		boolean success = daoHelper.execute(sql);
		return success;
	}
	
	//获取指定店id的商品
	public String getShopItem(String first, String pagesize, String shopid)
	{
		int size = Integer.parseInt(pagesize);
		int firstPage = Integer.parseInt(first);
		Map<String, String> map = null;
		JSONArray array = new JSONArray();
		String sql = "select * from shopitem where shopid = " + shopid;
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
				map.put("itemid", rSet.getString("id"));
				map.put("itemname", rSet.getString("itemname"));
				map.put("itemdesc", rSet.getString("itemdesc"));
				map.put("itemprice", rSet.getString("itemprice"));
				map.put("itemimg", rSet.getString("itemimg"));
				array.put(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return array.toString();
	}
	
	//获取指定范围内的所有店
	public String getAllStore(String first, String pagesize, String userid,  String latitude, String longitude)
	{
		int size = Integer.parseInt(pagesize);
		int firstPage = Integer.parseInt(first);
		double lat = Double.parseDouble(latitude);
		double lng = Double.parseDouble(longitude);
		Map<String, String> map = null;
		ArrayList<String> reslist = new ArrayList<String>();
		JSONArray array = new JSONArray();
		String sql = "select * from shop where userid != " + userid;
		ResultSet rSet = daoHelper.query(sql);
		try {
			while (rSet.next()) {
				int dist = Distance.GetDistance(lat, lng, rSet
						.getDouble("lat"), rSet.getDouble("lng"));
				String shop = "";
				shop += rSet.getString("userid") + ";";
				shop += rSet.getString("id") + ";";
				shop += rSet.getString("shopname") + ";";
				shop += rSet.getString("shopdesc") + ";";
				shop += rSet.getString("shopaddr") + ";";
				shop += rSet.getString("shopimg") + ";";
				shop += rSet.getString("linker") + ";";
				shop += rSet.getString("phone") + ";";
				shop += dist + ";";
				shop += rSet.getString("qq");
				reslist.add(shop);
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
			map.put("userid", info[0]);
			map.put("shopid", info[1]);
			map.put("shopname", info[2]);
			map.put("shopdesc", info[3]);
			map.put("shopaddr", info[4]);
			map.put("shopimg", info[5]);
			map.put("linker", info[6]);
			map.put("phone", info[7]);
			map.put("qq", info[9]);
			map.put("distance", info[8]);
			array.put(map);
		}
		return array.toString();
	}
}

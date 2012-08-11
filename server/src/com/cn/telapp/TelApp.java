package com.cn.telapp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;

import com.cn.telapp.common.Distance;
import com.cn.telapp.common.ListComparator;
import com.cn.telapp.sql.DAOHelper;

public class TelApp {

	private DAOHelper daoHelper = null;

	public TelApp() {
		daoHelper = new DAOHelper();
	}

	public String getShopInfo() {
		Map<String, String> map = null;
		JSONArray array = new JSONArray();
		String sql = "select * from restaurant";
		ResultSet rSet = daoHelper.query(sql);
		try {
			while (rSet.next()) {
				map = new HashMap<String, String>();
				map.put("shopid", rSet.getString("id"));
				map.put("shopname", rSet.getString("resname"));
				map.put("shopimg", rSet.getString("resimage"));
				map.put("shopphone", rSet.getString("resphone"));
				map.put("shopaddr", rSet.getString("resaddr"));
				map.put("shoplat", rSet.getString("reslat"));
				map.put("shoplng", rSet.getString("reslong"));
				map.put("shopother", rSet.getString("other"));
				array.put(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			map = null;
		}

		return array.toString();
	}

	public Map<String, String> getShopInfo(int shopid) {
		Map<String, String> map = null;
		String sql = "select * from restaurant where id =" + shopid;
		ResultSet rSet = daoHelper.query(sql);
		try {
			while (rSet.next()) {
				map = new HashMap<String, String>();
				map.put("shopid", rSet.getString("id"));
				map.put("shopname", rSet.getString("resname"));
				map.put("shopimg", rSet.getString("resimage"));
				map.put("shopphone", rSet.getString("resphone"));
				map.put("shopaddr", rSet.getString("resaddr"));
				map.put("shoplat", rSet.getString("reslat"));
				map.put("shoplng", rSet.getString("reslong"));
				map.put("shopother", rSet.getString("other"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			map = null;
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	public String getShopInfo(String first,String pagesize, String latitude, String longitude) {
		int size = Integer.parseInt(pagesize);
		int firstPage = Integer.parseInt(first);
		double lat = Double.parseDouble(latitude);
		double lng = Double.parseDouble(longitude);

		ArrayList<String> reslist = new ArrayList<String>();
		String sql = "select * from restaurant";
		ResultSet rSet = daoHelper.query(sql);

		int length = 0;
		try {
			while (rSet.next()) {
				String restaurant = "";
				int distance = Distance.GetDistance(lat, lng, rSet
						.getDouble("reslat"), rSet.getDouble("reslong"));
				restaurant += rSet.getInt("id") + ";";

				restaurant += rSet.getString("resname") + ";";
				restaurant += rSet.getString("resimage") + ";";
				restaurant += rSet.getString("resphone") + ";";
				restaurant += rSet.getString("resaddr") + ";";
				restaurant += rSet.getDouble("reslat") + ";";
				restaurant += rSet.getDouble("reslong") + ";";
				restaurant += rSet.getString("other") + ";";
				restaurant += distance;
				reslist.add(restaurant);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Collections.sort(reslist, new ListComparator.MemberListComparator());

		JSONArray array = new JSONArray();
		for (String res : reslist) {
			
			if (length >= size){
				break;
			}
			length++;
			if (length < firstPage) continue;
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
	public String getShopMenu(String first,String pagesize,int shopid) {
		int size = Integer.parseInt(pagesize);
		int firstPage = Integer.parseInt(first);
		JSONArray array = new JSONArray();
		String sql = "select * from menu where resid = " + shopid;
		ResultSet rSet = daoHelper.query(sql);
		int length =0;
		try {
			while (rSet.next()) {
				if (length >= size){
					break;
				}
				length++;
				if (length < firstPage) continue;
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

	public boolean addshops(Map<String, String> map) {
		String sql = "insert into restaurant(resname, resimage, resphone, resaddr, reslinker, reslong, reslat, other)"
				+ "values('"
				+ map.get("shopname")
				+ "','img/shops/"
				+ map.get("shopimg")
				+ "', '"
				+ map.get("shopphone")
				+ "', '"
				+ map.get("shopaddr")
				+ "','"
				+ map.get("shoplinker")
				+ "',"
				+ map.get("shoplng")
				+ ","
				+ map.get("shoplat")
				+ ", '"
				+ map.get("shopother") + "')";
		boolean flag = daoHelper.execute(sql);
		return flag;
	}

	public boolean addmenus(Map<String, String> map) {
		String sql = "insert into menu(resid, menuname, menuimage, menuprice)"
				+ "values(" + map.get("shopid") + ",'" + map.get("menuname")
				+ "','img/menus/" + map.get("menuimg") + "',"
				+ map.get("menuprice") + ")";
		boolean flag = daoHelper.execute(sql);
		return flag;
	}
	//
}
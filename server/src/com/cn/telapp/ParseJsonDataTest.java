package com.cn.telapp;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ParseJsonDataTest {

	public  void parseJsonArray(String json)
	{
		 //根据字符串生成JSON对象
		 JSONArray resultArray = null;
		try {
			resultArray = new JSONArray(json);
			 JSONObject resultObj = resultArray.optJSONObject(0);
			 
			 //获取数据项
			 String username = resultObj.getString("username");
			 
			 //获取数据对象
			 JSONObject user = resultObj.getJSONObject("user_json");
			 String nickname = user.getString("nickname");
			 System.out.println(username);
			 System.out.println(nickname);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public static String jsonToString()
	{
		//创建JSONObject对象
        JSONObject json = new JSONObject();
        JSONArray num = new JSONArray();
        //向json中添加数据
        try {
			json.put("username", "wanglihong");
	        json.put("height", 12.5);
	        json.put("age", 24);
	        
	        Map map = new HashMap();
	        map.put("aa", 10);
	        map.put("bb", 20);
	        num.put(map);
	        json.put("cc", num);
		} catch (JSONException e) {
			e.printStackTrace();
		}
        
        //创建JSONArray数组，并将json添加到数组
        JSONArray array = new JSONArray();
        array.put(json);
        array.put(num);
        
        //转换为字符串
        String jsonStr = array.toString();
        
        System.out.println(jsonStr);
        return jsonStr;
	}
}

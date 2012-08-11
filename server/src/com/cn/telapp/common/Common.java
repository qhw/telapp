package com.cn.telapp.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {

	//把发布的时间显示为 几天前 几小时前 几分钟前
	public String getPubTime(String pubtime)
	{
		long diff = 0;
		try {
			diff = new Date().getTime() - new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(pubtime).getTime();
			return TimeFormat(diff/1000, pubtime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String TimeFormat(long sum, String pubtime)
	{
		String value = "";
		if (sum < 1) {
			value = "刚刚";
			return value;
		}
		if (sum < 60) {
			value = sum + "秒前";
			return value;
		}
		if (sum < 3600) {
			value = sum/60 + "分钟前";
			return value;
		}
		if (sum < 86400) {
			value = sum/3600 + "小时前";
			return value;
		}
		if (sum < 432000) {
			value = sum / 86400 + "天前";
			return value;
		}
		
		long aa;
		try {
			aa = new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(pubtime).getTime();
			String str = new SimpleDateFormat("yyyy年MM月dd日").format(new Date(aa));
			return str;
		
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}

package com.cn.telapp;

public class Distance {

	private final static double EARTH_RADIUS = 6378137;//地球半径(米)
	private static double rad(double d)
	{
	   return d * Math.PI / 180.0;
	}
	//计算两个经纬度之间的距离
	public static int GetDistance(double lat1, double lng1, double lat2, double lng2)
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
	
	public static void main(String[] args)
	{
		double  lat1 = 34.2347831;
		double lng1 = 108.9130483;
		double lat2 = 34.2163000;
		double lng2 = 108.9059300;
		System.out.println(Distance.GetDistance(lat1, lng1, lat2, lng2));
	}
}

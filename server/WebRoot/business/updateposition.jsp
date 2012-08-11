<%@ page language="java" import="org.json.JSONException"
	pageEncoding="utf-8"%>
<%@page import="com.cn.shopapp.ShopApp"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="com.cn.shopapp.Store"%>
<% request.setCharacterEncoding("utf-8"); %>
<%
try
{
	ShopApp shopApp = new ShopApp();
	String userid = request.getParameter("userid").toString();
	String lat = request.getParameter("lat").toString();
	String lng = request.getParameter("lng").toString();
	boolean success = shopApp.updatePosition(userid, lat, lng);
	if (success){
		out.print("true");
	}
}catch(Exception e)
{
	e.printStackTrace();
}
%>
<%@ page language="java" import="org.json.JSONException"
	pageEncoding="utf-8"%>
<%@page import="com.cn.shopapp.ShopApp"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.io.FileOutputStream"%>
<% request.setCharacterEncoding("utf-8"); %>
<%
try
{
	ShopApp shopApp = new ShopApp();
	Map<String, String> map = new HashMap<String, String>();
	map.put("username", request.getParameter("username").toString());
	map.put("password", request.getParameter("password").toString());
	String returnvalue = shopApp.loginUser(map);
	out.print(returnvalue);
}catch(Exception e)
{
	e.printStackTrace();
}
%>
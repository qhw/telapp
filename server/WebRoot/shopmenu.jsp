<%@ page language="java" import="java.util.*" pageEncoding="gb2312"%>
<%@page import="com.cn.telapp.TelApp"%>
<%@page import="org.json.JSONArray"%>
<%@page import="org.json.JSONObject"%>
<% request.setCharacterEncoding("utf-8"); %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>²Ëµ¥</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  
  <body>
   <%
   	TelApp telapp = new TelApp();
		int shopid = Integer.valueOf(request.getParameter("shopid"));
		Map<String, String> map = telapp.getShopInfo(shopid);
		out.println(map.get("shopname"));
		String jsondata = telapp.getShopMenu(shopid);
		JSONArray array = new JSONArray(jsondata);
		for (int i = 0; i < array.length(); i++)
		{
			JSONObject object = new JSONObject(array.getString(i));
			out.println(object.getString("menuname"));
		}
    %>
  </body>
</html>

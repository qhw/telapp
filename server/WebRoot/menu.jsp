<%@ page language="java" import="org.json.JSONException"
	pageEncoding="gb2312"%>
<%@page import="com.cn.telapp.TelApp"%>
<%
	TelApp telapp = new TelApp();
	int shopid = Integer.valueOf(request.getParameter("shopid"));
	out.print(telapp.getShopMenu(shopid));
%>

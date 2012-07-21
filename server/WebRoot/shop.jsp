<%@ page language="java" import="org.json.JSONException"
	pageEncoding="gb2312"%>
<%@page import="com.cn.telapp.TelApp"%>
<%
	TelApp telapp = new TelApp();
	String lat = request.getParameter("latitude");
	String lng = request.getParameter("longitude");
	out.print(telapp.getShopInfo(lat, lng));
%>
<%@ page language="java" import="org.json.JSONException"
	pageEncoding="utf-8"%>
<%@page import="com.cn.telapp.TelApp"%>
<% request.setCharacterEncoding("utf-8"); %>
<%
	TelApp telapp = new TelApp();
	String first = request.getParameter("pagefirst");
	String pagesize = request.getParameter("pagesize");
	String lat = request.getParameter("latitude");
	String lng = request.getParameter("longitude");
	out.print(telapp.getShopInfo(first,pagesize,lat,lng));
%>
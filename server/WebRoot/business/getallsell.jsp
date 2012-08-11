<%@ page language="java" import="org.json.JSONException"
	pageEncoding="utf-8"%>
<%@page import="com.cn.shopapp.ShopApp"%>
<% request.setCharacterEncoding("utf-8"); %>
<%
	ShopApp shopApp = new ShopApp();
	String userid = request.getParameter("userid");
	String first = request.getParameter("pagefirst");
	String pagesize = request.getParameter("pagesize");
	String lat = request.getParameter("lat");
	String lng = request.getParameter("lng");
	out.print(shopApp.getAllSell(first, pagesize, userid, lat, lng));
%>
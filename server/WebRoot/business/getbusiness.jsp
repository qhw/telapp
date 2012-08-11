<%@ page language="java" import="org.json.JSONException"
	pageEncoding="utf-8"%>
<%@page import="com.cn.shopapp.ShopApp"%>
<% request.setCharacterEncoding("utf-8"); %>
<%
	ShopApp shopApp = new ShopApp();
	String businessid = request.getParameter("businessid");
	out.print(shopApp.getBusiness(businessid));
%>
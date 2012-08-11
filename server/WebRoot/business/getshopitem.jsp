<%@ page language="java" import="org.json.JSONException"
	pageEncoding="utf-8"%>
<%@page import="com.cn.shopapp.Store"%>
<% request.setCharacterEncoding("utf-8"); %>
<%
	Store store = new Store();
	String shopid = request.getParameter("shopid");
	out.print(store.getShopItem(shopid));
%>
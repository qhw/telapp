<%@ page language="java" import="org.json.JSONException"
	pageEncoding="utf-8"%>
<%@page import="com.cn.shopapp.Store"%>
<% request.setCharacterEncoding("utf-8"); %>
<%
	Store store = new Store();
	String itemid = request.getParameter("itemid");
	out.print(store.getItem(itemid));
%>
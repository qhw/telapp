<%@ page language="java" import="org.json.JSONException"
	pageEncoding="utf-8"%>
<%@page import="com.cn.shopapp.ShopApp"%>
<% request.setCharacterEncoding("utf-8"); %>
<%
	ShopApp shopApp = new ShopApp();
	String userid = request.getParameter("userid");
		String first = request.getParameter("pagefirst");
	String pagesize = request.getParameter("pagesize");
	out.print(shopApp.getMyBuy(first, pagesize, userid));
%>
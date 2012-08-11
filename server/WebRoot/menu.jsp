<%@ page language="java" import="org.json.JSONException"
	pageEncoding="gb2312"%>
<%@page import="com.cn.telapp.TelApp"%>
<% request.setCharacterEncoding("utf-8"); %>
<%
	TelApp telapp = new TelApp();
	String first = request.getParameter("pagefirst");
	String pagesize = request.getParameter("pagesize");
	int shopid = Integer.valueOf(request.getParameter("shopid"));
	out.print(telapp.getShopMenu(first,pagesize,shopid));
%>

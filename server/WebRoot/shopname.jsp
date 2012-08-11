<%@ page language="java" import="org.json.JSONException"
	pageEncoding="utf-8"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@page import="com.cn.telapp.TelApp"%>
<% request.setCharacterEncoding("utf-8"); %>
<%
	TelApp telapp = new TelApp();
	out.print(telapp.getShopInfo());
%>
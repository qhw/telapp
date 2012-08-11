<%@ page language="java" import="org.json.JSONException"
	pageEncoding="utf-8"%>
<%@page import="com.cn.shopapp.Store"%>
<% request.setCharacterEncoding("utf-8"); %>
<%
	Store store = new Store();
	String userid = request.getParameter("userid");
	String first = request.getParameter("pagefirst");
	String pagesize = request.getParameter("pagesize");
	String lat = request.getParameter("lat");
	String lng = request.getParameter("lng");
	out.print(store.getAllStore(first, pagesize, userid, lat, lng));
%>
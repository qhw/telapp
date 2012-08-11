<%@ page language="java" import="org.json.JSONException"
	pageEncoding="utf-8"%>
<%@page import="com.cn.shopapp.ShopApp"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.io.FileOutputStream"%>
<% request.setCharacterEncoding("utf-8"); %>
<%
try
{
	ShopApp shopApp = new ShopApp();
	Map<String, String> map = new HashMap<String, String>();
	map.put("username", request.getParameter("username").toString());
	map.put("password", request.getParameter("password").toString());
	String userimg = request.getParameter("userimg").toString();
	if (!userimg.equals(""))
	{
		userimg = "img/busi/head/" + request.getParameter("userimg").toString();
	}
	map.put("userimg", userimg);
	map.put("lat", request.getParameter("lat").toString());
	map.put("lng", request.getParameter("lng").toString());
	map.put("phone", request.getParameter("phone").toString());
	map.put("qq", request.getParameter("qq").toString());
	String value = request.getParameter("img");//获取value的值
	if (!value.equals(""))
	{
		FileOutputStream fileout = new FileOutputStream("D:/Program Files/Apache Software Foundation/Tomcat 6.0/webapps/telapp/img/busi/head/" + map.get("userimg").toString());//设置文件保存在服务器的什么位置
		fileout.write(com.sun.org.apache.xml.internal.security.utils.Base64.decode(value.getBytes()));//使用base64解码
		fileout.close();
	}
	String returnvalue = shopApp.addUser(map);
	if (!returnvalue.equals("")){
		out.print(returnvalue);
	}
}catch(Exception e)
{
	e.printStackTrace();
}
%>
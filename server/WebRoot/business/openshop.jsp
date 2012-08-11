<%@ page language="java" import="org.json.JSONException"
	pageEncoding="utf-8"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="com.cn.shopapp.Store"%>
<% request.setCharacterEncoding("utf-8"); %>
<%
try
{
	Store store = new Store();
	Map<String, String> map = new HashMap<String, String>();
	map.put("userid", request.getParameter("userid").toString());
	map.put("shopname", request.getParameter("shopname").toString());
	map.put("shopdesc", request.getParameter("shopdesc").toString());
	map.put("shopaddr", request.getParameter("shopaddr").toString());
	String img = request.getParameter("shopimg").toString();
	if (!img.equals(""))
	{
		img = "img/busi/shop/" + request.getParameter("shopimg").toString();
	}
	map.put("shopimg", img);
	map.put("linker", request.getParameter("linker").toString());
	map.put("phone", request.getParameter("phone").toString());
	map.put("qq", request.getParameter("qq").toString());
	map.put("lat", request.getParameter("lat").toString());
	map.put("lng", request.getParameter("lng").toString());
	String value = request.getParameter("img");//获取value的值
	if (!value.equals(""))
	{
		FileOutputStream fileout = new FileOutputStream("D:/Program Files/Apache Software Foundation/Tomcat 6.0/webapps/telapp/img/busi/shop/" + map.get("shopimg").toString());//设置文件保存在服务器的什么位置
		fileout.write(com.sun.org.apache.xml.internal.security.utils.Base64.decode(value.getBytes()));//使用base64解码
		fileout.close();
	}
	boolean success = store.openStore(map);
	if (success){
		out.print("true");
	}
}catch(Exception e)
{
	e.printStackTrace();
}
%>
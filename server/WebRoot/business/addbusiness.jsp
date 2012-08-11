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
	map.put("userid", request.getParameter("userid").toString());
	map.put("title", request.getParameter("title").toString());
	map.put("detail", request.getParameter("detail").toString());
	map.put("price", request.getParameter("price").toString());
	String img = request.getParameter("businessimg").toString();
	if (!img.equals(""))
	{
		img = "img/busi/photo/" + request.getParameter("businessimg").toString();
	}
	map.put("img", img);
	map.put("linker", request.getParameter("linker").toString());
	map.put("phone", request.getParameter("phone").toString());
	map.put("qq", request.getParameter("qq").toString());
	map.put("flag", request.getParameter("flag").toString());
	String value = request.getParameter("img");//获取value的值
	if (!value.equals(""))
	{
		FileOutputStream fileout = new FileOutputStream("D:/Program Files/Apache Software Foundation/Tomcat 6.0/webapps/telapp/img/busi/photo/" + map.get("businessimg").toString());//设置文件保存在服务器的什么位置
		fileout.write(com.sun.org.apache.xml.internal.security.utils.Base64.decode(value.getBytes()));//使用base64解码
		fileout.close();
	}
	boolean success = shopApp.addBusiness(map);
	if (success){
		out.print("true");
	}
}catch(Exception e)
{
	e.printStackTrace();
}
%>
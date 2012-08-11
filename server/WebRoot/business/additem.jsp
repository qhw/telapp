<%@ page language="java" import="org.json.JSONException"
	pageEncoding="utf-8"%>
<%@page import="com.cn.shopapp.ShopApp"%>
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
	map.put("shopid", request.getParameter("shopid").toString());
	map.put("itemname", request.getParameter("itemname").toString());
	String itemimg = request.getParameter("itemimg").toString();
	if (!itemimg.equals(""))
	{
		itemimg = "img/busi/item/" + request.getParameter("itemimg").toString();
	}
	map.put("itemimg", itemimg);
	map.put("itemprice", request.getParameter("itemprice").toString());
	map.put("itemdesc", request.getParameter("itemdesc").toString());
	String value = request.getParameter("img");//获取value的值
	if (!value.equals(""))
	{
		FileOutputStream fileout = new FileOutputStream("D:/Program Files/Apache Software Foundation/Tomcat 6.0/webapps/telapp/img/busi/item/" + map.get("itemimg").toString());//设置文件保存在服务器的什么位置
		fileout.write(com.sun.org.apache.xml.internal.security.utils.Base64.decode(value.getBytes()));//使用base64解码
		fileout.close();
	}
	boolean success = store.addItem(map);
	if (success){
		out.print("true");
	}
}catch(Exception e)
{
	e.printStackTrace();
}
%>
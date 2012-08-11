<%@ page language="java" import="org.json.JSONException"
	pageEncoding="utf-8"%>
<%@page import="com.cn.telapp.TelApp"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.io.FileOutputStream"%>
<% request.setCharacterEncoding("utf-8"); %>
<%
try
{
		TelApp telapp = new TelApp();
		Map<String, String> map = new HashMap<String, String>();
		map.put("shopname", request.getParameter("shopname").toString());
		map.put("shopimg", request.getParameter("shopimg").toString());
		map.put("shopphone", request.getParameter("shopphone").toString());
		map.put("shopaddr", request.getParameter("shopaddr").toString());
		map.put("shoplinker", request.getParameter("shoplinker").toString());
		map.put("shopother", request.getParameter("shopother").toString());
		map.put("shoplat", request.getParameter("shoplat").toString());
		map.put("shoplng", request.getParameter("shoplng").toString());
		String value = request.getParameter("img");//获取value的值
		FileOutputStream fileout = new FileOutputStream("D:/Program Files/Apache Software Foundation/Tomcat 6.0/webapps/telapp/img/shops/" + map.get("shopimg").toString());//设置文件保存在服务器的什么位置
		fileout.write(com.sun.org.apache.xml.internal.security.utils.Base64.decode(value.getBytes()));//使用base64解码
		fileout.close();
		if (telapp.addshops(map))
    {
    	out.print("true");
    }
}catch(Exception e)
{
	e.printStackTrace();
}
%>
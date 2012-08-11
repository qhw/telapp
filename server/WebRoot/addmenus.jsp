<%@ page language="java" import="org.json.JSONException"
	pageEncoding="utf-8"%>
<%@page import="com.cn.telapp.TelApp"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<% request.setCharacterEncoding("utf-8"); %>
<%
try
{
		TelApp telapp = new TelApp();
		Map<String, String> map = new HashMap<String, String>();
		map.put("menuname", request.getParameter("menuname"));
		map.put("menuimg", request.getParameter("menuimg"));
		map.put("shopid", request.getParameter("shopid").toString());
		map.put("menuprice", request.getParameter("menuprice").toString());
		String value = request.getParameter("img");//获取value的值
		FileOutputStream fileout = new FileOutputStream("D:/Program Files/Apache Software Foundation/Tomcat 6.0/webapps/telapp/img/menus/" + map.get("menuimg").toString());//设置文件保存在服务器的什么位置
		fileout.write(com.sun.org.apache.xml.internal.security.utils.Base64.decode(value.getBytes()));//使用base64解码
		fileout.close();
		if (telapp.addmenus(map))
    {
    	out.print("true");
    }
}catch(Exception e)
{
	e.printStackTrace();
}
%>
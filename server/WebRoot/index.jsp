<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.File"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  
  <body>
    <%
			 /*String value = request.getParameter("value");//获取value的值
			 FileOutputStream fileout = new FileOutputStream("D:/Program Files/Apache Software Foundation/Tomcat 6.0/webapps/telapp/img/music.jpg");//设置文件保存在服务器的什么位置
			 fileout.write(com.sun.org.apache.xml.internal.security.utils.Base64.decode(value.getBytes()));//使用base64解码
			 fileout.close();
    	 out.print("aa");
    	 
    	 out.println(path);
    	 out.println(basePath);*/
    	 out.println(new File("").getAbsolutePath());
     %>
  </body>
</html>

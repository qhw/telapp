package com.cn.telapp.config;

public class Config {

	public static final String  baseUrl= "http://219.245.92.195:8080/";
	
	public static final String workspace = "telapp/";
	//命名空间
	public static final String targetNameSpace = baseUrl + "axis/TelApp.jws";
	//wsdl address
	public static final String WSDL= baseUrl + "axis/TelApp.jws";
	//每页显示的数量
	public static int PAGE_SIZE = 15;
	public static int THREADPOOL_SIZE = 4;//线程池的大小
}

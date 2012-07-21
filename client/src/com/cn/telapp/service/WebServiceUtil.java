package com.cn.telapp.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.cn.telapp.config.Config;
public class WebServiceUtil {

	//获取店铺的名称
	public static List<String> getShopInfo(String latitude, String longtide)
	{
			List<String> list = new ArrayList<String>();
			String methodName = "getShopInfo";
		 	SoapObject soapObject=new SoapObject(Config.targetNameSpace, methodName);
	        
	        SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	        envelope.bodyOut=soapObject;
	        soapObject.addProperty("latitude", latitude);
	        soapObject.addProperty("longitude", longtide);
	        HttpTransportSE  httpTranstation=new HttpTransportSE(Config.WSDL);
	       // httpTranstation.debug = true;
	        try {
	            
	            httpTranstation.call(null, envelope);
	            if (envelope.getResponse() != null)
	            {
		            SoapObject result=(SoapObject)envelope.bodyIn;
		            int size = result.getPropertyCount();
		            for (int index = 0; index < size; index++)
		            {
		            	String str= result.getProperty(index).toString();
		            	String[] shops = str.substring(1, str.length() - 1).split(", ");
		            	for (int i = 0; i < shops.length; i++)
		            	{
		            		list.add(shops[i]);
		            	}
		            }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (XmlPullParserException e) {
	            e.printStackTrace();
	        } 
	        return list;
	}
	//
	//获取指定店铺的菜名
	public static List<String> getShopMenu(int shopid)
	{
			List<String> list = new ArrayList<String>();
			String methodName = "getShopMenu";
		 	SoapObject soapObject=new SoapObject(Config.targetNameSpace, methodName);
	        
	        SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	        envelope.bodyOut=soapObject;
	        soapObject.addProperty("shopid",  shopid);
	        HttpTransportSE  httpTranstation=new HttpTransportSE(Config.WSDL);
	       // httpTranstation.debug = true;
	        try {
	            
	            httpTranstation.call(null, envelope);
	            if (envelope.getResponse() != null)
	            {
		            SoapObject result=(SoapObject)envelope.bodyIn;
		            int size = result.getPropertyCount();
		            for (int index = 0; index < size; index++)
		            {
		            	String str= result.getProperty(index).toString();
		            	String[] shopmenus = str.substring(1, str.length() - 1).split(", ");
		            	for (int i = 0; i < shopmenus.length; i++)
		            	{
		            		list.add(shopmenus[i]);
		            	}
		            }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (XmlPullParserException e) {
	            e.printStackTrace();
	        } 
	        return list;
	}
}

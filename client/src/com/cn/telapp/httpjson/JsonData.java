package com.cn.telapp.httpjson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class JsonData {
	
	private Map map = null;
	public JsonData(Map map)
	{
		this.map = map;
	}
	
	public String getJson(String url){
 	       String resultData="";
	       
 	       HttpPost httpRequestHttpPost = new HttpPost(url);
 	       List<NameValuePair>params;
 	       params = new ArrayList<NameValuePair>();
 	      Set<String> keys = map.keySet();
		  for (Iterator it = keys.iterator(); it.hasNext();) {
				String key = (String) it.next();
				System.out.println(key + " ===> " + map.get(key));
				params.add(new BasicNameValuePair(key, map.get(key).toString()));
			}
 	       try {
			HttpEntity httpEntity = new UrlEncodedFormEntity(params, "gb2312");
			httpRequestHttpPost.setEntity(httpEntity);
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = httpClient.execute(httpRequestHttpPost);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				resultData = EntityUtils.toString(httpResponse.getEntity());
			}
			else {
				resultData = "«Î«Û¥ÌŒÛ";
			}
		} catch (Exception e) {
			// TODO: handle exception
			resultData = e.getMessage();
		}
	      return resultData; 
	}
}


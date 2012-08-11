package com.cn.shopapp;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cn.telapp.config.Config;
import com.cn.telapp.http.HttpJsonData;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Person extends Activity {

	private Bundle bundle = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personsetting);
		
		bundle = getIntent().getBundleExtra("bundle");
		
		Button mydata = (Button)findViewById(R.id.mydata);
		Button mybuy = (Button)findViewById(R.id.mybuy);
		Button mysell = (Button)findViewById(R.id.mysell);
		Button pub = (Button)findViewById(R.id.pub);
		Button  myshop = (Button)findViewById(R.id.myshop);
		//求购记录
		mybuy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Person.this, My_Buy.class);
				intent.putExtra("bundle", bundle);
				startActivity(intent);
			}
		});
		
			//个人资料
		mydata.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Person.this, MyInfo.class);
				intent.putExtra("bundle", bundle);
				startActivity(intent);
			}
		});
		//出售记录
		mysell.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Person.this, My_Sell.class);
				intent.putExtra("bundle", bundle);
				startActivity(intent);
			}
		});

		
		pub.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Person.this, PubBusiness.class);
				intent.putExtra("bundle", bundle);
				startActivityForResult(intent, 10);
			}
		});
		
		myshop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = null;
				if (isOpenShop()) 
				{
					intent = new Intent(Person.this, ShopDetail.class);
				}else {
					intent = new Intent(Person.this, ShopWarning.class);
				}
				bundle.putInt("flag", 0);
				intent.putExtra("bundle", bundle);
				startActivity(intent);
			}
		});
	}
	
	// http json
	private boolean isOpenShop() {
		Map map = new HashMap();
		map.put("userid", bundle.getInt("userid"));
		HttpJsonData jsonData = new HttpJsonData(map);
		String url = Config.baseUrl + "telapp/business/isopenshop.jsp";
		String businessInfo = jsonData.getJson(url).trim();
		JSONArray array;
		try {
			array = new JSONArray(businessInfo);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = new JSONObject(array.getString(i));
				if (object.getString("isopenshop").equals("1"))
				{
					return true;
				}else {
					return false;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}
}

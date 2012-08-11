package com.cn.shopapp;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cn.telapp.config.Config;
import com.cn.telapp.http.HttpJsonData;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemDetail extends Activity {

	private Bundle bundle = null;
	private TextView itemnameTextView = null;
	private TextView itemdescTextView = null;
	private TextView itempriceTextView = null;
	private ImageView itemimgImageView = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.itemdetail);
		
		bundle = getIntent().getBundleExtra("bundle");
		
		itemnameTextView = (TextView)findViewById(R.id.itemname);
		itemdescTextView = (TextView)findViewById(R.id.itemdesc);
		itempriceTextView = (TextView)findViewById(R.id.itemprice);
		
		getItemById();
	}

	private void getItemById() {
		Map map = new HashMap();
		map.put("itemid", bundle.getInt("itemid"));
		HttpJsonData jsonData = new HttpJsonData(map);
		String url = Config.baseUrl + "telapp/business/getitem.jsp";
		String businessInfo = jsonData.getJson(url).trim();
		JSONArray array;
		try {
			array = new JSONArray(businessInfo);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = new JSONObject(array.getString(i));
				itemnameTextView.setText(object.getString("itemname"));
				itemdescTextView.setText(object.getString("itemdesc"));
				itempriceTextView.setText(object.getString("itemprice"));
				String imgurl = Config.baseUrl + Config.workspace + object.getString("itemimg");
				loadImage(imgurl, R.id.itemimg);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	
	// Handler+Thread+Message模式
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.obj != null) {
					((ImageView) ItemDetail.this.findViewById(msg.arg1))
							.setImageDrawable((Drawable) msg.obj);
				}
			}
		};

		// 采用handler+Thread模式实现多线程异步加载
		private void loadImage(final String url, final int id) {
			Thread thread = new Thread() {
				@Override
				public void run() {
					Drawable drawable = null;
					try {
						drawable = Drawable.createFromStream(
								new URL(url).openStream(), "image.png");
					} catch (IOException e) {
						Log.d("test", e.getMessage());
					}

					Message message = handler.obtainMessage();
					message.arg1 = id;
					message.obj = drawable;
					handler.sendMessage(message);
				}
			};
			thread.start();
		}
}

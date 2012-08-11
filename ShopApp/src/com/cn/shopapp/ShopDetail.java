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
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ShopDetail extends Activity {

	private Bundle bundle = null;
	private TextView shopname = null;
	private TextView shopdesc = null;
	private TextView shopaddr = null;
	private TextView shoplinker = null;
	private TextView shopphone = null;
	private TextView shopqq = null;
	private String shopimg = null;
	private String shopid = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shopdetail);
		
		bundle = getIntent().getBundleExtra("bundle");
		
		shopname = (TextView)findViewById(R.id.shopname);
		shopdesc = (TextView)findViewById(R.id.shopdesc);
		shopaddr = (TextView)findViewById(R.id.shopaddr);
		shoplinker = (TextView)findViewById(R.id.shoplinker);
		shopphone = (TextView)findViewById(R.id.shopphone);
		shopqq = (TextView)findViewById(R.id.shopqq);
		Button additem = (Button)findViewById(R.id.additem);
		Button allitem = (Button)findViewById(R.id.allitem);
		
		if (bundle.getInt("flag") == 1)
		{
			additem.setVisibility(View.GONE);
		}
		
		getShop();
		
		//添加商品
		additem.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ShopDetail.this, AddItem.class);				
				bundle.putInt("shopid", Integer.parseInt(shopid));
				intent.putExtra("bundle", bundle);
				startActivityForResult(intent, 20);
			}
		});
		
		//查看所有商品
		allitem.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ShopDetail.this, ShopItemList.class);
				bundle.putInt("shopid", Integer.parseInt(shopid));
				intent.putExtra("bundle", bundle);
				startActivity(intent);
			}
		});
		
	}

	
	private void getShop() {
		Map map = new HashMap();
		map.put("userid", bundle.getInt("userid"));
		HttpJsonData jsonData = new HttpJsonData(map);
		String url = Config.baseUrl + "telapp/business/getshop.jsp";
		String shopInfo = jsonData.getJson(url).trim();
		JSONArray array;
		try {
			array = new JSONArray(shopInfo);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = new JSONObject(array.getString(i));
				shopname.setText(object.getString("shopname"));
				shopdesc.setText(object.getString("shopdesc"));
				shopaddr.setText(object.getString("shopaddr"));
				shoplinker.setText(object.getString("linker"));
				shopphone.setText(object.getString("phone"));
				shopqq.setText(object.getString("qq"));
				shopid = object.getString("shopid");
				shopimg = Config.baseUrl + Config.workspace + object.getString("shopimg");
				loadImage(shopimg, R.id.shopimg);
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
				((ImageView) ShopDetail.this.findViewById(msg.arg1))
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

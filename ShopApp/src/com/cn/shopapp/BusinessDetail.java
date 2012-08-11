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
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BusinessDetail extends Activity {

	private Bundle bundle = null;
	private TextView title = null;
	private TextView detail = null;
	private TextView price = null;
	private TextView linker = null;
	private TextView phone = null;
	private TextView qq = null;
	private ImageView img = null;
	private TextView pubtime = null;
	private LinearLayout layoutprice = null;
	private String phonenum = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.businessdetail);
		
		bundle = getIntent().getBundleExtra("bundle");
		
		title = (TextView)findViewById(R.id.title);
		detail = (TextView)findViewById(R.id.detail);
		price = (TextView)findViewById(R.id.price);
		linker = (TextView)findViewById(R.id.linker);
		phone = (TextView)findViewById(R.id.phone);
		qq = (TextView)findViewById(R.id.qq);
		pubtime = (TextView)findViewById(R.id.pubtime);
		img = (ImageView)findViewById(R.id.businessimg);
		layoutprice = (LinearLayout)findViewById(R.id.layoutprice);
		ImageButton callme = (ImageButton)findViewById(R.id.callme);

		getBusiness();
		
		callme.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ phonenum));
				startActivity(intent);
			}
		});
	}

	// http json
		private void getBusiness() {
			Map map = new HashMap();
			map.put("businessid", bundle.getInt("businessid"));
			HttpJsonData jsonData = new HttpJsonData(map);
			String url = Config.baseUrl + "telapp/business/getbusiness.jsp";
			String businessInfo = jsonData.getJson(url).trim();
			JSONArray array;
			try {
				array = new JSONArray(businessInfo);
				for (int i = 0; i < array.length(); i++) {
					JSONObject object = new JSONObject(array.getString(i));
					String imgUrl = Config.baseUrl + Config.workspace
							+ object.getString("businessimg");
					
					title.setText(object.getString("title"));
					detail.setText(object.getString("detail"));
					linker.setText(object.getString("linker"));
					phone.setText(object.getString("phone"));
					phonenum = object.getString("phone");
					qq.setText(object.getString("qq"));
					pubtime.setText(object.getString("pubtime"));
					
					if (object.getString("flag").equals("0"))
					{
						layoutprice.setVisibility(View.GONE);
						img.setVisibility(View.GONE);
					}else {
						price.setText(object.getString("price")+"元");
						loadImage(imgUrl, R.id.businessimg);
					}
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
					((ImageView) BusinessDetail.this.findViewById(msg.arg1))
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

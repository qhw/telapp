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

public class MyInfo extends Activity {

	private Bundle bundle = null;
	private TextView username = null;
	private TextView phone = null;
	private TextView qq = null;
	private ImageView userimg = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myinformation);

		bundle = getIntent().getBundleExtra("bundle");

		username = (TextView) findViewById(R.id.username);
		phone = (TextView) findViewById(R.id.phone);
		qq = (TextView) findViewById(R.id.qq);
		userimg = (ImageView) findViewById(R.id.userimg);

		getInfo();
	}

	// http json
	private void getInfo() {
		Map map = new HashMap();
		map.put("userid", bundle.getInt("userid"));
		HttpJsonData jsonData = new HttpJsonData(map);
		String url = Config.baseUrl + "telapp/business/getuser.jsp";
		String myinfo = jsonData.getJson(url).trim();
		JSONArray array;
		try {
			array = new JSONArray(myinfo);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = new JSONObject(array.getString(i));
				// 用户头像
				String imgUrl = Config.baseUrl + Config.workspace
						+ bundle.getString("userimg");

				username.setText("用户名:" + object.getString("username"));
				phone.setText("电话:" + object.getString("phone"));
				qq.setText("qq/msn:" + object.getString("qq"));
				loadImage(imgUrl, R.id.userimg);
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
				((ImageView) MyInfo.this.findViewById(msg.arg1))
						.setImageDrawable((Drawable) msg.obj);
			} else {
				userimg.setImageResource(R.drawable.icon);
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

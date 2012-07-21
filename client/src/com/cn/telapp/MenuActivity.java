package com.cn.telapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cn.telapp.asynimg.ImageAndText;
import com.cn.telapp.asynimg.MenuImageAndTextListAdapter;
import com.cn.telapp.config.Config;
import com.cn.telapp.httpjson.JsonData;
import com.cn.telapp.service.WebServiceUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MenuActivity extends Activity {

	private ListView listView = null;
	private Bundle bundle = null;
	private ProgressDialog mLoadingDialog = null;
	private ArrayList<ImageAndText> listItem = null;
	private TextView txtTextView = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.menuview);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.menutitle);

		showDialogLoading();

/*		//获取屏幕大小
		WindowManager windowManager = getWindowManager();
		Display dm = windowManager.getDefaultDisplay();
		LinearLayout layout = (LinearLayout)findViewById(R.id.menuview);
		layout.setMinimumHeight(dm.getHeight() - 70);*/
	
		bundle = getIntent().getBundleExtra("bundle");

		// 设置窗口栏显示
		TextView title = (TextView) findViewById(R.id.titlename);
		title.setText(bundle.getString("name"));
		ImageView callImageView = (ImageView) findViewById(R.id.titlecall);
		callImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ bundle.getString("tel")));
				startActivity(intent);
			}
		});
		
		txtTextView = (TextView)findViewById(R.id.txtWarning);
		listView = (ListView) findViewById(R.id.listview);

		getData();
		MenuImageAndTextListAdapter listItemAdapter = new MenuImageAndTextListAdapter(
				this, listItem, listView);

		listView.setAdapter(listItemAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// displayToast( Integer.toString(articleIds.get(arg2)));
				ImageAndText imageAndText = listItem.get(position);
				Intent intent = new Intent();

				Bundle bund = new Bundle();
				bund.putString("url", imageAndText.getImageUrl());
				bund.putString("tel", imageAndText.getShopTel());
				bund.putString("content", imageAndText.getText());
				bund.putString("name", bundle.getString("name"));
				intent.putExtra("bundle", bund);
				intent.setClass(MenuActivity.this, MenuDetail.class);
				startActivityForResult(intent, 11);
				// startActivity(intent);

			}
		});

		// 拨号
		Button call = (Button) findViewById(R.id.menucall);
		call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ bundle.getString("tel")));
				startActivity(intent);
			}
		});

		unShowDialogLoading();
	}
/*
 * webservice 
	private void getData() {
		listItem = new ArrayList<ImageAndText>();
		List<String> list = null;
		list = WebServiceUtil.getShopMenu(bundle.getInt("shopid"));
		if (list == null)
			return;
		for (int i = 0; i < list.size(); i++) {
			String[] menus = list.get(i).split(";");
			int id = bundle.getInt("shopid");
			String imgUrl = Config.baseUrl + menus[3];
			String content = "   菜名: " + menus[2] + "\n   价格: " + menus[4];
			String name = bundle.getString("name");
			String tel = bundle.getString("tel");
			ImageAndText imageAndText = new ImageAndText(imgUrl, content, id,
					name, tel);
			listItem.add(imageAndText);
		}
	}
*/
	
	//http json
	private void getData() {
		listItem = new ArrayList<ImageAndText>();
		Map map = new HashMap();
		map.put("shopid", bundle.getInt("shopid"));
		JsonData jsonData = new JsonData(map);
		String url = Config.baseUrl + "telapp/menu.jsp";
		String menuinfo = jsonData.getJson(url).trim();
		JSONArray array;
		try {
			array = new JSONArray(menuinfo);
			if (array.length() == 0) { txtTextView.setText("对不起,该餐厅暂时没有菜单..."); return;}
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = new JSONObject(array.getString(i));
				int id = bundle.getInt("shopid");
				String imgUrl = Config.baseUrl + Config.workspace + object.getString("menuimage");
				String content = "   菜名: " + object.getString("menuname") + "\n   价格: " + object.getString("menuprice")+"元";
				String name = bundle.getString("name");
				String tel = bundle.getString("tel");
				ImageAndText imageAndText = new ImageAndText(imgUrl, content, id,
						name, tel);
				listItem.add(imageAndText);
				listItem.add(imageAndText);
				listItem.add(imageAndText);
				listItem.add(imageAndText);
				listItem.add(imageAndText);
				listItem.add(imageAndText);
				listItem.add(imageAndText);
				listItem.add(imageAndText);
				listItem.add(imageAndText);
				listItem.add(imageAndText);
				listItem.add(imageAndText);
				listItem.add(imageAndText);
				listItem.add(imageAndText);
				listItem.add(imageAndText);
				listItem.add(imageAndText);
				listItem.add(imageAndText);
				listItem.add(imageAndText);
				listItem.add(imageAndText);
				listItem.add(imageAndText);
				listItem.add(imageAndText);
				listItem.add(imageAndText);
				listItem.add(imageAndText);
				listItem.add(imageAndText);
				listItem.add(imageAndText);
				listItem.add(imageAndText);
				listItem.add(imageAndText);
				listItem.add(imageAndText);
				listItem.add(imageAndText);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private void showDialogLoading() {
		mLoadingDialog = new ProgressDialog(this);
		mLoadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// spinner 自旋体 像螺旋桨那样
		mLoadingDialog.setMessage("载入中,请稍候...");
		mLoadingDialog.setIndeterminate(false);// 设置进度条是否为不明确
		mLoadingDialog.setCancelable(true);// 设置进度条是否可以按退回健取消
		mLoadingDialog.show();
	}

	private void unShowDialogLoading() {
		if (mLoadingDialog == null)
			return;
		else
			mLoadingDialog.dismiss();
	}
}

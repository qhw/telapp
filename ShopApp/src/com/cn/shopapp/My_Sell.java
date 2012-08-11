package com.cn.shopapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cn.shopapp.adapter.MySellListAdapter;
import com.cn.shopapp.model.BusinessInfo;
import com.cn.telapp.config.Config;
import com.cn.telapp.http.HttpJsonData;
import com.cn.telapp.util.ContextUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class My_Sell extends Activity {
	private ListView listView = null;
	private Bundle bundle = null;
	private ProgressDialog mLoadingDialog = null;
	private ArrayList<BusinessInfo> listItem = new ArrayList<BusinessInfo>();
	private MySellListAdapter listItemAdapter = null;
	private int TOTAL_PAGE = 0;
	private ExecutorService executorService;
	private Handler handler;
	private TextView tv_msg;
	private LinearLayout list_footer;
	private LinearLayout loading;
	private int currentSize = 0;
	private int lastSize = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ContextUtil contextUtil = new ContextUtil(this);
		contextUtil.noTitleBar();
		contextUtil.setFullScreen();
		setContentView(R.layout.listview);
		showDialogLoading();
		bundle = getIntent().getBundleExtra("bundle");
		
		TextView top_title = (TextView)findViewById(R.id.top_title);
		top_title.setText("我的出售记录");
		
		setUpViews();
		executorService.submit(new GetDataThread());
		tv_msg.setVisibility(View.GONE);//隐藏更多提示的TextView
		loading.setVisibility(View.VISIBLE);//显示最下方的进度条
		unShowDialogLoading();
	}

	private void setUpViews(){
		handler = new GetDataHandler();
		listView = (ListView) findViewById(R.id.listview);
		list_footer = (LinearLayout)LayoutInflater.from(My_Sell.this).inflate(R.layout.list_footer, null);
		tv_msg = (TextView)list_footer.findViewById(R.id.tv_msg);
		loading = (LinearLayout)list_footer.findViewById(R.id.loading);
		listView.addFooterView(list_footer);//这儿是关键中的关键呀，利用FooterVIew分页动态加载
		List<BusinessInfo> item = new ArrayList<BusinessInfo>();
		listItemAdapter = new MySellListAdapter(My_Sell.this, item);
		listView.setAdapter(listItemAdapter);
		executorService = Executors.newFixedThreadPool(Config.THREADPOOL_SIZE);
		
		tv_msg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				lastSize = currentSize;
				TOTAL_PAGE++;
				executorService.submit(new GetDataThread());
				tv_msg.setVisibility(View.GONE);//隐藏更多提示的TextView
				loading.setVisibility(View.VISIBLE);//显示最下方的进度条
			}
		});
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				BusinessInfo businessInfo = listItem.get(position);
				Intent intent = new Intent();

				Bundle bund = new Bundle();
				bund.putInt("businessid", businessInfo.getBusinessId());
				intent.putExtra("bundle", bund);
				intent.setClass(My_Sell.this, BusinessDetail.class);
				startActivityForResult(intent, 12);
			}
		});
	}
	
	class GetDataHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if(currentSize > lastSize){
				for (int i = lastSize; i < currentSize; i++) {
					listItemAdapter.add(listItem.get(i));
				}
				listItemAdapter.notifyDataSetChanged();
				listView.setSelection(lastSize + 1);//设置最新获取一页数据成功后显示数据的起始数据
			}
			loading.setVisibility(View.GONE);//隐藏下方的进度条
			tv_msg.setVisibility(View.VISIBLE);//显示出更多提示TextView
		}
	}
	
	class GetDataThread implements Runnable{
		@Override
		public void run() {
			getData();
			Message msg = handler.obtainMessage();//通知线程来处理范围的数据
			handler.sendMessage(msg);		
		}
	}
	
	// http json
	private void getData() {
		Map map = new HashMap();
		map.put("userid", bundle.getInt("userid"));
		map.put("pagefirst", currentSize + 1);
		map.put("pagesize", (TOTAL_PAGE + 1) * Config.PAGE_SIZE);
		HttpJsonData jsonData = new HttpJsonData(map);
		String url = Config.baseUrl + "telapp/business/getmysell.jsp";
		String menuinfo = jsonData.getJson(url).trim();
		JSONArray array;
		try {
			array = new JSONArray(menuinfo);
			currentSize += array.length();
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = new JSONObject(array.getString(i));
				String imgUrl = Config.baseUrl + Config.workspace + bundle.getString("userimg");
				int  businessId = Integer.parseInt(object.getString("businessid"));
				String title = object.getString("title");
				String detail = object.getString("detail");
				String distance = object.getString("distance");
				String pubDate = object.getString("pubtime");
				String price = object.getString("price");

				BusinessInfo businessInfo = new BusinessInfo();
				businessInfo.setBusinessBody(detail);
				businessInfo.setBusinessDistance(distance);
				businessInfo.setBusinessId(businessId);
				businessInfo.setBusinessTitle(title);
				businessInfo.setUserImgUrl(imgUrl);
				businessInfo.setPubDate(pubDate);
				businessInfo.setBusinessPrice(price);
				listItem.add(businessInfo);
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

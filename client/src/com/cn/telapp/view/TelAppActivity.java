package com.cn.telapp.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.cn.telapp.R;
import com.cn.telapp.adapter.ShopListAdapter;
import com.cn.telapp.config.Config;
import com.cn.telapp.http.*;
import com.cn.telapp.model.ShopInfo;
import com.cn.telapp.util.ContextUtil;
import com.cn.telapp.util.GPRS;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
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
import android.widget.Toast;

public class TelAppActivity extends Activity {

	private ListView listView = null;
	private ArrayList<ShopInfo> listItem = new ArrayList<ShopInfo>();
	private ProgressDialog mLoadingDialog = null;
	private ConnectivityManager mCM = null;
	private LocationClient mLocationClient = null;
	private int TOTAL_PAGE = 0;
	private ShopListAdapter listItemAdapter;
	private ExecutorService executorService;
	private double lat;
	private double lng;
	private Handler handler;
	private TextView tv_msg;
	private LinearLayout list_footer;
	private LinearLayout loading;
	private int currentSize = 0;
	private int lastSize = 0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ContextUtil contextUtil = new ContextUtil(this);
		contextUtil.noTitleBar();
		contextUtil.setFullScreen();
		setContentView(R.layout.shopview);
		
		showDialogLoading("正在定位,请稍候...");
		
        mCM = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE); 
        GPRS gprs = new GPRS(mCM);
        Dialog alertDialog = null;
        if (!gprs.isOpenNet())
        {
	        alertDialog = new AlertDialog.Builder(this). 
	                setMessage("请先开启网络"). 
	                setPositiveButton("确定", new DialogInterface.OnClickListener() { 
	                     
	                    @Override 
	                    public void onClick(DialogInterface dialog, int which) { 
	                         dialog.dismiss();
	                         finish();
	                    } 
	                }). 
	                create(); 
	        alertDialog.show();
        }
		setUpViews();
        //利用百度的api进行定位
        mLocationClient = new LocationClient(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);								//打开gps
        option.setCoorType("bd09ll");							//设置坐标类型为bd09ll
        option.setPriority(LocationClientOption.GpsFirst);	//设置网络优先
        option.setProdName("telapp");						//设置产品线名称
        option.setScanSpan(3000);								//定时定位，每隔3秒钟定位一次。
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(bdLocationListener);
        mLocationClient.start();
	}

	private void setUpViews(){
		handler = new GetDataHandler();
		listView = (ListView) findViewById(R.id.listview);
		list_footer = (LinearLayout)LayoutInflater.from(TelAppActivity.this).inflate(R.layout.list_footer, null);
		tv_msg = (TextView)list_footer.findViewById(R.id.tv_msg);
		loading = (LinearLayout)list_footer.findViewById(R.id.loading);
		listView.addFooterView(list_footer);//这儿是关键中的关键呀，利用FooterVIew分页动态加载
		List<ShopInfo> item = new ArrayList<ShopInfo>();
		listItemAdapter = new ShopListAdapter(TelAppActivity.this, item);
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
				ShopInfo item = listItem.get(position);
				Intent intent = new Intent();

				Bundle bundle = new Bundle();
				bundle.putInt("shopid", item.getShopId());
				bundle.putString("tel", item.getShopTel());
				bundle.putString("name", item.getShopName());
				bundle.putString("url", item.getShopUrl());

				intent.putExtra("bundle", bundle);
				intent.setClass(TelAppActivity.this, MenuActivity.class);
				startActivityForResult(intent, 10);
			}
		});
	}
	
	BDLocationListener bdLocationListener = new BDLocationListener() {

		@Override
		public void onReceiveLocation(BDLocation loc) {
			if (loc == null) return;
			if (loc.getLocType() == 65) return;
			unShowDialogLoading();
			lat = loc.getLatitude();
			lng = loc.getLongitude();
			executorService.submit(new GetDataThread());
			tv_msg.setVisibility(View.GONE);//隐藏更多提示的TextView
			loading.setVisibility(View.VISIBLE);//显示最下方的进度条
			mLocationClient.unRegisterLocationListener(bdLocationListener);
			mLocationClient.stop();
		}
	};
	
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
			getData(lat, lng);
			Message msg = handler.obtainMessage();//通知线程来处理范围的数据
			handler.sendMessage(msg);		
		}
	}
	
	// http json
	private void getData(double latitude, double longitude) {
		Map map = new HashMap();
		map.put("pagefirst", currentSize + 1);
		map.put("pagesize", (TOTAL_PAGE + 1) * Config.PAGE_SIZE);
		map.put("latitude", latitude);
		map.put("longitude", longitude);
		HttpJsonData jsonData = new HttpJsonData(map);
		String url = Config.baseUrl + "telapp/shop.jsp";
		String shopinfo = jsonData.getJson(url).trim();
		JSONArray array;
		try {
			array = new JSONArray(shopinfo);
			currentSize += array.length();
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = new JSONObject(array.getString(i));
				
				int shopId = Integer.parseInt(object.getString("shopid"));
				String imgUrl = Config.baseUrl + Config.workspace + object.getString("shopimg");
				String shopName = object.getString("shopname");
				String shopInfo = object.getString("shopphone");
				String shopDistance = object.getString("shopdistance");
				String shopTel = object.getString("shopphone");
				
				ShopInfo shop = new ShopInfo();
				shop.setShopId(shopId);
				shop.setShopUrl(imgUrl);
				shop.setShopTel(shopTel);
				shop.setShopInfo(shopInfo);
				shop.setShopName(shopName);
				shop.setShopDistance(shopDistance);
				listItem.add(shop);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void showDialogLoading(String message) {
		mLoadingDialog = new ProgressDialog(this);
		mLoadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// spinner 自旋体 像螺旋桨那样
		mLoadingDialog.setMessage(message);
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
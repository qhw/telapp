package com.cn.shopapp;

import java.util.HashMap;
import java.util.Map;

import org.androidpn.client.ServiceManager;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.cn.telapp.config.Config;
import com.cn.telapp.http.HttpJsonData;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TabHost;

public class Main extends TabActivity {

	private String lat = null;
	private String lng = null;
	private Bundle bundle = null;
	private LocationClient mLocationClient = null;
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.maininfo);
		//setFullScreen();
		
		bundle = getIntent().getBundleExtra("bundle");
		
	      //利用百度的api进行定位
        mLocationClient = new LocationClient(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);								//打开gps
        option.setCoorType("bd09ll");							//设置坐标类型为bd09ll
        option.setPriority(LocationClientOption.GpsFirst);	//设置网络优先
        option.setProdName("ShopApp");						//设置产品线名称
        option.setScanSpan(3000);								//定时定位，每隔3秒钟定位一次。
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(bdLocationListener);
        mLocationClient.start();
        
        // Start the service
        ServiceManager serviceManager = new ServiceManager(Main.this, 
        		bundle.getString("username"),  bundle.getString("password"));
        
        serviceManager.setNotificationIcon(R.drawable.icon);
        serviceManager.startService();
        
		// tabHost是一个标签容器
		TabHost tabHost = this.getTabHost();

		Intent buy = new Intent(Main.this, All_Buy.class);
		buy.putExtra("bundle", bundle);
		// 定义第一个标签
		tabHost.addTab(tabHost
				.newTabSpec("main_buy")
				.setIndicator("求购",
						getResources().getDrawable(android.R.drawable.star_on))
				.setContent(buy));

		Intent sell = new Intent(Main.this, All_Sell.class);
		sell.putExtra("bundle", bundle);
		// 定义第二个标签
		tabHost.addTab(tabHost
				.newTabSpec("main_sell")
				.setIndicator("出售",
						getResources().getDrawable(android.R.drawable.star_on))
				.setContent(sell));

		Intent shop = new Intent(Main.this, ShopList.class);
		shop.putExtra("bundle", bundle);
		tabHost.addTab(tabHost
				.newTabSpec("main_shop")
				.setIndicator("店铺",
						getResources().getDrawable(android.R.drawable.star_on))
				.setContent(shop));
		
		Intent info = new Intent(Main.this, Person.class);
		info.putExtra("bundle", bundle);
		tabHost.addTab(tabHost
				.newTabSpec("main_info")
				.setIndicator("个人中心",
						getResources().getDrawable(android.R.drawable.star_on))
				.setContent(info));
	}
	
	
	BDLocationListener bdLocationListener = new BDLocationListener() {

		@Override
		public void onReceiveLocation(BDLocation loc) {
			if (loc == null) return;

			if (loc.getLocType() == 61 || loc.getLocType() == 161) {
				lat = String.valueOf(loc.getLatitude());
				lng = String.valueOf(loc.getLongitude());
	
				//更新我的位置
				String url = Config.baseUrl + Config.workspace + "business/updateposition.jsp";
				Map<String, String> map = new HashMap<String, String>();
				map.put("userid", bundle.getInt("userid")+"");
				map.put("lat", lat);
				map.put("lng", lng);
	
				HttpJsonData jsonData = new HttpJsonData(map);
				String value = jsonData.getJson(url);
				if (value.trim().equals("true")) {
					mLocationClient.unRegisterLocationListener(bdLocationListener);
					bundle.putString("lat", lat);
					bundle.putString("lng", lng);
					mLocationClient.stop();
				}
			}
		}
	};
	
	//设置全屏
	private void setFullScreen(){
	     getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	    		 WindowManager.LayoutParams.FLAG_FULLSCREEN);
	 }
}

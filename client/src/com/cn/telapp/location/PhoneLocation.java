package com.cn.telapp.location;

import java.util.Iterator;

import com.cn.telapp.gprs.GPRS;

import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;

public class PhoneLocation {
	private LocationManager locManager = null;
	private ConnectivityManager mCM = null;
	private Location location  = null;
	private GpsStatus gpsstatus = null;
	private boolean isgpsopen = false;
	
	public PhoneLocation(LocationManager loc, ConnectivityManager mCM)
	{
		locManager = loc;
		this.mCM = mCM;
	}
	 //获取当前位置的经纬度
    public Location getLocation()
    {
    	//通过gps进行定位
    	getGPSLocation();
    	
    	//如果gps定位失败，尝试通过网络进行定位
    	if( location == null)
    	{
    		getNETWORKLocation();
    	}
    	return location;
    }
    
    //通过gps定位
    private void getGPSLocation()
    {
    	boolean listener = false;
    	
    	//判断gps是否开启，如果没有开 让用户开启
    	openGPSSettings();
    	if (!isgpsopen)
    		return;
    	
        //根据设置的Criteria对象，获取最符合此标准的provider对象
        String currentProvider = locManager.getProvider(LocationManager.GPS_PROVIDER).getName();
        
        //根据当前provider对象获取最后一次位置信息
        location = locManager.getLastKnownLocation(currentProvider);
        //如果位置信息为null，则请求更新位置信息
        if(location == null)
        {
        	locManager.requestLocationUpdates(currentProvider, 0, 0, locationListener);
        	listener = true;
        }
        //增加GPS状态监听器
       // locManager.addGpsStatusListener(gpsListener);
        int timeout = 0;
        while (location == null)
		{
			try {
				Thread.sleep(1000);
				timeout++;
				if (timeout > 5) break;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
        
        //locManager.removeGpsStatusListener(gpsListener);
        if (listener)
        {
        	locManager.removeUpdates(locationListener);
        	listener = false;
        }
    }
    //通过网络定位
    private void getNETWORKLocation()
    {
    	boolean listener = false;
    	
    	//记录gprs是否开启
    	boolean isopengprs = false;
    	GPRS gprs = new GPRS(mCM);
    	isopengprs = gprs.gprsIsOpenMethod();
    	if (!isopengprs)
    	{
    		gprs.setGprsEnabled(true);
    	}
		// gps定位失败，尝试通过网络定位
		String currentProvider = locManager.getProvider(
				LocationManager.NETWORK_PROVIDER).getName();

		// 根据当前provider对象获取最后一次位置信息
		location = locManager
				.getLastKnownLocation(currentProvider);
		// 如果位置信息为null，则请求更新位置信息
		if (location == null) {
			locManager.requestLocationUpdates(currentProvider, 0, 0,
					locationListener);
			listener = true;
		}
		int timeout = 0;
		
		 while (location == null)
			{
				try {
					Thread.sleep(1000);
					timeout++;
					if (timeout > 5) break;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		 
		if (listener) {
			locManager.removeUpdates(locationListener);
		}
		
		//恢复gprs原来的状态
		if (!isopengprs)
    	{
    		gprs.setGprsEnabled(isopengprs);
    	}
    }
    
    private void openGPSSettings() 
	{
        if (locManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
        	isgpsopen = true;
            return;
        }
    }
	
	 private GpsStatus.Listener gpsListener = new GpsStatus.Listener(){
         //GPS状态发生变化时触发
         @Override
         public void onGpsStatusChanged(int event) {
             //获取当前状态
             gpsstatus=locManager.getGpsStatus(null);
             switch(event){
                 //第一次定位时的事件
                 case GpsStatus.GPS_EVENT_FIRST_FIX:
                     break;
                 //开始定位的事件
                 case GpsStatus.GPS_EVENT_STARTED:
                     break;
                 //发送GPS卫星状态事件
                 case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                    // Toast.makeText(TelAppActivity.this, "GPS_EVENT_SATELLITE_STATUS", Toast.LENGTH_SHORT).show();
                     Iterable<GpsSatellite> allSatellites = gpsstatus.getSatellites();   
                     Iterator<GpsSatellite> it=allSatellites.iterator(); 
                     int count = 0;
                     while(it.hasNext())   
                     {   
                         count++;
                     }
                   //  Toast.makeText(TelAppActivity.this, "Satellite Count:" + count, Toast.LENGTH_SHORT).show();
                     break;
                 //停止定位事件
                 case GpsStatus.GPS_EVENT_STOPPED:
                     Log.d("Location", "GPS_EVENT_STOPPED");
                     break;
             }
         }
     };
     
     
     //创建位置监听器
     private LocationListener locationListener = new LocationListener(){
         //位置发生改变时调用
         @Override
         public void onLocationChanged(Location loc) {
        	 location = loc;
         }
 
         //provider失效时调用
         @Override
         public void onProviderDisabled(String provider) {
         }
 
         //provider启用时调用
         @Override
         public void onProviderEnabled(String provider) {
         }
 
         //状态改变时调用
         @Override
         public void onStatusChanged(String provider, int status, Bundle extras) {
         }
     };
}

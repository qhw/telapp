package com.cn.telapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class ContextUtil {

	private Context context;
	public  int SCREEN_WIDTH;
	public  int SCREEN_HEIGHT;
	public  int SCREEN_DPI;
	
	public ContextUtil(Context context) {
		this.context = context;
		initScreenData();
	}
	
	public void noTitleBar()
	{
		((Activity)context).requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	public  void setFullScreen()
	{
		//设置全屏
		((Activity)context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//设置横屏
		//((Activity)context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}
	
	/**
	 * 获取屏幕尺寸
	 */
	private void initScreenData() {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);

		SCREEN_WIDTH = dm.widthPixels;
		SCREEN_HEIGHT = dm.heightPixels;
		SCREEN_DPI = dm.densityDpi;
	}
	//获取屏幕宽度
	public int getScreenWidth() {
		return this.SCREEN_WIDTH;
	}
	//获取屏幕高度
	public int getScreenHeight() {
		return this.SCREEN_HEIGHT;
	}
	
	//获取屏幕分辨率
	public int getScreenDPI() {
		return this.SCREEN_DPI;
	}

	// 调试信息时使用
	public static void ShowXYMessage(Context con, String str) {
		Toast.makeText(con, str, Toast.LENGTH_SHORT).show();
	}
	
	// 停顿指定时间
	public void SleepTime(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}

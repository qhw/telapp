package com.cn.telapp.util;

import java.lang.reflect.Method;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class GPRS {

	private ConnectivityManager mCM;

	public GPRS(ConnectivityManager mCM) {
		this.mCM = mCM;
	}
	
	//检测是否连网 wifi gprs
	public boolean isOpenNet()
	{
		NetworkInfo info = mCM.getActiveNetworkInfo(); 
		if (info == null || !info.isAvailable()){ 
		return false; 
		}else{ 
		return true; 
		} 
	}
	
	public boolean gprsEnabled(boolean bEnable) {

		boolean isOpen = gprsIsOpenMethod();
		if (isOpen == !bEnable) {
			setGprsEnabled(bEnable);
		}

		return isOpen;
	}

	// 检测GPRS是否打开
	public boolean gprsIsOpenMethod() {
		String methodName = "getMobileDataEnabled";
		Class cmClass = mCM.getClass();
		Class[] argClasses = null;
		Object[] argObject = null;

		Boolean isOpen = false;
		try {
			Method method = cmClass.getMethod(methodName, argClasses);

			isOpen = (Boolean) method.invoke(mCM, argObject);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return isOpen;
	}

	// 开启/关闭GPRS
	public void setGprsEnabled(boolean isEnable) {
		String methodName = "setMobileDataEnabled";
		Class cmClass = mCM.getClass();
		Class[] argClasses = new Class[1];
		argClasses[0] = boolean.class;

		try {
			Method method = cmClass.getMethod(methodName, argClasses);
			method.invoke(mCM, isEnable);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

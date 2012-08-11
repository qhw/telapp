package com.cn.telapp.model;

import android.graphics.drawable.Drawable;

public class ShopInfo {
	
    	private int shopId;
    	
	    private String shopTel;
    	
	    private String shopInfo;
    	
	    private String shopName;
    	
	    private String shopUrl;
	    
		private String shopDistance;

	    public ShopInfo() {
	    	
	    }
	    
	    public String getShopUrl() {
	    	
	        return shopUrl;
	    }
	    
	    public void setShopUrl(String url) {
	    	
	    	this.shopUrl = url;
	    }
	    
	    public int getShopId() {
	    	
	        return shopId;
	    }
	    
	    public void setShopId(int shopId) {
	    	
	    	this.shopId = shopId;
	    }
	    
	    public String getShopName() {
	    	
	    	return shopName;
	    }
	    
	    public void setShopName(String shopName) {
	    	
	    	this.shopName = shopName;
	    }
	    
	    public String getShopInfo() {
	    	
	    	return shopInfo;
	    }
	    
	    public void setShopInfo(String shopInfo) {
	    	
	    	this.shopInfo = shopInfo;
	    }
	    
	    public String getShopTel() {
	    	
	    	return shopTel;
	    }
	    
	    public void setShopTel(String tel) {
	    	
	    	this.shopTel = tel;
	    }
	    
	    public String getShopDistance() {
	    	
	    	return this.shopDistance;
	    }
	    
	    public void setShopDistance(String distance) {
	    	
	    	if (Integer.valueOf(distance) / 1000 >= 1) {
	    		this.shopDistance = Double.parseDouble(distance) / 1000 + "วงรื ";
			} else {
				this.shopDistance = distance + "รื";
			}
	    }
}

package com.cn.telapp.model;

import android.graphics.drawable.Drawable;

public class MenuInfo {
	
    	private int menuId;
	    
	    private String menuPrice;
	    	
	    private String menuName;
    	
	    private String menuUrl;

	    public MenuInfo() {
	    	
	    }
	    
	    public String getMenuUrl() {
	    	
	        return menuUrl;
	    }
	    
	    public void setMenuIcon(String url) {
	    	
	    	menuUrl = url;
	    }
	    
	    public int getMenuId() {
	    	
	        return menuId;
	    }
	    
	    public void setMenuId(int menuId) {
	    	
	    	this.menuId = menuId;
	    }
	    
	    public String getMenuName() {
	    	
	    	return menuName;
	    }
	    
	    public void setMenuName(String menuName) {
	    	
	    	this.menuName = menuName;
	    }
	    
	    public String getMenuPrice() {
	    	
	    	return menuPrice;
	    }
	    
	    public void setMenuPrice(String price) {
	    	
	    	this.menuPrice = price + "Ԫ";
	    }
}

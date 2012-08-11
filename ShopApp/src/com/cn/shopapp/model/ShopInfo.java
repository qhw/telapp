package com.cn.shopapp.model;

public class ShopInfo {

	private int userId;

	private int shopId;
	
	private String shopName;
	
	private String shopDetail;
	
	private String shopAddr;
	
	private String shopImg;
	
	private String shopTel;
	
	private String shopDistance;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public String getShopDistance() {
		return shopDistance;
	}

	public void setShopDistance(String shopDistance) {
		if (Integer.valueOf(shopDistance) / 1000 >= 1) {
    		this.shopDistance = Double.parseDouble(shopDistance) / 1000 + "วงรื ";
		} else {
			this.shopDistance = shopDistance + "รื";
		}
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

	public String getShopDetail() {
		return shopDetail;
	}

	public void setShopDetail(String shopDetail) {
		this.shopDetail = shopDetail;
	}

	public String getShopAddr() {
		return shopAddr;
	}

	public void setShopAddr(String shopAddr) {
		this.shopAddr = shopAddr;
	}

	public String getShopImg() {
		return shopImg;
	}

	public void setShopImg(String shopImg) {
		this.shopImg = shopImg;
	}

	public String getShopTel() {
		return shopTel;
	}

	public void setShopTel(String shopTel) {
		this.shopTel = shopTel;
	}
}

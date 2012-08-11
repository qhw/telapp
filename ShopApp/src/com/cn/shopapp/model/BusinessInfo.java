package com.cn.shopapp.model;

public class BusinessInfo {

	private int businessId;

	private String pubDate;
	
	private String userImgUrl;

	private String businessTitle;
	
	private String businessBody;

	private String businessPrice;
	
	private String businessDistance;
	
	public BusinessInfo() {
		
	}
	
	public int getBusinessId() {
		return businessId;
	}

	public void setBusinessId(int businessId) {
		this.businessId = businessId;
	}
	
	public void setPubDate(String date) {
		this.pubDate = date;
	}
	
	public String getPubDate() {
		return pubDate;
	}
	
	public void setUserImgUrl(String url) {
		this.userImgUrl = url;
	}
	
	public String getUserImgUrl() {
		return userImgUrl;
	}
	
	public String getBusinessTitle() {
		return businessTitle;
	}

	public void setBusinessTitle(String businessTitle) {
		this.businessTitle = businessTitle;
	}

	public String getBusinessBody() {
		return businessBody;
	}

	public void setBusinessBody(String businessBody) {
		this.businessBody = businessBody;
	}

	public String getBusinessPrice() {
		return businessPrice;
	}

	public void setBusinessPrice(String businessPrice) {
		this.businessPrice = businessPrice + "ิช";
	}

	public String getBusinessDistance() {
		return businessDistance;
	}

	public void setBusinessDistance(String businessDistance) {
		if (Integer.valueOf(businessDistance) / 1000 >= 1) {
    		this.businessDistance = Double.parseDouble(businessDistance) / 1000 + "วงรื ";
		} else {
			this.businessDistance = businessDistance + "รื";
		}
	}
}

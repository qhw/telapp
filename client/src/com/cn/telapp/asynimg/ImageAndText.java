package com.cn.telapp.asynimg;

public class ImageAndText {
	    private String imageUrl;
	    private String text;
	    private int shopId;
	    private String shopName;
	    private String shopTel;

	    public ImageAndText(String imageUrl, String text, int id, String name, String tel) {
	        this.imageUrl = imageUrl;
	        this.text = text;
	        this.shopId = id;
	        this.shopName = name;
	        this.shopTel = tel;
	    }
	    
	    public String getImageUrl() {
	        return imageUrl;
	    }
	    public String getText() {
	        return text;
	    }
	    
	    public int getShopId()
	    {
	    	return shopId;
	    }
	    
	    public String getShopName()
	    {
	    	return shopName;
	    }
	    
	    public String getShopTel()
	    {
	    	return shopTel;
	    }
}

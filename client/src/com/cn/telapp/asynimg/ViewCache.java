package com.cn.telapp.asynimg;

import com.cn.telapp.R;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewCache {

	    private View baseView;
	    private TextView textView;
	    private ImageView imageView;
	    private ImageButton callButton;

	    public ViewCache(View baseView) {
	        this.baseView = baseView;
	    }

	    public TextView getTextView() {
	        if (textView == null) {
	            textView = (TextView) baseView.findViewById(R.id.content);
	        }
	        return textView;
	    }

	    public ImageView getImageView() {
	        if (imageView == null) {
	            imageView = (ImageView) baseView.findViewById(R.id.img);
	        }
	        return imageView;
	    }
	    
	    public ImageButton getShopButton() {
	        if (callButton == null) {
	            callButton = (ImageButton) baseView.findViewById(R.id.shopcall);
	        }
	        return callButton;
	    }

}

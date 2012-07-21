package com.cn.telapp.asynimg;

import com.cn.telapp.R;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuViewCache {

	    private View baseView;
	    private TextView textView;
	    private ImageView imageView;

	    public MenuViewCache(View baseView) {
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

}

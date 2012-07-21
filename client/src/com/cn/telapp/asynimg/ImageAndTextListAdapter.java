package com.cn.telapp.asynimg;

import java.util.List;

import com.cn.telapp.R;
import com.cn.telapp.asynimg.AsyncImageLoader.ImageCallback;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ImageAndTextListAdapter extends ArrayAdapter<ImageAndText> {

	    private ListView listView = null;
	    private List<ImageAndText> imgAndTexts = null;
	    private AsyncImageLoader asyncImageLoader = null;
	    Activity parentActivity = null;
	    public ImageAndTextListAdapter(Activity activity, List<ImageAndText> imageAndTexts, ListView listView) {
	        super(activity, 0, imageAndTexts);
	        this.parentActivity = activity;
	        this.listView = listView;
	        this.imgAndTexts = imageAndTexts;
	        asyncImageLoader = new AsyncImageLoader();
	    }

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return imgAndTexts.size();
		}

		@Override
		public ImageAndText getItem(int position) {
			// TODO Auto-generated method stub
			return imgAndTexts.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
	        Activity activity = (Activity) getContext();
	        
	        // Inflate the views from XML
	        View rowView = convertView;
	        ViewCache viewCache;
	        if (rowView == null) {
	            LayoutInflater inflater = activity.getLayoutInflater();
	            rowView = inflater.inflate(R.layout.item, null);
	            viewCache = new ViewCache(rowView);
	            rowView.setTag(viewCache);
	        } else {
	            viewCache = (ViewCache) rowView.getTag();
	        }
	        ImageAndText imageAndText = getItem(position);

	        // Load the image and set it on the ImageView
	        String imageUrl = imageAndText.getImageUrl();
	        ImageView imageView = viewCache.getImageView();
	        imageView.setTag(imageUrl);
	        Drawable cachedImage = asyncImageLoader.loadDrawable(imageUrl, new ImageCallback() {
	            public void imageLoaded(Drawable imageDrawable, String imageUrl) {
	                ImageView imageViewByTag = (ImageView) listView.findViewWithTag(imageUrl);
	                if (imageViewByTag != null) {
	                    imageViewByTag.setImageDrawable(imageDrawable);
	                }
	            }
	        });
			if (cachedImage == null) {
				imageView.setImageResource(R.drawable.icon);
			}else{
				imageView.setImageDrawable(cachedImage);
			}
	        // Set the text on the TextView
	        TextView textView = viewCache.getTextView();
	        textView.setText(imageAndText.getText());

	        ImageButton button = viewCache.getShopButton();
	        button.setOnClickListener(new LvOnClickListener(imageAndText.getShopTel()));
	        
	        return rowView;
	    }
		
		//自定义按钮响应监听类
		class LvOnClickListener implements OnClickListener
		{
			private String  tel;
			public LvOnClickListener(String tel)
			{
				this.tel = tel;
			}
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
				parentActivity.startActivity(intent);
			}
		}
}

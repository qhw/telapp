package com.cn.telapp.asynimg;

import java.util.List;

import com.cn.telapp.R;
import com.cn.telapp.asynimg.AsyncImageLoader.ImageCallback;

import android.app.Activity;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MenuImageAndTextListAdapter extends ArrayAdapter<ImageAndText> {

	    private ListView listView = null;
	    private List<ImageAndText> imgAndTexts = null;
	    private AsyncImageLoader asyncImageLoader = null;
	    Activity parentActivity = null;
	    public MenuImageAndTextListAdapter(Activity activity, List<ImageAndText> imageAndTexts, ListView listView) {
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
	        MenuViewCache viewCache;
	        if (rowView == null) {
	            LayoutInflater inflater = activity.getLayoutInflater();
	            rowView = inflater.inflate(R.layout.menulistitem, null);
	            viewCache = new MenuViewCache(rowView);
	            rowView.setTag(viewCache);
	        } else {
	            viewCache = (MenuViewCache) rowView.getTag();
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

	        return rowView;
	    }
}

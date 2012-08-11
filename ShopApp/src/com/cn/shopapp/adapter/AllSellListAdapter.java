package com.cn.shopapp.adapter;

import java.util.List;

import com.cn.shopapp.R;
import com.cn.shopapp.model.BusinessInfo;
import com.cn.telapp.util.AsyncImageLoader;
import com.cn.telapp.util.AsyncImageLoader.ImageCallback;
import android.content.Context;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AllSellListAdapter extends ArrayAdapter<BusinessInfo> {

	    private List<BusinessInfo> businessInfos = null;
	    private AsyncImageLoader asyncImageLoader = null;
	    private Context context = null;
	    public AllSellListAdapter(Context context, List<BusinessInfo> businessInfos) {
	        super(context, 0, businessInfos);
	        this.context = context;
	        this.businessInfos = businessInfos;
	        asyncImageLoader = new AsyncImageLoader();
	    }

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return businessInfos.size();
		}

		@Override
		public BusinessInfo getItem(int position) {
			// TODO Auto-generated method stub
			return businessInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			    BusinessInfo businessInfo = getItem(position);
				convertView = LayoutInflater.from(context).inflate(R.layout.all_sell_item, null);		       
				ViewCache viewCache = new ViewCache();
				
				viewCache.businesscontent = (TextView)convertView.findViewById(R.id.content);
				viewCache.businessdistance = (TextView)convertView.findViewById(R.id.businessdistance);
				viewCache.businesstitle = (TextView)convertView.findViewById(R.id.businesstitle);
				viewCache.pubdate = (TextView)convertView.findViewById(R.id.pubdate);
				viewCache.businessprice = (TextView)convertView.findViewById(R.id.businessprice);
				viewCache.userimg = (ImageView)convertView.findViewById(R.id.img);
	       
		        Drawable cachedImage = asyncImageLoader.loadDrawable(businessInfo.getUserImgUrl(), viewCache.userimg, new ImageCallback() {
		            public void imageLoaded(Drawable imageDrawable, ImageView imageView, String imageUrl) {
		            	imageView.setImageDrawable(imageDrawable);
		            }
		        });
				if (cachedImage == null) {
					viewCache.userimg.setImageResource(R.drawable.icon);
				}else{
					viewCache.userimg.setImageDrawable(cachedImage);
				}
				
				viewCache.businesscontent.setText(businessInfo.getBusinessBody());
				viewCache.businessdistance.setText(businessInfo.getBusinessDistance());
				viewCache.businesstitle.setText(businessInfo.getBusinessTitle());
				viewCache.businessprice.setText(businessInfo.getBusinessPrice());
				viewCache.pubdate.setText(businessInfo.getPubDate());
		        return convertView;
	    }
		
		public class ViewCache {

		    private TextView businesscontent;
		    private TextView businesstitle;
		    private ImageView userimg;
		    private TextView businessdistance;
		    private TextView businessprice;
		    private TextView	pubdate;
		}
}

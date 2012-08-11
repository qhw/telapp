package com.cn.telapp.adapter;

import java.util.List;

import com.cn.telapp.R;
import com.cn.telapp.model.ShopInfo;
import com.cn.telapp.util.AsyncImageLoader;
import com.cn.telapp.util.AsyncImageLoader.ImageCallback;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ShopListAdapter extends ArrayAdapter<ShopInfo> {

	    private List<ShopInfo> shops = null;
	    private AsyncImageLoader asyncImageLoader = null;
	    Context context = null;
	    public ShopListAdapter(Context context, List<ShopInfo> shops) {
	    	
	        super(context, 0, shops);
	        this.context = context;
	        this.shops = shops;
	        asyncImageLoader = new AsyncImageLoader();
	    }

		@Override
		public void add(ShopInfo object) {

			super.add(object);
		}

		@Override
		public void notifyDataSetChanged() {
			
			super.notifyDataSetChanged();
		}

		@Override
		public int getCount() {

			return shops.size();
		}

		@Override
		public ShopInfo getItem(int position) {

			return shops.get(position);
		}

		@Override
		public long getItemId(int position) {
			
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			
	        ShopInfo shop = getItem(position);
			convertView = LayoutInflater.from(context).inflate(R.layout.item, null);
	       
			ViewCache viewCache = new ViewCache();
	        viewCache.shopimg = (ImageView)convertView.findViewById(R.id.img);
	        viewCache.shopcall = (ImageView)convertView.findViewById(R.id.shopcall);
	        viewCache.shopcontent = (TextView)convertView.findViewById(R.id.content);
	        viewCache.shopname = (TextView)convertView.findViewById(R.id.shop_name);
	        viewCache.shopdistance = (TextView)convertView.findViewById(R.id.shop_distance);
	        
	        Drawable cachedImage = asyncImageLoader.loadDrawable(shop.getShopUrl(), viewCache.shopimg, new ImageCallback() {
	            public void imageLoaded(Drawable imageDrawable, ImageView imageView, String imageUrl) {
	            	imageView.setImageDrawable(imageDrawable);
	            }
	        });
			if (cachedImage == null) {
				viewCache.shopimg.setImageResource(R.drawable.icon);
			}else{
				viewCache.shopimg.setImageDrawable(cachedImage);
			}
			
			viewCache.shopcontent.setText(shop.getShopInfo());
			viewCache.shopdistance.setText(shop.getShopDistance());
			viewCache.shopname.setText(shop.getShopName());
			viewCache.shopcall.setOnClickListener(new LvOnClickListener(shop.getShopTel()));
	        
	        return convertView;
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
				context.startActivity(intent);
			}
		}
		
		public class ViewCache {

		    private TextView shopcontent;
		    private TextView shopname;
		    private ImageView shopimg;
		    private ImageView	shopcall;
		    private TextView	shopdistance;
		}
}

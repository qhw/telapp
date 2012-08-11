package com.cn.shopapp.adapter;

import java.util.List;

import com.cn.shopapp.R;
import com.cn.shopapp.model.ShopInfo;
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

public class ShopListAdapter extends ArrayAdapter<ShopInfo> {

	    private List<ShopInfo> shopInfos = null;
	    private AsyncImageLoader asyncImageLoader = null;
	    private Context context = null;
	    public ShopListAdapter(Context context, List<ShopInfo> shopInfos) {
	        super(context, 0, shopInfos);
	        this.context = context;
	        this.shopInfos = shopInfos;
	        asyncImageLoader = new AsyncImageLoader();
	    }

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return shopInfos.size();
		}

		@Override
		public ShopInfo getItem(int position) {
			// TODO Auto-generated method stub
			return shopInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			    ShopInfo shopInfo = getItem(position);
				convertView = LayoutInflater.from(context).inflate(R.layout.all_buy_item, null);		       
				ViewCache viewCache = new ViewCache();
				
				viewCache.shopdetail = (TextView)convertView.findViewById(R.id.content);
				viewCache.shopdistance = (TextView)convertView.findViewById(R.id.businessdistance);
				viewCache.shopname = (TextView)convertView.findViewById(R.id.businesstitle);
				viewCache.shopaddr = (TextView)convertView.findViewById(R.id.pubdate);
				viewCache.shopimg = (ImageView)convertView.findViewById(R.id.img);
	       
		        Drawable cachedImage = asyncImageLoader.loadDrawable(shopInfo.getShopImg(), viewCache.shopimg, new ImageCallback() {
		            public void imageLoaded(Drawable imageDrawable, ImageView imageView, String imageUrl) {
		            	imageView.setImageDrawable(imageDrawable);
		            }
		        });
				if (cachedImage == null) {
					viewCache.shopimg.setImageResource(R.drawable.icon);
				}else{
					viewCache.shopimg.setImageDrawable(cachedImage);
				}
				
				viewCache.shopdetail.setText(shopInfo.getShopDetail());
				viewCache.shopdistance.setText(shopInfo.getShopDistance());
				viewCache.shopname.setText(shopInfo.getShopName());
				viewCache.shopaddr.setText(shopInfo.getShopAddr());
		        return convertView;
	    }
		
		public class ViewCache {

		    private TextView shopdetail;
		    private TextView shopname;
		    private ImageView shopimg;
		    private TextView shopdistance;
		    private TextView	shopaddr;
		}
}

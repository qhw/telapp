package com.cn.shopapp.adapter;

import java.util.List;

import com.cn.shopapp.R;
import com.cn.shopapp.model.ShopItemInfo;
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

public class ShopItemListAdapter extends ArrayAdapter<ShopItemInfo> {

	    private List<ShopItemInfo> shopItemInfos = null;
	    private AsyncImageLoader asyncImageLoader = null;
	    private Context context = null;
	    public ShopItemListAdapter(Context context, List<ShopItemInfo> shopItemInfos) {
	        super(context, 0, shopItemInfos);
	        this.context = context;
	        this.shopItemInfos = shopItemInfos;
	        asyncImageLoader = new AsyncImageLoader();
	    }

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return shopItemInfos.size();
		}

		@Override
		public ShopItemInfo getItem(int position) {
			// TODO Auto-generated method stub
			return shopItemInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			    ShopItemInfo shopItemInfo = getItem(position);
				convertView = LayoutInflater.from(context).inflate(R.layout.shop_item, null);		       
				ViewCache viewCache = new ViewCache();
				
				viewCache.itemdetail = (TextView)convertView.findViewById(R.id.content);
				viewCache.itemname = (TextView)convertView.findViewById(R.id.itemname);
				viewCache.itemprice = (TextView)convertView.findViewById(R.id.itemprice);
				viewCache.itemimg = (ImageView)convertView.findViewById(R.id.img);
	       
		        Drawable cachedImage = asyncImageLoader.loadDrawable(shopItemInfo.getItemImg(), viewCache.itemimg, new ImageCallback() {
		            public void imageLoaded(Drawable imageDrawable, ImageView imageView, String imageUrl) {
		            	imageView.setImageDrawable(imageDrawable);
		            }
		        });
				if (cachedImage == null) {
					viewCache.itemimg.setImageResource(R.drawable.icon);
				}else{
					viewCache.itemimg.setImageDrawable(cachedImage);
				}
				
				viewCache.itemdetail.setText(shopItemInfo.getItemDetail());
				viewCache.itemname.setText(shopItemInfo.getItemName());
				viewCache.itemprice.setText(shopItemInfo.getItemPrice());
		        return convertView;
	    }
		
		public class ViewCache {

		    private TextView itemdetail;
		    private TextView itemname;
		    private ImageView itemimg;
		    private TextView itemprice;
		}
}

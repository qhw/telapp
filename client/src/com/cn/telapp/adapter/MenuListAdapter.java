package com.cn.telapp.adapter;

import java.util.List;

import com.cn.telapp.R;
import com.cn.telapp.model.MenuInfo;
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

public class MenuListAdapter extends ArrayAdapter<MenuInfo> {

	    private List<MenuInfo> menus = null;
	    private AsyncImageLoader asyncImageLoader = null;
	    private Context context = null;
	    public MenuListAdapter(Context context, List<MenuInfo> menus) {
	        super(context, 0, menus);
	        this.context = context;
	        this.menus = menus;
	        asyncImageLoader = new AsyncImageLoader();
	    }

		@Override
		public int getCount() {

			return menus.size();
		}

		@Override
		public MenuInfo getItem(int position) {
			
			return menus.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
	        
			MenuInfo menu = menus.get(position);
	        convertView = LayoutInflater.from(context).inflate(R.layout.menulistitem, null);
	        ViewCache viewCache = new ViewCache();
	        
	        viewCache.menuimg = (ImageView)convertView.findViewById(R.id.img);
	        viewCache.menuname = (TextView)convertView.findViewById(R.id.menuname);
	        viewCache.menuprice = (TextView)convertView.findViewById(R.id.menuprice);
	        
	        Drawable cachedImage = asyncImageLoader.loadDrawable(menu.getMenuUrl(), viewCache.menuimg, new ImageCallback() {
	            public void imageLoaded(Drawable imageDrawable, ImageView imageView, String imageUrl) {
	            	imageView.setImageDrawable(imageDrawable);
	            }
	        });
			if (cachedImage == null) {
				viewCache.menuimg.setImageResource(R.drawable.icon);
			}else{
				viewCache.menuimg.setImageDrawable(cachedImage);
			}
	        
			viewCache.menuname.setText(menu.getMenuName());
			viewCache.menuprice.setText(menu.getMenuPrice());

	        return convertView;
	    }
		
		public class ViewCache {

		    private TextView menuname;
		    private TextView menuprice;
		    private ImageView menuimg;
		}
}

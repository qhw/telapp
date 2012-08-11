package com.cn.telapp.util;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class AsyncImageLoader {

	 private HashMap<String, SoftReference<Drawable>> imageCache;
	  
	     public AsyncImageLoader() {
	    	 imageCache = new HashMap<String, SoftReference<Drawable>>();
	     }
	  
	     public Drawable loadDrawable(final String imageUrl, final ImageView imageView, final ImageCallback imageCallback) {
	    	 Drawable draw = null;
	         if (imageCache.containsKey(imageUrl)) {
	             SoftReference<Drawable> softReference = imageCache.get(imageUrl);
	             draw = softReference.get();
	             return draw;
	         }
	         final Handler handler = new Handler() {
	             public void handleMessage(Message message) {
	            	 imageCallback.imageLoaded((Drawable) message.obj, imageView, imageUrl);
	             }
	         };
	         new Thread() {
	             @Override
	             public void run() {
	                 Drawable drawable = loadImageFromUrl(imageUrl);
	                 imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
	                 Message message = handler.obtainMessage(0, drawable);
	                 handler.sendMessage(message);
	             }
	         }.start();
	         return draw;
	     }
	  
		public static Drawable loadImageFromUrl(String url) {
			Drawable d  = null;
			try {
				d = Drawable.createFromStream(
						new URL(url).openStream(), "image.png");
			} catch (MalformedURLException e1) { 
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return d;
		}
	  
	     public interface ImageCallback {
	         public void imageLoaded(Drawable imageDrawable, ImageView imageView, String imageUrl);
	     }

}

package com.cn.shopapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ShopWarning extends Activity {

	private Bundle bundle = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shopwarning);
		
		bundle = getIntent().getBundleExtra("bundle");
		Button openshop = (Button)findViewById(R.id.openshop);
		
		
		//¿ªÍ¨µêÆÌ
		openshop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ShopWarning.this, OpenShop.class);
				intent.putExtra("bundle", bundle);
				startActivity(intent);
			}
		});
	}
}

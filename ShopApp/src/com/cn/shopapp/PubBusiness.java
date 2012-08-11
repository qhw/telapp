package com.cn.shopapp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.cn.telapp.config.Config;
import com.cn.telapp.http.HttpJsonData;

import android.R.bool;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewDebug.FlagToString;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class PubBusiness extends Activity {

	private Bundle bundle = null;
	private RadioGroup r_group = null;
	private RadioButton r_buy = null;
	private RadioButton r_sell = null;
	private LinearLayout layoutprice = null;
	private LinearLayout layoutimg = null;
	private EditText 	titleEditText = null;
	private EditText	detailEditText = null;
	private EditText 	priceEditText = null;
	private EditText	linkerEditText = null;
	private EditText 	phoneEditText = null;
	private EditText 	qqEditText = null;
	private EditText	imgEditText  = null;
	private Button addBusiness = null;
	private Button addimg = null;
	private int  type = 0;
	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_CODE = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pubbusiness);
		
		bundle = getIntent().getBundleExtra("bundle");
		
        layoutprice = (LinearLayout)findViewById(R.id.layoutprice);
        layoutimg = (LinearLayout)findViewById(R.id.layoutimg);
		r_group = (RadioGroup)findViewById(R.id.usergroup);
        r_buy = (RadioButton)findViewById(R.id.buy);
        r_sell = (RadioButton)findViewById(R.id.sell);
        r_group.setOnCheckedChangeListener(mChangeRadio);
        r_group.check(r_buy.getId());
        addBusiness = (Button)findViewById(R.id.addbusiness);
        addimg = (Button)findViewById(R.id.addimg);
        
        titleEditText = (EditText)findViewById(R.id.title);
        detailEditText = (EditText)findViewById(R.id.detail);
        priceEditText = (EditText)findViewById(R.id.price);
        linkerEditText = (EditText)findViewById(R.id.linker);
        phoneEditText = (EditText)findViewById(R.id.phone);
        qqEditText = (EditText)findViewById(R.id.qq);
        imgEditText = (EditText)findViewById(R.id.businessimg);
        
        addBusiness.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog alertDialog = null;
				if (titleEditText.getText().toString().equals("")) {
					alertDialog = new AlertDialog.Builder(v.getContext())
							.setMessage("请先填写标题！")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									}).create();
					alertDialog.show();
					return;
				}
				addBusiness.setText("正在提交数据...");
				addBusiness.setEnabled(false);
				boolean success = addBusiness();
				if (success)
				{
					setResult(RESULT_OK);
					finish();
				}
				addBusiness.setText("添加");
				addBusiness.setEnabled(true);
			}
		});
		
        
        addimg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
				getAlbum.setType(IMAGE_TYPE);
				startActivityForResult(getAlbum, IMAGE_CODE);
			}
		});
	}

	private boolean addBusiness() {
		String url = Config.baseUrl + Config.workspace + "business/addbusiness.jsp";
		Map<String, String> map = new HashMap<String, String>();
		map.put("userid", bundle.getInt("userid")+"");
		map.put("title", titleEditText.getText().toString().trim());
		map.put("detail", detailEditText.getText().toString().trim());
		if (type == 0) {
			map.put("price", "0");
			map.put("businessimg", "");
			map.put("img", "");
		} else {
			map.put("price", priceEditText.getText().toString().trim());
			if (!imgEditText.getText().toString().equals("")) {
				String[] imgString = imgEditText.getText().toString()
						.split("/");
				map.put("businessimg", imgString[imgString.length - 1]);
				FileInputStream in = null;
				try {
					in = new FileInputStream(imgEditText.getText().toString());
					byte buffer[] = null;
					try {
						buffer = HttpJsonData.read(in);
					} catch (Exception e) {
						e.printStackTrace();
					}// 把图片文件流转成byte数组
					byte[] encod = Base64.encode(buffer, Base64.DEFAULT);// 使用base64编码
					map.put("img", new String(encod));// 保存数据到map对象
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			} else {
				map.put("img", "");
				map.put("businessimg", "");
			}
		}
		map.put("phone", phoneEditText.getText().toString().trim());
		map.put("linker", linkerEditText.getText().toString().trim());
		map.put("qq", qqEditText.getText().toString().trim());
		map.put("flag", type + "");

		try {
			HttpJsonData jsonData = new HttpJsonData(map);
			String value = jsonData.getJson(url);
			if (value.trim().equals("true")) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	 private RadioGroup.OnCheckedChangeListener mChangeRadio = new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if(checkedId == r_buy.getId())
				{
					type = 0;//求购
				}else if(checkedId == r_sell.getId()) {
					type = 1;//出售
				}
				isHidden();
			}
	    };
	    
	    private void isHidden()
	    {
	    	if (type == 0)
	    	{
	    		layoutimg.setVisibility(View.GONE);
	    		layoutprice.setVisibility(View.GONE);
	    	}else {
	    		layoutimg.setVisibility(View.VISIBLE);
	    		layoutprice.setVisibility(View.VISIBLE);
			}
	    }
	    
	    
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			if (resultCode != RESULT_OK) { // 此处的 RESULT_OK 是系统自定义得一个常量
				return;
			}
			Bitmap bm = null;
			ContentResolver resolver = getContentResolver();
			if (requestCode == IMAGE_CODE) {
				try {
					Uri originalUri = data.getData(); // 获得图片的uri
					bm = MediaStore.Images.Media.getBitmap(resolver, originalUri); // 显得到bitmap图片
					// 这里开始的第二部分，获取图片的路径：
					String[] proj = { MediaStore.Images.Media.DATA };
					Cursor cursor = managedQuery(originalUri, proj, null, null,
							null);
					// 按我个人理解 这个是获得用户选择的图片的索引值
					int column_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					cursor.moveToFirst();
					// 最后根据索引值获取图片路径
					String path = cursor.getString(column_index);
					imgEditText.setText(path);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	    
}

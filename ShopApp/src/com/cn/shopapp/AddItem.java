package com.cn.shopapp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.cn.telapp.config.Config;
import com.cn.telapp.http.HttpJsonData;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddItem extends Activity {

	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_CODE = 0;
	private Bundle bundle = null;
	private EditText itemnameEditText = null;
	private EditText itemdescEditText = null;
	private EditText itempriceEditText = null;
	private EditText itemimgEditText = null;
	private Button additem = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.additem);
		
		bundle = getIntent().getBundleExtra("bundle");
		
		itemnameEditText = (EditText)findViewById(R.id.itemname);
		itemdescEditText = (EditText)findViewById(R.id.itemdesc);
		itempriceEditText = (EditText)findViewById(R.id.itemprice);
		itemimgEditText = (EditText)findViewById(R.id.itemimg);
		additem = (Button)findViewById(R.id.additem);
		Button addimg = (Button)findViewById(R.id.addimg);
		//选择图片
		addimg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
				getAlbum.setType(IMAGE_TYPE);
				startActivityForResult(getAlbum, IMAGE_CODE);
			}
		});
		//添加商品
		additem.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog alertDialog = null;
				if (itemnameEditText.getText().toString().equals("")) {
					alertDialog = new AlertDialog.Builder(v.getContext())
							.setMessage("请先填写商品名！")
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
				additem.setText("正在提交数据...");
				additem.setEnabled(false);
				boolean success = additem();
				if (success)
				{
					setResult(RESULT_OK);
					finish();
				}
				additem.setText("添加");
				additem.setEnabled(true);
			}
		});
	}

	// http json
	private boolean additem() {
		Map map = new HashMap();
		map.put("shopid", bundle.getInt("shopid"));
		map.put("itemname", itemnameEditText.getText().toString());
		map.put("itemdesc", itemdescEditText.getText().toString());
		map.put("itemprice", itempriceEditText.getText().toString());
		if (!itemimgEditText.getText().toString().equals("")) {
			String[] imgString = itemimgEditText.getText().toString()
					.split("/");
			map.put("itemimg", imgString[imgString.length - 1]);
			FileInputStream in = null;
			try {
				in = new FileInputStream(itemimgEditText.getText().toString());
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
			map.put("itemimg", "");
		}
		HttpJsonData jsonData = new HttpJsonData(map);
		String url = Config.baseUrl + "telapp/business/additem.jsp";
		String value = jsonData.getJson(url).trim();
		if (value.trim().equals("true")) {
			return true;
		}
		return false;
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
				itemimgEditText.setText(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}

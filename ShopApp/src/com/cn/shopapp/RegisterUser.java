package com.cn.shopapp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.cn.telapp.config.Config;
import com.cn.telapp.http.HttpJsonData;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.Toast;

public class RegisterUser extends Activity {

	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_CODE = 0;
	private LocationClient mLocationClient = null;
	private ProgressDialog mLoadingDialog = null;
	private Button register = null;
	private Button addimg = null;
	private EditText imgEditText = null;
	private EditText usernameEditText = null;
	private EditText pwdEditText = null;
	private EditText phoneEditEditText = null;
	private EditText qqEditText = null;
	private String lat = null;
	private String lng = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registeruser);
		
		imgEditText = (EditText)findViewById(R.id.userimg);
		addimg = (Button)findViewById(R.id.addimg);
		register = (Button)findViewById(R.id.register);
		
		usernameEditText = (EditText)findViewById(R.id.username);
		pwdEditText = (EditText)findViewById(R.id.password);
		phoneEditEditText = (EditText)findViewById(R.id.phone);
		qqEditText = (EditText)findViewById(R.id.qq);
		
		 //利用百度的api进行定位
        showDialogLoading();
        mLocationClient = new LocationClient(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);								//打开gps
        option.setCoorType("bd09ll");							//设置坐标类型为bd09ll
        option.setPriority(LocationClientOption.GpsFirst);	//设置网络优先
        option.setProdName("ShopApp");						//设置产品线名称
        option.setScanSpan(3000);								//定时定位，每隔3秒钟定位一次。
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(bdLocationListener);
        mLocationClient.start();
        
		//注册
		register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog alertDialog = null;
				if (usernameEditText.getText().toString().equals("")) {
					alertDialog = new AlertDialog.Builder(v.getContext())
							.setMessage("请先填写用户名！")
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
				if (pwdEditText.getText().toString().equals("")) {
					alertDialog = new AlertDialog.Builder(v.getContext())
							.setMessage("请先填写密码！")
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
				
				register.setText("正在提交数据...");
				register.setEnabled(false);
				String value = addUser();
				if (!value.equals(""))
				{
					JSONArray array;
					try {
						array = new JSONArray(value);
						for (int i = 0; i < array.length(); i++) {
							JSONObject object = new JSONObject(array.getString(i));
							Intent intent = new Intent(RegisterUser.this, Main.class);
							Bundle bundle = new Bundle();
							bundle.putInt("userid", Integer.parseInt(object.getString("userid")));
							bundle.putString("username", object.getString("username"));
							bundle.putString("userimg", object.getString("userimg"));
							bundle.putString("lat", object.getString("lat"));
							bundle.putString("lng", object.getString("lng"));
							bundle.putString("isopenshop", object.getString("isopenshop"));
							intent.putExtra("bundle", bundle);
							startActivity(intent);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				register.setText("添加");
				register.setEnabled(true);
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
	
	 BDLocationListener bdLocationListener = new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation loc) {
				if (loc == null) return;
				if (loc.getLocType() == 65) return;
				String position = "纬度:" + loc.getLatitude() + "\n经度" + loc.getLongitude();
				lat = String.valueOf(loc.getLatitude());
				lng = String.valueOf(loc.getLongitude());
				displayToast(position);
				
				mLocationClient.unRegisterLocationListener(bdLocationListener);
				mLocationClient.stop();
				unShowDialogLoading();
			}
		};
		
		private void showDialogLoading() {
			mLoadingDialog = new ProgressDialog(this);
			mLoadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			// spinner 自旋体 像螺旋桨那样
			mLoadingDialog.setMessage("正在定位,请稍候...");
			mLoadingDialog.setIndeterminate(false);// 设置进度条是否为不明确
			mLoadingDialog.setCancelable(true);// 设置进度条是否可以按退回健取消
			mLoadingDialog.show();
		}

		private void unShowDialogLoading() {
			if (mLoadingDialog == null)
				return;
			else
				mLoadingDialog.dismiss();
		}
		
	private String addUser()
	{
		String url = Config.baseUrl + Config.workspace + "business/adduser.jsp";
		Map<String, String>map = new HashMap<String, String>();
		map.put("username", usernameEditText.getText().toString().trim());
		map.put("password", pwdEditText.getText().toString().trim());
		map.put("phone", phoneEditEditText.getText().toString().trim());
		map.put("qq", qqEditText.getText().toString().trim());
		map.put("lat", lat);
		map.put("lng", lng);
		if (!imgEditText.getText().toString().equals("")) {
			String[] imgString = imgEditText.getText().toString()
					.split("/");
			map.put("userimg", imgString[imgString.length - 1]);
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
			map.put("userimg", "");
		}
		
		try {
			HttpJsonData jsonData = new HttpJsonData(map);
			String value = jsonData.getJson(url);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
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

	private void displayToast(String str)
    {
    	Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}

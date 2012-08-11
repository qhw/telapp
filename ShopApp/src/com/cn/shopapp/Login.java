package com.cn.shopapp;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.cn.telapp.config.Config;
import com.cn.telapp.http.HttpJsonData;
import com.cn.telapp.util.GPRS;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	
	private ConnectivityManager mCM = null;
	private ProgressDialog mLoadingDialog = null;
	private Bundle bundle = null;
	private String username = null;
	private String pwd = null;
	private  EditText pwdEditText = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        EditText usernameEditText = (EditText)findViewById(R.id.username);
        pwdEditText = (EditText)findViewById(R.id.pwd);
        Button login = (Button)findViewById(R.id.loginuser);
        Button register = (Button)findViewById(R.id.register);
        
        mCM = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE); 
        GPRS gprs = new GPRS(mCM);
        Dialog alertDialog = null;
        if (!gprs.isOpenNet())
        {
	        alertDialog = new AlertDialog.Builder(this). 
	                setMessage("请先开启网络"). 
	                setPositiveButton("确定", new DialogInterface.OnClickListener() { 
	                     
	                    @Override 
	                    public void onClick(DialogInterface dialog, int which) { 
	                         dialog.dismiss();
	                         finish();
	                    } 
	                }). 
	                create(); 
	        alertDialog.show();
        }
             
        username = usernameEditText.getText().toString();
        pwd = pwdEditText.getText().toString();
        
        login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (!loginUser()) return;
				Intent intent = new Intent(Login.this, Main.class);
				intent.putExtra("bundle", bundle);
				startActivity(intent);
			}
		});
        
        register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Login.this, RegisterUser.class);
				startActivity(intent);
			}
		});
    }
    
	private boolean loginUser() {
		String url = Config.baseUrl + Config.workspace + "business/loginuser.jsp";
		Map<String, String> map = new HashMap<String, String>();
		map.put("username", username);
		map.put("password", pwd);

		HttpJsonData jsonData = new HttpJsonData(map);
		String value = jsonData.getJson(url);
		JSONArray array;
		try {
			array = new JSONArray(value);
			if (array.length() == 0) {
				displayToast("用户名或密码错误!");
				return false;
			}
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = new JSONObject(array.getString(i));
				bundle = new Bundle();
				bundle.putInt("userid", Integer.parseInt(object.getString("userid")));
				bundle.putString("username", object.getString("username"));
				bundle.putString("password", object.getString("password"));
				bundle.putString("userimg", object.getString("userimg"));
				bundle.putString("lat", object.getString("lat"));
				bundle.putString("lng", object.getString("lng"));
				bundle.putString("isopenshop", object.getString("isopenshop"));

				return true;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
        
		return false;
	}
    
    
	
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
	
    private void displayToast(String str)
    {
    	Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
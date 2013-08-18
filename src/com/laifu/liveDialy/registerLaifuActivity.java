package com.laifu.liveDialy;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.laifu.liveDialy.entity.UserInfoEntity;
import com.laifu.liveDialy.net.DefaultTools;
import com.laifu.liveDialy.net.LaifuConnective;
import com.laifu.liveDialy.parser.DefaultJSONData;
import com.laifu.liveDialy.parser.LaifuLoginJsonData;
import com.laifu.liveDialy.parser.NormalJsonData;
import com.laifu.liveDialy.tool.Constant;
import com.laifu.liveDialy.wxapi.AccessTokenKeeper;

public class registerLaifuActivity extends LiveBaseActivity{
	
	private TextView mText;
	public EditText username,password;
	public Button button;
	public static final int Regesiter_SUCCESS=200;
	Handler handle = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (progressDialog != null) {
				progressDialog.cancel();
			}
			switch (msg.what) {
			case Regesiter_SUCCESS:
				Intent i = new Intent(registerLaifuActivity.this,IndexActivity.class);
				startActivity(i);
				Toast.makeText(registerLaifuActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
				break;
			case SUCCESS:
				AccessTokenKeeper.setLaifuToken(registerLaifuActivity.this, jsonData.laifuTokenEntity.user_id, jsonData.laifuTokenEntity.token, Long.parseLong(jsonData.laifuTokenEntity.expires));
				saveUserInfo();
				if(!jsonData.userInfoEntity.username.equals("null")){
					Intent i2 = new Intent(registerLaifuActivity.this,IndexActivity.class);
					startActivity(i2);
				}
				break;
			case FAILED:
				Toast.makeText(registerLaifuActivity.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
				break;
			case ERROR:
				alertDialog("来福提示", (String)msg.obj);
				break;
			}
		}
	};
	
	
	
	@Override
	public void initPages() {
		// TODO Auto-generated method stub
		SpannableString msp;
		String type=getIntent().getExtras().getString("type");
		mText = (TextView)findViewById(R.id.laifu_text);
		if(type.equals("qq"))
			msp = new SpannableString("感谢您绑定腾讯QQ\n10秒钟快速注册来福ID\n立即享受一呼百应的精彩服务吧！");
		else
			msp = new SpannableString("感谢您绑定新浪微博\n10秒钟快速注册来福ID\n立即享受一呼百应的精彩服务吧！");
		int index = msp.toString().indexOf("10秒钟");
		msp.setSpan(
				new ForegroundColorSpan(getResources().getColor(R.color.white)),
				0, msp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		
		msp.setSpan(
				new ForegroundColorSpan(getResources().getColor(
						R.color.login_laifu)), index, index+4 ,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		mText.setText(msp);
		
		
		username=(EditText)findViewById(R.id.account_edit);
		password=(EditText)findViewById(R.id.mPassword_edit);
		button=(Button)findViewById(R.id.mloggin_btn);
		
		button.setOnClickListener(this);
		
		if(type.equals("qq")){
			url=Constant.URL_PREFIX+"auth/qq";
			String[] ss=AccessTokenKeeper.getQQToken(this);
			if(ss!=null){
				accesstoken=ss[1];
			}
		}else if(type.equals("sina")){
			url=Constant.URL_PREFIX+"auth/weibo";
			accesstoken=AccessTokenKeeper.readAccessToken(this).getToken();
		}
		if(!TextUtils.isEmpty(accesstoken)){
			operate();
		}
	}

	@Override
	protected void onClickListener(int viewId) {
		// TODO Auto-generated method stub
		if(viewId==R.id.mloggin_btn){
			sUsername=username.getText().toString();
			sPassword=password.getText().toString();
			if(TextUtils.isEmpty(sUsername)){
				Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			if(TextUtils.isEmpty(sPassword)){
				Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			
			String[] ss=AccessTokenKeeper.getLaifuToken(this);
			if(ss!=null){
				token=ss[1];
			}
			url=Constant.URL_PREFIX+"auth/setaccount";
			operate2();
		}
	}
	public void saveUserInfo(){
		SharedPreferences sp=getSharedPreferences(Constant.USERINFO_SHAREPREFENCE, 0);
		Editor edit=sp.edit();
		UserInfoEntity entity=jsonData.userInfoEntity;
		edit.putString(Constant.ID, entity.id);
		edit.putString(Constant.BIRTHDAY, entity.birthday);
		edit.putString(Constant.SEX, entity.sex);
		edit.putString(Constant.USERNAME, entity.username);
		edit.putString(Constant.REGESITER_TIME, entity.regesiter_time);
		edit.putString(Constant.COVER_LARGE, entity.cover_large);
		edit.putString(Constant.NICK, entity.nick);
		edit.putString(Constant.AGE, entity.age);
		edit.putString(Constant.SIGNATURE, entity.signature);
		edit.putString(Constant.AVATAR_LARGE, entity.avatar_large);
		edit.putString(Constant.CITY, entity.city);
		edit.putString(Constant.AVATAR_SMALL, entity.avatar_small);
		edit.commit();
	}
	@Override
	public int setModelId() {
		// TODO Auto-generated method stub
		return R.layout.index;
	}

	@Override
	public int setLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.register_laifu;
	}
	
	
	LaifuLoginJsonData jsonData=new LaifuLoginJsonData();
	Thread t;
	String url=Constant.URL_PREFIX+"auth/laifu";
	HashMap<String,String> map;
	ArrayList<DefaultJSONData> list;
	String accesstoken;
	public void operate(){
		if (!DefaultTools.isAccessNetwork(this)) {
			DefaultTools.netErrorToBack(this);
			return ;
		}
		map=new HashMap<String,String>();
		map.put("access_token", accesstoken);
		list=new ArrayList<DefaultJSONData>();
		
		showProgressDialog("正在请求数据，请稍候...");
		t = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				int result = LaifuConnective.getServiceHttpData(registerLaifuActivity.this,url,map,jsonData,list, false, false, LaifuConnective.POST);

				Log.i("aa", "result-->"+result);
				if (result == 1) {
					jsonData=(LaifuLoginJsonData)list.get(0);
					if(jsonData.result==0){
						handle.sendEmptyMessage(SUCCESS);
					}else{
						Message msg=new Message();
						msg.what=FAILED;
						msg.obj=jsonData.message;
						handle.sendMessage(msg);
					}
				} else {
					Message msg=new Message();
					msg.what=ERROR;
					msg.obj=LaifuConnective.getPromptMsg(result);
					handle.sendMessage(msg);
				}
			}
		});
		t.start();
	}
	
	String sUsername,sPassword;
	String token;
	NormalJsonData normalJsonData=new NormalJsonData();
	public void operate2(){
		if (!DefaultTools.isAccessNetwork(this)) {
			DefaultTools.netErrorToBack(this);
			return ;
		}
		map=new HashMap<String,String>();
		map.put("token", token);
		map.put("username", sUsername);
		map.put("password", sPassword);
		
		list=new ArrayList<DefaultJSONData>();
		
		showProgressDialog("正在请求数据，请稍候...");
		t = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				int result = LaifuConnective.getServiceHttpData(registerLaifuActivity.this,url,map,normalJsonData,list, false, false, LaifuConnective.POST);

				Log.i("aa", "result-->"+result);
				if (result == 1) {
					jsonData=(LaifuLoginJsonData)list.get(0);
					if(jsonData.result==0){
						handle.sendEmptyMessage(Regesiter_SUCCESS);
					}else{
						Message msg=new Message();
						msg.what=FAILED;
						msg.obj=jsonData.message;
						handle.sendMessage(msg);
					}
				} else {
					Message msg=new Message();
					msg.what=ERROR;
					msg.obj=LaifuConnective.getPromptMsg(result);
					handle.sendMessage(msg);
				}
			}
		});
		t.start();
	}
}

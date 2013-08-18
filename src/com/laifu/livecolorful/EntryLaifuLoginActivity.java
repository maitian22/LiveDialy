package com.laifu.livecolorful;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.laifu.livecolorful.entity.UserInfoEntity;
import com.laifu.livecolorful.net.DefaultTools;
import com.laifu.livecolorful.net.LaifuConnective;
import com.laifu.livecolorful.parser.DefaultJSONData;
import com.laifu.livecolorful.parser.LaifuLoginJsonData;
import com.laifu.livecolorful.tool.Constant;
import com.laifu.livecolorful.wxapi.AccessTokenKeeper;

public class EntryLaifuLoginActivity extends LiveBaseActivity{
	
	EditText account_edit,mPassword_edit;
	Button mLoginBtn;
	Context mContext;
	ImageView mCloseImg;
	TextView mFetchPassword;
	
	
	Handler magicListHandle = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (progressDialog != null) {
				progressDialog.cancel();
			}
			switch (msg.what) {
			case SUCCESS:
				AccessTokenKeeper.setLaifuToken(EntryLaifuLoginActivity.this, jsonData.laifuTokenEntity.user_id, jsonData.laifuTokenEntity.token, Long.parseLong(jsonData.laifuTokenEntity.expires));
				saveUserInfo();
				Intent i = new Intent(EntryLaifuLoginActivity.this,IndexActivity.class);
				startActivity(i);
				Toast.makeText(mContext, "登陆成功", Toast.LENGTH_SHORT).show();
				break;
			case FAILED:
				Toast.makeText(mContext, (String)msg.obj, Toast.LENGTH_SHORT).show();
				break;
			case ERROR:
				alertDialog("来福提示", (String)msg.obj);
				break;
			}
		}
	};
	
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
	public void initPages() {
		// TODO Auto-generated method stub
		mLoginBtn = (Button)findViewById(R.id.mloggin_btn);
		mLoginBtn.setOnClickListener(this);
		
		mCloseImg = (ImageView)findViewById(R.id.close_img);
		mCloseImg.setOnClickListener(this);
		
		mFetchPassword = (TextView)findViewById(R.id.fetch_password);
		mFetchPassword.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		mFetchPassword.setOnClickListener(this);
		
		account_edit=(EditText)findViewById(R.id.account_edit);
		mPassword_edit=(EditText)findViewById(R.id.mPassword_edit);
	}

	@Override
	protected void onClickListener(int viewId) {
		// TODO Auto-generated method stub
		if(viewId == mLoginBtn.getId()){
			username=account_edit.getText().toString();
			password=mPassword_edit.getText().toString();
			if(TextUtils.isEmpty(username)){
				Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			if(TextUtils.isEmpty(password)){
				Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			operate();
			
		}else if(viewId == mCloseImg.getId()){
			finish();
		}else if(viewId == mFetchPassword.getId()){
			Intent i = new Intent(this,FetchPasswordActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
		}
	}

	@Override
	public int setModelId() {
		// TODO Auto-generated method stub
		return R.layout.index;
	}

	@Override
	public int setLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.laifu_login;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mContext = this;
		super.onCreate(savedInstanceState);
	}
	
	LaifuLoginJsonData jsonData=new LaifuLoginJsonData();
	Thread t;
	String url=Constant.URL_PREFIX+"auth/laifu";
	HashMap<String,String> map;
	ArrayList<DefaultJSONData> list;
	String username,password;
	public void operate(){
		if (!DefaultTools.isAccessNetwork(this)) {
			DefaultTools.netErrorToBack(this);
			return ;
		}
		map=new HashMap<String,String>();
		map.put("username", username);
		map.put("password", password);
		list=new ArrayList<DefaultJSONData>();
		
		showProgressDialog("正在请求数据，请稍候...");
		t = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				int result = LaifuConnective.getServiceHttpData(mContext,url,map,jsonData,list, false, false, LaifuConnective.POST);

				Log.i("aa", "result-->"+result);
				if (result == 1) {
					jsonData=(LaifuLoginJsonData)list.get(0);
					if(jsonData.result==0){
						magicListHandle.sendEmptyMessage(SUCCESS);
					}else{
						Message msg=new Message();
						msg.what=FAILED;
						msg.obj=jsonData.message;
						magicListHandle.sendMessage(msg);
					}
				} else {
					Message msg=new Message();
					msg.what=ERROR;
					msg.obj=LaifuConnective.getPromptMsg(result);
					magicListHandle.sendMessage(msg);
				}
			}
		});
		t.start();
	}
}

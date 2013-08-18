package com.laifu.liveDialy.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.laifu.liveDialy.entity.LaifuTokenEntity;
import com.laifu.liveDialy.entity.UserInfoEntity;


public class NormalJsonData implements DefaultJSONData {
	public int result; // 结果
	public String message; // 消息
	public String error; // 错误信息
	public long severTime;
	//{"data":true,"status":0,"server_time":1376584776}

	public boolean data;
	public LaifuTokenEntity laifuTokenEntity;
	public UserInfoEntity userInfoEntity;
	
	@Override
	public void parse(JSONArray array) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void parse(JSONObject object) {
		// TODO Auto-generated method stub
		Log.i("aa", object.toString());
		severTime=object.optLong("server_time");
		result= object.optInt("status");
		if(result !=0){
			message=object.optString("error_message");
			return ;
		}else{
			data=object.optBoolean("data");
		}
		
	}

}

package com.laifu.livecolorful.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.laifu.livecolorful.entity.UserInfoEntity;


public class MyAttationJsonData implements DefaultJSONData{
	public int result; // 结果
	public String message; // 消息
	public String error; // 错误信息
	public long severTime;
	private String TAG = "MyAttationJsonData";
	// {"data":{"birthday":"1990-01-01","phone":null,"sex":"male","register_time":"2013年08月16日","email_verified":false,"following_count":0,"avatar_large":"http:\/\/img.sandbox.laifu.fm\/a\/l\/13\/08\/16\/r\/u\/q0pwzpdc.jpg","city":"北京","avatar_small":"http:\/\/img.sandbox.laifu.fm\/a\/s\/13\/08\/16\/r\/u\/q0pwzpdc.jpg","id":16,"phone_verified":false,"username":"dubin","cover_large":null,"nick":"asfdsa_gep","email":null,"age":23,"follower_count":0,"signature":null},"status":0,"server_time":1376663832}
	public UserInfoEntity userInfoEntity;

	@Override
	public void parse(JSONArray array) {
		// TODO Auto-generated method stub

	}

	@Override
	public void parse(JSONObject object) {
		// TODO Auto-generated method stub
		Log.i(TAG, object.toString());
		severTime = object.optLong("server_time");
		result = object.optInt("status");
		if (result != 0) {
			message = object.optString("error_message");
			return;
		} else {
			/*JSONObject dataJson = object.optJSONObject("data");
			userInfoEntity = new UserInfoEntity();
			userInfoEntity.birthday = dataJson.optString("birthday");
			userInfoEntity.sex = dataJson.optString("sex");
			userInfoEntity.username = dataJson.optString("username");
			userInfoEntity.regesiter_time = dataJson
					.optString("regesiter_time");
			userInfoEntity.cover_large = dataJson.optString("cover_large");
			userInfoEntity.nick = dataJson.optString("nick");
			userInfoEntity.age = dataJson.optString("age");
			userInfoEntity.signature = dataJson.optString("signature");
			userInfoEntity.avatar_large = dataJson.optString("avatar_large");
			userInfoEntity.city = dataJson.optString("city");
			userInfoEntity.avatar_small = dataJson.optString("avatar_small");
			userInfoEntity.id = dataJson.optString("id");*/
		}

	}
}

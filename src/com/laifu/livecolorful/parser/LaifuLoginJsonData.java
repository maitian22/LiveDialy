package com.laifu.livecolorful.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.laifu.livecolorful.entity.LaifuTokenEntity;
import com.laifu.livecolorful.entity.UserInfoEntity;


public class LaifuLoginJsonData implements DefaultJSONData {
	public int result; // 结果
	public String message; // 消息
	public String error; // 错误信息
	public long severTime;
	//{"data":{"token":{"token":"a3a42aa76c6de52bf7b9a11eb6ad43966a70b3af","user_id":12,"expires":1462960544},"user":{"id":12,"birthday":"1990-01-01","sex":"male","username":"ligang","register_time":"2013年08月09日","cover_large":"http:\/\/img.sandbox.laifu.fm\/p\/l\/13\/08\/10\/o\/r\/74rmwob8.jpg","nick":"w________s","age":23,"signature":null,"avatar_large":"http:\/\/img.sandbox.laifu.fm\/a\/l\/13\/08\/10\/t\/q\/pnu3a8n9.jpg","city":"北京 朝阳区","avatar_small":"http:\/\/img.sandbox.laifu.fm\/a\/s\/13\/08\/10\/t\/q\/pnu3a8n9.jpg"}},"status":0,"server_time":1376560544}
	
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
			JSONObject dataJson=object.optJSONObject("data");
			
			JSONObject tokenJson=dataJson.optJSONObject("token");
			laifuTokenEntity=new LaifuTokenEntity();
			laifuTokenEntity.token=tokenJson.optString("token");
			laifuTokenEntity.user_id=tokenJson.optString("user_id"); 
			laifuTokenEntity.expires=tokenJson.optString("expires"); 
			
			JSONObject userJson=dataJson.optJSONObject("user");
			userInfoEntity=new UserInfoEntity();
			userInfoEntity.birthday=userJson.optString("birthday");
			userInfoEntity.sex=userJson.optString("sex");
			userInfoEntity.username=userJson.optString("username");
			userInfoEntity.regesiter_time=userJson.optString("regesiter_time");
			userInfoEntity.cover_large=userJson.optString("cover_large");
			userInfoEntity.nick=userJson.optString("nick");
			userInfoEntity.age=userJson.optString("age");
			userInfoEntity.signature=userJson.optString("signature");
			userInfoEntity.avatar_large=userJson.optString("avatar_large");
			userInfoEntity.city=userJson.optString("city");
			userInfoEntity.avatar_small=userJson.optString("avatar_small");
		}
		
	}

}

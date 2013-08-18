package com.laifu.liveDialy.wxapi;

import com.weibo.sdk.android.Oauth2AccessToken;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
/**
 * 该类用于保存Oauth2AccessToken到sharepreference，并提供读取功能
 * @author xiaowei6@staff.sina.com.cn
 *
 */
public class AccessTokenKeeper {
	private static final String PREFERENCES_NAME = "com_weibo_sdk_android";
	/**
	 * 保存accesstoken到SharedPreferences
	 * @param context Activity 上下文环境
	 * @param token Oauth2AccessToken
	 */
	public static void keepAccessToken(Context context, Oauth2AccessToken token) {
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putString("token", token.getToken());
		editor.putLong("expiresTime", token.getExpiresTime());
		editor.commit();
	}
	/**
	 * 清空sharepreference
	 * @param context
	 */
	public static void clear(Context context){
	    SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
	    Editor editor = pref.edit();
	    editor.clear();
	    editor.commit();
	}

	/**
	 * 从SharedPreferences读取accessstoken
	 * @param context
	 * @return Oauth2AccessToken
	 */
	public static Oauth2AccessToken readAccessToken(Context context){
		Oauth2AccessToken token = new Oauth2AccessToken();
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		token.setToken(pref.getString("token", ""));
		token.setExpiresTime(pref.getLong("expiresTime", 0));
		return token;
	}
	
	public static String qq_shareperfence="com.tencent.sample";
	public static void setQQToken(Context context,String openid,String accesstoken,long expiresIn){
		SharedPreferences pref = context.getSharedPreferences(qq_shareperfence, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putString("token", accesstoken);
		editor.putString("openid", openid);
		editor.putLong("expiresIn", System.currentTimeMillis() + expiresIn * 1000);
		editor.commit();
	}
	public static String[] getQQToken(Context context){
		String[] token=new String[3];
		SharedPreferences pref = context.getSharedPreferences(qq_shareperfence, Context.MODE_APPEND);
		long expiresIn=pref.getLong("expiresIn", 0)-System.currentTimeMillis();
		if(expiresIn<=0){
			return null;
		}else{
			token[0]=pref.getString("openid", "");
			token[1]=pref.getString("token", "");
			token[2]=""+expiresIn/1000;
		}
		return token;
	}
	
	public static String laifu_token_shareperfence="com.laifu.token.shareprefence";
	public static void setLaifuToken(Context context,String user_id,String accesstoken,long expires){
		SharedPreferences pref = context.getSharedPreferences(laifu_token_shareperfence, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putString("token", accesstoken);
		editor.putString("user_id", user_id);
		editor.putLong("expires", System.currentTimeMillis() + expires * 1000);
		editor.commit();
	}
	public static String[] getLaifuToken(Context context){
		String[] token=new String[3];
		SharedPreferences pref = context.getSharedPreferences(laifu_token_shareperfence, Context.MODE_APPEND);
		long expires=pref.getLong("expires", 0)-System.currentTimeMillis();
		if(expires<=0){
			return null;
		}else{
			token[0]=pref.getString("user_id", "");
			token[1]=pref.getString("token", "");
			token[2]=""+expires/1000;
		}
		return token;
	}
}

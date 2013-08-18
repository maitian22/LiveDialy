package com.laifu.livecolorful;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.laifu.livecolorful.entity.UserInfoEntity;

public class GlobaleData {
	public static final int SUCCESS = 111;
	/***
	 * 联网成功，获取错误返回 比如参数错误，签名错误等
	 */
	public static final int FAILED = SUCCESS + 1;
	/***
	 * 联网失败，可能是没网或者超时，或者服务器service挂了 返回html代码
	 */
	public static final int ERROR = 113;

	/************************ 我的帐号 ******************************/
	/* 获取最热行程单 */
	public static List<Map<String, Object>> getHotestData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("leftimg", R.drawable.sina_on_btn);
		map.put("lines", R.color.red_line);
		map.put("title", "张根硕全球电视通告-中国歌迷会北京分会朝阳区分会");
		map.put("text0", "23580226");
		map.put("text1", "个成员");
		map.put("text2", "67556");
		map.put("text3", "个行程");
		map.put("text4", "分类排名：第1位");

		for (int i = 0; i < 6; i++) {
			list.add(map);
		}

		return list;
	}

	/* 获取最新的行程单 */
	public static List<Map<String, Object>> getLatestData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("leftimg", R.drawable.sina_on_btn);
		map.put("lines", R.color.red_line);
		map.put("title", "张根硕全球电视通告-中国歌迷会北京分会朝阳区分会");
		map.put("text1", "创建于七分钟前");
		for (int i = 0; i < 6; i++)
			list.add(map);
		return list;
	}

	/* 获取朋友推荐的行程单 */
	public static List<Map<String, Object>> getMyFriendAdviceData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("leftimg", R.drawable.sina_on_btn);
		map.put("lines", R.color.red_line);
		map.put("title", "张根硕全球电视通告-中国歌迷会北京分会朝阳区分会");
		map.put("text0", "草莓金丢丢");
		map.put("text1", "五分钟前创建了");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("leftimg", R.drawable.sina_on_btn);
		map.put("lines", R.color.red_line);
		map.put("title", "张根硕全球电视通告-中国歌迷会北京分会朝阳区分会");
		map.put("text0", "草莓金丢丢");
		map.put("text1", "五分钟前创建了");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("leftimg", R.drawable.sina_on_btn);
		map.put("lines", R.color.red_line);
		map.put("title", "张根硕全球电视通告-中国歌迷会北京分会朝阳区分会");
		map.put("text0", "草莓金丢丢");
		map.put("text1", "五分钟前创建了");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("leftimg", R.drawable.sina_on_btn);
		map.put("lines", R.color.red_line);
		map.put("title", "张根硕全球电视通告-中国歌迷会北京分会朝阳区分会");
		map.put("text0", "草莓金丢丢");
		map.put("text1", "五分钟前创建了");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("leftimg", R.drawable.sina_on_btn);
		map.put("lines", R.color.red_line);
		map.put("title", "张根硕全球电视通告-中国歌迷会北京分会朝阳区分会");
		map.put("text0", "草莓金丢丢");
		map.put("text1", "五分钟前创建了");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("leftimg", R.drawable.sina_on_btn);
		map.put("lines", R.color.red_line);
		map.put("title", "张根硕全球电视通告-中国歌迷会北京分会朝阳区分会");
		map.put("text0", "草莓金丢丢");
		map.put("text1", "五分钟前创建了");
		list.add(map);
		return list;
	}

	/* 获取附近行程单 */
	public static List<Map<String, Object>> getMyNearByData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("leftimg", R.drawable.sina_on_btn);
		map.put("lines", R.color.red_line);
		map.put("title", "张根硕全球电视通告-中国歌迷会北京分会朝阳区分会");
		map.put("img1", R.drawable.dz_blue_btn);
		map.put("text1", "1.5km");
		for (int i = 0; i < 6; i++) {
			list.add(map);
		}
		return list;
	}

	/* 获取我的关注 */
	public static List<Map<String, Object>> getMyAttationListData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("img", R.drawable.sina_on_btn);
		map.put("name", "维他命");
		map.put("addicon", R.drawable.arraw);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("img", R.drawable.sina_on_btn);
		map.put("name", "维他命");
		map.put("addicon", R.drawable.arraw);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("img", R.drawable.sina_on_btn);
		map.put("name", "维他命");
		map.put("addicon", R.drawable.arraw);
		list.add(map);
		return list;
	}

	/* 获取我的粉丝 */
	public static List<Map<String, Object>> getMyFansListData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("img", R.drawable.sina_on_btn);
		map.put("name", "维他命");
		map.put("addicon", R.drawable.gz_btn);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("img", R.drawable.sina_on_btn);
		map.put("name", "维他命");
		map.put("addicon", R.drawable.gz_btn);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("img", R.drawable.sina_on_btn);
		map.put("name", "维他命");
		map.put("addicon", R.drawable.gz_btn);
		list.add(map);
		return list;
	}

	/* 获取我的行程单 */
	public static List<Map<String, Object>> getMyRouontListData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("leftimg", R.drawable.sina_on_btn);
		map.put("lines", R.color.red_line);
		map.put("title", "张根硕全球电视通告-中国歌迷会北京分会朝阳区分会");
		map.put("text0", "23580266");
		map.put("text1", "个成员");
		map.put("text2", "67556");
		map.put("text3", "个行程");
		for (int i = 0; i < 6; i++)
			list.add(map);
		return list;
	}

	/* 获取我的头像图片 */
	public static Bitmap getMyheadPicture(Context mContext) {
		Bitmap bitmap = null;
		Uri headPic = Uri.parse("file://"
				+ "/sdcard/LiveColorful/Photo/headPic.png");
		try {
			bitmap = BitmapFactory.decodeStream(mContext.getContentResolver()
					.openInputStream(headPic));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;
	}

	/* 获取我的封面图片 */
	public static Bitmap getMyCoverPicture(Context mContext) {
		Bitmap bitmap = null;
		Uri headPic = Uri.parse("file://"
				+ "/sdcard/LiveColorful/Photo/CoverPic.png");
		try {
			bitmap = BitmapFactory.decodeStream(mContext.getContentResolver()
					.openInputStream(headPic));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;
	}

	public static final String[] MY_INFO_FEATURE = { "id", "nick",
			"sex", "city", "phone", "email", "signature" };
	/* 获取当前账户信息 */
	public static String[] getCurrentAccountMsg(Context mContext) {
		SharedPreferences mPre;
		mPre = PreferenceManager.getDefaultSharedPreferences(mContext);
		String[] mContent = new String[7];
		for (int i = 0; i < MY_INFO_FEATURE.length; i++) {
			String s = mPre.getString(MY_INFO_FEATURE[i], "");
			if(s.equals("null"))
				s = "";
			mContent[i] = s;
		}
		return mContent;
	}

	/* 保存当前用户信息 */
	public static void SaveCurrentAccountMsg(Context mContext,
			UserInfoEntity mUserInfo) {
		SharedPreferences mPre;
		mPre = PreferenceManager.getDefaultSharedPreferences(mContext);
		Editor mEditor = mPre.edit();
		mEditor.putString("id", mUserInfo.id);
		mEditor.putString("birthday", mUserInfo.birthday);
		mEditor.putString("sex", mUserInfo.sex.equals("male")?"男":"女");
		mEditor.putString("username", mUserInfo.username);
		mEditor.putString("regesiter_time", mUserInfo.regesiter_time);
		mEditor.putString("city", mUserInfo.city);
		mEditor.putString("nick", mUserInfo.nick);
		mEditor.putString("age", mUserInfo.age);
		mEditor.putString("avatar_large", mUserInfo.avatar_large);
		mEditor.putString("avatar_small", mUserInfo.avatar_small);
		mEditor.putString("signature", mUserInfo.signature);
		mEditor.putString("phone", mUserInfo.phone);
		mEditor.putString("email", mUserInfo.email);
		mEditor.putString("email_verified", mUserInfo.email_verified);
		mEditor.putString("follower_count", mUserInfo.follower_count);
		mEditor.putString("following_count", mUserInfo.following_count);
		mEditor.putString("cover_large", mUserInfo.cover_large);
		mEditor.putString("phone_verified", mUserInfo.phone_verified);
		mEditor.commit();
	}

	public static void SaveCurrentAccountMsg(Context mContext, String[] mContent) {
		SharedPreferences mPre;
		mPre = PreferenceManager.getDefaultSharedPreferences(mContext);
		for (int i = 0; i < MY_INFO_FEATURE.length; i++) {
			mPre.edit().putString(MY_INFO_FEATURE[i], mContent[i]).commit();
		}
	}

	/* 压缩图片 */
	public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
			double newHeight) {
		// 获取这个图片的宽和高
		float width = bgimage.getWidth();
		float height = bgimage.getHeight();
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 计算宽高缩放率
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
				(int) height, matrix, true);
		return bitmap;
	}

	/* 获取当前行程单个数 */
	public static int getCurrentRountListNumber() {
		return 29;
	}

	/* 获取当前关注人数 */
	public static int getCurrentAttationNumber() {
		return 168;
	}

	/* 获取当前粉丝人数 */
	public static int getCurrentFanNumber() {
		return 200;
	}

	/* 获取当前绑定帐号 */
	public static Map<String, Object> getCurrentBountAccount() {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("phone", false);
		map.put("sina", true);
		map.put("qq", false);

		return map;
		// return 0;
	}
	/************************ 我的帐号 ******************************/
}

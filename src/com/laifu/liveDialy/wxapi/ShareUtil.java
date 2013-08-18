package com.laifu.liveDialy.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.platformtools.Log;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.sso.SsoHandler;

public class ShareUtil {
	public Activity activity;
	String[] shareItem = { "微信好友", "新浪微博" };
	String[] item1 = { "微信朋友圈", "微信好友", "新浪微博" };
	String[] item2 = { "微信好友", "新浪微博" };
	String title;
	String message;
	String imageurl;
	String producturl;

	public IWXAPI api;
	String fromWhere = "";

	public ShareUtil(Activity activity, String fromWhere) {
		this.activity = activity;
//		api = WXAPIFactory.createWXAPI(activity, WXEntryActivity.APP_ID, true);
//		api.registerApp(WXEntryActivity.APP_ID);
		this.fromWhere = fromWhere;
	}

	/****
	 * 分享的方法 1，先选择分享的方式：新浪或者微信 2，当点击分享的时候直接调用分享API
	 * 
	 * @param title
	 *            分享的标题
	 * @param message
	 *            分享的内容
	 * @param imageurl
	 *            分享的图片
	 * @param producturl
	 *            分享的产品url 用于微信点击跳转到本app
	 */
	public void share(String title, String message, String imageurl, String producturl) {
		 this.title=title;
		this.message = message;
		this.imageurl = imageurl;
		this.producturl = producturl;
		if (checkSharetype()) {
			shareItem = item1;
		} else {
			shareItem = item2;
		}
		new AlertDialog.Builder(activity).setTitle("分享").setItems(shareItem, new AlertListener()).show();
	}
	
	class AlertListener implements OnClickListener {

		@Override
		public void onClick(DialogInterface view, int position) {
			String way = shareItem[position];
			if (way.equals("新浪微博")) {
				if("projectDetail".equals(fromWhere)){
				
				}else if("orderDetail".equals(fromWhere)){
				
				}else if("viewseat".equals(fromWhere)){
					
				}
				shareBySina(null);
			} else if (way.equals("微信好友")) {
				if("projectDetail".equals(fromWhere)){
				
				}else if("orderDetail".equals(fromWhere)){
				
				}else if("viewseat".equals(fromWhere)){
				
				}
				shareByWchart(0);
			} else if (way.equals("微信朋友圈")) {
				if("projectDetail".equals(fromWhere)){
					
				}else if("orderDetail".equals(fromWhere)){
					
				}else if("viewseat".equals(fromWhere)){
					
				}
				shareByWchart(SendMessageToWX.Req.WXSceneTimeline);
			}

		}

	}

	public void shareByWchart(int way) {
		Log.i("aa", "weixin share click");

		Intent intent = new Intent(activity, WXEntryActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("title", title);
		bundle.putString("message", message);
		bundle.putString("imageurl", imageurl);
		bundle.putString("producturl", producturl);
		bundle.putInt("way", way);
		intent.putExtras(bundle);
		activity.startActivity(intent);
	}

	public OnSinaOauthLintener onSinaOauthLintener;

	/*****
	 * 新浪微博分享 如果传null，直接分享 否则只执行获取token操作
	 * 
	 * @param onSinaOauthLintener
	 */
	public void shareBySina(OnSinaOauthLintener onSinaOauthLintener) {
		Log.i("aa", "xinlang share click");
		this.onSinaOauthLintener = onSinaOauthLintener;

		Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(activity);
		if (!token.isSessionValid()) {
			getSinaAccessToken(onSinaOauthLintener);
		} else {
			startSinaShare();
		}

	}

	public void startSinaShare() {
//		FragmentActivity a = (FragmentActivity) activity;
//		FragmentManager fgmr = a.getSupportFragmentManager();
//
//
//		FragmentTransaction ft = fgmr.beginTransaction();
//		AnimationUtil.startFragmentsAnimationWithRightInLeftOut(ft);
//		ShareDamai_fragment shareDamai_fragment = new ShareDamai_fragment();
//
//		Bundle bundle = new Bundle();
//		bundle.putString("title", title);
//		bundle.putString("message", message);
//		bundle.putString("imageurl", imageurl);
//		bundle.putString("producturl", producturl);
//		shareDamai_fragment.setArguments(bundle);
//
//		ft.add(R.id.fragmentRoot, shareDamai_fragment, FragmentFlagNameList.SHARESINA_FRAGMENT);
//		ft.addToBackStack(FragmentFlagNameList.SHARESINA_FRAGMENT);
//		ft.commit();
	}

	// 新浪微博的appkey
	public static final String AppKey = "1928291962";
	public static final String REDIRECT_URL = "http://www.4006009258.com/auth/callback/weibo";
	
//	public static final String AppKey = "2015087078";
//	public static final String REDIRECT_URL = "http://www.laifu.com";
	
	//我的账户测试
//	public static final String AppKey = "2015087078";
//	public static final String REDIRECT_URL = "http://www.laifu.com";
	

	private Weibo mWeibo;
	public static SsoHandler mSsoHandler;

	public void getSinaAccessToken(OnSinaOauthLintener onSinaOauthLintener) {
		this.onSinaOauthLintener = onSinaOauthLintener;
		mWeibo = Weibo.getInstance(AppKey, REDIRECT_URL);
		mSsoHandler = new SsoHandler(activity, mWeibo);
		mSsoHandler.authorize(new AuthDialogListener());
	}

	private Oauth2AccessToken accessToken;

	class AuthDialogListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {
			String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			accessToken = new Oauth2AccessToken(token, expires_in);
			if (accessToken.isSessionValid()) {
				Log.i("aa", "size--" + values.size());
				for (String ss : values.keySet()) {
					Log.i("aa", ss + "-->" + values.getString(ss));
				}

				AccessTokenKeeper.keepAccessToken(activity, accessToken);
				Toast.makeText(activity, "认证成功", Toast.LENGTH_SHORT).show();
				mSsoHandler = null;
				// UserController.getInstance().login(token, activity,
				// msgLoginHandler);
				if (onSinaOauthLintener != null) {
					onSinaOauthLintener.onComplete();
				} else {
					startSinaShare();
				}
			}
		}

		@Override
		public void onError(WeiboDialogError e) {
			if (onSinaOauthLintener != null) {
				onSinaOauthLintener.onError("认证错误 ");
			}

			mSsoHandler = null;
		}

		@Override
		public void onCancel() {
			if (onSinaOauthLintener != null) {
				onSinaOauthLintener.onError("取消认证");
			}

			mSsoHandler = null;
		}

		@Override
		public void onWeiboException(WeiboException e) {
			if (onSinaOauthLintener != null) {
				onSinaOauthLintener.onError("身份认证错误");
			}

			mSsoHandler = null;
		}

	}

	public boolean checkSharetype() {
		int wxSdkVersion = api.getWXAppSupportAPI();
		if (wxSdkVersion >= WXEntryActivity.TIMELINE_SUPPORTED_VERSION) {
			return true;
		} else {
			return false;
		}
	}

	public interface OnSinaOauthLintener {
		public void onComplete();

		public void onError(String result);
	}
}

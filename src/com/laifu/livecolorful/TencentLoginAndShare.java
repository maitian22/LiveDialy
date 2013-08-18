package com.laifu.livecolorful;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.laifu.livecolorful.tool.Constant;
import com.laifu.livecolorful.wxapi.AccessTokenKeeper;
import com.tencent.open.HttpStatusException;
import com.tencent.open.NetworkUnavailableException;
import com.tencent.tauth.Constants;
import com.tencent.tauth.IRequestListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

public class TencentLoginAndShare {
	public static Tencent mTencent;
	private static Activity mActivity;
	private static String TAG = "TencentLogAndShare";
	private static IUiListener mIuiListener;
	private IRequestListenerRequest mRequestResult;
	
	interface IRequestListenerRequest{
		void onComplete(JSONObject response);
	}
	
	public void setRequestResult(IRequestListenerRequest result){
		mRequestResult = result;
	}

	public TencentLoginAndShare(Activity activity, IUiListener mlistener) {
		mActivity = activity;
		mIuiListener = mlistener;
		mTencent = Tencent.createInstance(Constant.APP_ID_FOR_QQ, mActivity);
	}
	
	public class BaseApiListener implements IRequestListener {
		private String mScope = "all";
		private Boolean mNeedReAuth = false;
		private IUiListener iuiListener;

		public BaseApiListener(String scope, boolean needReAuth,
				IUiListener mbaseUilListener) {
			mScope = scope;
			mNeedReAuth = needReAuth;
			iuiListener = mbaseUilListener;
		}

		@Override
		public void onComplete(final JSONObject response, Object state) {
			Log.i(TAG, "IRequestListener.onComplete:" + response.toString());
			doComplete(response, state);
		}

		protected void doComplete(JSONObject response, Object state) {
			try {
				Log.i(TAG,"BaseApiListener doComplete------------>");
				int ret = response.getInt("ret");
				if (ret == 100030) {
					if (mNeedReAuth) {
						Runnable r = new Runnable() {
							public void run() {
								mTencent.reAuth(mActivity, mScope, iuiListener);
							}
						};
						mActivity.runOnUiThread(r);
					}

				} else
					//iuiListener.onComplete(response);
					mRequestResult.onComplete(response);
			} catch (JSONException e) {
				e.printStackTrace();
				Log.e("toddtest", response.toString());
			}

		}

		@Override
		public void onIOException(final IOException e, Object state) {
			Log.e(TAG, "IRequestListener.onIOException:" + e.getMessage());
		}

		@Override
		public void onMalformedURLException(final MalformedURLException e,
				Object state) {
			Log.e(TAG,
					"IRequestListener.onMalformedURLException" + e.toString());
		}

		@Override
		public void onJSONException(final JSONException e, Object state) {
			Log.e("IRequestListener.onJSONException:", e.getMessage());
		}

		@Override
		public void onConnectTimeoutException(ConnectTimeoutException arg0,
				Object arg1) {
			Log.e("IRequestListener.onConnectTimeoutException:",
					arg0.getMessage());

		}

		@Override
		public void onSocketTimeoutException(SocketTimeoutException arg0,
				Object arg1) {
			Log.e(TAG,
					"IRequestListener.SocketTimeoutException:"
							+ arg0.getMessage());
		}

		@Override
		public void onUnknowException(Exception arg0, Object arg1) {
			Log.e(TAG,
					"IRequestListener.onUnknowException:" + arg0.getMessage());
		}

		@Override
		public void onHttpStatusException(HttpStatusException arg0, Object arg1) {
			Log.e(TAG,
					"IRequestListener.HttpStatusException:" + arg0.getMessage());
		}

		@Override
		public void onNetworkUnavailableException(
				NetworkUnavailableException arg0, Object arg1) {
			Log.e("IRequestListener.onNetworkUnavailableException:",
					arg0.getMessage());
		}
	}

	public BaseApiListener getBaseListener(String scope, boolean needReAuth,
			IUiListener mbaseUilListener) {
		return new BaseApiListener(scope, needReAuth, mbaseUilListener);
	}

	public Tencent getTencent() {
		return mTencent;
	}
	public void AddShareToQQWeiBo(Bundle parmas){
		mTencent.requestAsync(Constants.GRAPH_ADD_PIC_T, parmas,
				Constants.HTTP_POST,
				getBaseListener("add_pic_t", false, mIuiListener), null);
	}
	public void AddShareToQzone(Bundle parmas) {
		mTencent.requestAsync(Constants.GRAPH_ADD_SHARE, parmas,
				Constants.HTTP_POST,
				getBaseListener("add_share", true, mIuiListener), null);
	}

	public void saveQQLoginInfomation(JSONObject values) {
		long time;
		try {
			time = Long.parseLong(values.getString("expires_in"));
			AccessTokenKeeper.setQQToken(mActivity, values.getString("openid"),
					values.getString("access_token"), time);
			Log.i(TAG, "setQQToken openid:" + values.getString("openid")
					+ ",access_token" + values.getString("access_token")
					+ ",time:" + time);
			// 调用服务器接口返回 来福token

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public boolean onActivityResult(int requestCode, int resultCode, Intent data){
		return mTencent.onActivityResult(requestCode, resultCode, data) ;
	}
	
	public boolean CheckQQLogin() {
		boolean hasLogin=false;
		String[] token = AccessTokenKeeper.getQQToken(mActivity);
		if (token != null) {
			Log.i(TAG, "token[0]:" + token[0] + ",token[1]:" + token[1]);
			mTencent.setOpenId(token[0]);
			mTencent.setAccessToken(token[1], token[2]);
			hasLogin=true;
		} else {
			mTencent.login(mActivity, "all", mIuiListener);
			hasLogin=false;
		}
		return hasLogin;
	}

	public boolean ready() {
		if (mTencent == null) {
			return false;
		}
		boolean ready = mTencent.isSessionValid()
				&& mTencent.getOpenId() != null;
		if (!ready)
			// Toast.makeText(mContext, "/*login and get openId first, please!",
			// Toast.LENGTH_SHORT).show();
			Toast.makeText(mActivity, "请先登入QQ帐号!", Toast.LENGTH_SHORT).show();
		return ready;
	}

}

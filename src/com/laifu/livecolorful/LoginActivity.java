package com.laifu.livecolorful;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.laifu.livecolorful.tool.Tools;
import com.laifu.livecolorful.wxapi.ShareUtil;
import com.laifu.livecolorful.wxapi.ShareUtil.OnSinaOauthLintener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class LoginActivity extends LiveBaseActivity {
	TextView mTextDes;
	SpannableString msp;
	ImageView mSinaImg, mQQImg, mlaifuImg;
	Context mContext;
	String TAG = "aa";
	public static Tencent mTencent;
	private TencentLoginAndShare mTencentLoginAndShare;

	private void InitTextdes() {
		mTextDes = (TextView) findViewById(R.id.text_des);
		msp = new SpannableString("嗨，欢迎来到来福大家庭！\n刚下载的朋友可能不知道这个App是干什么的，" + "没关系！熟悉之后你会发现：真是太好用了！");
		msp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.login_laifu)), 6, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		msp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		msp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 8, 55, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		msp.setSpan(new AbsoluteSizeSpan(35), 0, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		mTextDes.setText(msp);
	}

	@Override
	public void initPages() {
		// TODO Auto-generated method stub
		mContext = this;
		InitTextdes();
		mSinaImg = (ImageView) findViewById(R.id.sina_icon);
		mSinaImg.setOnClickListener(this);
		mQQImg = (ImageView) findViewById(R.id.qq_icon);
		mQQImg.setOnClickListener(this);
		mlaifuImg = (ImageView) findViewById(R.id.laifu_icon);
		mlaifuImg.setOnClickListener(this);

		mTencentLoginAndShare = new TencentLoginAndShare(this, new BaseUiListener());
	}

	private void onClickLogin() {
		if (mTencentLoginAndShare.CheckQQLogin()) {
			Intent i = new Intent(mContext, registerLaifuActivity.class);
			i.putExtra("type", "qq");
			startActivity(i);
		}
	}

	@Override
	protected void onClickListener(int viewId) {
		// TODO Auto-generated method stub
		if (viewId == mSinaImg.getId()) {
			LoginBySina();
		} else if (viewId == mQQImg.getId()) {
			//onClickLogin();
			 Intent i = new Intent(mContext,registerLaifuActivity.class);
			 i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			 i.putExtra("type", "qq");
			 startActivity(i);
		} else if (viewId == mlaifuImg.getId()) {
//			Log.i("aa", Tools.getUseragent(this));
			Intent i = new Intent(mContext, EntryLaifuLoginActivity.class);
			startActivity(i);
		}
	}

	@Override
	public int setModelId() {
		// TODO Auto-generated method stub
		return R.layout.login;
	}

	@Override
	public int setLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.login;
	}

	public void LoginBySina() {
		ShareUtil share = new ShareUtil(this, null);
		share.getSinaAccessToken(new OnSinaOauthLintener() {

			@Override
			public void onError(String result) {
				// TODO Auto-generated method stub
				Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onComplete() {
				// TODO Auto-generated method stub
				Log.i("aa", "size--" + "chenggong");
				// 调用服务器接口返回 来福token
				Intent i = new Intent(mContext, registerLaifuActivity.class);
				i.putExtra("type", "sina");
				startActivity(i);
			}
		});
	}

	private class BaseUiListener implements IUiListener {

		@Override
		public void onComplete(JSONObject response) {
			doComplete(response);
			Log.i(TAG, "response:" + response.toString());
		}

		protected void doComplete(JSONObject values) {
			Toast.makeText(LoginActivity.this, "认证成功", Toast.LENGTH_SHORT).show();
			mTencentLoginAndShare.saveQQLoginInfomation(values);

			Intent i = new Intent(mContext, registerLaifuActivity.class);
			i.putExtra("type", "qq");
			startActivity(i);
		}

		@Override
		public void onError(UiError e) {
			Log.i("aa", "认证错误" + e.errorDetail);
		}

		@Override
		public void onCancel() {
			Log.i("aa", "认证取消");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.i(TAG, "requestCode:" + requestCode + ",resultCode" + resultCode);

		if (32973 == requestCode) {
			/**
			 * 下面两个注释掉的代码，仅当sdk支持sso时有效，
			 */
			if (ShareUtil.mSsoHandler != null) {
				ShareUtil.mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
			}
		} else if (5657 == requestCode) {
			mTencentLoginAndShare.onActivityResult(requestCode, resultCode, data);
		}
	}

}

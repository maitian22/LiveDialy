package com.laifu.liveDialy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.laifu.liveDialy.parser.DefaultJSONData;
import com.laifu.liveDialy.parser.MyInfoJsonData;
import com.laifu.liveDialy.tool.Constant;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class MyInfoCtrl implements Button.OnClickListener {
	private RelativeLayout mRel;
	private static Button editInfo, updateheadPic, modifyCover;
	private static ThirdActivity mActivity;
	private ArrayList<EditText> mEditArray;
	private int[] mEditNameId = { R.id.edit_id, R.id.edit_nickname,
			R.id.edit_gender, R.id.edit_area, R.id.edit_tel, R.id.edit_email,
			R.id.edit_briefIntro };
	private static final int TAKE_PICTURE = 0;
	private static final int CHOOSE_PICTURE = 1;
	private ImageView mBoundPhoneImg, mBoundSinaImg, mBoundQQImg;
	private String TAG = "MyInfoCtrl";
	private TencentLoginAndShare mTencentLogAndShare;
	private ProgressDialog mProgressDialog;

	private TencentLoginAndShare.IRequestListenerRequest mRequestListener = new TencentLoginAndShare.IRequestListenerRequest() {
		@Override
		public void onComplete(JSONObject response) {
			// TODO Auto-generated method stub
			if (mProgressDialog.isShowing())
				mProgressDialog.dismiss();
		}

	};

	public MyInfoCtrl(ThirdActivity c, RelativeLayout ml) {
		mActivity = c;
		mRel = ml;
		mEditArray = new ArrayList<EditText>();
		initView();
	}

	public Tencent getTencent() {
		return mTencentLogAndShare.getTencent();
	}

	private void InitEditTextView() {
		mEditArray.clear();
		String[] mAccount = GlobaleData.getCurrentAccountMsg(mActivity);
		for (int i = 0; i < mEditNameId.length; i++) {
			EditText mEdit = (EditText) mRel.findViewById(mEditNameId[i]);
			String disText = mAccount[i];
			mEdit.setText(disText);
			mEdit.setCursorVisible(false);
			mEdit.setClickable(false);
			mEdit.setFocusable(false);
			mEditArray.add(mEdit);
		}
	}
	
	public void InitEditTextData(){
		String[] mAccount = GlobaleData.getCurrentAccountMsg(mActivity);
		for (int i = 0; i < mEditNameId.length; i++){
			EditText mEdit = (EditText) mRel.findViewById(mEditNameId[i]);
			if(mAccount[i].equals("null"))
				mAccount[i] = "";
			String disText = mAccount[i];
			mEdit.setText(disText);
		}
	}

	void InitBoundAccount() {
		Map<String, Object> map = GlobaleData.getCurrentBountAccount();
		boolean IsPhoneBound = Boolean
				.parseBoolean(map.get("phone").toString());
		boolean IsSinaBound = Boolean.parseBoolean(map.get("sina").toString());
		boolean IsQQBound = Boolean.parseBoolean(map.get("qq").toString());

		mBoundPhoneImg = (ImageView) mRel.findViewById(R.id.mPhone);
		mBoundPhoneImg
				.setBackgroundResource(IsPhoneBound ? R.drawable.iphone_on_btn
						: R.drawable.iphone_0ff_btn);
		mBoundPhoneImg.setOnClickListener(this);

		mBoundSinaImg = (ImageView) mRel.findViewById(R.id.sina);
		mBoundSinaImg
				.setBackgroundResource(IsSinaBound ? R.drawable.sina_on_btn
						: R.drawable.sina_off_btn);
		mBoundSinaImg.setOnClickListener(this);

		mBoundQQImg = (ImageView) mRel.findViewById(R.id.qq);
		mBoundQQImg.setBackgroundResource(IsQQBound ? R.drawable.qq_on_btn
				: R.drawable.qq_off_btn);
		mBoundQQImg.setOnClickListener(this);
	}

	ArrayList<DefaultJSONData> list;
	MyInfoJsonData jsonData = new MyInfoJsonData();

	private void initView() {
		mTencentLogAndShare = new TencentLoginAndShare(mActivity,
				mBaseUiListener);
		mProgressDialog = new ProgressDialog(mActivity);

		editInfo = (Button) mRel.findViewById(R.id.editInfo);
		updateheadPic = (Button) mRel.findViewById(R.id.updateheadPic);
		modifyCover = (Button) mRel.findViewById(R.id.modifyCover);

		editInfo.setOnClickListener(this);
		updateheadPic.setOnClickListener(this);
		modifyCover.setOnClickListener(this);
		
		InitEditTextView();
	}
	
	private void saveEditInfomation() {
		String[] mContent = new String[mEditArray.size()];
		for (int i = 0; i < mEditArray.size(); i++) {
			mContent[i] = mEditArray.get(i).getEditableText().toString();
		}
		GlobaleData.SaveCurrentAccountMsg(mActivity, mContent);
		mActivity.initNickNameAndBriefIntro();
		mActivity.PushEditInfoToServer(mContent);
	}

	private void hideSoftKeypad() {
		InputMethodManager imm = (InputMethodManager) mActivity
				.getSystemService(mActivity.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mEditArray.get(0).getWindowToken(), 0);
	}

	private void freezeEditText() {
		for (int i = 0; i < mEditArray.size(); i++) {
			mEditArray.get(i).setCursorVisible(false);
			mEditArray.get(i).setClickable(false);
			mEditArray.get(i).setFocusable(false);
		}
		editInfo.setBackgroundDrawable(mActivity.getResources().getDrawable(
				R.drawable.shape));
		editInfo.setText(R.string.edit_info);

	}

	public void showPicturePicker(Context context, final int requestCode) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("图片来源");
		builder.setNegativeButton("取消", null);
		builder.setItems(new String[] { "拍照", "相册" },
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case TAKE_PICTURE:
							// 保存本次截图临时文件名字
							String dictoryPath = Constant.PitcturPath;
							String fileName = String.valueOf(System
									.currentTimeMillis()) + ".jpg";
							Editor editor = mActivity.mPre.edit();
							editor.putString(
									Constant.PICTURE_CAPTURE_IMAGE_NAME,
									fileName);
							editor.commit();
							Intent openCameraIntent = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							Uri imageUri = Uri.fromFile(new File(dictoryPath,
									fileName));
							openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
									imageUri);
							// openCameraIntent.putExtra("return-data", true);
							mActivity.startActivityForResult(openCameraIntent,
									requestCode);
							break;

						case CHOOSE_PICTURE:
							Intent intent = new Intent(Intent.ACTION_PICK, null);
							intent.setDataAndType(
									MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
									"image/*");
							mActivity.startActivityForResult(intent,
									requestCode);
							break;

						default:
							break;
						}
					}
				});
		builder.create().show();
	}

	@Override
	public void onClick(View v) {
		int i = 0;
		// TODO Auto-generated method stub
		if (v == editInfo) {
			if (editInfo.getText().toString()
					.equals(mActivity.getString(R.string.finish))) {
				freezeEditText();
				hideSoftKeypad();
				saveEditInfomation();
				updateheadPic.setVisibility(android.view.View.VISIBLE);
				modifyCover.setText(R.string.modify_cover);

			} else {
				for (i = 0; i < mEditArray.size(); i++) {
					mEditArray.get(i).setCursorVisible(true);
					mEditArray.get(i).setClickable(true);
					mEditArray.get(i).setFocusable(true);
					mEditArray.get(i).setFocusableInTouchMode(true);
				}
				editInfo.setBackgroundDrawable(mActivity.getResources()
						.getDrawable(R.drawable.shape_blue));
				editInfo.setText(R.string.finish);

				updateheadPic.setVisibility(android.view.View.INVISIBLE);
				modifyCover.setText(R.string.cancel);
			}

		} else if (v == updateheadPic) {
			showPicturePicker(mActivity, Constant.PICTURE_HEAD_CAPTUIE);
		} else if (v == modifyCover) {
			if (editInfo.getText().toString()
					.equals(mActivity.getString(R.string.finish))) {
				freezeEditText();
				hideSoftKeypad();
				updateheadPic.setVisibility(android.view.View.VISIBLE);
				modifyCover.setText(R.string.modify_cover);
				InitEditTextView();

			} else {
				showPicturePicker(mActivity, Constant.PICTURE_COVER_CAPTUIE);
			}
		} else if (v == mBoundQQImg) {
			Log.i(TAG, "click mBoundQQImg ----------->");
	//		mTencentLogAndShare.CheckQQLogin();
	//		onClickAddShare();
		} else if (v == mBoundSinaImg) {
	//		mTencentLogAndShare.CheckQQLogin();
	//		onClickAddPicUrlTweet();

		}
	}

	private void onClickAddPicUrlTweet() {
		if (mTencentLogAndShare.ready()) {
			Bundle bundle = new Bundle();
			bundle.putString("format", "json");
			bundle.putString("content",
					"test add pic with url" + System.currentTimeMillis());
			// params.putString("clientip", "127.0.0.1");

			// 把 bitmap 转换为 byteArray , 用于发送请求
			Bitmap bitmap = BitmapFactory.decodeResource(
					mActivity.getResources(), R.drawable.ic_launcher);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);
			byte[] buff = baos.toByteArray();
			// Log.v(TAG, "length: " + buff.length);
			bundle.putByteArray("pic", buff);
			// mTencent.requestAsync(Constants.GRAPH_ADD_PIC_T, bundle,
			// Constants.HTTP_POST, new BaseApiListener("add_pic_t", false),
			// null);
			mTencentLogAndShare.setRequestResult(mRequestListener);
			mTencentLogAndShare.AddShareToQQWeiBo(bundle);
			bitmap.recycle();

			mProgressDialog.show();
		}
	}

	private void onClickAddShare() {
		if (mTencentLogAndShare.ready()) {
			Bundle parmas = new Bundle();
			parmas.putString("title", "QQ登陆SDK：Add_Share测试---->>>>>>");// 必须。feeds的标题，最长36个中文字，超出部分会被截断。
			parmas.putString("url",
					"http://www.qq.com" + "#" + System.currentTimeMillis());// 必须。分享所在网页资源的链接，点击后跳转至第三方网页，
																			// 请以http://开头。
			parmas.putString("comment", ("QQ登陆SDK：测试comment" + new Date()));// 用户评论内容，也叫发表分享时的分享理由。禁止使用系统生产的语句进行代替。最长40个中文字，超出部分会被截断。
			parmas.putString("summary", "QQ登陆SDK：测试summary");// 所分享的网页资源的摘要内容，或者是网页的概要描述。
																// 最长80个中文字，超出部分会被截断。
			parmas.putString("images",
					"http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");// 所分享的网页资源的代表性图片链接"，请以http://开头，长度限制255字符。多张图片以竖线（|）分隔，目前只有第一张图片有效，图片规格100*100为佳。
			parmas.putString("type", "5");// 分享内容的类型。
			parmas.putString(
					"playurl",
					"http://player.youku.com/player.php/Type/Folder/Fid/15442464/Ob/1/Pt/0/sid/XMzA0NDM2NTUy/v.swf");// 长度限制为256字节。仅在type=5的时候有效。

			mTencentLogAndShare.setRequestResult(mRequestListener);
			mTencentLogAndShare.AddShareToQzone(parmas);
			mProgressDialog.show();
		}
	}

	private IUiListener mBaseUiListener = new IUiListener() {

		@Override
		public void onComplete(JSONObject response) {
			Log.i(TAG, "onComplete---------->");
			doComplete(response);
			mTencentLogAndShare.saveQQLoginInfomation(response);
		}

		protected void doComplete(JSONObject values) {
		}

		@Override
		public void onError(UiError e) {
			Log.e(TAG, "onError:" + "code:" + e.errorCode + ", msg:"
					+ e.errorMessage + ", detail:" + e.errorDetail);
		}

		@Override
		public void onCancel() {
			Log.e(TAG, "onCancel" + "");
		}
	};
}
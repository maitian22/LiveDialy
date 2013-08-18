package com.laifu.liveDialy;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.laifu.liveDialy.adapter.MyRountListAdapter;
import com.laifu.liveDialy.entity.UserInfoEntity;
import com.laifu.liveDialy.net.DefaultTools;
import com.laifu.liveDialy.net.LaifuConnective;
import com.laifu.liveDialy.parser.DefaultJSONData;
import com.laifu.liveDialy.parser.MyAttationJsonData;
import com.laifu.liveDialy.parser.MyInfoJsonData;
import com.laifu.liveDialy.tool.Constant;
import com.laifu.liveDialy.wxapi.AccessTokenKeeper;

public class ThirdActivity extends LiveBaseActivity {

	
	void getMyInfoJsonData(){
		if (!DefaultTools.isAccessNetwork(this)) {
			DefaultTools.netErrorToBack(this);
			return ;
		}
		String[] ss=AccessTokenKeeper.getLaifuToken(this);
		if(ss!=null){
			map.clear();
			map.put("token", ss[1]);
		}
		jsonType = LaifuConnective.GET;
		new GetAndSaveMyInfoJsonDataTask().execute();
	}
	//获取个人资料 
	private String getUserProUrl = Constant.URL_PREFIX + "user/profile";
	private HashMap<String, String> map = new HashMap<String, String>();
	private MyInfoJsonData myInfojsonData = new MyInfoJsonData();
	private ArrayList<DefaultJSONData> list = new ArrayList<DefaultJSONData>();
	private boolean isUseCash = false;
	private boolean isuseSDcash = false;
	private String jsonType;
	public UserInfoEntity mUserInfo;
	//获取关注列表
	private String getAttationUrl = Constant.URL_PREFIX + "friend/followings";
	private HashMap<String, String> attationMap = new HashMap<String, String>();
	private MyAttationJsonData attationjsonData = new MyAttationJsonData();
	private ArrayList<DefaultJSONData> attationlist = new ArrayList<DefaultJSONData>();
	private String attationjsonType;
	public UserInfoEntity mAttationInfo;
	
	class GetAndSaveMyInfoJsonDataTask extends AsyncTask<String, String, Integer> {
		String message="";
		@Override
		protected Integer doInBackground(String... params) {
			int result = LaifuConnective.getServiceHttpData(mContext,
					getUserProUrl, map, myInfojsonData, list,
					isUseCash, isuseSDcash, jsonType);

			if (result == 1) {
				myInfojsonData = (MyInfoJsonData) list.get(0);
				if (myInfojsonData.result == 0) {
					mUserInfo = myInfojsonData.userInfoEntity;
					GlobaleData.SaveCurrentAccountMsg(mContext, myInfojsonData.userInfoEntity);
					return SUCCESS;
				} else {
					message = myInfojsonData.message;
					return FAILED;	
				}
			} else {
				message = LaifuConnective.getPromptMsg(result);
				return ERROR;	
			}
		}

		@Override
		protected void onPreExecute() {
			showProgressDialog();
		}

		@Override
		protected void onPostExecute(Integer flag) {
			cancelProgressDialog();
			switch(flag){
			case SUCCESS:
				if(mMyInfoCtrl!=null){
					mMyInfoCtrl.InitEditTextData();
				}
				break;
			case FAILED:
				Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
				break;
			case ERROR:
				alertDialog("来福提示", message);
				break;
			}
		}
	}
	
	public void PushEditInfoToServer(String[] content){
		if (!DefaultTools.isAccessNetwork(this)) {
			DefaultTools.netErrorToBack(this);
			return ;
		}
		String[] ss=AccessTokenKeeper.getLaifuToken(this);
		if(ss!=null){
			map.clear();
			map.put("token", ss[1]);
			for (int i = 0; i < GlobaleData.MY_INFO_FEATURE.length; i++) {
				map.put(GlobaleData.MY_INFO_FEATURE[i], content[i]);
			}
		}else{
			Toast.makeText(mContext, "缺少授权",Toast.LENGTH_SHORT).show();
			return;
		}
		
		jsonType = LaifuConnective.POST;
		new GetAndSaveMyInfoJsonDataTask().execute();
	}

	@Override
	public void initPages() {
		// TODO Auto-generated method stub
		mContext = this;
		getMyInfoJsonData();
		initView();
		initTitle();
		InitMyInfoPicture();
		InitCountNumbers();
	}

	@Override
	protected void onClickListener(int viewId) {
		// TODO Auto-generated method stub

	}

	@Override
	public int setModelId() {
		// TODO Auto-generated method stub
		return R.layout.third;
	}

	@Override
	public int setLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.third;
	}

	private static String TAG = "MyAccouontActivity";
	private LinearLayout my_rount_linear, my_fans_linear, my_attation_linear,
			my_info_linear;
	private ImageView my_rount_arrow, my_fans_arrow, my_attation_arrow,
			my_info_arrow;
	public SharedPreferences mPre;
	private int mCurrentPage;
	private ListView my_rount_list, my_attation_list;
	private RelativeLayout my_rount, my_attation, my_fans, my_info;
	private Context mContext;
	private MyInfoCtrl mMyInfoCtrl;
	private ImageView head_portrait;
	private TextView mNickName, mBriefIntro;
	private RelativeLayout mAccoutBg;
	private TextView mRountListNum, mAttationNum, mFansNum;
	private LinearLayout mArrowBg;
	private MyFansListCtrl mMyFansListCtrl;
	public static UserInfoEntity userInfoEntity;

	private OnClickListener mylinearListner = new LinearLayout.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.my_rount_linear:
				mCurrentPage = 0;
				break;
			case R.id.my_attation_linear:
				mCurrentPage = 1;
				break;
			case R.id.my_fans_linear:
				mCurrentPage = 2;
				break;
			case R.id.my_info_linear:
				mCurrentPage = 3;
				break;
			}
			mPre.edit()
					.putInt(Constant.CURRENT_THIRD_ACTIVITY_PAGE, mCurrentPage)
					.commit();
			initHighlight();
		}

	};

	private void initHighlight() {
		Log.i(TAG, "initHighlightArrow,mCurrentPage:" + mCurrentPage);
		my_rount_arrow.setVisibility(android.view.View.INVISIBLE);
		my_fans_arrow.setVisibility(android.view.View.INVISIBLE);
		my_attation_arrow.setVisibility(android.view.View.INVISIBLE);
		my_info_arrow.setVisibility(android.view.View.INVISIBLE);

		my_rount.setVisibility(android.view.View.INVISIBLE);
		my_fans.setVisibility(android.view.View.INVISIBLE);
		my_attation.setVisibility(android.view.View.INVISIBLE);
		my_info.setVisibility(android.view.View.INVISIBLE);

		if (mCurrentPage == 0) {
			mArrowBg.setBackgroundColor(this.getResources().getColor(
					R.color.grey));  //三角形图片下面的背景
			my_rount_arrow.setVisibility(android.view.View.VISIBLE);//三角形图片
			my_rount.setVisibility(android.view.View.VISIBLE); //对应的下半部分
		} else if (mCurrentPage == 1) {
			mArrowBg.setBackgroundColor(this.getResources().getColor(
					R.color.white));
			my_attation_arrow.setVisibility(android.view.View.VISIBLE);
			my_attation.setVisibility(android.view.View.VISIBLE);
		} else if (mCurrentPage == 2) {
			mArrowBg.setBackgroundColor(this.getResources().getColor(
					R.color.white));
			my_fans_arrow.setVisibility(android.view.View.VISIBLE);
			my_fans.setVisibility(android.view.View.VISIBLE);
		} else if (mCurrentPage == 3) {
			mArrowBg.setBackgroundColor(this.getResources().getColor(
					R.color.white));
			my_info_arrow.setVisibility(android.view.View.VISIBLE);
			my_info.setVisibility(android.view.View.VISIBLE);
		}
	}

	AdapterView.OnItemClickListener mRountListListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			Log.i(TAG, "111111111111");
		}

	};

	private void InitRountListView() {
		MyRountListAdapter adapter = new MyRountListAdapter(this,
				GlobaleData.getMyRouontListData(), R.layout.my_rount_list_adapter,new String[] { "leftimg",
						"lines", "title", "text0", "text1", "text2", "text3" },
				new int[] { R.id.left_img, R.id.mlines, R.id.title, R.id.text0,
						R.id.text1, R.id.text2, R.id.text3});
		my_rount_list.setAdapter(adapter);
		my_rount_list.setOnItemClickListener(mRountListListener);
		my_rount_list.setDividerHeight(0);
	}

	public void InitMyAttationList() {
		SimpleAdapter adapter = new SimpleAdapter(this,
				GlobaleData.getMyAttationListData(), R.layout.my_fans_list,
				new String[] { "img", "name", "addicon" }, new int[] {
						R.id.img, R.id.name, R.id.add_icon });
		my_attation_list.setAdapter(adapter);
	}
	
	class GetMyAttationJsonDataTask extends AsyncTask<String, String, Integer> {
		String message="";
		@Override
		protected Integer doInBackground(String... params) {
			int result = LaifuConnective.getServiceHttpData(mContext,
					getAttationUrl, attationMap,attationjsonData, attationlist,
					false, false, jsonType);

			if (result == 1) {
				attationjsonData = (MyAttationJsonData) attationlist.get(0);
				if (attationjsonData.result == 0) {
					mAttationInfo = attationjsonData.userInfoEntity;
					return SUCCESS;
				} else {
					message = attationjsonData.message;
					return FAILED;	
				}
			} else {
				message = LaifuConnective.getPromptMsg(result);
				return ERROR;	
			}
		}

		@Override
		protected void onPreExecute() {
			showProgressDialog();
		}

		@Override
		protected void onPostExecute(Integer flag) {
			cancelProgressDialog();
			switch(flag){
			case SUCCESS:
			//	InitMyAttationList();
				break;
			case FAILED:
				Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
				break;
			case ERROR:
				alertDialog("来福提示", message);
				break;
			}
		}
	}
	
	void getMyAttationJsonData(){
		if (!DefaultTools.isAccessNetwork(this)) {
			DefaultTools.netErrorToBack(this);
			return ;
		}
		String[] ss=AccessTokenKeeper.getLaifuToken(this);
		if(ss!=null){
			attationMap.clear();
			attationMap.put("token", ss[1]);
			attationMap.put("offset", "0");
			attationMap.put("limit", "10");
			attationMap.put("id", "10");
		}else{
			Toast.makeText(mContext, "缺少授权",Toast.LENGTH_SHORT).show();
			return;
		}
		
		attationjsonType = LaifuConnective.GET;
		new GetMyAttationJsonDataTask().execute();
	}
	
	private void initView() {
		Log.i(TAG, "initView ------------->");
		mArrowBg = (LinearLayout) findViewById(R.id.arrow_bg);
		mPre = PreferenceManager.getDefaultSharedPreferences(this);
		my_rount_linear = (LinearLayout) findViewById(R.id.my_rount_linear);
		my_rount_linear.setOnClickListener(mylinearListner);
		my_rount_arrow = (ImageView) findViewById(R.id.my_rount_arrow);
		my_rount_list = (ListView) findViewById(R.id.my_rount_list);
		my_rount = (RelativeLayout) findViewById(R.id.my_rount);
		InitRountListView();

		my_fans_linear = (LinearLayout) findViewById(R.id.my_fans_linear);
		my_fans_linear.setOnClickListener(mylinearListner);
		my_fans_arrow = (ImageView) findViewById(R.id.my_fans_arrow);
		my_fans = (RelativeLayout) findViewById(R.id.my_fans);
		mMyFansListCtrl = new MyFansListCtrl(this,my_fans);
		mMyFansListCtrl.InitMyFansList();
		
		

		my_attation_linear = (LinearLayout) findViewById(R.id.my_attation_linear);
		my_attation_linear.setOnClickListener(mylinearListner);
		my_attation_arrow = (ImageView) findViewById(R.id.my_attation_arrow);
		my_attation = (RelativeLayout) findViewById(R.id.my_attation);
		my_attation_list = (ListView) findViewById(R.id.my_attation_list);
		getMyAttationJsonData();

		my_info_linear = (LinearLayout) findViewById(R.id.my_info_linear);
		my_info_linear.setOnClickListener(mylinearListner);
		my_info_arrow = (ImageView) findViewById(R.id.my_info_arrow);
		my_info = (RelativeLayout) findViewById(R.id.my_info);
		mMyInfoCtrl = new MyInfoCtrl(this, my_info);
		mMyInfoCtrl.InitBoundAccount();

		head_portrait = (ImageView) findViewById(R.id.head_portrait);
		mAccoutBg = (RelativeLayout) findViewById(R.id.account_bg);

		mNickName = (TextView) findViewById(R.id.nick_name_disp);
		mBriefIntro = (TextView) findViewById(R.id.brief_intro_disp);
	}

	Button.OnClickListener settinglistener = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent i = new Intent();
			i.setClass(mContext, SettingActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(i);

		}
	};

	public void initTitle() {
		left.setBackgroundResource(R.drawable.sz_btn);
		left.setVisibility(android.view.View.VISIBLE);
		left.setOnClickListener(settinglistener);
		title.setText("我的账号");
		right.setVisibility(View.GONE);
	}

	public void initNickNameAndBriefIntro() {
		mNickName.setText(GlobaleData.getCurrentAccountMsg(this)[1]);
		mBriefIntro.setText(GlobaleData.getCurrentAccountMsg(this)[6]);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i(TAG, "onResume ---------->");
		mCurrentPage = mPre.getInt(Constant.CURRENT_THIRD_ACTIVITY_PAGE, 0);
		initNickNameAndBriefIntro();
		initHighlight();
	}

	void InitMyInfoPicture() {
		Bitmap mBitmapHead = GlobaleData.getMyheadPicture(this);
		head_portrait.setImageBitmap(mBitmapHead);

		Bitmap mBitmapCover = GlobaleData.getMyCoverPicture(this);
		Drawable bd = ImageTools.bitmapToDrawable(mBitmapCover);
		mAccoutBg.setBackgroundDrawable(bd);
	}

	void InitCountNumbers() {
		mRountListNum = (TextView) findViewById(R.id.rount_list_number);
		mRountListNum.setText("" + GlobaleData.getCurrentRountListNumber());

		mAttationNum = (TextView) findViewById(R.id.attation_number);
		mAttationNum.setText("" + GlobaleData.getCurrentAttationNumber());

		mFansNum = (TextView) findViewById(R.id.fans_number);
		mFansNum.setText("" + GlobaleData.getCurrentFanNumber());
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	private void startPhotoZoom(Uri uri, int width, int height, int resultCode) {
		Log.i(TAG, "startPhotoZoom------------>");
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", width);
		intent.putExtra("aspectY", height);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", width);
		intent.putExtra("outputY", height);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, resultCode);
	}

	class PictureChangeTask extends AsyncTask<String, Boolean, Boolean> {
		private Bitmap mBitmap;
		private int doAction;

		public PictureChangeTask(Bitmap photo, int what) {
			mBitmap = photo;
			doAction = what;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			return true;
		}

		@Override
		protected void onPreExecute() {
			showProgressDialog();
		}

		@Override
		protected void onPostExecute(Boolean flag) {
			cancelProgressDialog();
			if (doAction == 1) {
				// changeCoverDialogShow(mBitmap);
				ImageTools.savePhotoToSDCard(mBitmap, Constant.PitcturPath,
						"CoverPic");
				Drawable bd = ImageTools.bitmapToDrawable(mBitmap);
				mAccoutBg.setBackgroundDrawable(bd);
			} else if (doAction == 2) {
				// headPortraitDialogShow(mBitmap);
				ImageTools.savePhotoToSDCard(mBitmap, Constant.PitcturPath,
						"headPic");
				head_portrait.setImageBitmap(mBitmap);
			}
		}
	}

	private Bitmap photo;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Uri uri = null;
		int width, height;
		Log.i(TAG, "onActivityResult requestCode:" + requestCode);
		
		if(mMyInfoCtrl.getTencent().onActivityResult(requestCode, resultCode, data))
			return;
		
		if (photo != null)
			photo.recycle();
		if (data == null) {
			String filename = mPre.getString(
					Constant.PICTURE_CAPTURE_IMAGE_NAME, "");
			if (filename.equals("")) {
				Log.i(TAG, "filename euqual null");
				return;
			}
			uri = Uri.fromFile(new File(Constant.PitcturPath, filename));
		} else
			uri = data.getData();
		if (requestCode == Constant.RESULT_PICTURE_COVER) {
			Log.i(TAG, "requestCode = RESULT_PICTURE_COVER");
			Bundle extras = data.getExtras();
			if (extras != null) {
				photo = extras.getParcelable("data");
				new PictureChangeTask(photo, 1).execute();
			}
		} else if (requestCode == Constant.PICTURE_COVER_CAPTUIE) {
			width = getWindowManager().getDefaultDisplay().getWidth() / 2;
			height = mAccoutBg.getLayoutParams().height / 2;
			Log.i(TAG, "width:" + width + ",height:" + height);
			startPhotoZoom(uri, width, height, Constant.RESULT_PICTURE_COVER);
		} else if (requestCode == Constant.PICTURE_HEAD_CAPTUIE) {
			width = head_portrait.getLayoutParams().width;
			height = head_portrait.getLayoutParams().height;
			Log.i(TAG, "width:" + width + ",height:" + height);
			startPhotoZoom(uri, width, height, Constant.RESULT_PACTURE_HEAD);
		} else if (requestCode == Constant.RESULT_PACTURE_HEAD) {
			Bundle extras = data.getExtras();
			if (extras != null) {
				photo = extras.getParcelable("data");
				new PictureChangeTask(photo, 2).execute();
			}
		}
	}
}

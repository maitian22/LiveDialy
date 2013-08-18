package com.laifu.livecolorful;

import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingActivity extends Activity implements ImageView.OnClickListener{
	private ImageView mNotificationImg;
	private Button mLeftBtn,mRightBtn;
	private TextView mTitle;
	private RelativeLayout mChangePassword;
	private Button BoundPhoneBtn,BoundSinaBtn,BoundQQBtn;
	private ImageView mPhoneImg,mSinaImg,mQQImg;
	private void InitTitle(){
		mRightBtn = (Button)findViewById(R.id.right);
		mRightBtn.setBackgroundResource(R.drawable.gb_btn);
		mRightBtn.setOnClickListener(this);
		mTitle = (TextView)findViewById(R.id.title);
		mTitle.setText("设置");
		
		mLeftBtn = (Button)findViewById(R.id.left);
		mLeftBtn.setVisibility(android.view.View.INVISIBLE);
	}
	
	private void changeButtonStatus(Button mBtn,boolean isBound){
			mBtn.setBackgroundResource(isBound?R.drawable.shape_button_grey:R.drawable.shape_button_blue);
			mBtn.setText(isBound?R.string.already_bound:R.string.bound);
			mBtn.setOnClickListener(this);
			if(isBound){
				mBtn.setClickable(false);
			}else{
				mBtn.setClickable(true);
			}
	}
	
	private void InitBoundButtonAndImg(){
		Map<String, Object> map = GlobaleData.getCurrentBountAccount();
		boolean IsPhoneBound = Boolean
				.parseBoolean(map.get("phone").toString());
		boolean IsSinaBound = Boolean.parseBoolean(map.get("sina").toString());
		boolean IsQQBound = Boolean.parseBoolean(map.get("qq").toString());
		
		BoundPhoneBtn = (Button)findViewById(R.id.bound_phone_btn);
		changeButtonStatus(BoundPhoneBtn,IsPhoneBound);
		mPhoneImg = (ImageView)findViewById(R.id.phone_img);
		mPhoneImg.setBackgroundResource(IsPhoneBound?R.drawable.iphone_on_btn:R.drawable.iphone_0ff_btn);
		
		BoundSinaBtn = (Button)findViewById(R.id.bound_sina_btn);
		changeButtonStatus(BoundSinaBtn,IsSinaBound);
		mSinaImg = (ImageView)findViewById(R.id.sina_img);
		mSinaImg.setBackgroundResource(IsSinaBound?R.drawable.sina_on_btn:R.drawable.sina_off_btn);
		
		BoundQQBtn = (Button)findViewById(R.id.bound_qq_btn);
		changeButtonStatus(BoundQQBtn,IsQQBound);
		mQQImg = (ImageView)findViewById(R.id.qq_img);
		mQQImg.setBackgroundResource(IsQQBound?R.drawable.qq_on_btn:R.drawable.qq_off_btn);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState); 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting);
		
		InitTitle();
		
		mNotificationImg = (ImageView)findViewById(R.id.notification_switch);
		mNotificationImg.setOnClickListener(this);
		
		mChangePassword = (RelativeLayout)findViewById(R.id.change_password);
		mChangePassword.setOnClickListener(this);
		
		InitBoundButtonAndImg();
	}
	private void StartTheChooseActivity(Class<?> mActivity){
		Intent i = new Intent(this,mActivity);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(mNotificationImg == v){
			
		}else if(mRightBtn==v){
			finish();
		}else if(mChangePassword == v){
			StartTheChooseActivity(ChangePasswordActivity.class);
		}else if(BoundPhoneBtn == v){
			StartTheChooseActivity(BoundPhoneActivity.class);
		}
	}
}

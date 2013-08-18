package com.laifu.liveDialy;

import com.laifu.liveDialy.tool.DensityUtil;
import com.laifu.liveDialy.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class ChangePasswordActivity extends Activity implements Button.OnClickListener{
	
	private Button TitleLeft,TitleRight;
	private Button BoundPhoneBtn;
	
	private void InitTitle(){
		TitleLeft = (Button)findViewById(R.id.left);
		TitleRight = (Button)findViewById(R.id.right);
		TitleLeft.setOnClickListener(this);
		TitleRight.setOnClickListener(this);
		
	//	TitleLeft.setBackgroundResource(R.drawable.shape_button_white);
		int dp5 = DensityUtil.dip2px(this, 5);
		int dp10 = DensityUtil.dip2px(this, 10);
		//TitleLeft = (Button) findViewById(R.id.right);
		TitleLeft.setBackgroundResource(R.drawable.wdrl_cjxc_left);
		TitleLeft.setPadding(dp10, dp5, dp10, dp5);
		
	//	TitleRight.setBackgroundResource(R.drawable.shape_button_white);
		TitleRight.setBackgroundResource(R.drawable.wdrl_cjxc_left);
		TitleRight.setPadding(dp10, dp5, dp10, dp5);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.change_password);
		
		InitTitle();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==TitleLeft){
			finish();
		}else if(v==TitleRight){
		}
	}
}

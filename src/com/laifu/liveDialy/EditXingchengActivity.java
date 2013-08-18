package com.laifu.liveDialy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.laifu.liveDialy.tool.DensityUtil;

public class EditXingchengActivity extends LiveBaseActivity {
	
	@Override
	public void initPages() {
		// TODO Auto-generated method stub
		InitTitle();
		initView();
	}

	@Override
	protected void onClickListener(int viewId) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (viewId) {
		case R.id.right:
			
			break;
		case R.id.left:
			finish();
			break;
		case R.id.paizhao_view:
			break;
		case R.id.xiangce_view:
			break;
		case R.id.choose_address_view:
			intent=new Intent(this,MymapActivity.class);
			startActivityForResult(intent,100);
			break;
		case R.id.choose_time_view:
			break;
		case R.id.more_phone_view:
			break;
		case R.id.more_notify_view:
			break;
		case R.id.more_people_view:
			break;
		case R.id.more_price_view:
			break;
		case R.id.qq_share_image:	
			break;
		}
	}
	
	int lat,lng;
	Bitmap mapBitmap;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==100 && resultCode==100){
			lat=data.getIntExtra("Latitude", 0);
			lng=data.getIntExtra("Longitude", 0);
			mapBitmap=(Bitmap)data.getParcelableExtra("bitmap");
		}
		
	}
	
	@Override
	public int setModelId() {
		// TODO Auto-generated method stub
		return R.layout.index;
	}

	@Override
	public int setLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.edit_xingcheng;
	}

	ImageView map, cover;
	TextView address,maptext;
	EditText titleEdit;
	View pz_btn,xc_btn;
	View ChooseAddress;
	
	View time_view;
	TextView time_day,time_week,time_month,time_hour;
	EditText description_edit,url_edit;
	View more_phone,more_notify,nore_people,more_price;
	TextView more_phone_text,more_notify_text,more_people_text,more_price_text;
	
	ImageView share_sina,share_qq,share_qqkj;
	
	public void initView() {
		map=(ImageView)findViewById(R.id.map_image);
		cover=(ImageView)findViewById(R.id.cover_image);
		address=(TextView)findViewById(R.id.map_address_text);
		maptext=(TextView)findViewById(R.id.map_text);
		titleEdit=(EditText)findViewById(R.id.title_edit);
		pz_btn=findViewById(R.id.paizhao_view);
		xc_btn=findViewById(R.id.xiangce_view);
		ChooseAddress=findViewById(R.id.choose_address_view);
		
		pz_btn.setOnClickListener(this);
		xc_btn.setOnClickListener(this);
		ChooseAddress.setOnClickListener(this);
		
		maptext.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线

		
		time_view=findViewById(R.id.choose_time_view);
		time_day=(TextView)findViewById(R.id.day_text);
		time_week=(TextView)findViewById(R.id.week_text);
		time_month=(TextView)findViewById(R.id.month_text);
		time_hour=(TextView)findViewById(R.id.startend_time);
		time_view.setOnClickListener(this);
		
		
		description_edit=(EditText)findViewById(R.id.description_edit);
		url_edit=(EditText)findViewById(R.id.url_edit);
		
		more_phone=findViewById(R.id.more_phone_view);
		more_notify=findViewById(R.id.more_notify_view);
		nore_people=findViewById(R.id.more_people_view);
		more_price=findViewById(R.id.more_price_view);
		more_phone.setOnClickListener(this);
		more_notify.setOnClickListener(this);
		nore_people.setOnClickListener(this);
		more_price.setOnClickListener(this);
		
		more_phone_text=(TextView)findViewById(R.id.phone_text);
		more_notify_text=(TextView)findViewById(R.id.notify_text);
		more_people_text=(TextView)findViewById(R.id.people_text);
		more_price_text=(TextView)findViewById(R.id.price_text);
		
		share_sina=(ImageView)findViewById(R.id.sina_share_image);
		share_qq=(ImageView)findViewById(R.id.qq_share_image);
		share_qqkj=(ImageView)findViewById(R.id.qqkj_share_image);
		share_sina.setOnClickListener(this);
		share_qq.setOnClickListener(this);
		share_qqkj.setOnClickListener(this);
	}
	
	private void InitTitle() {
		mTextTitle = (TextView) findViewById(R.id.title);
		mTextTitle.setText("添加行程");

		int dp5=DensityUtil.dip2px(this, 5);
		int dp10=DensityUtil.dip2px(this, 10);
		
		mLeftBtn = (Button) findViewById(R.id.left);
		mLeftBtn.setBackgroundResource(R.drawable.wdrl_cjxc_left);
		mLeftBtn.setText("取消");
		mLeftBtn.setVisibility(View.VISIBLE);
		mLeftBtn.setOnClickListener(this);
		mLeftBtn.setPadding(dp10, dp5, dp10, dp5);
//		mLeftBtn.setLayoutParams(new RelativeLayout.LayoutParams(-2, -2));


		mRightBtn = (Button) findViewById(R.id.right);
		mRightBtn.setText("创建");
		mRightBtn.setBackgroundResource(R.drawable.wdrl_cjxc_left);
		mRightBtn.setOnClickListener(this);
		mRightBtn.setPadding(dp10, dp5, dp10, dp5);

	}

	private TextView mTextTitle;
	private Button mLeftBtn, mRightBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}
}

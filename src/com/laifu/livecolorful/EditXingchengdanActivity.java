package com.laifu.livecolorful;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.laifu.livecolorful.tool.DensityUtil;

public class EditXingchengdanActivity extends LiveBaseActivity {
	
	private int[] mStringIds = { R.string.discover_str_1,
			R.string.discover_str_2, R.string.discover_str_3,
			R.string.discover_str_4, R.string.discover_str_5,
			R.string.discover_str_6, R.string.discover_str_7,
			R.string.discover_str_8, R.string.discover_str_9,
			R.string.discover_str_10, R.string.discover_str_11,
			R.string.discover_str_12 };
	private String[] typeString;
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
		case R.id.choose_type_view:
			setChooseType();
			break;
		case R.id.choose_address_view:
			intent=new Intent(this,MymapActivity.class);
			startActivityForResult(intent,100);
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




	int witch=0;
	boolean hasChooseType=false;
	public void setChooseType(){
		new AlertDialog.Builder(this).setTitle("选择种类").setSingleChoiceItems(typeString, witch, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				witch = which;// 选项
			}
		}).setPositiveButton("完成", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {// 选择类别
				typeText.setText(mStringIds[witch]);
				hasChooseType = true;// 设置已经选择
				// 设置sku
				dialog.dismiss();
				dialog = null;
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO
				dialog.dismiss();
				dialog = null;
			}
		}).show();
	}
	
	@Override
	public int setModelId() {
		// TODO Auto-generated method stub
		return R.layout.index;
	}

	@Override
	public int setLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.edit_xingchengdan;
	}

	ImageView map, cover;
	TextView typeText,address,maptext;
	EditText titleEdit;
	View pz_btn,xc_btn;
	View chooseType,ChooseAddress;
	public void initView() {
		map=(ImageView)findViewById(R.id.map_image);
		cover=(ImageView)findViewById(R.id.cover_image);
		typeText=(TextView)findViewById(R.id.type_text);
		address=(TextView)findViewById(R.id.map_address_text);
		maptext=(TextView)findViewById(R.id.map_text);
		titleEdit=(EditText)findViewById(R.id.title_edit);
		pz_btn=findViewById(R.id.paizhao_view);
		xc_btn=findViewById(R.id.xiangce_view);
		chooseType=findViewById(R.id.choose_type_view);
		ChooseAddress=findViewById(R.id.choose_address_view);
		
		pz_btn.setOnClickListener(this);
		xc_btn.setOnClickListener(this);
		chooseType.setOnClickListener(this);
		ChooseAddress.setOnClickListener(this);
		
		maptext.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线

		initTypeString();
	}
	
	public void initTypeString(){
		typeString=new String[mStringIds.length];
		for(int i=0;i<mStringIds.length;i++){
			typeString[i]=getResources().getString(mStringIds[i]);
		}
	}
	
	private void InitTitle() {
		mTextTitle = (TextView) findViewById(R.id.title);
		mTextTitle.setText("创建行程单");

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

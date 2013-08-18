package com.laifu.livecolorful;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class ErweimaActivity extends LiveBaseActivity {

	@Override
	public void initPages() {
		// TODO Auto-generated method stub
		InitTitle();
		initView();
	}

	@Override
	protected void onClickListener(int viewId) {
		// TODO Auto-generated method stub
		switch (viewId) {
		case R.id.right:
			finish();
			break;
		case R.id.left2:
			break;
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
		return R.layout.erweima;
	}

	ImageView touxiang, erweima;
	TextView titleText, numberText;

	public void initView() {
		touxiang = (ImageView) findViewById(R.id.chuangjianren_image);
		erweima = (ImageView) findViewById(R.id.erweima_image);

		titleText = (TextView) findViewById(R.id.title_text);
		numberText = (TextView) findViewById(R.id.number_text);
	}

	private void InitTitle() {
		mTextTitle = (TextView) findViewById(R.id.title);
		mTextTitle.setText("二维码名片");

		mLeftBtn = (Button) findViewById(R.id.left2);
		mLeftBtn.setVisibility(View.VISIBLE);
		mLeftBtn.setOnClickListener(this);

		mRightBtn = (Button) findViewById(R.id.right);
		mRightBtn.setBackgroundResource(R.drawable.gb_btn);
		mRightBtn.setOnClickListener(this);
	}

	private TextView mTextTitle;
	private Button mLeftBtn, mRightBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}
}

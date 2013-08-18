package com.laifu.liveDialy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.laifu.liveDialy.adapter.MyRountListAdapter;
import com.laifu.liveDialy.tool.DensityUtil;
import com.laifu.liveDialy.zxing.CaptureActivity;

public class SecondActivity extends LiveBaseActivity implements
		AdapterView.OnItemClickListener {
	private GridView mGridView;
	private int[] mImageIds = { R.drawable.sina_on_btn, R.drawable.sina_on_btn,
			R.drawable.sina_on_btn, R.drawable.sina_on_btn,
			R.drawable.sina_on_btn, R.drawable.sina_on_btn,
			R.drawable.sina_on_btn, R.drawable.sina_on_btn,
			R.drawable.sina_on_btn, R.drawable.sina_on_btn,
			R.drawable.sina_on_btn, R.drawable.sina_on_btn };
	private int[] mStringIds = { R.string.discover_str_1,
			R.string.discover_str_2, R.string.discover_str_3,
			R.string.discover_str_4, R.string.discover_str_5,
			R.string.discover_str_6, R.string.discover_str_7,
			R.string.discover_str_8, R.string.discover_str_9,
			R.string.discover_str_10, R.string.discover_str_11,
			R.string.discover_str_12 };
	private int[] mCountNumbers = { 1234, 1234, 1234, 1234, 1234, 1234, 1234,
			1234, 1234, 1234, 1234, 1234 };

	@Override
	public void initPages() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onClickListener(int viewId) {
		// TODO Auto-generated method stub
		if (viewId == mSearchNearBy.getId()) {
			Intent i = new Intent(this, showNearByActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
		} else if (viewId == mfriendAdvice.getId()) {
			Intent i = new Intent(this, friendAdviceActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
		} else if (viewId == mSearchBtn.getId()) {
			if (mSearchEdit.getEditableText().toString().length() == 0) {
				Toast.makeText(mContext, "请输入搜索内容", Toast.LENGTH_SHORT).show();
			} else {
				if (mSearchBtn.getText().toString().equals("搜索")) {
					mSearchBtn.setText("取消");
					mSearchBtn.setBackgroundResource(R.drawable.qx_nothing_btn);
					mSearchResultList.setVisibility(android.view.View.VISIBLE);
					mSearchLayout.setVisibility(android.view.View.INVISIBLE);
					MyRountListAdapter adapter = new MyRountListAdapter(this,
							GlobaleData.getMyRouontListData(),
							R.layout.my_rount_list_adapter, new String[] {
									"leftimg", "lines", "title", "text0",
									"text1", "text2", "text3" }, new int[] {
									R.id.left_img, R.id.mlines, R.id.title,
									R.id.text0, R.id.text1, R.id.text2,
									R.id.text3 });
					mSearchResultList.setAdapter(adapter);
				} else if (mSearchBtn.getText().toString().equals("取消")) {
					mSearchBtn.setText("搜索");
					mSearchBtn
							.setBackgroundResource(R.drawable.search_nothing_btn);
					mSearchResultList
							.setVisibility(android.view.View.INVISIBLE);
					mSearchLayout.setVisibility(android.view.View.VISIBLE);
				}

			}
		} else if (viewId == R.id.right) {
			Intent intent = new Intent(this, CaptureActivity.class);
			startActivity(intent);
		}
	}

	@Override
	public int setModelId() {
		// TODO Auto-generated method stub
		return R.layout.second;
	}

	@Override
	public int setLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.second;
	}

	private void InitTitle() {
		mTextTitle = (TextView) findViewById(R.id.title);
		mTextTitle.setText("发现");

		mLeftBtn = (Button) findViewById(R.id.left);
		mLeftBtn.setVisibility(android.view.View.INVISIBLE);

		int dp5 = DensityUtil.dip2px(this, 5);
		int dp10 = DensityUtil.dip2px(this, 10);
		mRightBtn = (Button) findViewById(R.id.right);
		mRightBtn.setBackgroundResource(R.drawable.wdrl_cjxc_left);
		mRightBtn.setPadding(dp10, dp5, dp10, dp5);
		mRightBtn.setText("扫二维码");
		mRightBtn.setOnClickListener(this);
	}

	private TextView mTextTitle;
	private Button mLeftBtn, mRightBtn;
	private RelativeLayout mSearchNearBy, mfriendAdvice;
	private String TAG = "secondActivity";
	private Button mSearchBtn;
	private ListView mSearchResultList;
	private EditText mSearchEdit;
	private Context mContext;
	private RelativeLayout mSearchLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		InitTitle();
		mGridView = (GridView) findViewById(R.id.grid_view);
		LayoutAdapter ia = new LayoutAdapter(this, mImageIds, mStringIds,
				mCountNumbers);
		mGridView.setAdapter(ia);// 为GridView设置数据适配器
		mGridView.setOnItemClickListener(this);

		mSearchNearBy = (RelativeLayout) findViewById(R.id.search_nearby);
		mSearchNearBy.setOnClickListener(this);

		mfriendAdvice = (RelativeLayout) findViewById(R.id.friend_advice);
		mfriendAdvice.setOnClickListener(this);

		mSearchBtn = (Button) findViewById(R.id.search_btn);
		mSearchBtn.setOnClickListener(this);

		mSearchResultList = (ListView) findViewById(R.id.search_result_list);
		mSearchEdit = (EditText) findViewById(R.id.search_edit);

		mSearchLayout = (RelativeLayout) findViewById(R.id.grid_and_others_layout);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Log.i(TAG, "arg2:" + arg2 + ",arg3:" + arg3);
		Intent i = new Intent(this, GridItemActivity.class);
		i.putExtra("clickNumber", arg2);
		i.putExtra("titleName", getString(mStringIds[arg2]));
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
	}
}

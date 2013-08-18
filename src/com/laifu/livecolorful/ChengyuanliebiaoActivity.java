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

public class ChengyuanliebiaoActivity extends LiveBaseActivity {
	GridView mGridView;
	ImageView chuangjianren_image;
	TextView Nochengyuan_text;
	Button shareBtn;

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
		case R.id.share_btn:
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
		return R.layout.chengyuanliebiao;
	}
	
	ImageAdapter adapter;
	LayoutInflater inflater;
	public void initView() {
		inflater=LayoutInflater.from(this);
		
		mGridView = (GridView) findViewById(R.id.grid_view);
		mGridView.setVisibility(View.VISIBLE);
		chuangjianren_image = (ImageView) findViewById(R.id.image);
		Nochengyuan_text = (TextView) findViewById(R.id.nochengyuan_text);
		Nochengyuan_text.setVisibility(View.GONE);
		shareBtn = (Button) findViewById(R.id.share_btn);
		shareBtn.setOnClickListener(this);
		
		adapter=new ImageAdapter();
		mGridView.setAdapter(adapter);
	}

	private void InitTitle() {
		mTextTitle = (TextView) findViewById(R.id.title);
		mTextTitle.setText("成员");

		mLeftBtn = (Button) findViewById(R.id.left);
		mLeftBtn.setVisibility(View.GONE);

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

	class ImageAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 30;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView=inflater.inflate(R.layout.image_item, null);
			
			ImageView imageView = (ImageView)convertView.findViewById(R.id.imagView);

			return convertView;
		}

	}
}

package com.laifu.livecolorful;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.laifu.livecolorful.tool.DensityUtil;
import com.laifu.livecolorful.view.HorizontialListView;

public class XingchengDetailActivity extends LiveBaseActivity {
	
	@Override
	public void initPages() {
		// TODO Auto-generated method stub
		InitTitle();
		initView();
	}
	
	View add_view;
	ImageView add_image;
	TextView add_text;
	
	ImageView title_image;
	TextView title_text;
	TextView time_text;
	ImageView naozhong_image;
	
	TextView description_text;
	TextView openDes_text;
	
	Button copyurl_btn;
	ImageView map_image;
	View map_view;
	TextView address_text;
	TextView addNumber_text;
	HorizontialListView gallery;
	View people_view;
	public void initView(){
		add_view=findViewById(R.id.add_view);
		add_image=(ImageView)findViewById(R.id.add_image);
		add_text=(TextView)findViewById(R.id.add_text);
		
		title_image=(ImageView)findViewById(R.id.image);
		title_text=(TextView)findViewById(R.id.title_text);
		time_text=(TextView)findViewById(R.id.time_text);
		naozhong_image=(ImageView)findViewById(R.id.naozhong_image);
		
		description_text=(TextView)findViewById(R.id.description_text);
		openDes_text=(TextView)findViewById(R.id.opendesc_text);
		
		copyurl_btn=(Button)findViewById(R.id.fzlj_btn);
		map_image=(ImageView)findViewById(R.id.map_image);
		map_view=findViewById(R.id.choose_address_view);
		address_text=(TextView)findViewById(R.id.map_address_text);
		addNumber_text=(TextView)findViewById(R.id.add_people_num);
		gallery=(HorizontialListView)findViewById(R.id.type_gallery1);
		people_view=findViewById(R.id.people_view);
		
		openDes_text.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
		
		add_view.setOnClickListener(this);
		openDes_text.setOnClickListener(this);
		copyurl_btn.setOnClickListener(this);
		map_view.setOnClickListener(this);
		people_view.setOnClickListener(this);
		
		initPeopleView();
	}
	
	ImageAdapter adapter;
	LayoutInflater inflater;
	public void initPeopleView(){
		inflater=LayoutInflater.from(this);
		adapter=new ImageAdapter();
		gallery.setAdapter(adapter);
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
			imageView.setPadding(10,5,10,5);
			return convertView;
		}

	}
	
	
	boolean openDescFlag=false;
	@Override
	protected void onClickListener(int viewId) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (viewId) {

		case R.id.left:
			if (pop_menu == null) {
				initPopView();
			}
			if (pop_menu.isShowing()) {
				dismissPop();
			} else {
				showPop();
			}
			break;
		case R.id.left2:
			break;
		case R.id.right:
			finish();
			break;
		case R.id.btn2:
			dismissPop();
			intent = new Intent(this, ErweimaActivity.class);
			startActivity(intent);
			break;
		case R.id.btn3:
			dismissPop();
			 intent =new Intent(this,EditXingchengActivity.class);
			 startActivity(intent);
			break;
		case R.id.btn4:
			dismissPop();
			alertCustomeDialog(this, null, "确认退出该行程？\n退出后不会再收到\n该行程的动态提醒",
					"取消", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}

					}, "确定", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}

					});
			break;
			
		case R.id.add_view:
			break;
		case R.id.opendesc_text:
			if(openDescFlag){
				description_text.setMaxLines(4);
				openDes_text.setText("显示更多");
			}else{
				description_text.setMaxLines(Integer.MAX_VALUE);
				openDes_text.setText("显示简介");
			}
			openDescFlag=!openDescFlag;
			break;
		case R.id.fzlj_btn:
			break;
		case R.id.choose_address_view:
			intent=new Intent(this,MymapActivity.class);
			startActivity(intent);
			break;
		case R.id.people_view:
			intent=new Intent(this,ChengyuanliebiaoActivity.class);
			startActivity(intent);
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
		return R.layout.xingcheng_detail_activity;
	}

	private void InitTitle() {
		mTextTitle = (TextView) findViewById(R.id.title);
		mTextTitle.setText("");

		mLeftBtn = (Button) findViewById(R.id.left);
		mLeftBtn.setVisibility(android.view.View.VISIBLE);
		mLeftBtn.setBackgroundResource(R.drawable.sz_btn);
		mLeftBtn.setOnClickListener(this);

		mLeftBtn2 = (Button) findViewById(R.id.left2);
		mLeftBtn2.setVisibility(android.view.View.VISIBLE);
		mLeftBtn2.setOnClickListener(this);

		// mLeftBtn3 = (Button)findViewById(R.id.left3);
		// mLeftBtn3.setVisibility(android.view.View.VISIBLE);
		// mLeftBtn3.setOnClickListener(this);

		mRightBtn = (Button) findViewById(R.id.right);
		mRightBtn.setBackgroundResource(R.drawable.gb_btn);
		mRightBtn.setOnClickListener(this);
	}

	private TextView mTextTitle;
	private Button mLeftBtn, mRightBtn, mLeftBtn2, mLeftBtn3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	PopupWindow pop_menu = null;
	View view1;
	Button btn1, btn2, btn3, btn4;
	TextView pop_line;

	public void dismissPop() {
		if (pop_menu == null) {
			return;
		}
		pop_menu.dismiss();
	}

	boolean isOwner = true;

	public void showPop() {
		if (pop_menu == null) {
			return;
		}
		// btn3.setVisibility(View.VISIBLE);
		// pop_line.setVisibility(View.VISIBLE);
		btn1.setVisibility(View.GONE);
		view1.findViewById(R.id.line1).setVisibility(View.GONE);
		btn4.setText("取消行程");
		int hight = DensityUtil.dip2px(this, 62);
		int width = DensityUtil.dip2px(this, 6);
		pop_menu.setBackgroundDrawable(new BitmapDrawable());
		pop_menu.showAtLocation(mLeftBtn, Gravity.LEFT | Gravity.TOP, width,
				hight);
	}

	public void initPopView() {
		LayoutInflater inflater = LayoutInflater.from(this);
		view1 = inflater.inflate(R.layout.xingchengdan_detail_pop_view, null);
		pop_menu = new PopupWindow(view1, DensityUtil.dip2px(this, 159),
				LayoutParams.WRAP_CONTENT);

		pop_menu.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.bg_todo));
		pop_menu.setOutsideTouchable(true);

		// 自定义动画
		pop_menu.setAnimationStyle(R.style.PopupAnimation);
		// 使用系统动画
		pop_menu.setTouchable(true);
		pop_menu.setFocusable(true);

		btn1 = (Button) view1.findViewById(R.id.btn1);
		btn2 = (Button) view1.findViewById(R.id.btn2);
		btn3 = (Button) view1.findViewById(R.id.btn3);
		btn4 = (Button) view1.findViewById(R.id.btn4);
		pop_line = (TextView) view1.findViewById(R.id.special_pop_line);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		btn4.setOnClickListener(this);
	}
}

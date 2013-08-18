package com.laifu.livecolorful;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.laifu.livecolorful.adapter.XingchengdanDetailAdapter;
import com.laifu.livecolorful.tool.DensityUtil;
import com.laifu.livecolorful.view.PullDownView;
import com.laifu.livecolorful.view.PullDownView.OnRefreshListener;

public class XingchengdanDetailActivity extends LiveBaseActivity {
	@Override
	public void initPages() {
		// TODO Auto-generated method stub
		InitTitle();
		initListView();
	}
	/***
	 * 下拉刷新的view
	 */
	public PullDownView pullDownView;
	/***
	 * 加载正文的listview
	 */
	public ListView listView;
	XingchengdanDetailAdapter adapter;
	LayoutInflater inflater;
	View headView;
	public void initListView() {
		inflater=LayoutInflater.from(this);
		headView=inflater.inflate(R.layout.xingchengdan_detail_listheadview, null);
		pullDownView = (PullDownView) findViewById(R.id.feeds);
		pullDownView.init();
		pullDownView.setFooterView(R.layout.footer_item);

		pullDownView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				pullDownView.notifyRefreshComplete();
			}
		});
		listView = pullDownView.getListView();

		pullDownView.showFooterView(false);
		
		listView.addHeaderView(headView);
		adapter=new XingchengdanDetailAdapter(this);
		listView.setAdapter(adapter);
	}
	
	@Override
	protected void onClickListener(int viewId) {
		// TODO Auto-generated method stub
		Intent intent;
		switch(viewId){
		
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
		case R.id.left3:
			intent =new Intent(this,EditXingchengActivity.class);
			startActivity(intent);
			break;
		case R.id.right:
			finish();
			break;
		case R.id.btn1:
			dismissPop();
			intent =new Intent(this,ChengyuanliebiaoActivity.class);
			startActivity(intent);
			break;
		case R.id.btn2:
			dismissPop();
			intent =new Intent(this,ErweimaActivity.class);
			startActivity(intent);
			break;
		case R.id.btn3:
			dismissPop();
			intent =new Intent(this,EditXingchengdanActivity.class);
			startActivity(intent);
			break;
		case R.id.btn4:
			dismissPop();
			if (isOwner) {
//				btn4.setText("注销行程单");
				alertCustomeDialog(this, null, "您暂时无法注销行程单\n如需注销请联系客服\nkefu@laifu.fm", "知道了", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
					
				}, null, null);
			} else {
//				btn4.setText("退出行程单");
				alertCustomeDialog(this, null, "确认退出该行程单？\n退出后不会再收到\n该行程单的动态提醒", "取消", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
					
				}, "确定", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
					
				});
			}
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
		return R.layout.xingchengdan_detail_activity;
	}
	private void InitTitle(){
		mTextTitle = (TextView)findViewById(R.id.title);
		mTextTitle.setText("");
		
		mLeftBtn = (Button)findViewById(R.id.left);
		mLeftBtn.setVisibility(android.view.View.VISIBLE);
		mLeftBtn.setBackgroundResource(R.drawable.sz_btn);
		mLeftBtn.setOnClickListener(this);
		
		mLeftBtn2 = (Button)findViewById(R.id.left2);
		mLeftBtn2.setVisibility(android.view.View.VISIBLE);
		mLeftBtn2.setOnClickListener(this);
		
		mLeftBtn3 = (Button)findViewById(R.id.left3);
		mLeftBtn3.setVisibility(android.view.View.VISIBLE);
		mLeftBtn3.setOnClickListener(this);
		
		mRightBtn=(Button)findViewById(R.id.right);
		mRightBtn.setBackgroundResource(R.drawable.gb_btn);
		mRightBtn.setOnClickListener(this);
	}
	
	private TextView mTextTitle;
	private Button mLeftBtn,mRightBtn,mLeftBtn2,mLeftBtn3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	PopupWindow pop_menu = null;
	View view1;
	Button btn1, btn2,btn3,btn4;
	TextView pop_line;
	
	public void dismissPop() {
		if (pop_menu == null) {
			return;
		}
		pop_menu.dismiss();
	}
	
	boolean isOwner=true;
	public void showPop() {
		if (pop_menu == null) {
			return;
		}
		if (isOwner) {
			btn3.setVisibility(View.VISIBLE);
			pop_line.setVisibility(View.VISIBLE);
			btn4.setText("注销行程单");
		} else {
			btn3.setVisibility(View.GONE);
			pop_line.setVisibility(View.GONE);
			btn4.setText("退出行程单");
		}
		int hight = DensityUtil.dip2px(this, 62);
		int width=DensityUtil.dip2px(this, 6);
		pop_menu.setBackgroundDrawable(new BitmapDrawable());
		pop_menu.showAtLocation(mLeftBtn, Gravity.LEFT | Gravity.TOP, width, hight);
	}
	public void initPopView() {
		LayoutInflater inflater = LayoutInflater.from(this);
		view1 = inflater.inflate(R.layout.xingchengdan_detail_pop_view, null);
		pop_menu = new PopupWindow(view1, DensityUtil.dip2px(this, 159), LayoutParams.WRAP_CONTENT);

		pop_menu.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_todo));
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

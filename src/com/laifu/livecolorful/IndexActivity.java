package com.laifu.livecolorful;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.SlidingDrawer;

import com.laifu.livecolorful.adapter.IndexLaifuAdapter;
import com.laifu.livecolorful.adapter.IndexMyDiaryAdapter;
import com.laifu.livecolorful.adapter.IndexXingchengdanAdapter;
import com.laifu.livecolorful.tool.DensityUtil;
import com.laifu.livecolorful.view.Panel;
import com.laifu.livecolorful.view.PullDownView;
import com.laifu.livecolorful.view.PullDownView.OnRefreshListener;

public class IndexActivity extends LiveBaseActivity {
	/***
	 * 下拉刷新的view
	 */
	public PullDownView pullDownView;
	/***
	 * 加载正文的listview
	 */
	public ListView listView;
	/***
	 * 我的日历的view 点击弹出我的日历
	 */
	public View myDiary;
	/***
	 * 右上角编辑点击出现的抽屉
	 */
	public SlidingDrawer sd1;
	/***
	 * 我的日历的view的那个小箭头
	 */
	public ImageView mydiary_image;
	/***
	 * 左上角关于
	 */
	Button btn_left;
	/***
	 * 右上角关于
	 */
	Button btn_right;
	/***
	 * 抽屉1出现的时候页面的半透明背景
	 */
	View silding1_black;
	/***
	 * 抽屉1的title，包括左取消按钮
	 */
	View sliding1_title_view;
	/***
	 * 抽屉1的btn，取消按钮
	 */
	Button sliding1_title_left;
	/***
	 * 抽屉1的创建行程单的view
	 */
	View silding1_add_view;
	/***
	 * 抽屉1的listview
	 */
	ListView silding1_listview;

	Panel topPanel;

	public final static int PAGESIZE = 10;
	public final static int TOTALSIZE = 100;

	Handler handle = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			if (msg.what == 100) {// 加载更多
				
				if(listData.size()>=TOTALSIZE-PAGESIZE){// 没有更多了
					for (int i = 0; i < TOTALSIZE-listData.size(); i++) {
						listData.add(new Object());
					}
					pullDownView.showFooterView(false);
				}else{// 还有更多
					for (int i = 0; i < PAGESIZE; i++) {
						listData.add(new Object());
					}
					pullDownView.showFooterView(true);
					listView.setOnScrollListener(scorllListener);
				}
				laifuAdapter.notifyDataSetChanged();
				isRefresh=false;
			}
//			else if(msg.what==1000){// 加载更多失败
//				pullDownView.showFooterView(false);
//			}
		}

	};

	@Override
	public void initPages() {
		// TODO Auto-generated method stub

		initTitle();
		initListView();
		initSildingEdit();
		initWDRLView();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			// topPanel.setOpen(!topPanel.isOpen(), false);
			if (topPanel.isOpen()) {
				topPanel.setVisibility(View.GONE);
				topPanel.setOpen(false, false);
				return false;
			}
			if (sd1.isOpened()) {
				cancleSliding1();
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	/****
	 * 初始化title 左右两个button
	 */
	public void initTitle() {
		btn_left = (Button) findViewById(R.id.left);
		btn_right = (Button) findViewById(R.id.right);
		btn_left.setOnClickListener(this);
		btn_right.setOnClickListener(this);
	}

	IndexLaifuAdapter laifuAdapter;
	ArrayList listData;

	/***
	 * 初始化页面正文的listview
	 */
	public void initListView() {
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
		pullDownView.showFooterView(true);

		listView.setDividerHeight(0);
		listData = new ArrayList();
		for (int i = 0; i < PAGESIZE; i++) {
			listData.add(new Object());
		}
		laifuAdapter = new IndexLaifuAdapter(this, listData);
		listView.setAdapter(laifuAdapter);

		listView.setOnScrollListener(scorllListener);
		
		
	}
	
	OnScrollListener scorllListener=new OnScrollListener(){

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub
			Log.i("aa", "firstVisibleItem"+firstVisibleItem+"visibleItemCount"+visibleItemCount+"totalItemCount"+totalItemCount);
			if (((firstVisibleItem + visibleItemCount) == totalItemCount) && !isRefresh) {
				isRefresh=true;
				listView.setOnScrollListener(null);
				getMoreData();
			} else {
				
			}
		}
		
	};
	
	Thread t = null;
	boolean isRefresh=false;
	public void getMoreData() {
		t = new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handle.sendEmptyMessage(100);
			}
		});
		t.start();
	}

	IndexXingchengdanAdapter xingchengdan_adapter;

	/***
	 * 初始化编辑按钮出发的抽屉
	 */
	public void initSildingEdit() {

		silding1_black = findViewById(R.id.silding1_black);
		sliding1_title_view = findViewById(R.id.sliding_title_view);
		sliding1_title_left = (Button) findViewById(R.id.wdrl_cjxc_left);
		silding1_add_view = findViewById(R.id.cjxc_add_view);
		silding1_listview = (ListView) findViewById(R.id.sliding1_listview);

		sd1 = (SlidingDrawer) findViewById(R.id.sliding_add);

		sd1.setVisibility(View.GONE);
		silding1_black.setOnClickListener(this);
		sliding1_title_left.setOnClickListener(this);
		silding1_add_view.setOnClickListener(this);
		sd1.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener()// 开抽屉
		{
			@Override
			public void onDrawerOpened() {
				// mydiary_image.setImageResource(R.drawable.wdrl_zk_btn);//
				// 响应开抽屉事件
				// ，把图片设为向下的
			}
		});
		sd1.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
			@Override
			public void onDrawerClosed() {
				// mydiary_image.setImageResource(R.drawable.wdrl_sq_btn);//
				// 响应关抽屉事件
				silding1_black.setVisibility(View.GONE);
				sd1.setVisibility(View.GONE);
			}
		});

		xingchengdan_adapter = new IndexXingchengdanAdapter(this);
		silding1_listview.setAdapter(xingchengdan_adapter);
	}

	boolean hasInit = false;
	View contentView;
	View no_wdrl_view;
	ListView sliding2_listview;
	Button index_add_xingcheng;
	IndexMyDiaryAdapter wdrl_adapter;

	public void initWDRLView() {
		myDiary = findViewById(R.id.mydiary_view);
		mydiary_image = (ImageView) findViewById(R.id.wdrl_image);

		myDiary.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!hasInit) {
					hasInit = true;
					int diaryheight = myDiary.getHeight();
					int titleheight = findViewById(R.id.head_view).getHeight();
					// int
					// handleHeight=findViewById(R.id.panelHandle).getHeight();
					int handleHeight = DensityUtil.dip2px(IndexActivity.this,
							30);
					int height = outMetrics.heightPixels - titleheight
							- diaryheight - handleHeight;
					Log.i("aa", height + "==" + outMetrics.heightPixels + "~~"
							+ titleheight + "~~" + diaryheight + "~~"
							+ handleHeight);
					contentView.setLayoutParams(new LayoutParams(-1,
							height - 35));
					topPanel.setVisibility(View.VISIBLE);
					topPanel.setOpen(true, true);
				} else {
					if (topPanel.isOpen()) {
						topPanel.setVisibility(View.GONE);
						topPanel.setOpen(false, false);
					} else {
						topPanel.setVisibility(View.VISIBLE);
						topPanel.setOpen(true, true);

					}
				}
			}
		});

		topPanel = (Panel) findViewById(R.id.topPanel);
		topPanel.setVisibility(View.GONE);
		contentView = findViewById(R.id.panelContent);
		topPanel.setOnPanelListener(new Panel.OnPanelListener() {

			@Override
			public void onPanelOpened(Panel panel) {
				// TODO Auto-generated method stub
				Log.i("aa", "open");
				mydiary_image.setImageResource(R.drawable.sq_btn);
			}

			@Override
			public void onPanelClosed(Panel panel) {
				// TODO Auto-generated method stub
				Log.i("aa", "close");
				topPanel.setVisibility(View.GONE);
				mydiary_image.setImageResource(R.drawable.zk_btn);
			}
		});
		// topPanel.setInterpolator(new BounceInterpolator(Type.OUT));

		no_wdrl_view = findViewById(R.id.no_wdrl_view);
		sliding2_listview = (ListView) findViewById(R.id.sliding2_listview);
		index_add_xingcheng = (Button) findViewById(R.id.index_add_xingcheng);
		index_add_xingcheng.setOnClickListener(this);

		// 1 测试没有我的日历ui
		// sliding2_listview.setVisibility(View.GONE);
		// no_wdrl_view.setVisibility(View.VISIBLE);
		// 2 测试有的我日历的ui
		sliding2_listview.setVisibility(View.VISIBLE);
		no_wdrl_view.setVisibility(View.GONE);
		wdrl_adapter = new IndexMyDiaryAdapter(this);
		sliding2_listview.setAdapter(wdrl_adapter);
	}

	@Override
	protected void onClickListener(int viewId) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (viewId) {
		case R.id.left:
			break;
		case R.id.right:
			if (topPanel.isOpen()) {
				topPanel.setVisibility(View.GONE);
				topPanel.setOpen(false, false);
				return;
			}
			silding1_black.setVisibility(View.VISIBLE);
			sd1.setVisibility(View.VISIBLE);
			sd1.animateOpen();
			break;
		case R.id.silding1_black:
		case R.id.wdrl_cjxc_left:
			cancleSliding1();
			break;
		case R.id.cjxc_add_view:
			// 点击创建行程单
			intent = new Intent(this, EditXingchengdanActivity.class);
			startActivity(intent);
			break;
		case R.id.index_add_xingcheng:
			break;
		}
	}

	/***
	 * 取消编辑的抽屉
	 */
	public void cancleSliding1() {
		silding1_black.setVisibility(View.GONE);
		sd1.setVisibility(View.GONE);
		sd1.animateClose();
	}

	@Override
	public int setModelId() {
		// TODO Auto-generated method stub
		return R.layout.index;
	}

	@Override
	public int setLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.index;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

}

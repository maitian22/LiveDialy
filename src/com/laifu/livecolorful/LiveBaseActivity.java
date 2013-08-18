package com.laifu.livecolorful;

/**
 * Filename:    ShoppingActivity.java     
 * Copyright:   Copyright (c)2011 
 * @author: zoutiao
 * @version: 1.0
 * Create at:   2011-7-18 下午04:29:14  
 *
 * Modification History:  
 * Date         Author      Version     Description  
 * ------------------------------------------------------------------  
 * 2011-7-18    zoutiao             1.0        1.0 Version  
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.laifu.livecolorful.tools.ActivityManagerTool;
import com.laifu.livecolorful.view.JuMeiCustomProgressDlg;

public abstract class LiveBaseActivity extends Activity implements OnClickListener {
	/***
	 * 联网成功，获取正确返回
	 */
	public static final int SUCCESS = 111;
	/***
	 * 联网成功，获取错误返回 比如参数错误，签名错误等
	 */
	public static final int FAILED = SUCCESS+1;
	/***
	 * 联网失败，可能是没网或者超时，或者服务器service挂了 返回html代码
	 */
	public static final int ERROR = 113;
	
	protected JuMeiCustomProgressDlg progressDialog = null; // 加载对话框
    private AlertDialog.Builder adb;
    protected TextView  title;// 顶部标题栏
    public Button left,right;
    /**
     * 各模块编号
     */
    protected final int INDEX_ID = 100, SECOND_ID = 101, THIRD_ID = 102;
    /**
     * 底部导航的控件，分别为首页、第二个、第三个
     */
    protected RelativeLayout index, second, third;
    /**
     * 各模块主页页面Activity
     */
    protected Class<?> indexActivity=IndexActivity.class, secondActivity=SecondActivity.class,thirdActivity=ThirdActivity.class;
    /**
     * 底部导航控件的id
     */
    protected int indexId, secondId, thirdId;

    /**
     * 当前Activity所属导航模块
     */
    protected int nowId = -1;
    /**
     * 屏幕分辨率信息DisplayMetrics
     */
    protected DisplayMetrics outMetrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(setLayoutId());
        outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        setBottomBarViewId();
        // 初始化底部控件
        setBottomBarView();
        judgeBotteom();
        initTitleBar();
        ActivityManagerTool.getActivityManager().add(this);// 将每一个新开的acitivity放在activity管理集合中
        initPages();
    }

    public void judgeBotteom() {
        // 高亮显示底部导航栏对应的模块
        switch (setModelId()) {
            case R.layout.index:
            	index.setBackgroundResource(R.drawable.xdh_lf_on_btn);
                second.setBackgroundResource(R.drawable.xdh_fx_off_btn);
                third.setBackgroundResource(R.drawable.xdh_me_off_btn);
                break;
            case R.layout.second:
            	index.setBackgroundResource(R.drawable.xdh_lf_off_btn);
                second.setBackgroundResource(R.drawable.xdh_fx_on_btn);
                third.setBackgroundResource(R.drawable.xdh_me_off_btn);
                break;
            case R.layout.third:
            	index.setBackgroundResource(R.drawable.xdh_lf_off_btn);
                second.setBackgroundResource(R.drawable.xdh_fx_off_btn);
                third.setBackgroundResource(R.drawable.xdh_me_on_btn);
                break;
        }
    }

    /**
     * 初始化顶部的标题栏
     */
    public void initTitleBar() {
        left = (Button) findViewById(R.id.left);
        title = (TextView) findViewById(R.id.title);
        right = (Button) findViewById(R.id.right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

		ActivityManagerTool.getActivityManager().removeActivity(this);// 将当前acitivity移除activity管理集合中
	}

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        judgeBotteom();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        judgeBotteom();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * 初始化页面组件
     */
    public abstract void initPages();

    /**
     * 处理单击事件，子类必须实现该方法，用来处理页面中控件的单击事件
     */
    public void onClickListener(View view) {
        int id = view.getId();

        if (id == indexId) {
//            DefaultConstant.BLIGHTICON = 0;
            if (this.getClass() == indexActivity) {
                return;
            }
            
            index.setBackgroundResource(R.drawable.xdh_lf_on_btn);
            second.setBackgroundResource(R.drawable.xdh_fx_off_btn);
            third.setBackgroundResource(R.drawable.xdh_me_off_btn);
            
            Intent intent = new Intent();
			intent.setClass(this, indexActivity);
			intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
			startActivity(intent);
			
        } else if (id == secondId) {
//            DefaultConstant.BLIGHTICON = 1;
            if (this.getClass() == secondActivity) {
                return;
            }
            
            index.setBackgroundResource(R.drawable.xdh_lf_off_btn);
            second.setBackgroundResource(R.drawable.xdh_fx_on_btn);
            third.setBackgroundResource(R.drawable.xdh_me_off_btn);
            
            Intent intent = new Intent();
			intent.setClass(this, secondActivity);
			startActivity(intent);
        } else if (id == thirdId) {
//            DefaultConstant.BLIGHTICON = 2;
            if (this.getClass() == thirdActivity) {
                return;
            }

            index.setBackgroundResource(R.drawable.xdh_lf_off_btn);
            second.setBackgroundResource(R.drawable.xdh_fx_off_btn);
            third.setBackgroundResource(R.drawable.xdh_me_on_btn);
            
            Intent intent = new Intent();
			intent.setClass(this, thirdActivity);
			startActivity(intent);
        } 

        onClickListener(id);
    }

    /**
     * 绘制购物车的商品数量信息
     */

    /**
     * 处理控件的单击事件，继承该类时必须实现该方法
     *
     * @param viewId 控件的ID
     * @author zoutiao
     */
    protected abstract void onClickListener(int viewId);


    /**
     * 设置底部导航条图片控件的id
     */
    public void setBottomBarViewId(){
    		indexId = R.id.index_view;
    		secondId = R.id.second_view;
    		thirdId = R.id.third_view;

    };

    /**
     * 设置模块ID
     */
    public abstract int setModelId();

    /**
     * 初始化底部导航的控件，即findViewById
     */
    public void setBottomBarView() {
        index = (RelativeLayout) findViewById(indexId);
        second = (RelativeLayout) findViewById(secondId);
        third = (RelativeLayout) findViewById(thirdId);

        index.setOnClickListener(this);
        second.setOnClickListener(this);
        third.setOnClickListener(this);
    }

    /**
     * 显示网络不可用对话框，该对话框有一个"确定"按钮，点击确定按钮，返回到给定的Activity
     *
     * @param title   对话框标题
     * @param des     对话框内容
     * @param goClass 将要跳转到的页面
     */
    public void alertDialog(String title, String des, final Class<?> goClass) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle(title);
        adb.setMessage(des);
        adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (goClass != null) {
                    Intent intent = new Intent();
                    intent.setClass(LiveBaseActivity.this, goClass);
                    startActivity(intent);
                    // finish();
                }

            }

        });
        adb.show();
    }

    /**
     * 普通的提示框，只有确定按钮，点击关闭对话框，停留当前页
     *
     * @param title 对话框标题
     * @param des   对话框内容
     */
    protected void alertDialog(String title, String des) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle(title);
        adb.setMessage(des);
        adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }

        });
        adb.show();
    }

    /**
     * 底部导航栏点击返回跳到首页操作，如果是首页则执行退出操作
     */
    private long exiting = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        ActivityManagerTool.getActivityManager();
        // 先判断是否是返回键
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // 判断是不是首页
            if (this.getClass() != ActivityManagerTool.indexActivity) {
                // 如果不是首页但是底部导航则执行跳转到首页操作
                if (ActivityManagerTool.getActivityManager().isBottomActivity(this)) {
                    ActivityManagerTool.getActivityManager().backIndex(this);
                } else {
                    return super.onKeyDown(keyCode, event);
                }
            } else {
                if (System.currentTimeMillis() - exiting < 2000) {
                    ActivityManagerTool.getActivityManager().exit();
                    finish();
                } else
                    Toast.makeText(this, "再按一次返回键退出", Toast.LENGTH_SHORT).show();
                exiting = System.currentTimeMillis();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 若在2秒内有其它操作则打断退出操作
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            exiting = 0;
        }
        return super.dispatchTouchEvent(ev);
    }


    private static String getChannelCode(Context context) {

        String code = getMetaData(context, "LIVE");
        if (code != null) {
            return code;
        }
        return "C_000";

    }

    private static String getMetaData(Context context, String key) {

        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Object value = ai.metaData.get(key);
            if (value != null) {
                return value.toString();
            }
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        onClickListener(v);
    }

    /**
     * 设置页面的布局文件,返回布局文件的id数值,例如R.layout.index
     *
     * @return
     */
    public abstract int setLayoutId();

    
    /**
	 * 可适配的alterDialog 
	 * @param title 
	 * @param des
	 * @param leftBtnStr
	 * @param listenerLeft
	 * @param rightStr
	 * @param listenerRigth
	 * @return
	 */
	public AlertDialog alertCustomeDialog(Context context,String title, String des,
			String leftBtnStr, DialogInterface.OnClickListener listenerLeft,
			String rightStr, DialogInterface.OnClickListener listenerRigth) {
		AlertDialog.Builder adb = new AlertDialog.Builder(context);
		if (title != null) {
			adb.setTitle(title);
		}
		if (des != null) {
			adb.setMessage(des);
		}
		if (leftBtnStr != null) {
			adb.setPositiveButton(leftBtnStr, listenerLeft);
		}
		if (rightStr != null) {
			adb.setNegativeButton(rightStr, listenerRigth);
		}

		return adb.show();
	}
    
	/**
	 *全局登录判断
	 * 
	 * @param login
	 * @return
	 */
	public static boolean isLogin(Context context) {	
		
		boolean islogin = false;

//		SharedPreferences preferences = context.getSharedPreferences(
//				Constant.HTTPHEAD, Context.MODE_PRIVATE);
//		String account = preferences.getString(Constant.ACCOUNT, "");
//		String tk = preferences.getString(Constant.TK, "");
//		if (!"".equals(account) && !"".equals(tk)) {
//			islogin = true;
//		}
		return islogin;
	}
    
    
	/**
	 * 取消加载框
	 */
	public void cancelProgressDialog() {
		
		if ( !this.isFinishing() ) {
			if (progressDialog != null && progressDialog.isShowing() ) {
				progressDialog.cancel();
				progressDialog = null;
			}
		}
	}
	

	
	public void showProgressDialog() {
		showProgressDialog("正在加载数据，请稍候...");
	}

	public void showProgressDialog(String message) {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}

		progressDialog = JuMeiCustomProgressDlg.createDialog(this);
		if (progressDialog != null) {
			progressDialog.setMessage(message);
			progressDialog.show();
		}
	}
}

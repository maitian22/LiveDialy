package com.laifu.livecolorful;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.laifu.livecolorful.tools.ActivityManagerTool;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.MemoryCacheAware;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/*****
 * 初始化application 主要是图片加载jar的
 * 
 */
public class MyApplication extends Application {

	private static MyApplication mInstance = null;
	public boolean m_bKeyRight = true;
	BMapManager mBMapManager = null;

	public static final String strKey = "36d920db44752df0019e8676dbb8ad0c";

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		initImageLoader(getApplicationContext());
		initActivity();
		initMap();
	}

	public void initMap() {
		mInstance = this;
		if (mBMapManager == null) {
			mBMapManager = new BMapManager(this);
		}

		if (!mBMapManager.init(strKey, new MyGeneralListener())) {
//			Toast.makeText(MyApplication.getInstance().getApplicationContext(),
//					"BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
		}
	}

	public static MyApplication getInstance() {
		return mInstance;
	}

	
	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	static class MyGeneralListener implements MKGeneralListener {

		@Override
		public void onGetNetworkState(int iError) {
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
//				Toast.makeText(
//						MyApplication.getInstance().getApplicationContext(),
//						"您的网络出错啦！", Toast.LENGTH_LONG).show();
			} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
//				Toast.makeText(
//						MyApplication.getInstance().getApplicationContext(),
//						"输入正确的检索条件！", Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void onGetPermissionState(int iError) {
			
			if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
				Toast.makeText(
						MyApplication.getInstance().getApplicationContext(),
						"打签名包才能测试地图",
						Toast.LENGTH_LONG).show();
				// 授权Key错误：
//				Toast.makeText(
//						MyApplication.getInstance().getApplicationContext(),
//						"请在 DemoApplication.java文件输入正确的授权Key！",
//						Toast.LENGTH_LONG).show();
				MyApplication.getInstance().m_bKeyRight = false;
			}
		}
	}

	public void initActivity() {
		ActivityManagerTool.indexActivity = IndexActivity.class;
		ActivityManagerTool.getActivityManager().setBottomActivities(
				IndexActivity.class);
		ActivityManagerTool.getActivityManager().setBottomActivities(
				SecondActivity.class);
		ActivityManagerTool.getActivityManager().setBottomActivities(
				ThirdActivity.class);
	}

	/****
	 * 配置全局config
	 * 
	 * @param context
	 */
	public static void initImageLoader(Context context) {
		int memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 8);

		MemoryCacheAware<String, Bitmap> memoryCache;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			memoryCache = new LruMemoryCache(memoryCacheSize);
		} else {
			memoryCache = new LRULimitedMemoryCache(memoryCacheSize);
		}
		// 初始化sd卡缓存和内存缓存
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory().cacheOnDisc().build();

		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).defaultDisplayImageOptions(defaultOptions)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.memoryCache(memoryCache).denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).enableLogging() // Not
																				// necessary
																				// in
																				// common
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
}
